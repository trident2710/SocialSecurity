/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import inria.socialsecurity.test.config.Config;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {Config.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HarmTreeTestCase {
    
    @Autowired
    HarmTreeRepository htr;
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Test
    public void basicCrudHarmLogicalNode(){
        HarmTreeLogicalNode htln = new HarmTreeLogicalNode();
        htln.setLogicalRequirement(HarmTreeLogicalNode.AND);
        Long id = htr.save(htln).getId();  
        
        HarmTreeLogicalNode g = (HarmTreeLogicalNode)htr.findOne(id);
        Assert.assertNotNull(g);
        
        g.setLogicalRequirement(HarmTreeLogicalNode.OR);
        htr.save(g);
        
        HarmTreeLogicalNode ng = (HarmTreeLogicalNode)htr.findOne(id);
        Assert.assertNotNull(ng);
        Assert.assertEquals(HarmTreeLogicalNode.OR, ng.getLogicalRequirement());
        
        htr.delete(ng);
        Assert.assertNull(htr.findOne(id));
    }
    
    @Test
    public void basicCrudHarmLeaf(){
        HarmTreeLeaf harmTreeLeaf = new HarmTreeLeaf();
        harmTreeLeaf.setAttributeDefinition(adr.findByName(PrimitiveAttributeName.FIRST_NAME.getValue()));
        Long id =htr.save(harmTreeLeaf).getId();
        
        HarmTreeLeaf g = (HarmTreeLeaf)htr.findOne(id);
        Assert.assertNotNull(g);
        
        g.setAttributeDefinition(adr.findByName(PrimitiveAttributeName.LAST_NAME.getValue()));
        htr.save(g);
        
        HarmTreeLeaf ng = (HarmTreeLeaf)htr.findOne(id);
        Assert.assertNotNull(ng);
        Assert.assertEquals(PrimitiveAttributeName.LAST_NAME.getValue(), ng.getAttributeDefinition().getName());
        
        htr.delete(ng);
        Assert.assertNull(htr.findOne(id));
    }
    
    
    //@Test
    public void testHarmTreeCrud(){
        HarmTreeLogicalNode node = generateRandomTree(1, 5, new Random(), 5);
        Long id = htr.save(node).getId();
        node = (HarmTreeLogicalNode)htr.findOne(id);
        List<HarmTreeLogicalNode> roots = htr.getTreeHeads();
        Assert.assertTrue(roots.contains(node));
        htr.delete(node);
        Assert.assertFalse(htr.getTreeHeads().contains(node));


    }
    
     public HarmTreeLogicalNode generateRandomTree(int depthActual,int depthMax,Random random,int edgesMax){
        HarmTreeLogicalNode htln = new HarmTreeLogicalNode();
        int c = 1+random.nextInt(edgesMax);
        htln.setLogicalRequirement(
                    random.nextBoolean()?
                        HarmTreeLogicalNode.OR:
                        random.nextBoolean()?
                            HarmTreeLogicalNode.AND:
                            c>1?1+random.nextInt(c-1):1);
        if(depthActual == depthMax){
            for(int i=0;i<c;i++){
                HarmTreeLeaf hl = new HarmTreeLeaf();
                hl.setRiskSource(RiskSource.values()[random.nextInt(RiskSource.values().length)].getValue());
                hl.setThreatType(ThreatType.values()[random.nextInt(ThreatType.values().length)].getValue());
                hl.setAttributeDefinition(adr.findByName(PrimitiveAttributeName.values()[random.nextInt(PrimitiveAttributeName.values().length)].getValue()));
                
                htln.getLeafs().add(hl);
            }
        } 
        else{
            for(int i=0;i<c;i++){
                htln.getDescendants().add(generateRandomTree(depthActual+1, depthMax, random, edgesMax));
            }
        }
        return htln;
    }
}
