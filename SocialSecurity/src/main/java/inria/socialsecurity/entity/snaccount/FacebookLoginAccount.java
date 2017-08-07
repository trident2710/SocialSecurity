/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.snaccount;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author adychka
 * entity for saving login account details for facebook in DB
 * @see inria.crawlerv2.engine.account.Account
 */
@Deprecated
@NodeEntity
public class FacebookLoginAccount{
    
    @GraphId
    Long id;
    
    @Property
    String login;
    
    @Property
    String password;
    
    @Property
    Boolean isClosed = false;
    
    @Relationship(type = "IS_FRIEND_WITH", direction = "BOTH")
    FacebookLoginAccount friend;
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getId(){
        return this.id;
    }
    
    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }
    
    public FacebookLoginAccount getFriend() {
        return friend;
    }

    public void setFriend(FacebookLoginAccount friend) {
        this.friend = friend;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
