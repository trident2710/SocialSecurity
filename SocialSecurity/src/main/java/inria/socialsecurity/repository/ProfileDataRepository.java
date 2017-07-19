/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.ProfileData;
import java.util.List;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * interface defining the data accessing repository for the objects of type User
 * and it's subclasses
 *
 * @see ProfileData
 * @author adychka
 */
public interface ProfileDataRepository extends GraphRepository<ProfileData> {
    List<ProfileData> findByName(String name);
}
