/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * represents the leaf of the harm tree i.e. the element which contains
 * riskSource, threatType and AttributeDefinition for this node
 *
 * @see RiskSource
 * @see ThreatType
 * @see AttributeDefinition
 * @author adychka
 */
@NodeEntity
public class HarmTreeLeaf extends HarmTreeElement {

    /**
     * one of ThreatType enum values
     */
    @Property
    private String threatType;

    /**
     * one of RiskSource enum values
     */
    @Property
    private String riskSource;

    /**
     * relation with the attribute definition
     */
    @Relationship(type = "ATTRIBUTE", direction = Relationship.OUTGOING)
    private AttributeDefinition attributeDefinition;

    /**
     * incoming relation defining the parent of this leaf
     */
    @Relationship(type = "HAS_DESCENDANT", direction = Relationship.INCOMING)
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

}
