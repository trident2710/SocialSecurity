/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.socialsecurity.constants.CrawlDepth;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.entity.analysis.AnalysisReportItem;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.model.attributedefinition.AttributeDefinitionModel;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.ProfileDataRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    AttributeDefinitionModel adm;
    
    @Autowired
    ProfileDataRepository pdr;
    
    @Autowired
    FacebookProfileRepository fpr;
    
    @Autowired
    AttributeDefinitionRepository adr;
    

    /**
     * list all collected profile data
     * @param model
     * @return 
     */
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public String getAllProfileDataPage(Model model){
        model.addAttribute("profile_data",pdm.getAllProfileData());
        return "profiledata/profiledata_all";
    }
    
    /**
     * get page for inserting params for start clawling new data
     * @param model
     * @return 
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String getAddProfileDataPage(Model model){
        model.addAttribute("depth_values",CrawlDepth.values());
        return "profiledata/profiledata_add";
    }
    
    /**
     * view the profile data with specified id
     * @param id - profile data id
     * @param model
     * @return 
     */
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String getViewProfileDataPage(@PathVariable("id") Long id,Model model){
        model.addAttribute("data",pdr.findOne(id));
        return "profiledata/profiledata_view";
    }
    
    /**
     * get the attribute vales matrix for specified profile data
     * @param id - profile data id
     * @param model
     * @return 
     */
    @RequestMapping(value = "amatrix/{id}", method = RequestMethod.GET)
    public String getAttributeMatrixProfileDataPage(@PathVariable("id") Long id,Model model){ 
        ProfileData d = pdr.findOne(id);
        if(d.getAttributeVisibilityJsonString()!=null&&!d.getAttributeVisibilityJsonString().isEmpty()){
            JsonObject o = new JsonParser().parse(pdr.findOne(id).getAttributeVisibilityJsonString()).getAsJsonObject();
            model.addAttribute("self",o);
        }
        
        model.addAttribute("stranger_perspective",pdm.getAttributeMatrixForPerspective(CrawlResultPerspective.STRANGER, id));
        model.addAttribute("friend_perspective",pdm.getAttributeMatrixForPerspective(CrawlResultPerspective.FRIEND, id));
        model.addAttribute("ffriend_perspective",pdm.getAttributeMatrixForPerspective(CrawlResultPerspective.FRIEND_OF_FRIEND, id));
        model.addAttribute("pdid",id);
        return "profiledata/profiledata_amatrix";
    }
    
    /**
     * get attribute visibility matrix (i.e. which risk source can view this attribute)
     * @param id - profile data id
     * @param model
     * @return 
     */
    @RequestMapping(value = "avmatrix/{id}", method = RequestMethod.GET)
    public String getAttributeVisibilityMatrixProfileDataPage(@PathVariable("id") Long id,Model model){
        ProfileData d = pdr.findOne(id);
        if(d.getRiskSourceForAttributesJsonString()!=null&&!d.getRiskSourceForAttributesJsonString().isEmpty()){
            JsonObject o = new JsonParser().parse(pdr.findOne(id).getRiskSourceForAttributesJsonString()).getAsJsonObject();
            model.addAttribute("self",o);
        }
        model.addAttribute("avmatrix",pdm.getAttributeVisibilityMatrix(id));
        model.addAttribute("pdid",id);
        return "profiledata/profiledata_avmatrix";
    }
    
    /**
     * get the page for displaying the friendship graph
     * @param id
     * @param model
     * @return 
     */
    @RequestMapping(value = "fgraph/{id}", method = RequestMethod.GET)
    public String getFriendGraphForProfileDataPage(@PathVariable("id") Long id,Model model){
        return "profiledata/profiledata_fgraph";
    }
    
    /**
     * get the page displaying the results of analysis report
     * @param id - profile data id
     * @param model
     * @return 
     */
    @RequestMapping(value = "analysis/{id}", method = RequestMethod.GET)
    public String getAnalysisReportProfileDataPage(@PathVariable("id") Long id,Model model){
        List<AnalysisReportItem> items = pdm.generateAnalysisReport(pdr.findOne(id));
        boolean hasErroneus = false;
        for(AnalysisReportItem i:items){
            if(!i.isIsValid()) hasErroneus = true;
        }
        
        model.addAttribute("hasWrong", hasErroneus);
        model.addAttribute("analysis_data",items);
        return "profiledata/profiledata_analysis";
    }
    
    /**
     * create new profile data and start crawling specified facebook page
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    String createNewDataSource(HttpServletRequest request) throws WrongArgumentException{
        pdm.crawlFacebookData(pdm.createProfileDataFromHttpRequest(request));
        return "redirect:profiledata/all";
    }
    
    /**
     * delete the specified profile data and all dependent elements i.e. facebook profiles 
     * @param id - profile data id
     * @return 
     */
    @RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
    String removeProfileData(@PathVariable("id") long id){
        pdr.detachDelete(id);
        pdr.delete(id);
        return "redirect:profiledata/all";
    }
    
    /**
     * show the page displaying the atributes for specified facebook profile
     * @param id - facebook profile id
     * @param model
     * @return 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getFacebookProfilePage(@PathVariable("id") Long id,Model model){
        Map<String,String> s_attrs = pdm.getAttributesForFacebookProfileFromPerspective(id, CrawlResultPerspective.STRANGER);
        List<String> attrs = new ArrayList<>();
        if(s_attrs!=null&&!s_attrs.isEmpty()){
            model.addAttribute("s_attrs",s_attrs);
            model.addAttribute("attrs",s_attrs.keySet());
        }
        Map<String,String> f_attrs = pdm.getAttributesForFacebookProfileFromPerspective(id, CrawlResultPerspective.FRIEND);
        if(f_attrs!=null&&!f_attrs.isEmpty()){
            model.addAttribute("f_attrs",f_attrs);
            model.addAttribute("attrs",f_attrs.keySet());
        }
        Map<String,String> ff_attrs = pdm.getAttributesForFacebookProfileFromPerspective(id, CrawlResultPerspective.FRIEND_OF_FRIEND);
        if(ff_attrs!=null&&!ff_attrs.isEmpty()){
            model.addAttribute("ff_attrs",ff_attrs);
            model.addAttribute("attrs",ff_attrs.keySet());
        }
        return "profiledata/profiledata_info";
    }
    
    /**
     * obtains the inner id for the facebook url and redirects to getFacebookProfilePage()
     * @param pdid
     * @param sid
     * @param model
     * @return 
     */
    @RequestMapping(value = "/fbprofile", method = RequestMethod.GET)
    public String getFacebookProfilePage(@RequestParam("id")String sid,@RequestParam("pdid") Long pdid,Model model){
        Long id = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(pdid,"https://facebook.com/profile.php?id="+sid).getId();
        return "redirect:profiledata/"+id;
    }
    
}
