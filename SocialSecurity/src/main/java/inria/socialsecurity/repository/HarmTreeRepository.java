/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 *
 * @author adychka
 */
public interface HarmTreeRepository extends GraphRepository<HarmTreeElement> {
    
    /**
     * get the heads of the harm tree
     * @return 
     */
    @Query("match (a:HarmTreeLogicalNode) where not (:HarmTreeLogicalNode)-[:HAS_DESCENDANTS]->(a) return a")
    List<HarmTreeLogicalNode> getTreeHeads(); 
}
