/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonObject;
import inria.socialsecurity.converter.Converter;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author adychka
 */
public class AttributeMatrixToJsonConverter implements Converter<Map<String,Map<String,String>>,JsonObject>{

    @Override
    public JsonObject convertFrom(Map<String, Map<String, String>> source) {
        JsonObject object = new JsonObject();
        source.entrySet().forEach(entry->{
            JsonObject o = new JsonObject();
            entry.getValue().entrySet().forEach(e->{
                o.addProperty(e.getKey(), e.getValue());
            });
            object.add(entry.getKey(), o);
        });
        return object;
    }

    @Override
    public Map<String, Map<String, String>> convertTo(JsonObject destination) {
        Map<String,Map<String,String>> res = new HashMap<>();
        destination.entrySet().forEach(element->{
            Map<String,String> o = new HashMap<>();
            if(element.getValue().isJsonObject()){
                element.getValue().getAsJsonObject().entrySet().forEach(e->{ 
                    o.put(e.getKey(), e.getValue().toString().replace("\"", ""));
                });
            }
            res.put(element.getKey(), o);
        });
        return res;
    }

  
    
}
