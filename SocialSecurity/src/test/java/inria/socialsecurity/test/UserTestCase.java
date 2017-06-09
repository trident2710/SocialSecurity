/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.config.AppConfiguration;
import inria.socialsecurity.createdb.CreateDB;
import inria.socialsecurity.test.config.Config;
import inria.socialsecurity.entity.FacebookAccount;
import inria.socialsecurity.entity.User;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.FacebookAccountRepository;
import inria.socialsecurity.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {Config.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTestCase {
    
    
    @Autowired 
    UserRepository userRepository;
    
    @Autowired
    FacebookAccountRepository facebookAccountRepository;
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Test
    public void CRUDFacebookAccount(){
        Long fbId = facebookAccountRepository.save(getFbAcc()).getId();
        Assert.assertNotNull(fbId);
        FacebookAccount account = facebookAccountRepository.findOne(fbId);
        Assert.assertNotNull(account);
        Assert.assertEquals(fbId, account.getId());
        facebookAccountRepository.delete(account);
        Assert.assertNull(facebookAccountRepository.findOne(fbId));
        
        CreateDB cdb = new CreateDB(adr);
        cdb.createBasicComplexAttributes();
        cdb.createPrimitiveAttributes();
    }
    
    @Test
    public void CRUDUser(){
        User newUser = new User();
        FacebookAccount fa = getFbAcc();
        newUser.setFacebookAccount(fa);
        Long userId = userRepository.save(newUser).getId();
        Assert.assertNotNull(userId);
        
        User user = userRepository.findOne(userId);
        Assert.assertNotNull(user.getFacebookAccount());
        
        user.setFacebookAccount(null);
        userRepository.save(user);
        User updatedUser;
        updatedUser = userRepository.findOne(userId);
        Assert.assertNull(updatedUser.getFacebookAccount());
        
        userRepository.delete(updatedUser);
        Assert.assertNull(userRepository.findOne(userId));    
    }

  
    private FacebookAccount getFbAcc(){
        FacebookAccount fb = new FacebookAccount("");
        return fb;
    }
}
