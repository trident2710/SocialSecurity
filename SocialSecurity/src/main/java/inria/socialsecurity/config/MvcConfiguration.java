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
 * declaration of the beans representing the model level of web application i.e.
 * data processing and providing
 *
 * @author adychka
 */
@Configuration
public class MvcConfiguration {

    /**
     * model level of harm tree interaction contains CRUD methods etc.
     *
     * @return
     */
    @Bean
    public HarmTreeModel getHarmTreeModel() {
        return new HarmTreeModelImpl();
    }
}
