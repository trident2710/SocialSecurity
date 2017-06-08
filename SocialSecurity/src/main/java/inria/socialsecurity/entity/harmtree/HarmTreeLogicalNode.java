/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author adychka
 */
@NodeEntity
public class HarmTreeLogicalNode {
    public static final Integer OR = 1;
    public static final Integer AND = -1;
    
    @GraphId 
    private Long id;
    
    @Property
    private Integer logicalRequirement;
    
    @Relationship(type = "BELONGS_TO",direction = Relationship.OUTGOING)
    HarmTreeLogicalNode parent;
}
