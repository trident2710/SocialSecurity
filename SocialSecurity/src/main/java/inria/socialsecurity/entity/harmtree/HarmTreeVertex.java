/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.harmtree;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * represents the vertex of the harm tree i.e. the head node which contains the
 * information such as name of this harm tree and it's description
 *
 * @author adychka
 */
@NodeEntity
public class HarmTreeVertex extends HarmTreeNode {

    /**
     * name of the harm tree
     */
    @Property
    private String name;

    /**
     * harm tree description
     */
    @Property
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
