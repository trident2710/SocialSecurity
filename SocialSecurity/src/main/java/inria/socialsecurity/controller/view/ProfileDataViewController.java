/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.view;

import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.ProfileDataRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * view controller for displaying the available profiles to analyse
 * or to crawl new data
 * @author adychka
 */
@Controller
@RequestMapping("/profiledata/**")
public class ProfileDataViewController {
    
    @Autowired
    ProfileDataModel pdm;
    
    @Autowired
    FacebookLoginAccountRepository flar;
    
    @Autowired
    ProfileDataRepository pdr;
    
    @Autowired
    FacebookProfileRepository fpr;
    
    @Autowired
    AttributeDefinitionRepository adr;
  
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public String getAllProfileDataPage(Model model){
        model.addAttribute("profile_data",pdm.getAllProfileData());
        return "profiledata/profiledata_all";
    }
    
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String getAddProfileDataPage(Model model){
        model.addAttribute("accounts",flar.findByIsClosed(Boolean.FALSE));
        return "profiledata/profiledata_add";
    }
    
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String getViewProfileDataPage(@PathVariable("id") Long id,Model model){
        model.addAttribute("data",pdr.findOne(id));
        return "profiledata/profiledata_view";
    }
    
    @RequestMapping(value = "avmatrix/{id}", method = RequestMethod.GET)
    public String getAttributeValueMatrixProfileDataPage(@PathVariable("id") Long id,Model model){
        model.addAttribute("stranger_perspective",pdm.getAttributeMatrixForPerspective(CrawlResultPerspective.STRANGER, id));
        model.addAttribute("friend_perspective",pdm.getAttributeMatrixForPerspective(CrawlResultPerspective.FRIEND, id));
        model.addAttribute("ffriend_perspective",pdm.getAttributeMatrixForPerspective(CrawlResultPerspective.FRIEND_OF_FRIEND, id));
        model.addAttribute("attributes",adr.findAll());
        return "profiledata/profiledata_view";
    }
    
    /**
     * create new profile data and start crawling specified facebook page
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    String createNewDataSource(HttpServletRequest request) throws WrongArgumentException{
        pdm.crawlFacebookData(pdm.createProfileDataFromHttpRequest(request));
        return "redirect:profiledata/all";
    }
    
    @RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
    String removeProfileData(@PathVariable("id") long id){
        ProfileData data = pdr.findOne(id);
        if(data!=null){
            if(data.getFacebookProfile()!=null)
            fpr.delete(data.getFacebookProfile());
            pdr.delete(data);
        }

        return "redirect:profiledata/all";
    }
}
