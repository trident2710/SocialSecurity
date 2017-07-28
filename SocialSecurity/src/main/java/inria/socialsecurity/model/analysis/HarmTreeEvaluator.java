/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.analysis;

import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.HarmTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class HarmTreeEvaluator {
    @Autowired
    HarmTreeRepository htr;
    
    public void validateHarmTree(Long harmTreeVertexId) throws HarmTreeNotValidException{
        HarmTreeVertex vertex = (HarmTreeVertex)htr.findOne(harmTreeVertexId);
        if(vertex==null) {
            throw new HarmTreeNotValidException("there is no such harm tree vertex");
        }
        try {
            validateLogicalNodes(vertex);
        } catch (HarmTreeNotValidException e) {
            throw e;
        }     
    }
    
    private void validateLogicalNodes(HarmTreeNode htn) throws HarmTreeNotValidException{
        
        if(htn==null)
            throw new HarmTreeNotValidException("harm tree contains null node");
        
        if(htn instanceof HarmTreeVertex){
            if(htn.getDescendants().isEmpty()||htn.getDescendants().size()>1)
                throw new HarmTreeNotValidException("vertex has number of descendants not equals 1");
            else
                for(HarmTreeLogicalNode n:htn.getDescendants())
                    validateLogicalNodes(n); 
        }else{
            htn = (HarmTreeLogicalNode)htr.findOne(htn.getId());
            if(htn.getDescendants().isEmpty()) {
                if(((HarmTreeLogicalNode)htn).getLeafs().isEmpty())
                    throw new HarmTreeNotValidException("some logical node does not have leafs");
                for(HarmTreeLeaf l:((HarmTreeLogicalNode)htn).getLeafs()){
                    if(l==null)throw new HarmTreeNotValidException("harm tree contains null node");
                }
            }    
            else
                for(HarmTreeLogicalNode n:htn.getDescendants())
                    validateLogicalNodes(n);
        }   
    }
    
    public static class HarmTreeNotValidException extends Exception{
        public HarmTreeNotValidException(String msg){
            super(msg);
        }
    }
}
