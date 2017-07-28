/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.JsonStoringEntity;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author adychka
 */
public interface JsonStoringEntityRepository extends GraphRepository<JsonStoringEntity>{
    @Query("MATCH (n:FacebookProfile) WHERE id(n)={id} MATCH n-[:ATTRIBUTES]->(k:JsonStoringEntity) RETURN DISTINCT k")
    List<JsonStoringEntity> getAttributesForFacebookProfile(@Param("id") Long facebookProfileId);
    
    @Query("MATCH (n:FacebookProfile) WHERE id(n)={id} MATCH n-[:ATTRIBUTES]->(k:JsonStoringEntity {perspective:{perspective}}) RETURN k")
    JsonStoringEntity getAttributesForFacebookProfile(@Param("id") Long facebookProfileId,@Param("perspective") String aPerspective);
}
