/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Transient;

/**
 *
 * @author adychka
 */
@NodeEntity
public class JsonStoringEntity {
    
    @GraphId
    Long id;
    
    @Transient
    private JsonObject info;
    
    @Property
    String jsonString;
    
    
    public JsonStoringEntity(){
        
    }
    
    public JsonStoringEntity(String jsonString){
        this.jsonString = jsonString;
    }
    
    public Long getId() {
        return id;
    }
    
    protected JsonElement getJsonElementByName(JsonElement source,String propertyName){
        return source.getAsJsonObject().get(propertyName);
    }
    
    public JsonObject getInfo() {
        return info;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
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
