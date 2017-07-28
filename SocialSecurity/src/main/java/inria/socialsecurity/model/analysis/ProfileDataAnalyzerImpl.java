/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.analysis;

import inria.socialsecurity.constants.LogicalRequirement;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.model.analysis.HarmTreeEvaluator.HarmTreeNotValidException;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class ProfileDataAnalyzerImpl implements ProfileDataAnalyzer{
    
    @Autowired
    protected HarmTreeEvaluator hte;
    
    protected static final Logger LOG = Logger.getLogger(ProfileDataAnalyzerImpl.class.getName());

    @Override
    public List<Double> calculateLikelihoodForHarmTree(HarmTreeVertex vertex,ProfileData data) throws HarmTreeNotValidException{
        LOG.log(Level.INFO,"start calculating likelihood for harmtree");
        try {
            hte.validateHarmTree(vertex.getId());
            LOG.log(Level.INFO,"harmtree is valid");
            return calculateLikelihoodRecursively(vertex,data); 
        } catch (HarmTreeNotValidException e) {
            LOG.log(Level.INFO,"harmtree is not valid exceptionnally");
            throw e;
        }
    }

    @Override
    public Map.Entry<String,Double> calculateAccuracyFor(ProfileData data, RiskSource source, ThreatType type,AttributeDefinition definition) {
        //@todo: implement
        LOG.log(Level.INFO,"calculating accuracy with params riskSource:{0} type:{1} for attribute {2}",new String[]{source.getValue(),type.getValue(),definition.getName()});
        return new AbstractMap.SimpleEntry<>("abc",0.5);
    }
    
    
    protected List<Double> calculateLikelihoodRecursively(HarmTreeElement element,ProfileData data){
        LOG.log(Level.INFO,"calculating likelihood recursively");
        if(element instanceof HarmTreeVertex){
            List<Double> val =  calculateLikelihoodRecursively(((HarmTreeVertex) element).getDescendants().get(0),data);
            LOG.log(Level.INFO, "calculated for vertex: {0}", Arrays.toString(val.toArray()));
            return val;
        }
        else{

            List<List<Double>> les = new ArrayList<>();
            if(!((HarmTreeLogicalNode) element).getLeafs().isEmpty()){
                for(HarmTreeLeaf htl: ((HarmTreeLogicalNode) element).getLeafs()){
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
                    for(int i=1;i<l.size();i++){
                        double v = 1d;
                        for(int j=1;j<i;j++){
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
