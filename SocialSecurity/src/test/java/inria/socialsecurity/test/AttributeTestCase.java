/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.constants.DataType;
import inria.socialsecurity.constants.DefaultDataSourceName;
import inria.socialsecurity.constants.BasicPrimitiveAttributes;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.attribute.Synonim;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.SynonimRepository;
import inria.socialsecurity.test.config.TestConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
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
    
    @Autowired
    SynonimRepository sr;
    
    Random random;
    
    @Before
    public void setup(){
        random = new Random();
    }
          
    @Test
    public void crudBasicTest(){
        BasicDataScript.getInstance().initDB(adr,sr);
        basicComplexAttrCrud();
        basicPrimitiveAttrCrud();
    }
      
    public void basicComplexAttrCrud(){
        AttributeDefinition a = new AttributeDefinition();
        a.setName(BasicPrimitiveAttributes.FIRST_NAME.getValue());
        adr.save(a);
        
        AttributeDefinition b = new AttributeDefinition();
        b.setName(BasicPrimitiveAttributes.LAST_NAME.getValue());
        adr.save(b);
        
        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        cad.setName("full name");
        
        List<AttributeDefinition> primitiveAttributes = new ArrayList<>();
        primitiveAttributes.add(adr.findByName(BasicPrimitiveAttributes.FIRST_NAME.getValue()));
        primitiveAttributes.add(adr.findByName(BasicPrimitiveAttributes.LAST_NAME.getValue()));
        
        cad.setSubAttributes(primitiveAttributes);
        Long id = adr.save(cad).getId();
        Assert.assertNotNull(id);
        
        ComplexAttributeDefinition cad1 = (ComplexAttributeDefinition)adr.findOne(id);
        Assert.assertNotNull(cad1);
        
        adr.delete(cad1);
        Assert.assertNull(adr.findOne(id));
        
        adr.delete(a);
        adr.delete(b);
            
    }
    
    public void basicPrimitiveAttrCrud(){
        PrimitiveAttributeDefinition cad = new PrimitiveAttributeDefinition();
        cad.setName("full name");
        cad.setDisplayName("some_name");
        cad.setDataType(DataType.values()[random.nextInt(DataType.values().length)].getName());
        
        Synonim s = new Synonim();
        s.setAttributeName("name");
        s.setDataSourceName(DefaultDataSourceName.FACEBOOK.getName());
        
        s = sr.save(s);
        
        cad.getSynonims().add(s);
        
        Long id = adr.save(cad).getId();
        Assert.assertNotNull(id);
        
        AttributeDefinition cad1 = adr.findOne(id);
        Assert.assertNotNull(cad1);
        Assert.assertTrue(cad1 instanceof PrimitiveAttributeDefinition);
        Assert.assertTrue(((PrimitiveAttributeDefinition)cad1).getSynonims().contains(s));
        adr.delete(cad1);
        sr.delete(s);
        Assert.assertNull(adr.findOne(id));
    }
    
    
    
   
    
   
}
