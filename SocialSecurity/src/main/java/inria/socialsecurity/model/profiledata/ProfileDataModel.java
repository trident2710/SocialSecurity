/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.exception.WrongArgumentException;
import java.net.URISyntaxException;
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
    
    
    Map<String,Map<String, String>> getAttributeMatrixForPerspective(CrawlResultPerspective perspective,Long profileDataId);
    
    Map<String,Map<String, String>> getAttributeVisibilityMatrix(Long profileDataId);
    
}
