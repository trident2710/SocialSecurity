/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.test.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * test for converter from/to cytoscape understandable form
 * @see Converter
 * @see http://js.cytoscape.org/
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CytoscapeCoverterTestCase {
    
    @Test
    public void testCytoscapeNotation(){
        
    }
}
