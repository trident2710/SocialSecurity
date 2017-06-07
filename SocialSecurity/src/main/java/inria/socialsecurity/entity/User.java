/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author adychka
 */
@NodeEntity
public class User {
    @GraphId
    Long id;
    
    @Relationship(type = "HAS_FB_ACCOUNT",direction = "OUTGOING")
    FacebookAccount facebookAccount;

    public FacebookAccount getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(FacebookAccount facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public Long getId() {
        return id;
    }
    
    
    
}
