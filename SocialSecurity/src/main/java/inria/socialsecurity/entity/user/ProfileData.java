/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    Long estimateFinishTime;
    
    @Property
    Long realFinishTime;
    
    @Property
    Integer depth;

    /**
     * user's facebook account
     */
    @Relationship(type = "HAS_FB_ACCOUNT", direction = "OUTGOING")
    FacebookProfile facebookAccount;
    
    String visibilityMatrixJsonString;
    
    String visibilityRespectTargetMatrixJsonString;
    
    @Relationship(type = "HAS_ATTRIBUTE_MATRIX", direction = "OUTGOING")
    List<JsonStoringEntity> attributeMatrix;
      
    String attributeVisibilityJsonString;
    
    String riskSourceForAttributesJsonString;

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

    public Long getEstimateFinishTime() {
        return estimateFinishTime;
    }

    public void setEstimateFinishTime(Long estimateFinishTime) {
        this.estimateFinishTime = estimateFinishTime;
    }

    public Long getRealFinishTime() {
        return realFinishTime;
    }

    public void setRealFinishTime(Long realFinishTime) {
        this.realFinishTime = realFinishTime;
    }
    
    public String getEstimateFinishTimeDate() {
        if(estimateFinishTime!=null)
        return new SimpleDateFormat("MM.dd  HH:mm:ss ").format(new Date(estimateFinishTime));
        return "-";
    }

    public String getRealFinishTimeDate() {
        if(realFinishTime!=null)
        return new SimpleDateFormat("MM.dd  HH:mm:ss ").format(new Date(realFinishTime));
        return "-";
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

    public String getVisibilityRespectTargetMatrixJsonString() {
        return visibilityRespectTargetMatrixJsonString;
    }

    public void setVisibilityRespectTargetMatrixJsonString(String visibilityRespectTargetMatrixJsonString) {
        this.visibilityRespectTargetMatrixJsonString = visibilityRespectTargetMatrixJsonString;
    }
    
    public List<JsonStoringEntity> getAttributeMatrix() {
        if(attributeMatrix==null) attributeMatrix = new ArrayList<>();
        return attributeMatrix;
    }

    public void setAttributeMatrix(List<JsonStoringEntity> attributeMatrix) {
        this.attributeMatrix = attributeMatrix;
    }

    public String getAttributeVisibilityJsonString() {
        return attributeVisibilityJsonString;
    }

    public void setAttributeVisibilityJsonString(String attributeVisibilityJsonString) {
        this.attributeVisibilityJsonString = attributeVisibilityJsonString;
    }

    public String getRiskSourceForAttributesJsonString() {
        return riskSourceForAttributesJsonString;
    }

    public void setRiskSourceForAttributesJsonString(String riskSourceForAttributesJsonString) {
        this.riskSourceForAttributesJsonString = riskSourceForAttributesJsonString;
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
        return ReflectionToStringBuilder.toStringExclude(this, "facebookAccount");
    }

}
