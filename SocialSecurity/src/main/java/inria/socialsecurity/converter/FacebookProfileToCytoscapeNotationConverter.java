/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.repository.FacebookProfileRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class FacebookProfileToCytoscapeNotationConverter extends CytoscapeNotationConverter<FacebookProfile>{
 
    @Autowired
    FacebookProfileRepository fpr;
    
    DisplayNotationFactory factory = new DisplayNotationFactory(100,400,400,36);
    
    
    @Override
    public JsonElement convertFrom(FacebookProfile object) {
        JsonObject res = createDefaultJsonObject();
        
        createFriendshipGraph(res, fpr.getFriendshipTreeForFacebookProfile(object.getId()));
        return res;
    }
    
    private void createFriendshipGraph(JsonObject source,List<FacebookProfile> tree){
        for(FacebookProfile profile:tree){
            saveProfileData(profile, source, profile.getDisplayNotation()==null?factory.next():null);
           
            try {
                Set<Long> s = fpr.getFriendIdsForFacebookProfile(profile.getId());
//                while (s.iterator().hasNext()) {                    
//                    System.out.println(s.iterator().next());
//                }
                for(Long i:s){
                    System.out.println(i);
                }
                System.out.println("wtf");
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(Long id:fpr.getFriendIdsForFacebookProfile(profile.getId())){
                saveFriendship(profile.getId(), id, source);
            }
        }
        
    }
    
    private void saveProfileData(FacebookProfile profile,JsonObject source,JsonObject displayNotation){
        if(profile==null) return;
        if(displayNotation!=null)
            profile.setDisplayNotation(displayNotation.toString());
        JsonObject object = new JsonObject();
        object.addProperty("group", "nodes");
        object.addProperty("classes", profile.getClass().getSimpleName());
        JsonObject data = new JsonObject();
        data.addProperty("id", "" + profile.getId());
        data.addProperty("label", profile.getFbUrl().replace("https://facebook.com/profile.php?id=", ""));
        object.add("data", data);
        object.add("position",new JsonParser().parse(profile.getDisplayNotation()));
        source.get("elements").getAsJsonObject().get("nodes").getAsJsonArray().add(object);
    }
    
    private void saveFriendship(Long from,Long to,JsonObject source){
        JsonObject object = new JsonObject();
        JsonObject data = new JsonObject();
        object.addProperty("group", "edges");
        data.addProperty("id", "" + from + "-" + to);
        data.addProperty("source", "" + from);
        data.addProperty("target", "" + to);
        object.add("data", data);

        source.get("elements").getAsJsonObject().get("edges").getAsJsonArray().add(object);
    }

    @Override
    public FacebookProfile convertTo(JsonElement destination) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private class DisplayNotationFactory{
        double angle;
        int level;
        int radius;
        int startX;
        int startY;
        int moveDeg;
        
        public DisplayNotationFactory(int radius, int startX,int startY,int moveDeg){
            this.radius = radius;
            this.startX = startX;
            this.startY = startY;
            this.moveDeg = moveDeg;
        }
        
        public JsonObject next(){
            if(angle==0&&level==0){
                level = 1;
                return getDefault();
            }
            if(angle>=360){
                level++;
                angle = 0;
            }
            JsonObject res = createForXY(startX-radius*level*Math.sin(Math.toRadians(angle)), startY+radius*level*Math.cos(Math.toRadians(angle)));
            angle+=moveDeg;
            return res;
        }
        
        private JsonObject getDefault(){
            return createForXY(400, 400);
        }
        
        private JsonObject createForXY(double x,double y){
            JsonObject object = new JsonObject();
            object.addProperty("x", x);
            object.addProperty("y", y);
            return object;
        }
    }
    
}
