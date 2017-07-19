/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.FacebookProfile;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

/**
 * interface defining the data accessing repository for the objects of type
 * FacebookAccount and it's subclasses
 *
 * @see FacebookProfile
 * @author adychka
 */
public interface FacebookProfileRepository extends GraphRepository<FacebookProfile> {
    List<FacebookProfile> findByFbUrl(String fbUrl);
    
    @Query("MATCH (p) WHERE id(p)={id}  MATCH p-[:HAS_FRIENDS]->()-[*0..10]->n RETURN DISTINCT n")
    List<FacebookProfile> getFriendshipTreeForFacebookProfile(@Param("id") Long fbProfileid);
    
    @Query("MATCH (k) WHERE id(k)={id}  MATCH k-[:HAS_FB_ACCOUNT]->p  MATCH p-[:HAS_FRIENDS]->()-[*0..10]->(n {fbUrl:{url}}) RETURN DISTINCT n")
    FacebookProfile findByFbUrlInFriendshipTreeForFacebookProfile(@Param("id") Long profileDataId, @Param("url") String fbId);
    
    @Query("MATCH (k) WHERE id(k)={id}  MATCH k-[:HAS_FB_ACCOUNT]->p MATCH p-[:HAS_FRIENDS]->()-[*0..10]->n RETURN DISTINCT n.fbUrl")
    List<String> getUrlsInFriendshipTreeForFacebookProfile(@Param("id") Long profileDataId);
}
