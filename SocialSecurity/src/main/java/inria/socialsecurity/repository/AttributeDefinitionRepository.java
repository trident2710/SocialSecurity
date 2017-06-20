/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * interface defining the data accessing repository for the objects of type
 * AttributeDefinition and it's subclasses
 *
 * @see AttributeDefinition
 * @author adychka
 */
public interface AttributeDefinitionRepository extends GraphRepository<AttributeDefinition> {

    /**
     * find attribute definiton by name
     *
     * @param name - name of the attribute definition
     * @return
     */
    AttributeDefinition findByName(String name);

    /**
     * find all primitive attributes i.e. which consist only of one value
     *
     * @return
     */
    @Query("match (n) where not n:ComplexAttributeDefinition and n:AttributeDefinition return n")
    List<AttributeDefinition> findPrimitiveAttributes();

    /**
     * return the complex definitions
     *
     * @return
     */
    @Query("match (n) where n:ComplexAttributeDefinition return n")
    List<ComplexAttributeDefinition> findComplexAttributes();

    /**
     * delete and detach the attribute definition i.e. delete also all it's
     * relationships. Important: not the relating nodes, but only the
     * relationships
     *
     * @param id
     */
    @Query("match (n) where id(n)={0} detach delete n")
    void detachDelete(Long id);
}
