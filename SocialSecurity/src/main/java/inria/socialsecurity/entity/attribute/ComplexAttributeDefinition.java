/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.attribute;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author adychka
 */
@NodeEntity
public class ComplexAttributeDefinition extends AttributeDefinition{

    @Relationship(type = "CONTAINS",direction = Relationship.OUTGOING)
    private List<AttributeDefinition> primitiveAttributes;


    public List<AttributeDefinition> getPrimitiveAttributes() {
        return primitiveAttributes;
    }

    public void setPrimitiveAttributes(List<AttributeDefinition> primitiveAttributes) {
        this.primitiveAttributes = primitiveAttributes;
    }
    
    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }    
    
}
