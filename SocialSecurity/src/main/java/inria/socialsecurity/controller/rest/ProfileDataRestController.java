/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.rest;

import com.google.gson.JsonParser;
import inria.socialsecurity.converter.FacebookProfileToCytoscapeNotationConverter;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import inria.socialsecurity.repository.ProfileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    @Autowired
    FacebookProfileToCytoscapeNotationConverter fptcnc;
    
    @Autowired
    ProfileDataRepository pdr;
    
    /**
     * get the friendship graph for the profile data with specified id
     * @param profileId - profile data id
     * @return 
     */
    @RequestMapping(value = "friendgraph/{id}", method = RequestMethod.GET)
    String getFriendGraphForProfileId(@PathVariable("id") Long profileId){
        return fptcnc.convertFrom(pdr.findOne(profileId).getFacebookAccount()).toString();
    }
    
    /**
     * update friendship graph for the profile data with specified id
     * @param profileId- profile data id
     * @param body- cytscape notation json string
     */
    @RequestMapping(value = "friendgraph/{id}", method = RequestMethod.POST)
    void updateFriendGraphForProfileId(@PathVariable("id") Long profileId,@RequestBody String body){
        fptcnc.convertTo(new JsonParser().parse(body).getAsJsonObject());
    }
    
}
