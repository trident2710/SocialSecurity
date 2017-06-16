/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import inria.socialsecurity.attributeprovider.exception.ObjectNotFoundException;
import inria.socialsecurity.attributeprovider.exception.WrongArgumentException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author adychka
 */
@Controller
public class ExceptionHandlingController {
    
    static final String ATTR_REASON = "reason";
    
     
    @ExceptionHandler({Exception.class,ObjectNotFoundException.class,WrongArgumentException.class})
    public ModelAndView error(HttpServletRequest hsr,Exception ex){
        ModelAndView mav = new ModelAndView();
        mav.addObject(ATTR_REASON,ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
