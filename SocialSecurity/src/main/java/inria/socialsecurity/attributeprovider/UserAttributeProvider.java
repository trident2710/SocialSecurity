/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.attributeprovider;

import inria.socialsecurity.attribute.ComplexAttribute;
import inria.socialsecurity.attribute.PrimitiveAttribute;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;

/**
 *
 * @author adychka
 * This interface describes the set of methods which define the instance as
 * provider of the user private information properties used in 
 * the social security app. Should be implemented by each class 
 * which represents account information. 
 * @see FacebookAccount;
 */
public interface UserAttributeProvider {
    /**
     * get the primitive attribute by name
     * @param attributeDefinition
     * @return the value of this primitive attribute
     */
    PrimitiveAttribute getPrimitiveAttribute(AttributeDefinition attributeDefinition); 
    
    /**
     * get the complex attribute 
     * @param cad @see ComplexAttributeDefinition
     * @return 
     */
    ComplexAttribute getComplexAttribute(ComplexAttributeDefinition cad);
}
