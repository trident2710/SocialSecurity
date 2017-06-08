/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test.helper;

import inria.socialsecurity.attribute.AttributeValue;
import inria.socialsecurity.attribute.ComplexAttribute;
import inria.socialsecurity.attribute.PrimitiveAttribute;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

/**
 *
 * @author adychka
 */
public class CRUDAttributeTest {
    
    
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
}
