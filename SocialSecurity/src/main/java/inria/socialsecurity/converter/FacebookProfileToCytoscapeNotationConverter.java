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
import inria.socialsecurity.constants.BasicComplexAttributes;
import inria.socialsecurity.constants.BasicPrimitiveAttributes;
import inria.socialsecurity.converter.transformer.AttributesParser;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.user.FacebookProfile;
import inria.socialsecurity.entity.user.JsonStoringEntity;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.FacebookProfileRepository;
import inria.socialsecurity.repository.JsonStoringEntityRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author adychka
 */
public class FacebookProfileToCytoscapeNotationConverter extends CytoscapeNotationConverter<FacebookProfile>{
 
    @Autowired
    FacebookProfileRepository fpr;
    
    @Autowired
    JsonStoringEntityRepository jser;
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Autowired
    @Qualifier("basic parser")
    AttributesParser ap;
    
    DisplayNotationFactory factory;
    
    
    @Override
    public JsonElement convertFrom(FacebookProfile object) {
        factory = new DisplayNotationFactory(100,400,400,20);
        JsonObject res = createDefaultJsonObject();
        
        createFriendshipGraph(res, fpr.getFriendshipTreeForFacebookProfile(object.getId()),object.getId());
        return res;
    }
    
    private void createFriendshipGraph(JsonObject source,List<FacebookProfile> tree,Long targetId){
        for(FacebookProfile profile:tree){
            saveProfileData(profile, source, profile.getDisplayNotation()!=null?null:factory.next(),targetId);
            for(Integer id:fpr.getFriendIdsForFacebookProfile(profile.getId())){
                saveFriendship(profile.getId(), Integer.toUnsignedLong(id), source);
            }
        }
        
    }
    
    private String getAttributeFromFacebookProfile(AttributeDefinition ad,Long fbProfileId){
        List<JsonStoringEntity> entities = jser.getAttributesForFacebookProfile(fbProfileId);
        JsonParser parser = new JsonParser();
        for(JsonStoringEntity entity:entities){
            if(!entity.getJsonString().isEmpty()){
                JsonObject object = parser.parse(entity.getJsonString()).getAsJsonObject();
                String res = ap.getValueForAttribute(object, ad);
                if(!res.equals("-"))
                    return res;
            }
            
        }
        return null;
    }
    
    private void saveProfileData(FacebookProfile profile,JsonObject source,JsonObject displayNotation,Long targetId){
        if(profile==null) return;
        if(displayNotation!=null)
            profile.setDisplayNotation(displayNotation.toString());
        JsonObject object = new JsonObject();
        object.addProperty("group", "nodes");
        String gender = getAttributeFromFacebookProfile(adr.findPrimitiveAttributeDefinitionByName(BasicPrimitiveAttributes.GENDER.getValue()), profile.getId());
        object.addProperty("classes", gender!=null?gender:"undefined"+" "+(Objects.equals(targetId, profile.getId())?"target":""));
        JsonObject data = new JsonObject();
        data.addProperty("id", "" + profile.getId());
        String fullName = getAttributeFromFacebookProfile(adr.findComplexAttributeDefinitionByName(BasicComplexAttributes.FULL_NAME.getName()), profile.getId());
        data.addProperty("label", fullName);
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
        Long id = destination.getAsJsonObject().get("id").getAsLong();
        FacebookProfile profile = fpr.getOne(id);
        profile.setDisplayNotation(destination.getAsJsonObject().get("position").toString());
        fpr.save(profile);
        return null;
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
                moveDeg = (int) (moveDeg*0.8);
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
