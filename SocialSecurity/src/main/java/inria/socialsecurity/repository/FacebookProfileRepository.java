/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.FacebookProfile;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    
    @Query("MATCH (p:FacebookProfile) WHERE id(p)={id}  MATCH p-[:HAS_FRIENDS*0..2]-(n:FacebookProfile) RETURN DISTINCT n")
    List<FacebookProfile> getFriendshipTreeForFacebookProfile(@Param("id") Long fbProfileid);
    
    @Query("MATCH (k:ProfileData) WHERE id(k)={id}  MATCH k-[:HAS_FB_ACCOUNT]->(p:FacebookProfile)  MATCH p-[:HAS_FRIENDS*0..2]-(n:FacebookProfile {fbUrl:{url}}) RETURN DISTINCT n")
    FacebookProfile findByFbUrlInFriendshipTreeForFacebookProfile(@Param("id") Long profileDataId, @Param("url") String fbId);
    
    @Query("MATCH (k:ProfileData) WHERE id(k)={id}  MATCH k-[:HAS_FB_ACCOUNT]->(p:FacebookProfile) MATCH p-[:HAS_FRIENDS*0..2]-(n:FacebookProfile) RETURN DISTINCT n.fbUrl")
    List<String> getUrlsInFriendshipTreeForFacebookProfile(@Param("id") Long profileDataId);
    
    @Query("MATCH (n:FacebookProfile) MATCH n-[:HAS_FRIENDS]->(k:FacebookProfile)  WHERE id(n)={id} WITH DISTINCT k return DISTINCT id(k) LIMIT 1000")
    Set<Integer> getFriendIdsForFacebookProfile(@Param("id") Long fbProfileid);
    
    @Query("MATCH (n:FacebookProfile) WHERE id(n)={id} return n")
    FacebookProfile getOne(@Param("id") Long fbProfileid);
    
   
}
