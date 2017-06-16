/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author adychka
 */
@Controller
@RequestMapping({"/","/homepage","/about"})
public class ViewController {
    

    @RequestMapping(value = {"/","/homepage"})
    public String home(){
        return "welcomepage";
    }
    
    @RequestMapping(value = {"/about"})
    public String about(){
        return "about";
    }
    
    
    
}
