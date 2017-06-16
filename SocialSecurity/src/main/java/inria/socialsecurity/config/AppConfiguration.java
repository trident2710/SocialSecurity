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
 * @author adychka
 */
@Configuration
@Import({
    WebConfig.class,
    PersistenceContext.class,
    ConverterConfiguration.class,
    MvcConfiguration.class,
    RootConfig.class})
public class AppConfiguration {
    
}
