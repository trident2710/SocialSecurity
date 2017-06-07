/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity;

import inria.socialsecurity.config.PersistenceContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 *
 * @author adychka
 */
@Configuration
@Import(PersistenceContext.class)
@ImportResource("classpath:aop-config.xml")
public class AppConfiguration {
    
}
