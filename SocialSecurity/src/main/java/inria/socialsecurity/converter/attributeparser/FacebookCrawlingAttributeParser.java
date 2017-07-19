/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.attributeparser;

import com.google.gson.JsonObject;
import inria.socialsecurity.constants.BasicComplexAttributes;
import inria.socialsecurity.constants.DataType;
import inria.socialsecurity.constants.DefaultDataSourceName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.attribute.Synonim;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class FacebookCrawlingAttributeParser implements AttributeParser<JsonObject>{
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    private static final Logger LOG = Logger.getLogger(FacebookCrawlingAttributeParser.class.getName());

    @Override
    public Map<String, Map<AttributeDefinition, Object>> parsefromSourceSet(Set<JsonObject> sourceSet) {
        Map<String, Map<AttributeDefinition, Object>> result = new HashMap<>();
        sourceSet.forEach((object) -> {
            Map<AttributeDefinition,Object> res = parseAttributesFromJson(object);
            String name = "friend attributes";
            if(object.has("id")) name = object.get("id").getAsString();
            if(res.containsKey(adr.findByName(BasicComplexAttributes.FULL_NAME.getName()))) 
                name = (String)res.get(adr.findByName(BasicComplexAttributes.FULL_NAME.getName()));
            result.put(name, res);
        });
        return result;
    }
    
    @Override
    public Map<AttributeDefinition, Object> parseFromSource(JsonObject source) {
        return parseAttributesFromJson(source);
    }
    
    private Map<AttributeDefinition,Object> parseAttributesFromJson(JsonObject object){
        Map<AttributeDefinition,Object> res = new HashMap<>();
        adr.findPrimitiveAttributes().forEach((d) -> {
            Object val = getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)d);
            if (val!=null) {
                res.put(d, val);
            }
        });
        adr.findComplexAttributes().forEach((cd)->{
            StringBuilder builder = new StringBuilder();
            cd.getSubAttributes().forEach((a)->{
                Object v = getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)a);
                if(v!=null)builder.append(String.valueOf(v)).append(" ");
            });
            String s = builder.toString();
            if(!s.isEmpty())res.put(cd, s);
        });
        return res;
    }
    
    private Object getValueForPrimitiveAttribute(JsonObject object,PrimitiveAttributeDefinition pd){
        Synonim s = pd.getSynonimByDataSourceName(DefaultDataSourceName.FACEBOOK);
        DataType type = DataType.getByName(pd.getDataType());
        try {
            switch(type){
                case STRING:
                    return object.get(s.getAttributeName()).getAsString();
                case NUMBER:
                    return  object.get(s.getAttributeName()).getAsNumber();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE,new StringBuilder().append("unable to collect: ").append(s.getAttributeName()).append("of type: ").append(type.getName()).toString(),e);
        }
        return null;
    }   
}
