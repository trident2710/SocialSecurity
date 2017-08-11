/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

/**
 *
 * @author adychka
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.constants.RiskSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * calculates attribute visibility by comparing the attribute value received form 3 perspectives: friend, friend of friend, stranger
 * AND with respect to the target profile i.e. respecting mutuality of friendship between target and examined profile
 * @author adychka
 */
public class FacebookDatasetToTargetViewAttributeVisibilityTransformer extends FacebookDatasetToAttributeVisibilityTransformer{
    
    private List<String> targetFriendIds;
    
    public void setTargetFriendIds( List<String> targetFriendIds){
        this.targetFriendIds =targetFriendIds;    
    }
    
    @Override
    public Map<String, String> parseFromSource(Map<String,JsonObject> source) {
        Map<String,String> res = new HashMap<>();
        
        adr.findAll().forEach((a)->{
            StringBuilder builder = new StringBuilder();
            for(CrawlResultPerspective perspective:CrawlResultPerspective.values()){
                if(source.containsKey(perspective.name())){
                    if(!getValueForAttribute(source.get(perspective.name()),a).equals("-")&&!getValueForAttribute(source.get(perspective.name()),a).contains("Ask for")){
                        int mutuality = 0;
                        if(source.get(CrawlResultPerspective.FRIEND.name()).has("friend_ids")){
                            mutuality = checkMutualFriendship(source.get(CrawlResultPerspective.FRIEND.name()).get("friend_ids").getAsJsonArray());
                        }
                        switch(perspective){
                        case FRIEND: 
                            switch(mutuality){
                            case -1:
                                builder.append(" ").append(RiskSource.A2.getValue());
                                break;
                            case 0:
                                builder.append(" ").append(RiskSource.A1.getValue());
                                builder.append(" ").append(RiskSource.A2.getValue());
                                break;
                            case 1:
                            case 2:
                                builder.append(" ").append(RiskSource.A1.getValue());
                                break;
                            }   
                            break;
                        case FRIEND_OF_FRIEND:
                        case STRANGER:
                            switch(mutuality){
                            case -1:
                                builder.append(" ").append(RiskSource.A1.getValue());
                                builder.append(" ").append(RiskSource.A2.getValue());
                                builder.append(" ").append(RiskSource.A4.getValue());
                                break;
                            case 0:
                                builder.append(" ").append(RiskSource.A1.getValue());
                                builder.append(" ").append(RiskSource.A2.getValue());
                                builder.append(" ").append(RiskSource.A4.getValue());
                                break;
                            case 1:
                            case 2:
                                builder.append(" ").append(RiskSource.A1.getValue());
                                break;
                            }   
                            break;
                        }   
                    }        
                }    
            }
            
            String s = builder.toString();
            if(s.isEmpty()) s="-";
            
            res.put(a.getDisplayName(), Arrays.stream(s.split(" ")).distinct().collect(Collectors.joining(" ")));
        });
        return res;
    }

    /**
     * crecks the mutuality of friendship
     * @param friendIds - examined profile friend ids
     * @return 
     *  2 if the profile does not have friends
     *  1 if the profile has only mutual friends 
     *  0 if profile has both mutual and not mutual friends
     *  -1 if profile has only non mutual friends
     * 
     */
    private int checkMutualFriendship(JsonArray friendIds){
        if(friendIds.size()==0) return 2;
        boolean containsDifferent = false;
        boolean containsMutual = false;
        for(JsonElement e:friendIds){
            if(containsMutual&&containsDifferent) break;
            if(!e.isJsonNull()){
                if(targetFriendIds.stream().anyMatch((s)->{return s.contains(e.getAsString());})) {
                    containsMutual=true;
                }
                else{
                    containsDifferent=true;  
                }       
            } 
        }
        return containsMutual?(containsDifferent?0:1):-1;
    }
   
}

