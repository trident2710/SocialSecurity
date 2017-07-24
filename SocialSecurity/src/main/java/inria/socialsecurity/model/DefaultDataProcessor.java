/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model;

import inria.socialsecurity.constants.BasicPrimitiveAttributes;
import inria.socialsecurity.constants.DefaultDataSourceName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.attribute.Synonim;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.SynonimRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * class filling the database with basic entities
 * @author adychka
 */
public final class DefaultDataProcessor {

    @Autowired
    private AttributeDefinitionRepository adr;
    
    @Autowired
    private SynonimRepository sr;
    
    public void initAttributeDefinitions(){
        createBasicPrimitiveAttributes();
        createBasicComplexAttributes();        
    }
    
    public void deleteAllAttributeDefinitions(){
        for(AttributeDefinition a:adr.findAll()){
            adr.detachDelete(a.getId());
        }
    }
     
    private void createBasicPrimitiveAttributes(){
        for(BasicPrimitiveAttributes n:BasicPrimitiveAttributes.values()){
            PrimitiveAttributeDefinition pad = new PrimitiveAttributeDefinition();
            pad.setName(n.getValue());
            pad.setDisplayName(n.getDisplayName());
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
}
