/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.entity.analysis.AnalysisReportItem;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.exception.WrongArgumentException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * defines the model layer for the user data
 * @author adychka
 */
public interface ProfileDataModel {
    /**
     * get all users from DB
     * @return 
     */
    Set<ProfileData> getAllProfileData();
    
    /**
     * create new profile data from post request
     * @param request
     * @return
     * @throws WrongArgumentException 
     */
    CrawlingInfo createProfileDataFromHttpRequest(HttpServletRequest request) throws WrongArgumentException;
    
    /**
     * start crawling data from cpecified url with specified depth
     * @param ci
     */
    void crawlFacebookData(CrawlingInfo ci);
    
    /**
     * get the matrix of attribute values for spectified perspective
     * @param perspective
     * @param profileDataId
     * @return 
     */
    Map<String,Map<String, String>> getAttributeMatrixForPerspective(CrawlResultPerspective perspective,Long profileDataId);
    
    /**
     * get the matrix of attribute visibilities for spectified perspective
     * @param profileDataId
     * @return 
     */
    Map<String,Map<String, String>> getAttributeVisibilityMatrix(Long profileDataId);
    
    /**
     * get the matrix of attribute values for spectified perspective and specified profile
     * @param facebookProfileId
     * @param perspective
     * @return 
     */
    Map<String,String> getAttributesForFacebookProfileFromPerspective(Long facebookProfileId,CrawlResultPerspective perspective);
    
    /**
     * performs the analysis of collected data 
     * @param profileData
     * @return 
     */
    List<AnalysisReportItem> generateAnalysisReport(ProfileData profileData);
    
    /**
     * puts crawling engine factory (for testability)
     * @param cef 
     */
    void setCrawlingEngineFactory(CrawlingEngineFactory cef);
    
}
