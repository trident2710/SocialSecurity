/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author adychka the main configuration of the project
 * @see springframework
 */
@Configuration
@Import({
    WebConfig.class, //configuration of the web app
    PersistenceContext.class, //configuration of the db connection
    ConverterConfiguration.class, //custom configuration declaring the converters @see converter.Converter.java
    MvcConfiguration.class, //custom configuration for the declaration of model layer beans 
    RootConfig.class})
public class AppConfiguration {
    //assembles all declared configuraions in one
}
