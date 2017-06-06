/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author adychka
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("inria.socialsecurity")
@EnableNeo4jRepositories("inria.socialsecurity.repository")
public class PersistenceContext extends Neo4jConfiguration {
    
    public static final int NEO4J_PORT = 7474;
    
  
    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory("inria.socialsecurity.entity");
    }
    
    @Bean
    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer("http://localhost:" + NEO4J_PORT,"neo4j","gtheyjdfhfnm");
    }
    
    @Override
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        return super.getSession();
    }
    
   
}
