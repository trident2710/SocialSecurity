/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * for adding the custom scope called 'thread'
 * @see CustomThreadScope
 * @author adychka
 */
@Configuration
public class ScopeConfig {
    
    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();

        Map<String,Object> scopes = new HashMap<>();
        scopes.put("thread",new CustomThreadScope());

        configurer.setScopes(scopes);
        return configurer;
    }
}
