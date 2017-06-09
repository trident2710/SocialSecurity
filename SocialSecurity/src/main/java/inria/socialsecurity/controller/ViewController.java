/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author adychka
 */
@Controller
@RequestMapping({"/","/view/*"})
public class ViewController {
    @RequestMapping(value = {"/","/homepage"})
    public String home(){
        return "welcomepage";
    }
    
    @RequestMapping(value = {"view/about"})
    public String about(){
        return "about";
    }
    
    @RequestMapping(value = {"view/attr_bld"})
    public String attrBuilder(){
        return "attribute_builder";
    }
    
    @RequestMapping(value = {"view/ht_bld"})
    public String harmtreeBuilder(){
         return "harm_tree_builder";
    }
}
