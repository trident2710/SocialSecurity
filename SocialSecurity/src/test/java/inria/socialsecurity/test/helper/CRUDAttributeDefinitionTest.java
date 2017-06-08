/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test.helper;

import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class CRUDAttributeDefinitionTest {
    
    @Autowired
    AttributeDefinitionRepository adr;
    

    public void basicComplexAttrCrud(){
        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        cad.setName("full name");
        
        List<String> primitiveAttributes = new ArrayList<>();
        primitiveAttributes.add(PrimitiveAttributeName.FIRST_NAME.getValue());
        primitiveAttributes.add(PrimitiveAttributeName.LAST_NAME.getValue());
        
        cad.setPrimitiveAttributes(primitiveAttributes);
        Long id = adr.save(cad).getId();
        Assert.assertNotNull(id);
        
        ComplexAttributeDefinition cad1 = (ComplexAttributeDefinition)adr.findOne(id);
        Assert.assertNotNull(cad1);
        
        adr.delete(cad1);
        Assert.assertNull(adr.findOne(id));
    }
    

    public void basicAttrCrud(){
        AttributeDefinition cad = new AttributeDefinition();
        cad.setName("full name");
        
        
        Long id = adr.save(cad).getId();
        Assert.assertNotNull(id);
        
        AttributeDefinition cad1 = adr.findOne(id);
        Assert.assertNotNull(cad1);
        
        adr.delete(cad1);
        Assert.assertNull(adr.findOne(id));
    }
    
    
}
