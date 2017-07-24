/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author adychka
 */
public class FacebookDatasetToAttributeMatrixTransformer extends AttributesParser implements DatasetTransformer<JsonObject>{
   
    @Override
    public Map<String,Map<String,String>> parsefromSourceSet(Set<JsonObject> sourceSet) {
        Map<String,Map<String,String>> result = new HashMap<>();
        sourceSet.forEach((object) -> {
            Map<String,String> res = parseAttributesFromJson(object);
            result.put(object.get("id").getAsString(),res);
        });
        return result;
    }
    
    @Override
    public Map<String, String> parseFromSource(JsonObject source) {
        return parseAttributesFromJson(source);
    }
       
}
