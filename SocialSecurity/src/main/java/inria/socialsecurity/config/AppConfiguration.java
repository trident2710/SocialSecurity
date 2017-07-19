/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *
 * @author adychka the main configuration of the project
 * @see springframework
 */
@Configuration
@EnableAsync
@Import({
    ScopeConfig.class,
    PersistenceContext.class, //configuration of the db connection
    WebConfig.class, //configuration of the web app
    ConverterConfiguration.class, //custom configuration declaring the converters @see converter.Converter.java
    MvcConfiguration.class, //custom configuration for the declaration of model layer beans 
    RootConfig.class})
public class AppConfiguration {
    //assembles all declared configuraions in one
    
}
