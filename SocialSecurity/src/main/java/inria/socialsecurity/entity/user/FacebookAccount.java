/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.user;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * represents the facebook account information of the user
 *
 * @see User
 * @author adychka
 */
@NodeEntity
public class FacebookAccount extends JsonStoringEntity {

    /**
     * friend relationship between this account and other ones
     */
    @Relationship(type = "FRIEND", direction = "BOTH")
    List<FacebookAccount> friends;

    public FacebookAccount(String jsonInfo) {
        super(jsonInfo);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
