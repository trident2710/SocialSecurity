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
import inria.crawlerv2.engine.CrawlingRunable;
import inria.crawlerv2.engine.CrawlingInstanceSettings;
import inria.crawlerv2.engine.account.Account;
import inria.crawlerv2.provider.AttributeName;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.converter.transformer.AttributeMatrixToJsonConverter;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeMatrixTransformer;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeVisibilityTransformer;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.neo4j.ogm.session.Session;
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
    FacebookDatasetToAttributeMatrixTransformer fdtamt;
    
    @Autowired
    FacebookDatasetToAttributeVisibilityTransformer fdtavt;
    
    @Autowired
    AttributeMatrixToJsonConverter amtjc;
      
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
        data.setDepth(depth);
        pdr.save(data);
        
        return new CrawlingInfo(data, depth, fbUrl, account);
    }
    
    @Async
    @Override
    public void crawlFacebookData(CrawlingInfo ci){
        LOG.log(Level.INFO,"start crawling data with parameters: {0}",ci.toString());
        CrawlingInstanceSettings settings = createCrawlingEngineSettings();
        ProfileData parent = ci.getPd();
        parent.setEstimateTimeInMinutes(getEstimatedCrawlingTimeInMinutes(settings, ci.getDepth(),  getAccountSatisfyingPerspective(ci.getAccount(), CrawlResultPerspective.FRIEND)!=null));
        parent = pdr.save(parent);
        long timeBeginnig = System.currentTimeMillis();
        for(CrawlResultPerspective p:CrawlResultPerspective.values()){
            Account account = getAccountSatisfyingPerspective(ci.getAccount(), p);
            Long id ;
            if(account!=null){
                LOG.log(Level.INFO,"start crawling data");
                crawlRecursively(ci.getUrl(),settings,account,null,ci.getDepth(),1,p,parent);
                id = parent.getFacebookProfile().getId();
                LOG.log(Level.INFO,"update with weaker perspectives");
                updateFacebookProfilesWithWeakerPerspectives(pdr.findOne(ci.getPd().getId()).getFacebookProfile(),settings,ci.getAccount());
                LOG.log(Level.INFO,"filling attribute matrices");
                fillAttributeMatrices(parent);
                parent.setCompleted(true);
                parent.setRealTimeInMinutes((System.currentTimeMillis()-timeBeginnig)/60000);
                parent.setFacebookProfile(fpr.findOne(id));
                parent = pdr.save(parent);
                break;
            }
        }
    }
    
    private void fillAttributeMatrices(ProfileData data){
        for(CrawlResultPerspective p:CrawlResultPerspective.values()){
            getAttributeMatrixForPerspective(p, data.getId());
        }
        getAttributeVisibilityMatrix(data.getId());
    }
     
    private void updateFacebookProfilesWithWeakerPerspectives(FacebookProfile profile,CrawlingInstanceSettings settings,FacebookLoginAccount acc){
        settings.setAttributes(getAttributesWithoutFriends());
        try {
            Account ff = getAccountSatisfyingPerspective(acc, CrawlResultPerspective.FRIEND_OF_FRIEND);
            if(ff!=null){
                LOG.log(Level.INFO,"updating target from friend of friend perspective");
                JsonObject res = new CrawlingCallable(settings, new URI(profile.getFbUrl()), ff).call();
                JsonStoringEntity entity = new JsonStoringEntity(res.toString());
                entity.setPerspective(CrawlResultPerspective.FRIEND_OF_FRIEND.name());
                entity = jser.save(entity);
                profile.getAttributes().add(entity);
                fpr.save(profile); 
            }
            Account s = getAccountSatisfyingPerspective(acc, CrawlResultPerspective.STRANGER);
            if(s!=null){
                LOG.log(Level.INFO,"updating target from stranger perspective");
                JsonObject res = new CrawlingCallable(settings, new URI(profile.getFbUrl()), ff).call();
                JsonStoringEntity entity = new JsonStoringEntity(res.toString());
                entity.setPerspective(CrawlResultPerspective.STRANGER.name());
                entity = jser.save(entity);
                profile.getAttributes().add(entity);
                fpr.save(profile); 
            }
            for(FacebookProfile friend:profile.getFriends()){
                if(ff!=null||s!=null){
                    LOG.log(Level.INFO,"updating friend from stranger perspective");
                    JsonObject res = new CrawlingCallable(settings, new URI(friend.getFbUrl()), ff!=null?ff:s).call();
                    JsonStoringEntity entity = new JsonStoringEntity(res.toString());
                    entity.setPerspective(CrawlResultPerspective.STRANGER.name());
                    entity = jser.save(entity);
                    friend.getAttributes().add(entity);
                    fpr.save(friend); 
                }
            }
            
        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, "unable to update, wrong URI", ex);
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
    
    private void crawlRecursively(String target,CrawlingInstanceSettings settings,Account account,FacebookProfile parent,int depthMax,int depthActual,CrawlResultPerspective perspective,ProfileData head){
        try {
            URI t = new URI(target);
            JsonObject res = new CrawlingCallable(settings, t, account).call();
            FacebookProfile profile;
            FacebookProfile sameUrlProfile = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(head.getId(), target);
            if(sameUrlProfile==null){
                JsonStoringEntity entity = new JsonStoringEntity(res.toString());
                entity.setPerspective(perspective.name());
                jser.save(entity);
                profile = new FacebookProfile();
                profile.setFbUrl(t.toString());
                profile.getAttributes().add(entity);

                profile = fpr.save(profile);
            } else profile = sameUrlProfile;

            if(depthActual==1)
                saveFacebookProfileForHead(head, profile);
            if(parent!=null)
            establishFriendship(parent, profile);

            if(res.has(AttributeName.FRIEND_IDS.getName())){
                List<String> ids = new ArrayList<>();
                for(JsonElement e: res.get(AttributeName.FRIEND_IDS.getName()).getAsJsonArray()){
                    if(!e.isJsonNull()) ids.add(e.toString());
                }   
                Collections.shuffle(ids);

                for(int i=0;i<Math.min(settings.getMaxFriendsToCollect(),ids.size());i++){
                    String url = "https://facebook.com/profile.php?id="+ids.get(i).replace("\"", "");
                    FacebookProfile friendInGraph = fpr.findByFbUrlInFriendshipTreeForFacebookProfile(head.getId(), url);
                    if(friendInGraph!=null)
                        establishFriendship(friendInGraph, profile);
                    else
                    if(depthActual<depthMax)
                        crawlRecursively(url,settings,account,profile,depthMax,depthActual+1,CrawlResultPerspective.getWeakerFor(perspective), head);
                }
            }
            
        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, "unable to crawl, wrong URI", ex);
        }
    }
    
    private AttributeName[] getAttributesWithoutFriends(){
        AttributeName[] res = new AttributeName[AttributeName.values().length-1];
        int j=0;
        for(int i=0;i<AttributeName.values().length;i++){
            if(AttributeName.values()[i]!=AttributeName.FRIEND_IDS){
                res[j++] = AttributeName.values()[i];
            }
        }
        return res;
    }
    
    private void saveFacebookProfileForHead(ProfileData head,FacebookProfile profile){
        LOG.log(Level.INFO,"saving facebookProfile {0} for profileData: {1}",new String[]{head.toString(),profile.toString()});
        profile = fpr.save(profile);
        head.setFacebookProfile(profile);
        head = pdr.save(head);
    }
    
    private void establishFriendship(FacebookProfile p1,FacebookProfile p2){
        LOG.log(Level.INFO,"establishing friendship between {0} and {1}",new String[]{p1.toString(),p2.toString()});
        p1.getFriends().add(p2);
        LOG.log(Level.INFO,"saving");
        p1 = fpr.save(p1);
        LOG.log(Level.INFO,"done");   
    }

    private CrawlingInstanceSettings createCrawlingEngineSettings(){
        
        CrawlingSettings settings = getCrawlingSettings();
        return CrawlingInstanceSettings.getStaticBuilder()
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
    
    private long getEstimatedCrawlingTimeInMinutes(CrawlingInstanceSettings settings,int depth,boolean hasFriendAccount){
        return (long)Math.pow(settings.getMaxFriendsToCollect(), depth-1)*(settings.getShortWaitMillis()*(settings.getMaxFriendsToCollect()/20)+settings.getMaxFriendsToCollect()*1000)/60000*(hasFriendAccount?1:3);
    }

    @Override
    public Map<String,Map<String, String>> getAttributeMatrixForPerspective(CrawlResultPerspective perspective,Long profileDataId) {
        ProfileData data = pdr.findOne(profileDataId);
        JsonParser parser = new JsonParser();
        if(!data.getAttributeMatrix().isEmpty()){
            for(JsonStoringEntity entity:data.getAttributeMatrix()){
                if(entity.getPerspective().equals(perspective.name())){
                    return amtjc.convertTo(parser.parse(entity.getJsonString()).getAsJsonObject());
                }
            }    
        }
        
        FacebookProfile target = data.getFacebookProfile(); 
        List<FacebookProfile> friends = fpr.getFriendshipTreeForFacebookProfile(target.getId());
        
        Set<JsonObject> objects = new HashSet<>();
        friends.forEach((f)->{      
            List<JsonStoringEntity> lst  = jser.getAttributesForFacebookProfile(f.getId());
            for(JsonStoringEntity e:lst){
                if(e.getPerspective().equals(perspective.name())){
                    objects.add(parser.parse(e.getJsonString()).getAsJsonObject());
              }
            }       
        });
        
        Map<String,Map<String, String>> res = fdtamt.parsefromSourceSet(objects);
        JsonStoringEntity entity = new JsonStoringEntity();
        entity.setJsonString(amtjc.convertFrom(res).toString());
        entity.setPerspective(perspective.name());
        entity = jser.save(entity);
        data.getAttributeMatrix().add(entity);
        pdr.save(data);
        return res;
    }

    @Override
    public Map<String, Map<String, String>> getAttributeVisibilityMatrix(Long profileDataId) {
        ProfileData data = pdr.findOne(profileDataId);
        FacebookProfile target = data.getFacebookProfile();
        JsonParser parser = new JsonParser();
        if(data.getVisibilityMatrixJsonString()!=null){
            return amtjc.convertTo(parser.parse(data.getVisibilityMatrixJsonString()).getAsJsonObject());
        }
        List<FacebookProfile> friends = fpr.getFriendshipTreeForFacebookProfile(target.getId());
        
        Set<Map<String,JsonObject>> objects = new HashSet<>();
        friends.forEach((f)->{      
            List<JsonStoringEntity> lst  = jser.getAttributesForFacebookProfile(f.getId());
            Map<String,JsonObject> attrsForPersp = new HashMap<>();
            for(JsonStoringEntity e:lst){
                attrsForPersp.put(e.getPerspective(),parser.parse(e.getJsonString()).getAsJsonObject());
            }
            objects.add(attrsForPersp);
        });
        
        Map<String,Map<String,String>> res= fdtavt.parsefromSourceSet(objects);
        data.setVisibilityMatrixJsonString(amtjc.convertFrom(res).toString());
        pdr.save(data);
        return res;
    }
}
