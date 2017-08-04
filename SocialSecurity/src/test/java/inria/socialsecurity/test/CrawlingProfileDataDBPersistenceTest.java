/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import inria.crawlerv2.engine.CrawlingCallable;
import inria.crawlerv2.engine.CrawlingInstanceSettings;
import inria.crawlerv2.engine.account.Account;
import inria.crawlerv2.engine.account.AccountManager;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.model.profiledata.CrawlingEngineFactory;
import inria.socialsecurity.model.profiledata.CrawlingInfo;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import inria.socialsecurity.model.profiledata.ProfileDataModelImpl;
import inria.socialsecurity.repository.ProfileDataRepository;
import inria.socialsecurity.test.config.TestConfig;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CrawlingProfileDataDBPersistenceTest {
    
    private List<String> names;
    private List<String> langs; 
    private List<String> eduworks; 
    private List<String> gen; 
    private List<String> gin; 
    
    @Mock
    CrawlingEngineFactory cef;
    
    @Mock
    CrawlingInfo info;
     
    @Autowired
    @InjectMocks
    ProfileDataModel crawler;
    
    @Autowired
    ProfileDataRepository pdr;
    
    Map<String,JsonObject> crawlAccountsF = new HashMap<>();
    Map<String,JsonObject> crawlAccountsFF = new HashMap<>();
    Map<String,JsonObject> crawlAccountsS = new HashMap<>();
    
    List<String> ids;
    
    Random random;
    
    ProfileData data;
    
    Account[] accounts = new Account[]{new Account("andy1994dic@gmai.com", "anarchist1994", Boolean.FALSE),new Account("portnovsam@yandex.ru", "portnovsammy", Boolean.FALSE),new Account("andriydychka@gmail.com", "andreasdickens", Boolean.FALSE)};
    
    @Before
    public void init() throws URISyntaxException{
       
        MockitoAnnotations.initMocks(this);
        random = new Random(System.currentTimeMillis());
        names = Arrays.asList(new String[]{"A","B","C","D"});
        langs = Arrays.asList(new String[]{"l1","l2","l3"});
        eduworks = Arrays.asList(new String[]{"ew1","ew2","ew3"});
        gen = Arrays.asList(new String[]{"Male","Female"});
        gin = Arrays.asList(new String[]{"Men","Women"});
        ids = generateRandomIds(1000);
        JsonObject h = createDummyCrawlingResults(2,0,crawlAccountsF,1000);
        createDummyCrawlingResults(2,0,crawlAccountsFF,10);
        createDummyCrawlingResults(2,0,crawlAccountsF,10);
        data = new ProfileData();
        data.setName("mocktest");
        data.setRequestUrl(h.get("id").getAsString());
        System.out.println(data.getRequestUrl());
        System.out.println(crawlAccountsF.containsKey(data.getRequestUrl()));
        System.out.println(crawlAccountsF);
        data = pdr.save(data);
        when(info.getAccounts()).thenReturn(accounts);
        when(info.getProfileData()).thenReturn(data);
        when(info.isShouldCollectFF()).thenReturn(true);
        
        for(Map.Entry<String,JsonObject> o:crawlAccountsF.entrySet()){
            when(cef.createCrawlingCallable(any(), Matchers.contains(o.getKey()), any())).thenReturn(() -> {
                System.out.println("here f");
            return o.getValue();});
        } 
//        for(Map.Entry<String,JsonObject> o:crawlAccountsFF.entrySet()){
//            when(cef.createCrawlingCallable(any(), Matchers.contains(o.getKey()), Matchers.refEq(accounts[1]))).thenReturn(() -> {
//                System.out.println("here ff");
//            return o.getValue();});
//        } 
//        for(Map.Entry<String,JsonObject> o:crawlAccountsS.entrySet()){
//            when(cef.createCrawlingCallable(any(), Matchers.contains(o.getKey()), Matchers.refEq(accounts[2]))).thenReturn(() -> {
//                System.out.println("here s");
//            return o.getValue();});
//        } 
    }
    
    @Test
    public void testCrawl(){
        Assert.assertTrue(true);
//        crawler.setCrawlingEngineFactory(cef);
//        crawler.crawlFacebookData(info);
//        Assert.assertNotNull(pdr.findByName("mocktest"));
        //pdr.delete(pdr.findByName("mocktest"));
    }
    
    private void removeSelfFriends(Map<String,JsonObject> crawlAccounts){
        crawlAccounts.entrySet().forEach((o)->{
            o.getValue().get("friend_ids").getAsJsonArray().remove(new JsonPrimitive(o.getKey()));
        });
    }
    private JsonObject createDummyCrawlingResults(int depth, int depthActual,Map<String,JsonObject> res,int maxFriendsCount){
        String id = getRandom(ids);
        JsonObject parent = createDummyCrawlingResult(id);
        res.put(id, parent);
        JsonArray fl = new JsonArray();
        int b = maxFriendsCount;
        if(depthActual<depth){
            for(int i=0;i<b;i++){
                JsonObject friend = createDummyCrawlingResults(depth, depthActual+1, res, maxFriendsCount);
                fl.add(friend.get("id").getAsString());   
            } 
        }else{
           b = random.nextInt()%(maxFriendsCount/3);
           for(int i=0;i<b;i++){
               fl.add(getRandom(ids));
           }
        }
            
        parent.add("friend_ids", fl);
        if(depthActual==0){
            removeSelfFriends(res);
        }
        return parent;
    }
    
    private <T> T getRandom(Collection<T> objects){
        int n = random.nextInt(objects.size());
        Iterator<T> it = objects.iterator();
        while(n>0){
            it.next();
            n--;
        }
        return it.next();
    }
    
    private JsonObject createDummyCrawlingResult(String id){
        
        JsonObject object = new JsonObject();
        if(random.nextInt(5)==0)object.addProperty("first_name", getRandom(names));
        if(random.nextInt(5)==0)object.addProperty("last_name", getRandom(names));
        if(random.nextInt(5)==0)object.addProperty("languages", getRandom(langs));
        if(random.nextInt(5)==0)object.addProperty("gender_interests", getRandom(gin));
        if(random.nextInt(5)==0)object.addProperty("gender", getRandom(gen));
        if(random.nextInt(5)==0)object.addProperty("education_ids", getRandom(eduworks));
        if(random.nextInt(5)==0)object.addProperty("work_ids", getRandom(eduworks));
        object.addProperty("id",id);
        return object;
    }
    
    private List<String> generateRandomIds(int count){
        List<String> res = new ArrayList<>();
        for(int i=0;i<count;i++){
            res.add(""+random.nextInt(1<<15));
        }
        return res;
    }
}
