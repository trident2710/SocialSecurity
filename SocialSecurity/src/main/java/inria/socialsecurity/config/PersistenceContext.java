/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

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
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * configuration of data source
 *
 * @see Spring Data Neo4j
 * @author adychka
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("inria.socialsecurity") //the root package of the project
@EnableNeo4jRepositories("inria.socialsecurity.repository") //the package which contains the repository interfaces for accessing data
public class PersistenceContext extends Neo4jConfiguration {

    //reads the neo4j connection properties form file
    @Autowired
    SettingsLoader settingsLoader;
    
    private static final String ENTITY_PACKAGE = "inria.socialsecurity.entity"; //the package containing db entinty classes

    public PersistenceContext() {}

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory(ENTITY_PACKAGE);
    }
    
    @Bean
    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer(settingsLoader.getNeo4jUrl()+ ":" + settingsLoader.getNeo4jPort(), settingsLoader.getNeo4jLogin(), settingsLoader.getNeo4jPassword());
    }

    /**
     * used custom scope 'thread' for overcoming an issue of using session beans in other thread
     * @return
     * @throws Exception 
     */
    @Override
    @Bean
    @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        return super.getSession();
    }
}
