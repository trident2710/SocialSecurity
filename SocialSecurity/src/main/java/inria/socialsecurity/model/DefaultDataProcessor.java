/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import inria.socialsecurity.constants.BasicPrimitiveAttributes;
import inria.socialsecurity.constants.DefaultDataSourceName;
import inria.socialsecurity.converter.transformer.HarmTreeToJsonConverter;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.attribute.Synonim;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import inria.socialsecurity.repository.SynonimRepository;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

/**
 * class filling the database with basic entities
 * @author adychka
 */
public class DefaultDataProcessor {
    
    @Autowired
    private AttributeDefinitionRepository adr;
    
    @Autowired
    private HarmTreeRepository htr;
    
    @Autowired
    private SynonimRepository sr;
    
    @Autowired
    HarmTreeToJsonConverter httjc;
    
    @Value(value = "classpath:HT.json")
    private Resource ht;
    
    public void initAttributeDefinitions(){
        createBasicPrimitiveAttributes();
        createBasicComplexAttributes();        
    }
    
    public void deleteAllAttributeDefinitions(){
        for(AttributeDefinition a:adr.findAll()){
            adr.detachDelete(a.getId());
        }
    }
    
    public void deleteAllHarmTrees(){
       htr.deleteAll();
    }
    
    private void createBasicPrimitiveAttributes(){
        for(BasicPrimitiveAttributes n:BasicPrimitiveAttributes.values()){
            PrimitiveAttributeDefinition pad = new PrimitiveAttributeDefinition();
            pad.setName(n.getValue());
            pad.setDisplayName(n.getDisplayName());
            pad.setIsUnique(n.isUnique());
            pad.setIsList(n.isList());
            pad = adr.save(pad);
            createSynonimsForAttribute(pad);
        }
    }
    
    private void createBasicComplexAttributes(){
        PrimitiveAttributeDefinition fn = 
                (PrimitiveAttributeDefinition)adr.findByName(BasicPrimitiveAttributes.FIRST_NAME.getValue());
        PrimitiveAttributeDefinition ln = 
                (PrimitiveAttributeDefinition)adr.findByName(BasicPrimitiveAttributes.LAST_NAME.getValue());
        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        cad.setName("full_name");
        cad.setDisplayName("Full name");
        cad.getSubAttributes().add(ln);
        cad.getSubAttributes().add(fn);
        adr.save(cad);
    }
    
    private void createSynonimsForAttribute(PrimitiveAttributeDefinition pad){
        for(DefaultDataSourceName d:DefaultDataSourceName.values()){
            Synonim s = new Synonim();
            s.setDataSourceName(d.getName());
            s.setAttributeName(pad.getName());
            sr.save(s);
            pad.getSynonims().add(s);    
        }
        adr.save(pad);
    }
    
    @Transactional
    public void createDefaultHarmTrees(){
        Gson gson = new Gson();
        JsonArray object;
        object = gson.fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("HT.json")), JsonArray.class);
        System.out.println(object.toString());
        for(JsonElement e:object){
            HarmTreeVertex vertex = httjc.convertTo(e.getAsJsonObject());
            htr.save(vertex);
        }   
    }
}
