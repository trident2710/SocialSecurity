/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.attribute;

import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 *
 * @author adychka
 */
@NodeEntity
public class ComplexAttributeDefinition extends AttributeDefinition{

    @Property
    private List<String> primitiveAttributes;


    public List<String> getPrimitiveAttributes() {
        return primitiveAttributes;
    }

    public void setPrimitiveAttributes(List<String> primitiveAttributes) {
        this.primitiveAttributes = primitiveAttributes;
    }
    
    
    
}
