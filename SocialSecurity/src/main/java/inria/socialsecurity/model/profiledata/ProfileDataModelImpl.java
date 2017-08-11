/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.crawlerv2.engine.CrawlingInstanceSettings;
import inria.crawlerv2.engine.account.Account;
import inria.crawlerv2.provider.AttributeName;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.converter.transformer.AttributeMatrixToJsonConverter;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeMatrixTransformer;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeVisibilityTransformer;
import inria.socialsecurity.converter.transformer.FacebookDatasetToTargetViewAttributeVisibilityTransformer;
import inria.socialsecurity.converter.transformer.FacebookTrueVisibilityToAttributeMatrixTransformer;
import inria.socialsecurity.converter.transformer.MapToJsonConverter;
import inria.socialsecurity.entity.analysis.AnalysisReportItem;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.entity.settings.CrawlingSettings;
import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.JsonStoringEntity;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.model.analysis.HarmTreeValidator;
import inria.socialsecurity.model.analysis.ProfileDataAnalyzer;
import inria.socialsecurity.repository.CrawlingSettingsRepository;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import inria.socialsecurity.repository.JsonStoringEntityRepository;
import inria.socialsecurity.repository.ProfileDataRepository;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.template.Neo4jOperations;
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
    FacebookLoginAccountRepository flar;
    
    @Autowired
    FacebookDatasetToAttributeMatrixTransformer fdtamt;
    
    @Autowired
    FacebookDatasetToAttributeVisibilityTransformer fdtavt;
    
    @Autowired
    @Qualifier("target respecting visibility")
    FacebookDatasetToTargetViewAttributeVisibilityTransformer fdttvavt;
    
    @Autowired
    AttributeMatrixToJsonConverter amtjc;
    
    @Autowired
    CrawlingEngineFactory factory;
    
    @Autowired
    ProfileDataAnalyzer pda;
    
    @Autowired
    HarmTreeRepository htr;
    
    @Autowired
    FacebookTrueVisibilityToAttributeMatrixTransformer ftvtamt;
    
    @Autowired
    MapToJsonConverter mtjc;
    
    @Autowired
    CrawlingEngineFactory cef;
    
    @Autowired
    Session session;
    
    @Autowired
    Neo4jOperations template;
    
    private static final Logger LOG = Logger.getLogger(ProfileDataModel.class.getName());
    
    @Override
    public void setCrawlingEngineFactory(CrawlingEngineFactory cef){
        this.cef = cef;
    }
    
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
        
        String name,fbUrl;
        Account[] accounts = new Account[3];
        
        if(request.getParameter("name")==null||request.getParameter("name").isEmpty())
            throw new WrongArgumentException();
        name = request.getParameter("name");
        
        if(request.getParameter("fburl")==null||request.getParameter("fburl").isEmpty())
            throw new WrongArgumentException();
        fbUrl = request.getParameter("fburl");
        
        String tl,tp;
        if(request.getParameter("t_login")==null||request.getParameter("t_login").isEmpty())
            throw new WrongArgumentException();
        tl = request.getParameter("t_login");
        if(request.getParameter("t_pass")==null||request.getParameter("t_pass").isEmpty())
            throw new WrongArgumentException();
        tp = request.getParameter("t_pass");
        accounts[0]=new Account(tl, tp, Boolean.FALSE);
        
        if(request.getParameter("t_prime_login")==null||request.getParameter("t_prime_login").isEmpty())
            throw new WrongArgumentException();
        tl = request.getParameter("t_prime_login");
        if(request.getParameter("t_prime_pass")==null||request.getParameter("t_prime_pass").isEmpty())
            throw new WrongArgumentException();
        tp = request.getParameter("t_prime_pass");
        accounts[1]=new Account(tl, tp, Boolean.FALSE);
        
        if(request.getParameter("t_sec_login")==null||request.getParameter("t_sec_login").isEmpty())
            throw new WrongArgumentException();
        tl = request.getParameter("t_sec_login");
        if(request.getParameter("t_sec_pass")==null||request.getParameter("t_sec_pass").isEmpty())
            throw new WrongArgumentException();
        tp = request.getParameter("t_sec_pass");
        accounts[2]=new Account(tl, tp, Boolean.FALSE);
        
        boolean colFr = false;
        if(request.getParameter("col_ff")!=null&&request.getParameter("col_ff").equals("true"))
            colFr = true;
       
        ProfileData data = new ProfileData();
        data.setName(name);
        data.setRequestUrl(fbUrl);
        data = pdr.save(data);
        return new CrawlingInfo(data, accounts,colFr);
    }
    
    @Async
    @Override
    public void crawlFacebookData(CrawlingInfo ci){
        ProfileData parent = pdr.save(ci.getProfileData());
        Map<CrawlResultPerspective,List<JsonObject>> friends = new HashMap<>();
        JsonObject targetData = null;
        JsonObject trueVis = null;
        try {
            LOG.log(Level.INFO,"start crawling data");
            CrawlingInstanceSettings settings = createCrawlingEngineSettings();
            String target = ci.getProfileData().getRequestUrl();
            
            parent.setEstimateFinishTime(getEstimatedCrawlingTimeInMillis(settings, 2));
            parent = pdr.save(parent);
            trueVis = collectTrueVisibility(ci.getAccounts()[0], target, settings);
            
            targetData = getTargetData(settings, target, ci.getAccounts()[1]);
            if(targetData!=null){
                if( targetData.has(AttributeName.FRIEND_IDS.getName())){
                    if(!ci.isShouldCollectFF())
                        settings.setAttributes(getAttributesWithoutFriends());
                    for(CrawlResultPerspective perspective:CrawlResultPerspective.values()){
                        friends.put(perspective, new ArrayList<>());
                        Account a = ci.getAccounts()[perspective.ordinal()];
                        if(perspective.ordinal()>0)
                            settings.setAttributes(getAttributesWithoutFriends());
                        List<String> ids = new ArrayList<>();
                        for(JsonElement e:  targetData.get(AttributeName.FRIEND_IDS.getName()).getAsJsonArray()){
                            if(!e.isJsonNull()) ids.add(e.getAsString());
                        }   
                        Collections.shuffle(ids);
                        for(int i=0;i<Math.min(settings.getMaxFriendsToCollect(),ids.size());i++){
                            String url = "https://facebook.com/profile.php?id="+ids.get(i);
                            try {
                                JsonObject f = getTargetData(settings, url, a);
                                if(f!=null){
                                    friends.get(perspective).add(f);
                                }
                            } catch (Exception ex) {
                                LOG.log(Level.SEVERE, "unable to get friend", ex);
                            }
                        }
                    }   
                    try {
                        if(trueVis!=null){
                            LOG.log(Level.INFO,"true visibility: {0}",trueVis.toString());
                            parent.setAttributeVisibilityJsonString(trueVis.toString());
                            parent.setRiskSourceForAttributesJsonString(mtjc.convertFrom(ftvtamt.parseFromSource(trueVis)).toString());
                            parent = pdr.save(parent);
                        } else{
                            LOG.log(Level.INFO,"unable to collect true visibility");
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "unable to collect true visibility",e);
                    }
                    try {
                        
                        putDataToDB(parent, friends, targetData);
                        parent = pdr.save(parent);
                        LOG.log(Level.INFO,"filling attribute matrices");
                        fillAttributeMatrices(parent);
                        parent = pdr.save(parent);
                        LOG.log(Level.INFO,"done");
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "unable to put data to db",e);
                    }
                }    
            } else{
                LOG.log(Level.SEVERE, "unable to get target data");
            }
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "exception in crawl data", ex);
        }
        finally{          
            LOG.log(Level.SEVERE, "final block");
            parent.setRealFinishTime(System.currentTimeMillis());
            parent.setCompleted(Boolean.TRUE);
            parent = pdr.save(parent);
        }
    }
    
    public void putDataToDB(ProfileData data,Map<CrawlResultPerspective,List<JsonObject>> friends,JsonObject targetData){
        LOG.log(Level.INFO, "put data to DB");
        FacebookProfile target = new FacebookProfile();
        LOG.info("adding target");
        target.setFbUrl(data.getRequestUrl());
        target = fpr.save(target);
        data.setFacebookProfile(target);
        pdr.save(data);
        if(targetData.has(AttributeName.ID.getName())){
            target.setFbUrl("https://facebook.com/profile.php?id="+targetData.get(AttributeName.ID.getName()).getAsString());
            LOG.log(Level.INFO, "adding friends");
            Set<FacebookProfile> res = establishMutualFriendship(createProfilesForFriends(friends));
            int i=0;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            for(FacebookProfile p:res){
                establsihFriendship(target, p);
                LOG.log(Level.INFO,"saving friend {0}/{1}",new String[]{""+i++,""+res.size()});
            }
        }
        LOG.info("done");
    }
    
    private void establsihFriendship(FacebookProfile p1,FacebookProfile p2){
        try {
            template.execute("MATCH (a:FacebookProfile),(b:FacebookProfile) WHERE id(a) = "+p1.getId()+" AND id(b)= "+p2.getId()+" CREATE UNIQUE (a)-[r:HAS_FRIENDS]-(b)");
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
    
    private Set<FacebookProfile> establishMutualFriendship(Map<FacebookProfile,List<String>> friends){
        LOG.log(Level.INFO, "establishing mutual friendship");
        for(Map.Entry<FacebookProfile,List<String>> e:friends.entrySet()){
            for(String url:e.getValue()){
                for(FacebookProfile p:friends.keySet()){
                    if(p.getFbUrl().equals(url)){
                        establsihFriendship(p, e.getKey());
                    }
                }
            }
        }
        LOG.log(Level.INFO, "establishing mutual friendship finish");
        return friends.keySet();
    }
    
    private Map<FacebookProfile,List<String>> createProfilesForFriends(Map<CrawlResultPerspective,List<JsonObject>> friends){
        LOG.log(Level.INFO, "creating profiles for friends");
        Map<FacebookProfile,List<String>> res = new HashMap<>();
        if(!friends.keySet().isEmpty()){
            List<JsonObject> objects = friends.get(CrawlResultPerspective.FRIEND);
            for(JsonObject o:objects){
                if(o.has(AttributeName.ID.getName())){
                    FacebookProfile profile = new FacebookProfile();
                    profile.setFbUrl("https://facebook.com/profile.php?id="+o.get(AttributeName.ID.getName()).getAsString());
                    List<String> friendIds = new ArrayList<>();
                    if(o.has(AttributeName.FRIEND_IDS.getName())){
                        
                        JsonArray f = o.get(AttributeName.FRIEND_IDS.getName()).getAsJsonArray();
                        for(JsonElement el:f){
                            if(!el.isJsonNull()&&el.isJsonPrimitive()){
                                LOG.log(Level.INFO, "adding friend ids");
                                friendIds.add("https://facebook.com/profile.php?id="+el.getAsString());
                            }
                        }
                    }
                    Map<CrawlResultPerspective,JsonObject> same = getPerspectivesFor(o.get(AttributeName.ID.getName()).getAsString(), friends);
                    for(Map.Entry<CrawlResultPerspective,JsonObject> e:same.entrySet()){
                        JsonStoringEntity obj = new JsonStoringEntity();
                        obj.setPerspective(e.getKey().name());
                        obj.setJsonString(e.getValue().toString());
                        profile.getAttributes().add(obj);
                    }
                    profile = fpr.save(profile);
                    res.put(profile, friendIds);
                }
            }    
        }
        LOG.log(Level.INFO, "creating profiles for friends finish");
        return res;
    }
    
    private Map<CrawlResultPerspective,JsonObject> getPerspectivesFor(String id,Map<CrawlResultPerspective,List<JsonObject>> friends){
        LOG.log(Level.INFO, "getting perspectives for");
        Map<CrawlResultPerspective,JsonObject> res = new HashMap();
        for(CrawlResultPerspective p:CrawlResultPerspective.values()){
            for(JsonObject o:friends.get(p)){
                if(o.has(AttributeName.ID.getName())){
                    if(o.get(AttributeName.ID.getName()).getAsString().equals(id)){
                        res.put(p, o);
                    }
                }
            }
        }
        LOG.log(Level.INFO, "getting perspectives finish");
        return res;
    }
    
    private JsonObject collectTrueVisibility(Account t,String target,CrawlingInstanceSettings settings){
        try {
            LOG.log(Level.INFO,"collect true visibility for : {0}",target);
            return cef.createCrawlingVisibilityCallable(settings, target, t).call();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "unable to collect true visibility");
            return null;
        }
    }
    
    private JsonObject getTargetData(CrawlingInstanceSettings settings,String target,Account a){
        try {
            LOG.log(Level.INFO,"get target data for : {0}",target);
            return cef.createCrawlingCallable(settings, target, a).call();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "unable to facebook profile");
            return null;
        }
    }
    
    private void fillAttributeMatrices(ProfileData data){
        LOG.log(Level.INFO, "filling attribute matrices");
        for(CrawlResultPerspective p:CrawlResultPerspective.values()){
            getAttributeMatrixForPerspective(p, data.getId());
        }
        getAttributeVisibilityMatrix(data.getId());
        LOG.log(Level.INFO, "filling attribute matrices finish");
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
    
    private long getEstimatedCrawlingTimeInMillis(CrawlingInstanceSettings settings,int depth){
        double totalAccs = settings.getMaxFriendsToCollect();
        int perspectives = 3;
        int friendsDiscover = settings.getMaxFriendsToDiscover();
        double crawlTime = settings.getShortWaitMillis()*settings.getAttributes().length;
        double crawlTimeWithFriends = settings.getShortWaitMillis()*settings.getAttributes().length+friendsDiscover*550;
        long res = System.currentTimeMillis()+(long) (totalAccs*(crawlTimeWithFriends+(perspectives-1)*crawlTime));
        return res;
    }

    @Override
    public Map<String,Map<String, String>> getAttributeMatrixForPerspective(CrawlResultPerspective perspective,Long profileDataId) {
        ProfileData data = pdr.findOne(profileDataId);
        JsonParser parser = new JsonParser();
//        if(!data.getAttributeMatrix().isEmpty()){
//            for(JsonStoringEntity entity:data.getAttributeMatrix()){
//                if(entity.getPerspective().equals(perspective.name())){
//                    return amtjc.convertTo(parser.parse(entity.getJsonString()).getAsJsonObject());
//                }
//            }    
//        }
        
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
//        if(data.getVisibilityRespectTargetMatrixJsonString()!=null){
//            return amtjc.convertTo(parser.parse(data.getVisibilityRespectTargetMatrixJsonString()).getAsJsonObject());
//        }
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
          
        fdttvavt.setTargetFriendIds(fpr.getUrlsInFriendshipTreeForFacebookProfile(profileDataId));
        Map<String,Map<String,String>> res2= fdttvavt.parsefromSourceSet(objects);
        data.setVisibilityRespectTargetMatrixJsonString(amtjc.convertFrom(res2).toString());
        
        
        pdr.save(data);
        return res2;
    }

    @Override
    public Map<String, String> getAttributesForFacebookProfileFromPerspective(Long facebookProfileId, CrawlResultPerspective perspective) {
        JsonStoringEntity entity = jser.getAttributesForFacebookProfile(facebookProfileId, perspective.name());
        if(entity!=null)
            return fdtamt.parseFromSource(new JsonParser().parse(entity.getJsonString()).getAsJsonObject());
        return null;
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
    

    @Override
    public List<AnalysisReportItem> generateAnalysisReport(ProfileData data) {
        List<HarmTreeVertex> vertices = htr.getTreeVertices();
        List<AnalysisReportItem> results = new ArrayList<>();
        for(HarmTreeVertex vertex:vertices){
            JsonObject object = new JsonObject();
            AnalysisReportItem item = new AnalysisReportItem();
            try {
                item.setIsValid(true);
                item.setHarmTreeName(vertex.getName());
                item.setSeverity(vertex.getSeverity());
                Map.Entry<String,Set<Double>> res = pda.calculateLikelihoodForHarmTree(vertex, data);
                item.setLikelihood(res.getValue());
                Set<Double> score = res.getValue().stream().map(x->vertex.getSeverity()*x).collect(Collectors.toSet());
                item.setScore(score);
                List<Double> l = new ArrayList(score);
                Collections.sort(l);
                item.setBestCase(l.get(0));
                item.setWorstCase(l.get(l.size()-1));
                item.setReport(res.getKey().split("\n"));
            } catch (HarmTreeValidator.HarmTreeNotValidException ex) {
                item.setIsValid(false);
                item.setErrMsg(ex.getMessage());
                LOG.log(Level.INFO, "harm tree is not valid");
            }
            results.add(item);
        }
        return results;
    }


}
