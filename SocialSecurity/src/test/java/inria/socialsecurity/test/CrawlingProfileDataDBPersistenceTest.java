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
     
    
    @Autowired
    @InjectMocks
    ProfileDataModel crawler;
    
    @Autowired
    ProfileDataRepository pdr;
    
    Map<String,JsonObject> crawlAccounts = new HashMap<>();
    JsonObject head;
    
    List<String> ids;
    
    Random random;
    
    
    @Before
    public void init() throws URISyntaxException{
        random = new Random(System.currentTimeMillis());
        MockitoAnnotations.initMocks(this);
        names = Arrays.asList(new String[]{"A","B","C","D"});
        langs = Arrays.asList(new String[]{"l1","l2","l3"});
        eduworks = Arrays.asList(new String[]{"ew1","ew2","ew3"});
        gen = Arrays.asList(new String[]{"Male","Female"});
        gin = Arrays.asList(new String[]{"Men","Women"});
        ids = generateRandomIds(1000);
        head = createDummyCrawlingResults(2,0,crawlAccounts,80);
        System.out.println(Arrays.toString(crawlAccounts.entrySet().toArray()));
        for(Map.Entry<String,JsonObject> o:crawlAccounts.entrySet()){
            when(cef.createCrawlingCallable(any(), Matchers.contains(o.getKey()), any())).thenReturn(() -> {
                System.out.println("here");
            return o.getValue();});
        }  
    }
    
    @Test
    public void testCrawl(){
        //Assert.assertTrue(true);
        ProfileData data = new ProfileData();
        data.setName("mocktest");
        data.setRequestUrl(head.get("id").getAsString());
        data = pdr.save(data);
        CrawlingInfo info = new CrawlingInfo(data, 2, head.get("id").getAsString(), null);
        
        crawler.crawlFacebookData(info);
        Assert.assertNotNull(pdr.findByName("mocktest"));
    }
    
    private void removeSelfFriends(){
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
        for(int i=0;i<b;i++){
            if(depthActual<depth){
                JsonObject friend = createDummyCrawlingResults(depth, depthActual+1, res, maxFriendsCount);
                fl.add(friend.get("id").getAsString());
            } else{
                fl.add(getRandom(ids));
            }
            
        }
        parent.add("friend_ids", fl);
        if(depthActual==0){
            removeSelfFriends();
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
