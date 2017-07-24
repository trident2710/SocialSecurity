/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import inria.socialsecurity.constants.DefaultDataSourceName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.attribute.Synonim;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public abstract class AttributesParser {
    @Autowired
    AttributeDefinitionRepository adr;
    
    protected static final Logger LOG = Logger.getLogger(AttributesParser.class.getName());
     
    protected Map<String,String> parseAttributesFromJson(JsonObject object){
        Map<String,String> res = new HashMap<>();
        adr.findPrimitiveAttributes().forEach((d) -> {
            String val = getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)d);
            if (val!=null) {
                res.put(d.getDisplayName(), val);
            }
        });
        adr.findComplexAttributes().forEach((cd)->{
            StringBuilder builder = new StringBuilder();
            cd.getSubAttributes().forEach((a)->{
                Object v = getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)a);
                if(v!=null)builder.append(String.valueOf(v)).append(" ");
            });
            String s = builder.toString();
            if(!s.isEmpty())res.put(cd.getDisplayName(), s);
        });
        return res;
    }
    
    protected String getValueForPrimitiveAttribute(JsonObject object,PrimitiveAttributeDefinition pd){
        pd = (PrimitiveAttributeDefinition)adr.findOne(pd.getId());
        Synonim s = pd.getSynonimByDataSourceName(DefaultDataSourceName.FACEBOOK);
        if(object.has(s.getAttributeName())){
            return getStringFromJsonElement(object.get(s.getAttributeName()));
        } else{
             return "-";
        }
    }   
    
    protected String getStringFromJsonElement(JsonElement e){
        if(e==null||e.isJsonNull()) return "-";
        if(e.isJsonPrimitive()) return e.toString().replace("\"", "");
        if(e.isJsonArray()){
            String res = "";
            for(JsonElement el:e.getAsJsonArray()) res+=getStringFromJsonElement(el)+"\n";
            return res;
        }
        if(e.isJsonObject()){
            String res = "";
            for(Map.Entry<String,JsonElement> el:e.getAsJsonObject().entrySet()){
                res+=getStringFromJsonElement(el.getValue())+"\n";
            }
            return res;
        }
        return "-";
    }
}
