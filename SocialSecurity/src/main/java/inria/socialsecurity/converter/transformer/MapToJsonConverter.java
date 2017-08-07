/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import inria.socialsecurity.converter.Converter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * converts json object from/to map of name/value
 * @author adychka
 */
public class MapToJsonConverter implements Converter<Map<String,String>,JsonObject>{

    @Override
    public JsonObject convertFrom(Map<String, String> source) {
        System.out.println(Arrays.toString(source.entrySet().toArray()));
        JsonObject object = new JsonObject();
        JsonParser parser = new JsonParser();
        source.entrySet().forEach(entry->{
            System.out.println(entry.getValue());
            JsonPrimitive p = new JsonPrimitive(entry.getValue());
            object.add(entry.getKey(), p);
        });
        return object;
    }

    @Override
    public Map<String, String> convertTo(JsonObject destination) {
        Map<String,String> res = new HashMap<>();
        destination.entrySet().forEach(element->{
            res.put(element.getKey(), element.getValue().toString());
        });
        return res;
    }
    
}
