/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonObject;
import inria.socialsecurity.constants.CrawlResultPerspective;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author adychka
 */
public class FacebookDatasetToAttributeVisibilityTransformer extends AttributesParser implements DatasetTransformer<Map<String,JsonObject>>{
    
    @Override
    public Map<String,Map<String,String>> parsefromSourceSet(Set<Map<String,JsonObject>> sourceSet) {
        Map<String,Map<String,String>> result = new HashMap<>();
        sourceSet.forEach((attrPerspectives) -> { 
            if(attrPerspectives.entrySet().iterator().hasNext())
            result.put(attrPerspectives.entrySet().iterator().next().getValue().get("id").getAsString(), parseFromSource(attrPerspectives));
        });
        return result;
    }
    
    @Override
    public Map<String, String> parseFromSource(Map<String,JsonObject> source) {
        Map<String,String> res = new HashMap<>();
        
        adr.findAll().forEach((a)->{
            StringBuilder builder = new StringBuilder();
            for(CrawlResultPerspective perspective:CrawlResultPerspective.values()){
                if(source.containsKey(perspective.name())){
                    if(!getValueForAttribute(source.get(perspective.name()),a).equals("-"))
                        builder.append(" ").append(perspective.getRiskSource().name());
                }
                
            }
            String s = builder.toString();
            if(s.isEmpty()) s="-";
            res.put(a.getDisplayName(), s);
        });
        return res;
    }
   
}
