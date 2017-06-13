/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.config.AppConfiguration;
import inria.socialsecurity.attribute.AttributeValue;
import inria.socialsecurity.attribute.ComplexAttribute;
import inria.socialsecurity.attribute.PrimitiveAttribute;
import inria.socialsecurity.test.config.Config;
import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.createdb.CreateDB;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
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
@ContextConfiguration(classes = {Config.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AttributeTestCase {
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    //@Test
    public void createBasicInstances(){
        CreateDB cdb = new CreateDB(adr);
        cdb.createBasicComplexAttributes();
        cdb.createPrimitiveAttributes();
    }
        
    @Test
    public void crudBasicTest(){
        basicComplexAttrCrud();
        basicAttrCrud();
    }
    
    @Test
    public void CRUDAttributeDefinition(){
        checkPrimitiveAttrbute();
        checkComplexAttribute();
    }
    
     public void checkPrimitiveAttrbute(){
        PrimitiveAttribute pas = new PrimitiveAttribute("pas", new AttributeValue("some string"));
        PrimitiveAttribute pai = new PrimitiveAttribute("pai", new AttributeValue("123"));
        PrimitiveAttribute pad = new PrimitiveAttribute("pad", new AttributeValue("123.456"));
        PrimitiveAttribute pac = new PrimitiveAttribute("pac", new AttributeValue("123;456;789"));
        
        Assert.assertTrue(pai.getValue().isInteger());
        Assert.assertTrue(pad.getValue().isDouble());
        Assert.assertTrue(pac.getValue().isCollection());
        
        Assert.assertEquals("some string", pas.getValue().getAsString());
        Assert.assertEquals(pai.getValue().getAsInteger(), new Integer("123"));
        Assert.assertEquals(pad.getValue().getAsDouble(), new Double("123.456"));
        Assert.assertEquals((Integer)pac.getValue().getAsCollection().length, new Integer(3));
    }
    
    public void checkComplexAttribute(){
        List<PrimitiveAttribute> attrs = new ArrayList<>();
        attrs.add(new PrimitiveAttribute("pas", new AttributeValue("some string")));
        attrs.add(new PrimitiveAttribute("pai", new AttributeValue("123")));
        attrs.add(new PrimitiveAttribute("pad", new AttributeValue("123.456")));
        attrs.add(new PrimitiveAttribute("pac", new AttributeValue("123;456;789")));
        
        ComplexAttribute ca = new ComplexAttribute("ca", attrs);
        System.out.println(ca.toString());
        Assert.assertEquals((Integer)ca.getPrimitiveAttributes().size(), new Integer(4));
        Assert.assertTrue(ca.getPrimitiveAttributes().get(1).getValue().isInteger());
        Assert.assertTrue(ca.getPrimitiveAttributes().get(2).getValue().isDouble());
        Assert.assertTrue(ca.getPrimitiveAttributes().get(3).getValue().isCollection());
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
