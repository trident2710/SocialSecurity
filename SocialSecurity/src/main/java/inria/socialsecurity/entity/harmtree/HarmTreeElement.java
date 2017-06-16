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
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author adychka
 */
@NodeEntity
public abstract class HarmTreeElement implements Serializable{
    
    @GraphId
    private Long id;
    
    @JsonIgnore
    @Property
    private String displayNotation;
    
    @JsonProperty("class")
    public String getClassName(){
        return this.getClass().getSimpleName();
    }
     
    public Long getId() {
        return id;
    }
    
    public String getDisplayNotation() {
        if(displayNotation==null)
            displayNotation = "";
        return displayNotation;
    }

    public void setDisplayNotation(String displayNotation) {
        this.displayNotation = displayNotation;
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
