/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.constants.LogicalRequirement;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.converter.HarmTreeToCytoscapeNotationConverter;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import inria.socialsecurity.test.config.TestConfig;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neo4j.ogm.session.Session;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CytoscapeConverterTestCase {
    
    MockMvc mockMvc;
    
    @Mock
    HarmTreeRepository htr;
    
    @Mock
    AttributeDefinitionRepository adr;
    
    @Mock
    Session session;
    
    @InjectMocks
    HarmTreeToCytoscapeNotationConverter converter;
            
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConvertToHarmTree(){
        HarmTreeVertex htv = new HarmTreeVertex();
        Random r = new Random();
        for(int i=0;i<r.nextInt(4);i++){
            htv.getDescendants().add(generateRandomTree(0,r.nextInt(5),r, r.nextInt(7)));
        }
        converter.convertFrom(htv);
    }
    
    public HarmTreeLogicalNode generateRandomTree(int depthActual,int depthMax,Random random,int edgesMax){
        HarmTreeLogicalNode htln = new HarmTreeLogicalNode();
        int c = 1+random.nextInt(1+edgesMax);
        
        if(depthActual!=0)
            htln.setLogicalRequirement(
                random.nextBoolean()?
                    LogicalRequirement.OR.getName():
                    random.nextBoolean()?
                        LogicalRequirement.AND.getName():
                             LogicalRequirement.K_OUT_OF_N.getName()
                            );
        else htln.setLogicalRequirement(LogicalRequirement.AND.getName());
        
        if(depthActual == depthMax){
            for(int i=0;i<c;i++){
                HarmTreeLeaf hl = new HarmTreeLeaf();
                hl.setRiskSource(RiskSource.values()[random.nextInt(RiskSource.values().length)].getValue());
                hl.setThreatType(ThreatType.values()[random.nextInt(ThreatType.values().length)].getValue());
                hl.setAttributeDefinition(new AttributeDefinition());
                Long id = random.nextLong()%10000;
                hl.setId(id);
                when(htr.findOne(id)).thenReturn(hl);
                htln.getLeafs().add(hl);
            }
        } 
        else{
            for(int i=0;i<c;i++){
                htln.getDescendants().add(generateRandomTree(depthActual+1, depthMax, random, edgesMax));
            }
        }
        Long id = random.nextLong()%10000;
        htln.setId(id);
        when(htr.findOne(id)).thenReturn(htln);
        return htln;
    }
    
}
