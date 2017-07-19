/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * class represents the element of the harm tree can be either vertex, leaf or
 * logic
 *
 * @see HarmTreeLogicalNode
 * @see HarmTreeLeaf
 * @see HarmTreeVertex
 * @see Documentation
 *
 * @author adychka
 */
@NodeEntity
public abstract class HarmTreeElement implements Serializable {

    @GraphId
    private Long id;

    /**
     * field which contains the data how to display this element i.e. position x
     * and y of this element on canvas
     */
    @JsonIgnore
    @Property
    private String displayNotation;

    /**
     * class of this element can be either
     *
     * @see HarmTreeLogicalNode
     * @see HarmTreeLeaf
     * @see HarmTreeVertex
     * @return
     */
    @JsonProperty("class")
    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    public String getDisplayNotation() {
        if (displayNotation == null) {
            displayNotation = "{}";
        }
        return displayNotation;
    }

    public void setDisplayNotation(String displayNotation) {
        this.displayNotation = displayNotation;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
