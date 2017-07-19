/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.test.config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import inria.socialsecurity.repository.ProfileDataRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTestCase {
    
    
    @Autowired 
    ProfileDataRepository userRepository;
    
    @Autowired
    FacebookProfileRepository facebookAccountRepository;
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Test
    public void CRUDFacebookAccount(){
        Long fbId = facebookAccountRepository.save(getFbAcc()).getId();
        Assert.assertNotNull(fbId);
        FacebookProfile account = facebookAccountRepository.findOne(fbId);
        Assert.assertNotNull(account);
        Assert.assertEquals(fbId, account.getId());
        facebookAccountRepository.delete(account);
        Assert.assertNull(facebookAccountRepository.findOne(fbId));
       
    }
    
    
    @Test
    public void CRUDUser(){
        ProfileData newUser = new ProfileData();
        FacebookProfile fa = getFbAcc();
        newUser.setFacebookProfile(fa);
        Long userId = userRepository.save(newUser).getId();
        Assert.assertNotNull(userId);
        
        ProfileData user = userRepository.findOne(userId);
        Assert.assertNotNull(user.getFacebookProfile());
        
        user.setFacebookProfile(null);
        userRepository.save(user);
        ProfileData updatedUser;
        updatedUser = userRepository.findOne(userId);
        Assert.assertNull(updatedUser.getFacebookProfile());
        
        userRepository.delete(updatedUser);
        Assert.assertNull(userRepository.findOne(userId));    
    }

  
    private FacebookProfile getFbAcc(){
        FacebookProfile fb = new FacebookProfile();
        return fb;
    }
}
