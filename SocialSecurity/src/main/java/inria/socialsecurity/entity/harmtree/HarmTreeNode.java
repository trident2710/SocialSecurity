/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * class represents the node of the harm tree can be either
 *
 * @see HarmTreeVertex
 * @see HarmTreeLogicalNode
 * @author adychka
 */
@NodeEntity
public abstract class HarmTreeNode extends HarmTreeElement {

    /**
     * the descendants of this node
     */
    @JsonIgnore
    @Relationship(type = "HAS_DESCENDANTS", direction = Relationship.OUTGOING)
    private List<HarmTreeLogicalNode> descendants;

    public List<HarmTreeLogicalNode> getDescendants() {
        if (descendants == null) {
            descendants = new ArrayList<>();
        }
        return descendants;
    }

    public void setDescendants(List<HarmTreeLogicalNode> descendants) {
        this.descendants = descendants;
    }

    public void addDescendant(HarmTreeLogicalNode node) {
        if (descendants == null) {
            descendants = new ArrayList<>();
        }
        descendants.add(node);
    }

    public void removeDescendant(HarmTreeLogicalNode node) {
        if (descendants == null) {
            descendants = new ArrayList<>();
            return;
        }
        descendants.remove(node);
    }
}
