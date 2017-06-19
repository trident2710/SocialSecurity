/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.test.config.TestConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AttributeTestCase {
    
    @Autowired
    AttributeDefinitionRepository adr;
          
    @Test
    public void crudBasicTest(){
        basicComplexAttrCrud();
        basicAttrCrud();
    }
      
    public void basicComplexAttrCrud(){
        AttributeDefinition a = new AttributeDefinition();
        a.setName(PrimitiveAttributeName.FIRST_NAME.getValue());
        adr.save(a);
        
        AttributeDefinition b = new AttributeDefinition();
        b.setName(PrimitiveAttributeName.LAST_NAME.getValue());
        adr.save(b);
        
        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        cad.setName("full name");
        
        List<AttributeDefinition> primitiveAttributes = new ArrayList<>();
        primitiveAttributes.add(adr.findByName(PrimitiveAttributeName.FIRST_NAME.getValue()));
        primitiveAttributes.add(adr.findByName(PrimitiveAttributeName.LAST_NAME.getValue()));
        
        cad.setPrimitiveAttributes(primitiveAttributes);
        Long id = adr.save(cad).getId();
        Assert.assertNotNull(id);
        
        ComplexAttributeDefinition cad1 = (ComplexAttributeDefinition)adr.findOne(id);
        Assert.assertNotNull(cad1);
        
        adr.delete(cad1);
        Assert.assertNull(adr.findOne(id));
        
         adr.delete(a);
         adr.delete(b);
            
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
