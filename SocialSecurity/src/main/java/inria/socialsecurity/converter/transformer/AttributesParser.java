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
import inria.socialsecurity.model.DefaultDataProcessor;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * class for parsing the attributes from the crawling result i.e. json object containing the collected attributes 
 * @see CrawlingCallable
 * @author adychka
 */
public class AttributesParser {
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Autowired
    DefaultDataProcessor ddp;
    
    protected static final Logger LOG = Logger.getLogger(AttributesParser.class.getName());
     
    protected Map<String,String> parseAttributesFromJson(JsonObject object){
        Map<String,String> res = new HashMap<>();
        adr.findPrimitiveAttributes().forEach((d) -> {
            res.put(d.getDisplayName(), getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)d));
        });
        adr.findComplexAttributes().forEach((cd)->{
            res.put(cd.getDisplayName(), getValueForComplexAttribute(object, cd));
        });
        return res;
    }
    
    protected String getValueForPrimitiveAttribute(JsonObject object,PrimitiveAttributeDefinition pd){
        pd = (PrimitiveAttributeDefinition)adr.findOne(pd.getId());
        Synonim s = pd.getSynonimByDataSourceName(DefaultDataSourceName.FACEBOOK);
        if(s==null){
            ddp.repairAttributeDefinitions();
            return getValueForPrimitiveAttribute(object, pd);  
        }
        if(object.has(s.getAttributeName())){
            String str=  getStringFromJsonElement(object.get(s.getAttributeName()));
            return str.contains("Ask for")?"-":str;
        } else{
             return "-";
        }
    }  
    
    /**
     * get the value for attribute definition if exists in json object
     * @param object
     * @param ad
     * @return 
     */
    public String getValueForAttribute(JsonObject object,AttributeDefinition ad){
        return ad instanceof PrimitiveAttributeDefinition?
            getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)ad):
            getValueForComplexAttribute(object, (ComplexAttributeDefinition)ad);
    }
    
    protected String getValueForComplexAttribute(JsonObject object,ComplexAttributeDefinition cd){
        cd = (ComplexAttributeDefinition)adr.findOne(cd.getId());
        StringBuilder builder = new StringBuilder();
        cd.getSubAttributes().forEach((a)->{
            builder.append(getValueForPrimitiveAttribute(object, (PrimitiveAttributeDefinition)a)).append(" ");
        });
        String s = builder.toString();
        return s;
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
