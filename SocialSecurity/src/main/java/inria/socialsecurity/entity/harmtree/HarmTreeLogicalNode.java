/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

/**
 *
 * @author adychka
 */
@NodeEntity
public class HarmTreeLogicalNode extends HarmTreeElement{
    
    @Transient
    public static final Integer OR = 1;
    @Transient
    public static final Integer AND = -1;
    
    @Property
    private Integer logicalRequirement;
    
    @Relationship(type = "HAS_DESCENDANTS",direction = Relationship.INCOMING)
    private HarmTreeLogicalNode parent;
    
    @Relationship(type = "HAS_DESCENDANTS",direction = Relationship.OUTGOING)
    private List<HarmTreeLogicalNode> descendants;
    
    @Relationship(type = "HAS_LEAFS",direction = Relationship.OUTGOING)
    private List<HarmTreeLeaf> leafs;

    public Integer getLogicalRequirement() {
        return logicalRequirement;
    }

    public void setLogicalRequirement(Integer logicalRequirement) {
        this.logicalRequirement = logicalRequirement;
    }

    public List<HarmTreeLogicalNode> getDescendants() {
        if(descendants==null) descendants = new ArrayList<>();
        return descendants;
    }

    public void setDescendants(List<HarmTreeLogicalNode> descendants) {
        this.descendants = descendants;
    }

    public List<HarmTreeLeaf> getLeafs() {
        if(leafs==null) leafs = new ArrayList<>();
        return leafs;
    }

    public void setLeafs(List<HarmTreeLeaf> leafs) {
        this.leafs = leafs;
    }

    public HarmTreeLogicalNode getParent() {
        return parent;
    }

    public void setParent(HarmTreeLogicalNode parent) {
        this.parent = parent;
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
