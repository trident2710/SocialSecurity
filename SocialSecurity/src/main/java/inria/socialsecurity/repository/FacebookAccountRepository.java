/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.user.FacebookAccount;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * interface defining the data accessing repository for the objects of type
 * FacebookAccount and it's subclasses
 *
 * @see FacebookAccount
 * @author adychka
 */
public interface FacebookAccountRepository extends GraphRepository<FacebookAccount> {
    //custom methods are not necessary yet
}
