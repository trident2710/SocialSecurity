/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
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
public class HarmTreeLeaf extends HarmTreeElement{
    
    @Property
    private String threatType;
    
    @Property
    private String riskSource;
    
    @Relationship(type = "ATTRIBUTE",direction = Relationship.OUTGOING)
    private AttributeDefinition attributeDefinition;
    
    @Relationship(type = "HAS_LEAFS", direction = Relationship.INCOMING)
    private HarmTreeLogicalNode harmTreeLogicalNode;

    public String getThreatType() {
        return threatType;
    }

    public void setThreatType(String threatType) {
        this.threatType = threatType;
    }

    public String getRiskSource() {
        return riskSource;
    }

    public void setRiskSource(String riskSource) {
        this.riskSource = riskSource;
    }

    public AttributeDefinition getAttributeDefinition() {
        return attributeDefinition;
    }

    public void setAttributeDefinition(AttributeDefinition attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
    }

    public HarmTreeLogicalNode getHarmTreeLogicalNode() {
        return harmTreeLogicalNode;
    }

    public void setHarmTreeLogicalNode(HarmTreeLogicalNode harmTreeLogicalNode) {
        this.harmTreeLogicalNode = harmTreeLogicalNode;
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
