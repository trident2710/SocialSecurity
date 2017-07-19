/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.crawlerv2.engine.CrawlingCallable;
import inria.crawlerv2.engine.CrawlingEngine;
import inria.crawlerv2.engine.CrawlingEngineSettings;
import inria.crawlerv2.engine.account.Account;
import inria.crawlerv2.provider.AttributeName;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.converter.attributeparser.FacebookCrawlingAttributeParser;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.settings.CrawlingSettings;
import inria.socialsecurity.entity.snaccount.FacebookLoginAccount;
import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.JsonStoringEntity;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.repository.CrawlingSettingsRepository;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.JsonStoringEntityRepository;
import inria.socialsecurity.repository.ProfileDataRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author adychka
 */
public class ProfileDataModelImpl implements ProfileDataModel{

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
    AccountManagerImpl am;
    
    @Autowired
    FacebookLoginAccountRepository flar;
    
    @Autowired
    FacebookCrawlingAttributeParser fcap;
    
    private static final Logger LOG = Logger.getLogger(ProfileDataModel.class.getName());
    
    @Override
    public Set<ProfileData> getAllProfileData() {
        Set<ProfileData>  data = new HashSet<>();
        pdr.findAll().forEach(data::add);
        return data;
    }
    
    @Override
    public CrawlingInfo createProfileDataFromHttpRequest(HttpServletRequest request) throws WrongArgumentException {
        if(request==null)
            throw new WrongArgumentException();
        
        String fbUrl,name;
        Integer depth;
        
        if(request.getParameter("name")==null||request.getParameter("name").isEmpty())
            throw new WrongArgumentException();
        name = request.getParameter("name");
        
        if(request.getParameter("fb_url")==null||request.getParameter("fb_url").isEmpty())
            throw new WrongArgumentException();
        fbUrl = request.getParameter("fb_url");
        if(!fbUrl.contains("https://www.facebook.com/"))
            throw new WrongArgumentException();
        
        if(request.getParameter("depth")==null||request.getParameter("depth").isEmpty())
            throw new WrongArgumentException();
        depth = Integer.parseInt(request.getParameter("depth"));
        if(depth==null)
            throw new WrongArgumentException();
        
        if(request.getParameter("acc_friend")==null||request.getParameter("acc_friend").isEmpty())
            throw new WrongArgumentException();
        FacebookLoginAccount account = request.getParameter("acc_friend").equals("-1")?null:flar.findOne(Long.parseLong(request.getParameter("acc_friend")));
        ProfileData data = new ProfileData();
        data.setName(name);
        data.setRequestUrl(fbUrl);
        pdr.save(data);
        
        return new CrawlingInfo(data, depth, fbUrl, account);
    }

//    @Async
//    @Override
//    public void crawlFacebookData(CrawlingInfo ci){
//        try {
//            LOG.log(Level.INFO,"start crawling data with parameters: {0}",ci.toString());
//            
//            CrawlingEngineSettings settings = createCrawlingEngineSettings();
//            ProfileData parent = ci.getPd();
//            ExecutorService es = Executors.newSingleThreadExecutor();
//            InverseSemaphore crawlSemaphore = new InverseSemaphore();
//            parent.setEstimateTimeInMinutes(getEstimatedCrawlingTimeInMinutes(settings, ci.getDepth(),  getAccountSatisfyingPerspective(ci.getAccount(), CrawlResultPerspective.FRIEND)!=null));
//            parent = pdr.save(parent);
//            long timeBeginnig = System.currentTimeMillis();
//            for(CrawlResultPerspective p:CrawlResultPerspective.values()){
//                Account account = getAccountSatisfyingPerspective(ci.getAccount(), p);
//                if(account!=null){
//                    crawl(ci.getUrl(),settings,account,null,ci.getDepth(),1,parent,es,p,crawlSemaphore);
//                    crawlSemaphore.awaitCompletion();
//                    
//                    InverseSemaphore updateSemaphore = new InverseSemaphore();
//                    while (p!=CrawlResultPerspective.getWeakerFor(p)) {            
//                        p = CrawlResultPerspective.getWeakerFor(p);
//                        updateFacebookProfile(getAccountSatisfyingPerspective(ci.getAccount(), p), pdr.findOne(ci.getPd().getId()).getFacebookProfile(),es,settings,p,updateSemaphore);
//                    }
//                    updateSemaphore.awaitCompletion();
//                    parent.setCompleted(true);
//                    parent.setRealTimeInMinutes((System.currentTimeMillis()-timeBeginnig)/60000);
//                    parent = pdr.save(parent);
//                    break;
//                }
//            }
//            
//        } catch (InterruptedException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//    }
    
    @Async
    @Override
    public void crawlFacebookData(CrawlingInfo ci){
        LOG.log(Level.INFO,"start crawling data with parameters: {0}",ci.toString());
        CrawlingEngineSettings settings = createCrawlingEngineSettings();
        ProfileData parent = ci.getPd();
        parent.setEstimateTimeInMinutes(getEstimatedCrawlingTimeInMinutes(settings, ci.getDepth(),  getAccountSatisfyingPerspective(ci.getAccount(), CrawlResultPerspective.FRIEND)!=null));
        parent = pdr.save(parent);
        long timeBeginnig = System.currentTimeMillis();
        for(CrawlResultPerspective p:CrawlResultPerspective.values()){
            Account account = getAccountSatisfyingPerspective(ci.getAccount(), p);
            if(account!=null){
                FacebookProfile tree = crawlRecursively(ci.getUrl(),settings,account,null,ci.getDepth(),1,p,parent.getId());
                saveFacebookProfileForHead(parent, tree);
                parent.setCompleted(true);
                parent.setRealTimeInMinutes((System.currentTimeMillis()-timeBeginnig)/60000);
                parent = pdr.save(parent);
                break;
            }
        }
    }
    
    private void updateFacebookProfile(Account account,FacebookProfile profile,ExecutorService es,CrawlingEngineSettings settings,CrawlResultPerspective perspective,InverseSemaphore semaphore){
        profile = fpr.findOne(profile.getId());
        for(FacebookProfile p:profile.getFriends()){
            try {
                es.submit(new CrawlingEngine(settings, new URI(profile.getFbUrl()), account, (object)->{
                    try {
                        if(p.getAttributesForPerspective(perspective.name())==null){
                            JsonStoringEntity entity = new JsonStoringEntity(object.toString());
                            entity.setPerspective(perspective.name());
                            entity = jser.save(entity);
                            p.getAttributes().add(entity);
                            fpr.save(p);

                            updateFacebookProfile(account, p, es, settings, CrawlResultPerspective.getWeakerFor(perspective),semaphore);
                        }
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    } finally{
                       semaphore.taskCompleted(); 
                    }
                    
                }));
            } catch (URISyntaxException ex) {
                LOG.log(Level.SEVERE, null, ex);
            } finally{
                semaphore.beforeSubmit();
            }
        }
    }
    
    private Account getAccountSatisfyingPerspective(FacebookLoginAccount friendAccount,CrawlResultPerspective perspective){
        switch (perspective){
            case FRIEND:
                if(friendAccount!=null)
                return am.get(friendAccount.getLogin(), friendAccount.getPassword());
                break;
            case FRIEND_OF_FRIEND:
                if(friendAccount!=null&&friendAccount.getFriend()!=null)
                    return am.get(friendAccount.getFriend().getLogin(), friendAccount.getFriend().getPassword());
                break;
            case STRANGER:
                List<Account> diff = am.getWorkingAccounts();
                if(friendAccount!=null){diff.remove(am.get(friendAccount.getLogin(), friendAccount.getPassword()));}
                if(friendAccount!=null&&friendAccount.getFriend()!=null){diff.remove(am.get(friendAccount.getFriend().getLogin(), friendAccount.getFriend().getPassword()));}
                Collections.shuffle(diff);
                return diff.isEmpty()?null:diff.get(0);
        }   
        return null;
    }
    
    private void crawl(String target,CrawlingEngineSettings settings,Account account,FacebookProfile parent,int depthMax,int depthActual,ProfileData head,ExecutorService es,CrawlResultPerspective perspective,InverseSemaphore semaphore){
        try {
            LOG.log(Level.INFO,"start crawling target: {0} with call params: depthMax={1} depthActual={2} crawlResultPerspective={3}",new Object[]{target,depthMax,depthActual,perspective});
            URI t = new URI(target);
            es.submit(new CrawlingEngine(settings,t,account, (obj)->{
                try {
                    FacebookProfile profile;
                    FacebookProfile sameUrlProfile = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(head.getId(), target);
                    if(sameUrlProfile==null){
                        JsonStoringEntity entity = new JsonStoringEntity(obj.toString());
                        entity.setPerspective(perspective.name());
                        jser.save(entity);
                        profile = new FacebookProfile();
                        profile.setFbUrl(t.toString());
                        profile.getAttributes().add(entity);
                        
                        profile = fpr.save(profile);
                    } else profile = sameUrlProfile;

                    if(depthActual==1) saveFacebookProfileForHead(head, profile);
                    else establishFriendship(parent, profile);

                    if(depthActual<depthMax){
                        List<String> ids = new ArrayList<>();
                        for(JsonElement e: obj.get(AttributeName.FRIEND_IDS.getName()).getAsJsonArray()){
                            if(!e.isJsonNull()) ids.add(e.toString());
                        }   
                        Collections.shuffle(ids);

                        for(int i=0;i<Math.min(settings.getMaxFriendsToCollect(),ids.size());i++){
                            crawl("https://facebook.com/profile.php?id="+ids.get(i).replace("\"", ""),
                                    settings,
                                    account,
                                    profile,
                                    depthMax,
                                    depthActual+1,
                                    head,
                                    es,
                                    CrawlResultPerspective.getWeakerFor(perspective),
                                    semaphore);
                        }
                    }
                } catch(Exception ex){
                    LOG.log(Level.SEVERE, null, ex);
                } finally{
                    semaphore.taskCompleted();
                    LOG.log(Level.INFO,"semaphore: {0}",semaphore.getValue());
                }     
            }));   
        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        finally{
            semaphore.beforeSubmit();
            LOG.log(Level.INFO,"semaphore: {0}",semaphore.getValue());
        }
    }
    
    private FacebookProfile crawlRecursively(String target,CrawlingEngineSettings settings,Account account,FacebookProfile parent,int depthMax,int depthActual,CrawlResultPerspective perspective,Long profileDataId){
        try {
            URI t = new URI(target);
            JsonObject res = new CrawlingCallable(settings, t, account).call();
            FacebookProfile profile;
            FacebookProfile sameUrlProfile = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(profileDataId, target);
            if(sameUrlProfile==null){
                JsonStoringEntity entity = new JsonStoringEntity(res.toString());
                entity.setPerspective(perspective.name());
                jser.save(entity);
                profile = new FacebookProfile();
                profile.setFbUrl(t.toString());
                profile.getAttributes().add(entity);

                profile = fpr.save(profile);
            } else profile = sameUrlProfile;

            if(parent!=null)
            establishFriendship(parent, profile);

            if(depthActual<depthMax){
                List<String> ids = new ArrayList<>();
                for(JsonElement e: res.get(AttributeName.FRIEND_IDS.getName()).getAsJsonArray()){
                    if(!e.isJsonNull()) ids.add(e.toString());
                }   
                Collections.shuffle(ids);

                for(int i=0;i<Math.min(settings.getMaxFriendsToCollect(),ids.size());i++){
                    crawlRecursively("https://facebook.com/profile.php?id="+ids.get(i).replace("\"", ""),
                            settings,
                            account,
                            profile,
                            depthMax,
                            depthActual+1,
                            CrawlResultPerspective.getWeakerFor(perspective),
                            profileDataId);
                }
            }
            return profile;
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProfileDataModelImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void saveFacebookProfileForHead(ProfileData head,FacebookProfile profile){
        LOG.log(Level.INFO,"saving facebookProfile {0} for profileData: {1}",new String[]{head.toString(),profile.toString()});
        head = pdr.findOne(head.getId());
        profile = fpr.save(profile);
        head.setFacebookProfile(profile);
        head = pdr.save(head);
    }
    
    private void establishFriendship(FacebookProfile p1,FacebookProfile p2){
        LOG.log(Level.INFO,"establishing friendship between {0} and {1}",new String[]{p1.toString(),p2.toString()});
        p1 = fpr.findOne(p1.getId());
        p2 = fpr.findOne(p2.getId());
        p1.getFriends().add(p2);
        p1 = fpr.save(p1);
        p2.getFriends().add(p1);
        p2 = fpr.save(p2);
    }

    private CrawlingEngineSettings createCrawlingEngineSettings(){
        
        CrawlingSettings settings = getCrawlingSettings();
        return CrawlingEngineSettings.getStaticBuilder()
                .setWebDriverOption(settings.getWebDriverOption())
                .setChangeAccountAfter(settings.getChangeAccountAfter())
                .setCollectAttributes(AttributeName.values())
                .setDelayBeforeRunInMillis(settings.getDelayBeforeRunInMillis())
                .setLongWaitMillis(settings.getLongWaitMillis())
                .setRequestDelay(settings.getRequestDelay())
                .setShortWaitMillis(settings.getShortWaitMillis())
                .setWaitForElemLoadSec(settings.getWaitForElemLoadSec())
                .setMaxFriendsToCollect(settings.getMaxFriendsToCollect())
                .setMaxFriendsToDiscover(settings.getMaxFriendsToCollect())
                .build();
    }
     
    private CrawlingSettings getCrawlingSettings(){
        CrawlingSettings settings;
        if(!csr.findAll().iterator().hasNext()){
            settings = new CrawlingSettings();
            csr.save(settings);
            return settings;
        }
        return csr.findAll().iterator().next();
    } 
    
    private long getEstimatedCrawlingTimeInMinutes(CrawlingEngineSettings settings,int depth,boolean hasFriendAccount){
        return (long)Math.pow(settings.getMaxFriendsToCollect(), depth-1)*(settings.getShortWaitMillis()*(settings.getMaxFriendsToCollect()/20)+settings.getMaxFriendsToCollect()*1000)/60000;
    }

    @Override
    public Map<String, Map<AttributeDefinition, Object>> getAttributeMatrixForPerspective(CrawlResultPerspective perspective,Long profileDataId) {
        FacebookProfile target = pdr.findOne(profileDataId).getFacebookProfile();
        List<FacebookProfile> friends = fpr.getFriendshipTreeForFacebookProfile(target.getId());
        JsonParser parser = new JsonParser();
        Set<JsonObject> objects = new HashSet<>();
        friends.forEach((f)->{
            if(f.getAttributesForPerspective(perspective.name())!=null)
                objects.add(parser.parse(f.getAttributesForPerspective(perspective.name()).getJsonString()).getAsJsonObject());
        });
        Map<String,Map<AttributeDefinition,Object>> res = fcap.parsefromSourceSet(objects);
        if(target.getAttributesForPerspective(perspective.name())!=null)
            res.put("target", fcap.parseFromSource(parser.parse(target.getAttributesForPerspective(perspective.name()).getJsonString()).getAsJsonObject()));
        return res;
    }
}
