/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import inria.socialsecurity.model.HarmTreeModel;
import inria.socialsecurity.model.HarmTreeModelImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author adychka
 */
@Configuration
public class MvcConfiguration {
    
    
    @Bean
    public HarmTreeModel getHarmTreeModel(){
        return new HarmTreeModelImpl();
    }
}
