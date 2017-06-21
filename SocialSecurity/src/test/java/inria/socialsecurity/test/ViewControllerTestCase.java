/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.controller.view.AttributeDefinitionViewController;
import inria.socialsecurity.controller.view.BasicViewController;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.model.AttributeDefinitionModel;
import inria.socialsecurity.test.config.TestConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static inria.socialsecurity.constants.param.AttributeDefinition.*;
import static inria.socialsecurity.constants.param.HarmTree.*;
import inria.socialsecurity.controller.view.HarmTreeViewController;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.model.HarmTreeModel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ViewControllerTestCase {

    MockMvc mockMvc;
    InternalResourceViewResolver viewResolver;
    
    @Mock
    AttributeDefinitionModel adr;
    
    @Mock
    HarmTreeModel htm;
    
    @InjectMocks
    BasicViewController bvc;
    
    @InjectMocks
    AttributeDefinitionViewController advc;
    
    @InjectMocks
    HarmTreeViewController htvc;
    
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");   
    }
    
    @Test
    public void testBasicViewController() throws Exception{
        mockMvc = standaloneSetup(bvc).setViewResolvers(viewResolver).build();
        
        mockMvc.perform(get("/")).andExpect(view().name("basic/welcomepage"));
        mockMvc.perform(get("/homepage")).andExpect(view().name("basic/welcomepage"));
        mockMvc.perform(get("/about")).andExpect(view().name("basic/about"));
    }
    
    @Test
    public void testAttributeDefinitionViewController() throws Exception{
        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        AttributeDefinition ad = new AttributeDefinition();
        List<AttributeDefinition> p = new ArrayList<>();
        List<ComplexAttributeDefinition> c = new ArrayList<>();
        p.add(ad);
        c.add(cad);
        
        when(adr.getPrimitiveAttributeDefinitions()).thenReturn(p);
        when(adr.getComlexAttributeDefinitions()).thenReturn(c);
        when(adr.getAttributeDefinitionById((long)1)).thenReturn(cad);
        when(adr.getComplexAttributeDefinitionById((long)1)).thenReturn(cad);
        when(adr.getAttributeDefinitionById((long)2)).thenReturn(ad);
        
        mockMvc = standaloneSetup(advc).setViewResolvers(viewResolver).build();
        
        mockMvc.perform(get("/attributes/all"))
                .andExpect(view().name("attribute/attribute_all"))
                .andExpect(model().attribute(PRIMITIVE_ATTRIBUTES, p))
                .andExpect(model().attribute(COMPLEX_ATTRIBUTES, c));
        
        mockMvc.perform(get("/attributes/add?type=complex"))
                .andExpect(view().name("attribute/complex_attribute_add"))
                .andExpect(model().attribute(PRIMITIVE_ATTRIBUTES, p));
        
        mockMvc.perform(get("/attributes/add?type=primitive"))
                .andExpect(view().name("attribute/primitive_attribute_add"));
        
        mockMvc.perform(get("/attributes/update/"+1))
                .andExpect(view().name("attribute/complex_attribute_update"))
                .andExpect(model().attribute(ATTRIBUTE, cad))
                .andExpect(model().attribute(PRIMITIVE_ATTRIBUTES, p));
        
        mockMvc.perform(get("/attributes/update/"+2))
                .andExpect(view().name("attribute/primitive_attribute_update"));
    }
    
    @Test
    public void testHarmTreeViewController() throws Exception{
        HarmTreeVertex vertex = new HarmTreeVertex();
        vertex.setId((long)1);
        List<HarmTreeVertex> h = new ArrayList<>();
        h.add(vertex);
        
        when(htm.getHarmTrees()).thenReturn(h);
        when(htm.createHarmTree("new harm tree", "description")).thenReturn(vertex);
        mockMvc = standaloneSetup(htvc).setViewResolvers(viewResolver).build();
        
        mockMvc.perform(get("/harmtrees/all"))
                .andExpect(view().name("harmtree/harmtree_all"))
                .andExpect(model().attribute(HARM_TREES, h));
        
        mockMvc.perform(get("/harmtrees/update/"+vertex.getId()))
                .andExpect(view().name("harmtree/harmtree_update"))
                .andExpect(model().attributeExists(ID));
        
        mockMvc.perform(get("/harmtrees/add"))
                .andExpect(view().name("redirect:harmtrees/update/" + vertex.getId()));
        
    }
}
