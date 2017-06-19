/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.User;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * interface defining the data accessing repository for the objects of type User
 * and it's subclasses
 *
 * @see User
 * @author adychka
 */
public interface UserRepository extends GraphRepository<User> {
    //no custom methods needed yet
}
