/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test.helper;

import inria.socialsecurity.attribute.ComplexAttribute;
import inria.socialsecurity.attribute.PrimitiveAttribute;
import inria.socialsecurity.attributeprovider.UserAttributeProvider;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;

/**
 *
 * @author adychka
 */
public class UserAttributeProviderTestImpl implements UserAttributeProvider{


    
     public static final String someComAttr = "attr1";
    @Override
    public PrimitiveAttribute getPrimitiveAttribute(AttributeDefinition attributeDefinition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComplexAttribute getComplexAttribute(ComplexAttributeDefinition cad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
