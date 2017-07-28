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
 * represents the user of the social networks user can have accounts in
 * different social networks
 *
 * @author adychka
 */
@NodeEntity
public class ProfileData {

    @GraphId
    Long id;
    
    @Property
    String name;
    
    @Property
    Boolean completed = false;
    
    @Property
    String requestUrl;
    
    @Property
    Long estimateTimeInMinutes;
    
    @Property
    Long realTimeInMinutes;
    
    @Property
    Integer depth;

    /**
     * user's facebook account
     */
    @Relationship(type = "HAS_FB_ACCOUNT", direction = "OUTGOING")
    FacebookProfile facebookAccount;
    
    String visibilityMatrixJsonString;
    
    @Relationship(type = "HAS_ATTRIBUTE_MATRIX", direction = "OUTGOING")
    List<JsonStoringEntity> attributeMatrix;
    
    String likelihoodCalculationJsonString;

    public FacebookProfile getFacebookProfile() {
        return facebookAccount;
    }

    public void setFacebookProfile(FacebookProfile facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Long getEstimateTimeInMinutes() {
        return estimateTimeInMinutes;
    }

    public void setEstimateTimeInMinutes(Long estimateTimeInMinutes) {
        this.estimateTimeInMinutes = estimateTimeInMinutes;
    }

    public Long getRealTimeInMinutes() {
        return realTimeInMinutes;
    }

    public void setRealTimeInMinutes(Long realTimeInMinutes) {
        this.realTimeInMinutes = realTimeInMinutes;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public FacebookProfile getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(FacebookProfile facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public String getVisibilityMatrixJsonString() {
        return visibilityMatrixJsonString;
    }

    public void setVisibilityMatrixJsonString(String visibilityMatrixJsonString) {
        this.visibilityMatrixJsonString = visibilityMatrixJsonString;
    }

    public List<JsonStoringEntity> getAttributeMatrix() {
        if(attributeMatrix==null) attributeMatrix = new ArrayList<>();
        return attributeMatrix;
    }

    public void setAttributeMatrix(List<JsonStoringEntity> attributeMatrix) {
        this.attributeMatrix = attributeMatrix;
    }

    public String getLikelihoodCalculationJsonString() {
        return likelihoodCalculationJsonString;
    }

    public void setLikelihoodCalculationJsonString(String likelihoodCalculationJsonString) {
        this.likelihoodCalculationJsonString = likelihoodCalculationJsonString;
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
