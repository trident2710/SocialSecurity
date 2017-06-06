package inria.socialsecurity.config.test;

import inria.socialsecurity.config.PersistenceContext;
import inria.socialsecurity.entity.User;
import inria.socialsecurity.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {PersistenceContext.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CrudTest {
    
    @Autowired 
    UserRepository userRepository;
    
    
    @Test
    public void userSave(){
        User user = new User();
        Assert.assertNotNull(userRepository.save(user));
    }
}
