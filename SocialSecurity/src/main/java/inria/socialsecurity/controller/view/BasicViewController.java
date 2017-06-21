/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * controller for resolving the basic application pages
 *
 * @author adychka
 */
@Controller
public class BasicViewController {

    @RequestMapping(value = {"/", "/homepage"})
    public String home() {
        return "basic/welcomepage";
    }

    @RequestMapping(value = {"/about"})
    public String about() {
        return "basic/about";
    }
    
    @RequestMapping(value = {"/settings"})
    public String setings() {
        return "basic/settings";
    }

}
