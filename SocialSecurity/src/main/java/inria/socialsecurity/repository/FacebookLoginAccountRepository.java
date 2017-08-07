/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.snaccount.FacebookLoginAccount;
import java.util.List;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 *
 * @author adychka
 */
@Deprecated
public interface FacebookLoginAccountRepository extends GraphRepository<FacebookLoginAccount>{
    FacebookLoginAccount findByLoginAndPassword(String login, String password);
    List<FacebookLoginAccount> findByIsClosed(Boolean isClosed);
}
