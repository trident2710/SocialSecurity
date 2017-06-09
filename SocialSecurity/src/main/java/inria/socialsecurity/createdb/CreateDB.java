/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.createdb;

import inria.socialsecurity.constants.BasicComplexAttributeName;
import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class CreateDB {

    
    
    private AttributeDefinitionRepository adr;
    
    public CreateDB(AttributeDefinitionRepository adr){
        this.adr= adr;
    }
    
    
    public void createPrimitiveAttributes(){
        for(PrimitiveAttributeName name:PrimitiveAttributeName.values()){
            AttributeDefinition ad = new AttributeDefinition();
            ad.setName(name.getValue());
            adr.save(ad);
        }
    }
    
    public void createBasicComplexAttributes(){
        for(BasicComplexAttributeName name:BasicComplexAttributeName.values()){
            List<AttributeDefinition> l = new ArrayList<>();
            for(PrimitiveAttributeName n:name.getSubnames()){
                 AttributeDefinition ad = new AttributeDefinition();
                 ad.setName(n.getValue());
                 l.add(ad);
            }
            ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
            cad.setName(name.getName());
            cad.setPrimitiveAttributes(l);
            adr.save(cad);
        }
    }
    
    
}
