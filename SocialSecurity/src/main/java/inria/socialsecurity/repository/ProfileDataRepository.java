/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.ProfileData;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

/**
 * interface defining the data accessing repository for the objects of type User
 * and it's subclasses
 *
 * @see ProfileData
 * @author adychka
 */
public interface ProfileDataRepository extends GraphRepository<ProfileData> {
    List<ProfileData> findByName(String name);
    @Query("MATCH (k:ProfileData) WHERE id(k)={id} MATCH k-[:HAS_FB_ACCOUNT]->(p:FacebookProfile) MATCH p-[:HAS_FRIENDS*0..2]-(n:FacebookProfile) MATCH n-[:ATTRIBUTES]->l DETACH DELETE l DETACH DELETE n DETACH DELETE k ")
    void detachDelete(@Param("id") Long profileDataId);
}
