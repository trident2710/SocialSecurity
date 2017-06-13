/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author adychka
 */
@Controller
@RequestMapping({"/","/homepage","/about","/error"})
public class ViewController {

    @RequestMapping(value = {"/","/homepage"})
    public String home(){
        return "welcomepage";
    }
    
    @RequestMapping(value = {"/about"})
    public String about(){
        return "about";
    }
    
    @RequestMapping(value = {"/error"})
    public String error(){
        return "error";
    }
    
}
