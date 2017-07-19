/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.user;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * represents the facebook account information of the user
 *
 * @see User
 * @author adychka
 */
@NodeEntity
public class FacebookProfile {

    @GraphId
    Long id;
    
    @Property
    String fbUrl;
    
    /**
     * friend relationship between this account and other ones
     */
    @Relationship(type = "HAS_FRIENDS", direction = "OUTGOING")
    List<FacebookProfile> friends;
    
    @Relationship(type = "ATTRIBUTES", direction = "OUTGOING")
    List<JsonStoringEntity> attributes;
    

    public FacebookProfile() {
    }

    public Long getId() {
        return id;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbId) {
        this.fbUrl = fbId;
    }

    public List<FacebookProfile> getFriends() {
        if(friends==null) friends = new ArrayList<>();
        return friends;
    }

    public void setFriends(List<FacebookProfile> friends) {
        this.friends = friends;
    }

    public List<JsonStoringEntity> getAttributes() {
        if(attributes==null) attributes = new ArrayList<>();
        return attributes;
    }

    public void setAttributes(List<JsonStoringEntity> attributes) {
        this.attributes = attributes;
    }
    
    public JsonStoringEntity getAttributesForPerspective(String perspective){
        if(attributes==null) {
            attributes = new ArrayList<>();
            return null;
        }
        for(JsonStoringEntity e:attributes){
            if(e.getPerspective().equals(perspective))
                return e;
        }
        return null;
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
