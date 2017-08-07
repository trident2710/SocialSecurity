/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonObject;
import inria.crawlerv2.driver.AttributeVisibility;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * for processing the target true visibility ( attribute value + real visibility setting) collected by 
 * @see AttributeVisibilityCrawlingCallable
 * @author adychka
 */
public class FacebookTrueVisibilityToAttributeMatrixTransformer implements DatasetTransformer<JsonObject>{
    
    @Autowired
    AttributeDefinitionRepository adr;

    @Override
    public Map<String, Map<String, String>> parsefromSourceSet(Set<JsonObject> sourceSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> parseFromSource(JsonObject source) {
        Map<String,String> res = new HashMap<>();
        for(AttributeDefinition d:adr.findAll()){
            RiskSource[] rs = parseRiskSourceForAttributeDefinition(d, source);
            if(rs==null){
                res.put(d.getName(), "-");
            } else{
                String rss = "";
                for(RiskSource r:rs){
                    rss+=r.name()+" ";
                }
                res.put(d.getName(), rss);
            }
        }
        return res;
    }
    
    private RiskSource[] parseRiskSourceForAttributeDefinition(AttributeDefinition definition,JsonObject source){
        if(definition instanceof PrimitiveAttributeDefinition){
            if(source.has(definition.getName())){
                AttributeVisibility v = AttributeVisibility.valueOf(source.get(definition.getName()).getAsJsonObject().get("visibility").getAsString());
                RiskSource s = RiskSource.getForAttributeVisibility(v);
                return s==null?null:s.getWeaker();
            } return null;
        }
        if(definition instanceof ComplexAttributeDefinition){
            Set<RiskSource> res = new HashSet<>();
            
            for(AttributeDefinition d:((ComplexAttributeDefinition) definition).getSubAttributes()){
                RiskSource[] rs = parseRiskSourceForAttributeDefinition(d, source);
                res.addAll(Arrays.asList(rs));
            }
            return res.toArray(new RiskSource[res.size()]);    
        }
        return null;
    }
    
}
