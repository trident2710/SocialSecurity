/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.repository.HarmTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author adychka
 */
@Controller
@RequestMapping("/harmtrees*")
public class HarmTreeController {
    
    static final String ATR_HARMTREES = "harm_trees";
    
    @Autowired
    HarmTreeRepository htr;
    
    @RequestMapping(value = "/harmtrees-all",method = RequestMethod.GET)
    public String showAll(Model model){
        model.addAttribute(ATR_HARMTREES,htr.getTreeHeads());
        return "harmtree_all";
    }
    
    @RequestMapping(value = "/harmtrees-specific",method = RequestMethod.DELETE)
    public ResponseEntity deleteAttribute(@RequestParam String id,Model model){
        Long idValue;
        try {
            idValue = Long.parseLong(id);
            HarmTreeLogicalNode htln =  (HarmTreeLogicalNode)htr.findOne(idValue);
            htr.delete(htln);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NumberFormatException e) {
            System.out.println(id);
            System.out.println("here");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch(ClassCastException ce){
            System.out.println("here2");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
