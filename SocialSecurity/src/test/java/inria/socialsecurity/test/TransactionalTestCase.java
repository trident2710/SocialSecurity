/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.model.profiledata.AccountManagerImpl;
import inria.socialsecurity.repository.CrawlingSettingsRepository;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.JsonStoringEntityRepository;
import inria.socialsecurity.repository.ProfileDataRepository;
import inria.socialsecurity.test.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionalTestCase {
    
    @Autowired
    ProfileDataRepository pdr;
    
    @Autowired
    CrawlingSettingsRepository csr;
    
    @Autowired
    FacebookLoginAccountRepository snar;
    
    @Autowired
    JsonStoringEntityRepository jser;
    
    @Autowired
    FacebookProfileRepository fpr;
    
    @Autowired
    FacebookLoginAccountRepository flar;
    
    
    @Test
    @Transactional
    @Async
    public void testTransactional(){
        ProfileData data = new ProfileData();
        data.setName("abc");
        ProfileData ndata  = pdr.save(data);
        Long id = ndata.getId();
        pdr.delete(ndata);
        Assert.assertNull(pdr.findOne(id));
        
        
        ProfileData data2 = new ProfileData();
        FacebookProfile profile =  new FacebookProfile();
        profile.setFbUrl("abc");
        data2.setFacebookAccount(profile);
        Assert.assertNull(profile.getId());
        ProfileData ndata2 = pdr.save(data2);
        Assert.assertNotNull(ndata2.getFacebookAccount().getId());
        Assert.assertNotNull(profile.getId());
        System.out.println(profile.getId());
        ndata2.setFacebookProfile(null);
        ProfileData ndata3 = pdr.save(ndata2);
        Long id3 = ndata3.getId();
        Assert.assertEquals(ndata3.getId(), ndata2.getId());
        
        FacebookProfile alreadyExists = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(id3, "abc");
        Assert.assertNull(alreadyExists);
        
        ndata2.setFacebookProfile(profile);
        ndata2 = pdr.save(ndata2);
        alreadyExists = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(id3, "abc");
        Assert.assertNotNull(alreadyExists);
        
        pdr.delete(ndata3);
        Assert.assertNull(pdr.findOne(id3));
    }
}
