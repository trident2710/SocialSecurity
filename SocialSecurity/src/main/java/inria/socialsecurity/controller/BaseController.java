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
@RequestMapping({"/","/homepage","/about"})
public class BaseController {
    @RequestMapping(value = {"/","/homepage"})
    public String home(){
        return "welcomepage";
    }
    
    @RequestMapping(value = {"/about"},method = RequestMethod.GET)
    public String about(){
        return "about";
    }
}
