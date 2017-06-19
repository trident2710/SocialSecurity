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

    private static final int NEO4J_PORT = 7474; // db connection port
    private static final String SERVER = "localhost"; //db connection ip address

    private static final String LOGIN = "neo4j"; //db connection login
    private static final String PASSWORD = "gtheyjdfhfnm"; //db connection password
    private static final String ENTITY_PACKAGE = "inria.socialsecurity.entity"; //the package containing db entinty classes

    public PersistenceContext() {
    }

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory(ENTITY_PACKAGE);
    }

    @Bean
    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer("http://" + SERVER + ":" + NEO4J_PORT, LOGIN, PASSWORD);
    }

    @Override
    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        return super.getSession();
    }

}
