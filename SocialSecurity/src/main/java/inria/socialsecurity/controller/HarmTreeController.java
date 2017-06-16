/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import inria.socialsecurity.attributeprovider.exception.ObjectNotFoundException;
import inria.socialsecurity.attributeprovider.exception.WrongArgumentException;
import static inria.socialsecurity.controller.AttributeController.ATR_ATTRIBUTE;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
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
    
    static final String ATR_HARMT = "harm_trees";
    static final String ATR_HRMT_ID = "id";
    
    @Autowired
    HarmTreeRepository htr;
    
    @RequestMapping(value = "/harmtrees-all",method = RequestMethod.GET)
    public String showAll(Model model){
        model.addAttribute(ATR_HARMT,htr.getTreeVertices());
        return "harmtree_all";
    }
    @RequestMapping(value = "/harmtrees-update",method = RequestMethod.GET)
    public String showUpdatePage(@RequestParam String id,Model model) throws WrongArgumentException, ObjectNotFoundException{
        model.addAttribute(ATR_HRMT_ID,id);
        return "harmtree_update";
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
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch(ClassCastException ce){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
