/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

/**
 * represents the logic node of the harm tree i.e. logical requirement, how many
 * descendants should be true for this node to be true
 *
 * @author adychka
 */
@NodeEntity
public class HarmTreeLogicalNode extends HarmTreeNode {

    @Transient
    public static final Integer OR = 1;
    //i.e all descendants should be true.
    //Impossible to define preciesely because the count of descendants is not static
    @Transient
    public static final Integer AND = -1;

    //the interger value representing the logical requirement for this element to be true
    @Property
    private Integer logicalRequirement;

    //the list of descendant leafs
    @JsonIgnore
    @Relationship(type = "HAS_LEAFS", direction = Relationship.OUTGOING)
    private List<HarmTreeLeaf> leafs;

    public Integer getLogicalRequirement() {
        return logicalRequirement;
    }

    public void setLogicalRequirement(Integer logicalRequirement) {
        this.logicalRequirement = logicalRequirement;
    }

    public List<HarmTreeLeaf> getLeafs() {
        if (leafs == null) {
            leafs = new ArrayList<>();
        }
        return leafs;
    }

    public void setLeafs(List<HarmTreeLeaf> leafs) {
        if (leafs == null) {
            return;
        }
        this.leafs = leafs;
    }

    /**
     * add leaf to the descendants Attention: this will change the parent of the
     * leaf, because the leaf can have only one parent
     *
     * @param leaf
     */
    public void addLeaf(HarmTreeLeaf leaf) {
        if (leafs == null) {
            leafs = new ArrayList<>();
        }
        leafs.add(leaf);
    }

    /**
     * remove the leaf from the descendants Attention: this will remove the
     * parent of the leaf
     *
     * @param leaf
     */
    public void removeLeaf(HarmTreeLeaf leaf) {
        if (leafs == null) {
            leafs = new ArrayList<>();
            return;
        }
        leafs.remove(leaf);
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
