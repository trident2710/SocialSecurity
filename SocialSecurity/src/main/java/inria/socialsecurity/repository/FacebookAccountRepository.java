/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.FacebookAccount;
import inria.socialsecurity.entity.User;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 *
 * @author adychka
 */
public interface FacebookAccountRepository extends GraphRepository<FacebookAccount>{
    
}
