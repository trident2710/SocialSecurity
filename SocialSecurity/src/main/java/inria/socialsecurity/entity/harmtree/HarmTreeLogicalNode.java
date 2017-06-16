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
 *
 * @author adychka
 */
@NodeEntity
public class HarmTreeLogicalNode extends HarmTreeNode{
    
    @Transient
    public static final Integer OR = 1;
    @Transient
    public static final Integer AND = -1;
    
    @Property
    private Integer logicalRequirement;
    
    @JsonIgnore
    @Relationship(type = "HAS_LEAFS",direction = Relationship.OUTGOING)
    private List<HarmTreeLeaf> leafs;

    public Integer getLogicalRequirement() {
        return logicalRequirement;
    }

    public void setLogicalRequirement(Integer logicalRequirement) {
        this.logicalRequirement = logicalRequirement;
    }

    public List<HarmTreeLeaf> getLeafs() {
        if(leafs==null) 
            leafs = new ArrayList<>();
        return leafs;
    }

    public void setLeafs(List<HarmTreeLeaf> leafs) {
        if(leafs==null)
            return;
        this.leafs = leafs;
    }  
    
    public void addLeaf(HarmTreeLeaf leaf){
        if(leafs==null) 
            leafs = new ArrayList<>();
        leafs.add(leaf);
    }
    
    public void removeLeaf(HarmTreeLeaf leaf){
        if(leafs==null){
            leafs = new ArrayList<>();
            return;
        } 
        leafs.remove(leaf);
    }
}
