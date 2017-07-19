/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.rest;

import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adychka
 */
@RestController
@RequestMapping("/rest/profiledata/**")
public class ProfileDataRestController {
    
    @Autowired
    ProfileDataModel pdm;
    
    
    
}
