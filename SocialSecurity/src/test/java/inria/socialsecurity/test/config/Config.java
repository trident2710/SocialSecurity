/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test.config;

import inria.socialsecurity.AppConfiguration;
import inria.socialsecurity.test.helper.CRUDAttributeDefinitionTest;
import inria.socialsecurity.test.helper.CRUDAttributeTest;
import inria.socialsecurity.test.helper.CRUDUserTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author adychka
 */
@Configuration
@Import({AppConfiguration.class})
public class Config {
    @Bean
    public CRUDAttributeDefinitionTest crudadt(){
        return new  CRUDAttributeDefinitionTest();
    }
    @Bean
    public CRUDUserTest crudut(){
        return new  CRUDUserTest();
    }  
    @Bean
    public CRUDAttributeTest crudat(){
        return new CRUDAttributeTest();
    }
}
