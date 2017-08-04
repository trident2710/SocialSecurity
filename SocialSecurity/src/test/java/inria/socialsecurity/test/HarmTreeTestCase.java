/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.constants.BasicPrimitiveAttributes;
import inria.socialsecurity.constants.LogicalRequirement;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.model.analysis.HarmTreeValidator;
import inria.socialsecurity.model.analysis.ProfileDataAnalyzerImpl;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import inria.socialsecurity.test.config.TestConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.validation.constraints.Null;
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
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HarmTreeTestCase {
    
    @Autowired
    HarmTreeRepository htr;
    @Autowired
    AttributeDefinitionRepository adr;
    @Autowired
    HarmTreeValidator hte;
    
    @Test
    public void basicCrudHarmLogicalNode(){
        HarmTreeLogicalNode htln = new HarmTreeLogicalNode();
        htln.setLogicalRequirement(LogicalRequirement.AND.getName());
        Long id = htr.save(htln).getId();  
        
        HarmTreeLogicalNode g = (HarmTreeLogicalNode)htr.findOne(id);
        Assert.assertNotNull(g);
        
        g.setLogicalRequirement(LogicalRequirement.OR.getName());
        htr.save(g);
        
        HarmTreeLogicalNode ng = (HarmTreeLogicalNode)htr.findOne(id);
        Assert.assertNotNull(ng);
        Assert.assertEquals(LogicalRequirement.OR.getName(), ng.getLogicalRequirement());
        
        htr.delete(ng);
        Assert.assertNull(htr.findOne(id));
    }
    
    @Test
    public void basicCrudHarmLeaf(){
        HarmTreeLeaf harmTreeLeaf = new HarmTreeLeaf();
        harmTreeLeaf.setAttributeDefinition(adr.findByName(BasicPrimitiveAttributes.FIRST_NAME.getValue()));
        Long id =htr.save(harmTreeLeaf).getId();
        
        HarmTreeLeaf g = (HarmTreeLeaf)htr.findOne(id);
        Assert.assertNotNull(g);
        
        g.setAttributeDefinition(adr.findByName(BasicPrimitiveAttributes.LAST_NAME.getValue()));
        htr.save(g);
        
        HarmTreeLeaf ng = (HarmTreeLeaf)htr.findOne(id);
        Assert.assertNotNull(ng);
        Assert.assertEquals(BasicPrimitiveAttributes.LAST_NAME.getValue(), ng.getAttributeDefinition().getName());
        
        htr.delete(ng);
        Assert.assertNull(htr.findOne(id));
    }
    
    
    @Test
    public void testHarmTreeCrud(){
        HarmTreeVertex node = new HarmTreeVertex();
        node.setName("some test name");
        node.setDescription("some test description");
        node.addDescendant(generateRandomTree(0, 2, new Random(), 4));
        Long id = htr.save(node).getId();
        node = (HarmTreeVertex)htr.findOne(id);
        List<HarmTreeVertex> roots = htr.getTreeVertices();
        Assert.assertTrue(roots.contains(node));
        htr.detachDelete(node.getId());
        Assert.assertFalse(htr.getTreeVertices().contains(node));


    }
    
    public HarmTreeLogicalNode generateRandomTree(int depthActual,int depthMax,Random random,int edgesMax){
        HarmTreeLogicalNode htln = new HarmTreeLogicalNode();
        int c = 1+random.nextInt(edgesMax);
        
        htln.setLogicalRequirement(
            random.nextBoolean()?
                LogicalRequirement.OR.getName():
                random.nextBoolean()?
                    LogicalRequirement.AND.getName():
                        LogicalRequirement.K_OUT_OF_N.getName());
        
        if(depthActual == depthMax){
            for(int i=0;i<c;i++){
                HarmTreeLeaf hl = new HarmTreeLeaf();
                hl.setRiskSource(RiskSource.values()[random.nextInt(RiskSource.values().length)].getValue());
                hl.setThreatType(ThreatType.values()[random.nextInt(ThreatType.values().length)].getValue());
                hl.setAttributeDefinition(adr.findByName(BasicPrimitiveAttributes.values()[random.nextInt(BasicPrimitiveAttributes.values().length)].getValue()));
                
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
     
    public static enum TreeDefect{
        COUPLE_DESC_IN_VERTEX,
        LOGIC_EMPTY,
        
    }
    
    private HarmTreeVertex generateHarmTreeWithDefect(TreeDefect defect){
        Random random = new Random();
        HarmTreeVertex vertex  = new HarmTreeVertex();
        vertex.getDescendants().add(generateRandomTree(0, 2, new Random(), 4));
        if(defect!=null)
        switch(defect){
            case COUPLE_DESC_IN_VERTEX:
                HarmTreeLogicalNode node  = new HarmTreeLogicalNode();
                vertex.getDescendants().add(node);
                break;
            case LOGIC_EMPTY:
                removeRandomPartWithLeafs(vertex.getDescendants().get(0), random);
                break;     
        }
        return htr.save(vertex);
        
       
    }
    
    private void removeRandomPartWithLeafs(HarmTreeNode node,Random random){
        HarmTreeLogicalNode n = node.getDescendants().get(random.nextInt(node.getDescendants().size()));
        if(!n.getLeafs().isEmpty()){
            n.setLeafs(null);
        } else removeRandomPartWithLeafs(n, random);
    }
    
    
     
    @Test
    public void testHarmTreeEvaluator(){
        HarmTreeVertex vertex = generateHarmTreeWithDefect(HarmTreeTestCase.TreeDefect.COUPLE_DESC_IN_VERTEX);
        try {
            hte.validateHarmTree(vertex.getId());
            Assert.fail();
        } catch (HarmTreeValidator.HarmTreeNotValidException e) {
            Assert.assertEquals("vertex has number of descendants not equals 1", e.getMessage());
        }
        htr.detachDelete(vertex.getId());
        
        vertex = generateHarmTreeWithDefect(HarmTreeTestCase.TreeDefect.LOGIC_EMPTY);
        try {
            hte.validateHarmTree(vertex.getId());
            Assert.fail();
        } catch (HarmTreeValidator.HarmTreeNotValidException e) {
            Assert.assertEquals("some logical node does not have leafs", e.getMessage());
        }
        htr.detachDelete(vertex.getId());
         
        vertex = generateHarmTreeWithDefect(null);
        try {
            hte.validateHarmTree(vertex.getId());
            Assert.assertTrue(true);
        } catch (HarmTreeValidator.HarmTreeNotValidException e) {
           Assert.fail();
        }
        htr.detachDelete(vertex.getId());
        
        try {
            hte.validateHarmTree(-12345l);
            Assert.fail();
        } catch (HarmTreeValidator.HarmTreeNotValidException e) {
            Assert.assertEquals("there is no such harm tree vertex", e.getMessage());
        }
        
    }
    
    private class TestableAnalyser extends ProfileDataAnalyzerImpl{
        public TestableAnalyser(HarmTreeValidator evaluator){
            this.hte = evaluator;    
        }
        
        public List<Double> testableCombineForLogicRequirement(List<List<Double>> input,LogicalRequirement requirement){
            return combineForLogicRequirement(input, requirement);
        }
    
        public List<List<Double>> testableGetAllCombinations(List<List<Double>> input){
            return getAllCombinations(input);
        }

        public boolean testableUpdateIndexes(List<Integer> indexes,List<List<Double>> input){
            return updateIndexes(indexes, input);
        }
        
    }
    
    @Test
    public void analysisGetAllCombinationsTest(){
        TestableAnalyser analyser = new TestableAnalyser(hte);
        for(int i=0;i<100;i++){
            List<List<Double>> lst = generateRandomList(15);
            int ccombos = countCombinations(lst);
            List<List<Double>>  combos = analyser.testableGetAllCombinations(lst);
            Assert.assertTrue(ccombos==combos.size());
        }
        
    }
    
    @Test
    public void analysisCombineForLogicRequirementTest(){
        TestableAnalyser analyser = new TestableAnalyser(hte);
        for(int i=0;i<150;i++){
            List<List<Double>> input = generateRandomList(20);
            System.out.println("input "+Arrays.toString(input.toArray()));
            List<Double> output = analyser.testableCombineForLogicRequirement(input, LogicalRequirement.AND);
            System.out.println("and "+Arrays.toString(output.toArray()));
            output = analyser.testableCombineForLogicRequirement(input, LogicalRequirement.OR);
            System.out.println("or "+Arrays.toString(output.toArray()));
            output = analyser.testableCombineForLogicRequirement(input, LogicalRequirement.K_OUT_OF_N);
            System.out.println("k-n "+Arrays.toString(output.toArray()));
        }
        Assert.assertTrue(true);
        
    }
    
    
    
    private int countCombinations(List<List<Double>> lst){
        if(lst.size()<=1) return lst.size();
        int count=1;
        for(List<Double> i:lst) if(!i.isEmpty()) count*=i.size();
        return count;
    }
    
    private List<List<Double>> generateRandomList(int maxItems){
        List<List<Double>> res = new ArrayList<>();
        Random n = new Random();
        for(int i=0;i<1+n.nextInt(maxItems-1);i++){
            List<Double> r = new ArrayList<>();
            for(int j=0;j<1+n.nextInt(maxItems-1);j++){
                r.add(n.nextDouble()%1);
            }
            res.add(r);
        }
        return res;        
    }
}
