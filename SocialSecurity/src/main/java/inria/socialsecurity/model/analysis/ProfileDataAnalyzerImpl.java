/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.analysis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.crawlerv2.driver.AttributeVisibility;
import inria.socialsecurity.constants.BasicPrimitiveAttributes;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.constants.LogicalRequirement;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.converter.transformer.AttributesParser;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeVisibilityTransformer;
import inria.socialsecurity.converter.transformer.MapToJsonConverter;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.JsonStoringEntity;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.model.analysis.HarmTreeValidator.HarmTreeNotValidException;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import inria.socialsecurity.repository.JsonStoringEntityRepository;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class ProfileDataAnalyzerImpl extends AttributesParser implements ProfileDataAnalyzer{
    
    @Autowired
    protected HarmTreeValidator hte;
    
    @Autowired
    protected MapToJsonConverter mtjc;
    
    @Autowired
    protected FacebookDatasetToAttributeVisibilityTransformer fdtavt;
    
    @Autowired
    protected FacebookProfileRepository fpr;
    
    @Autowired
    protected JsonStoringEntityRepository jser;
    
    @Autowired
    protected HarmTreeRepository htr;
    
    @Autowired
    protected AttributeDefinitionRepository adr;
    
    protected static final Logger LOG = Logger.getLogger(ProfileDataAnalyzerImpl.class.getName());
    
    private Map<Long,Map<String,List<JsonObject>>> attributesCash = new HashMap<>();

    @Override
    public Set<Double> calculateLikelihoodForHarmTree(HarmTreeVertex vertex,ProfileData data) throws HarmTreeNotValidException{
        LOG.log(Level.INFO,"start calculating likelihood for harmtree");
        try {
            hte.validateHarmTree(vertex.getId());
            LOG.log(Level.INFO,"harmtree is valid");
            
            return new HashSet<Double>(calculateLikelihoodRecursively(vertex,data)); 
        } catch (HarmTreeNotValidException e) {
            LOG.log(Level.INFO,"harmtree is not valid exceptionnally");
            throw e;
        }
    }

    @Override
    public Map.Entry<String,Double> calculateAccuracyFor(ProfileData data, RiskSource source, ThreatType type,AttributeDefinition definition) {
        definition = adr.findByName(definition.getName());
        boolean isCalculable = isAttributeCalculable(definition);
        LOG.log(Level.INFO,"calculating for {0} {1} {2} {3}",new String[]{source.name(),type.getValue(),""+isCalculable,definition.getName()});
        
        JsonParser p = new JsonParser();
        Map<String,String> targetParams = mtjc.convertTo(p.parse(data.getAttributeVisibilityJsonString()).getAsJsonObject());
        if(type==ThreatType.FE1){
            if(targetParams.containsKey(definition.getName())){
                AttributeVisibility v = AttributeVisibility.valueOf(p.parse(targetParams.get(definition.getName())).getAsJsonObject().get("visibility").getAsString());
                RiskSource s = RiskSource.getForAttributeVisibility(v);
                LOG.log(Level.INFO,"source {0} res {1}",new String[]{s.getValue(),""+s.compareTo(source)});
                if(s.compareTo(source)>=0) 
                    return new AbstractMap.SimpleEntry<>(p.parse(targetParams.get(definition.getName())).getAsJsonObject().get("value").toString(),1.0);
                else 
                    return new AbstractMap.SimpleEntry<>("-",0d);
            } else return new AbstractMap.SimpleEntry<>("-",0d);
        }
        else{
            if(isCalculable){
                FacebookProfile t = fpr.findOne(data.getFacebookProfile().getId());
                return getMostCommonValue(data, source,definition);
            } return new AbstractMap.SimpleEntry<>("-",0d);
        }
        
        //return new AbstractMap.SimpleEntry<>("-",0.5d);
    }
    
    private List<JsonObject> getFriendsAttributes(ProfileData data,RiskSource source){
        CrawlResultPerspective p = CrawlResultPerspective.getForRiskSource(source);
        if(attributesCash.containsKey(data.getId())&&attributesCash.get(data.getId()).containsKey(p.name()))
            return attributesCash.get(data.getId()).get(p.name());
        FacebookProfile t = fpr.findOne(data.getFacebookProfile().getId());
        JsonParser parser = new JsonParser();
        List<FacebookProfile> friends = t.getFriends();
        List<JsonStoringEntity> entities = new ArrayList<>();
        for(FacebookProfile pr:friends){
            JsonStoringEntity e = jser.getAttributesForFacebookProfile(pr.getId(), p.name());
            if(e!=null)entities.add(e);
        }
        List<JsonObject> res = new ArrayList<>();
        for(JsonStoringEntity entity:entities){
            JsonObject o = parser.parse(entity.getJsonString()).getAsJsonObject();
            res.add(o);
        }
        Map<String,List<JsonObject>> r = new HashMap<>();
        r.put(p.name(), res);
        attributesCash.put(data.getId(),r);
        return res;    
    }
    
    private Map.Entry<String,Double> getMostCommonValue(ProfileData data,RiskSource source,AttributeDefinition definition){
        if(definition.getName().equals(BasicPrimitiveAttributes.BIRTHDAY.getValue()))
            return getAge(data, source, definition);
        Map<String,Double> vals = new HashMap<>();
        List<JsonObject> attrs = getFriendsAttributes(data, source);
      
        for(JsonObject o:attrs){
            String v = getValueForAttribute(o, definition);
            if(definition.getIsList()){
                String[] vs = v.split("[\n ]");
                for(String s:vs){
                    System.out.println("splitted v "+v);
                    if(vals.containsKey(s)){
                        vals.replace(s, vals.get(s), vals.get(s)+1);
                    } else vals.put(s, 1d);    
                }
            } else{
                System.out.println("attr v "+v);
                if(vals.containsKey(v)){
                    vals.replace(v, vals.get(v), vals.get(v)+1);
                } else vals.put(v, 1d);    
            }
            
        }
        vals.replace("-",0d);
        if(vals.isEmpty())
            return new AbstractMap.SimpleEntry<>("-",0d);
        
        Map.Entry<String,Double> max = vals.entrySet().iterator().next();
        for(Map.Entry<String,Double> o:vals.entrySet()){
            if(o.getValue()>max.getValue())
                max = o;
        }
        LOG.log(Level.INFO,"calculated count {0}",max.getValue()+"/"+attrs.size());
        max.setValue(max.getValue()/attrs.size());
        LOG.log(Level.INFO,"calculated {0}",max.getKey()+" "+max.getValue());
        return max;
    }
    
    private Map.Entry<String,Double> getAge(ProfileData data,RiskSource source,AttributeDefinition definition){
        Map<Integer,Double> vals = new HashMap<>();
        int year = new Date(System.currentTimeMillis()).getYear();
        List<JsonObject> attrs = getFriendsAttributes(data, source);
      
        System.out.println("size "+ attrs.size());
        for(JsonObject o:attrs){
            String v = getValueForAttribute(o, definition);
            System.out.println("v "+v);
            Matcher m = Pattern.compile("\\d{4}").matcher(v);
            String syear =  m.find()?m.group():null;
            System.out.println("syear "+syear);
            if(syear!=null){
               int y = year-Integer.parseInt(syear);
               y= y+(10-y%10);
               if(vals.containsKey(y)){
                   vals.replace(y, vals.get(y), vals.get(y)+1);
               } else vals.put(y, 1d);    
            } 
        }
        if(attrs.size()>0){
            Map.Entry<Integer,Double> max = vals.entrySet().iterator().next();
            for(Map.Entry<Integer,Double> o:vals.entrySet()){
                if(o.getValue()>max.getValue())
                    max = o;
            }
            return new AbstractMap.SimpleEntry<>(""+max.getKey(),max.getValue()/attrs.size());
        } else return new AbstractMap.SimpleEntry<>("-",0d);
        
    }
    
    
    private boolean isAttributeCalculable(AttributeDefinition definition){
        if(definition instanceof PrimitiveAttributeDefinition){
            return !definition.getIsUnique();
        } else{
            boolean val = true;
            for(AttributeDefinition d:((ComplexAttributeDefinition)definition).getSubAttributes()){
                val = val&isAttributeCalculable(d);
            }
            return val;   
        }
    }
    
    
    protected List<Double> calculateLikelihoodRecursively(HarmTreeElement element,ProfileData data){
        LOG.log(Level.INFO,"calculating likelihood recursively");
        if(element instanceof HarmTreeVertex){
            List<Double> val =  calculateLikelihoodRecursively(((HarmTreeVertex) element).getDescendants().get(0),data);
            LOG.log(Level.INFO, "calculated for vertex: {0}", Arrays.toString(val.toArray()));
            return val;
        }
        else{
            element = htr.findOne(element.getId());
            List<List<Double>> les = new ArrayList<>();
            if(!((HarmTreeLogicalNode) element).getLeafs().isEmpty()){
                for(HarmTreeLeaf htl: ((HarmTreeLogicalNode) element).getLeafs()){
                    htl = (HarmTreeLeaf)htr.findOne(htl.getId());
                    System.out.println(htl.toString());
                    les.add(Arrays.asList(calculateAccuracyFor(data, RiskSource.valueOf(htl.getRiskSource()), ThreatType.valueOf(htl.getThreatType()), htl.getAttributeDefinition()).getValue()));
                }
            }

            if(!((HarmTreeLogicalNode) element).getDescendants().isEmpty()){
                for(HarmTreeLogicalNode htln: ((HarmTreeLogicalNode) element).getDescendants()){
                    les.add(calculateLikelihoodRecursively(htln, data));
                }
            }
            les = getAllCombinations(les);
            LOG.log(Level.INFO,"combinations: {0}",Arrays.toString(les.toArray()));
            LogicalRequirement req = LogicalRequirement.getForName(((HarmTreeLogicalNode) element).getLogicalRequirement());

            List<Double> val = combineForLogicRequirement(les, req);
            LOG.log(Level.INFO, "calculated {0} for logical node with id: {1} and requirement: {2}",new String[]{Arrays.toString(val.toArray()),""+element.getId(),((HarmTreeLogicalNode)element).getLogicalRequirement()});
            return val;
        }
    }
    
    protected List<Double> combineForLogicRequirement(List<List<Double>> input,LogicalRequirement requirement){
        List<Double> res = new ArrayList<>();
        switch(requirement){
            case AND:
                for(List<Double> l:input){
                    double v = 1d;
                    for(Double d:l){
                        v*=d;
                    }
                    res.add(v);
                }
                break;
            case OR:
                for(List<Double> l:input){
                    double v = 0;
                    for(Double d:l){
                        if(d>v) v=d;
                    }
                    res.add(v);
                }
                break;
            case K_OUT_OF_N:
                for(List<Double> l:input){
                    Collections.sort(l);
                    if(l.size()==1)
                        res.add(l.get(0));
                    else
                    for(int i=0;i<l.size();i++){
                        double v = l.get(0);
                        for(int j=1;j<=i;j++){
                            v*=l.get(j);
                        }
                        res.add(v);
                    }
                }
                break;
        }
        return res;
    }
    
    
    protected List<List<Double>> getAllCombinations(List<List<Double>> input){
        if(input.size()<=1) return input;
        List<List<Double>> res = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        input.removeIf(l->l.isEmpty());
        for(int i=0;i<input.size();i++){
            indexes.add(0);
        }
        do{  
            List<Double> item = new ArrayList<>();
            for(int i=0;i<input.size();i++){
                item.add(input.get(i).get(indexes.get(i)));
            }
            res.add(item);
        } while (updateIndexes(indexes, input));
        return res;
    }
    
    protected boolean updateIndexes(List<Integer> indexes,List<List<Double>> input){
        indexes.set(indexes.size()-1, indexes.get(indexes.size()-1)+1);
        boolean check = true;
        while (check) {        
            check = false;
            if(indexes.get(0)==input.get(0).size())
                return false;
            for(int i=1;i<indexes.size();i++){
                if(indexes.get(i)==input.get(i).size()){
                    indexes.set(i,0);
                    indexes.set(i-1,indexes.get(i-1)+1);
                    check = true;
                }
            }
        }
        return true;
    }
    
}
