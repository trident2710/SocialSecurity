/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.controller.view.BasicController;
import inria.socialsecurity.test.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ViewControllerTestCase {

    MockMvc mockMvc;
    
    @Before
    public void setup(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");
        
        BasicController controller = new BasicController();
        mockMvc = standaloneSetup(controller).setViewResolvers(viewResolver).build();
    }
    
    @Test
    public void testViewController() throws Exception{
        mockMvc.perform(get("/")).andExpect(view().name("welcomepage"));
        mockMvc.perform(get("/homepage")).andExpect(view().name("welcomepage"));
        mockMvc.perform(get("/about")).andExpect(view().name("about"));
    }
}
