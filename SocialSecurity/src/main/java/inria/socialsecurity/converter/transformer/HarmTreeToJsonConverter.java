/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import inria.socialsecurity.converter.Converter;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * converts harm tree from/to json representatin
 * used to store default harmtrees in properties to be able to restore them if needed
 * @author adychka
 */
public class HarmTreeToJsonConverter implements Converter<HarmTreeVertex, JsonObject>{
    
    @Autowired
    HarmTreeRepository htr;
    
    @Autowired
    AttributeDefinitionRepository adr;

    @Override
    public JsonObject convertFrom(HarmTreeVertex source) {
        return createJsonObjectRecursively(source);
    }

    @Override
    public HarmTreeVertex convertTo(JsonObject destination) {
        return (HarmTreeVertex)createHarmTreeRecursively(destination);
    }
    
    private JsonObject createJsonObjectRecursively(HarmTreeElement head){
        JsonObject object = new JsonObject();
        if(head instanceof HarmTreeVertex){
            object.addProperty("type", HarmTreeVertex.class.getSimpleName());
            object.addProperty("name", ((HarmTreeVertex) head).getName());
            object.addProperty("description", ((HarmTreeVertex) head).getDescription());
            object.addProperty("severity", ((HarmTreeVertex) head).getSeverity());
            JsonArray descendants = new JsonArray();
            for(HarmTreeLogicalNode n:((HarmTreeVertex) head).getDescendants()){
                descendants.add(createJsonObjectRecursively(n));
            }
            object.add("descendants", descendants);
        }
        if(head instanceof HarmTreeLogicalNode){
            head = htr.findOne(head.getId());
            object.addProperty("type", HarmTreeLogicalNode.class.getSimpleName());
            object.addProperty("logicalRequirement", ((HarmTreeLogicalNode) head).getLogicalRequirement());
            JsonArray loDescendants = new JsonArray();
            JsonArray leDescendants = new JsonArray();
            for(HarmTreeLogicalNode n:((HarmTreeLogicalNode) head).getDescendants()){
                loDescendants.add(createJsonObjectRecursively(n));
            }
            for(HarmTreeLeaf l:((HarmTreeLogicalNode) head).getLeafs()){
                leDescendants.add(createJsonObjectRecursively(l));
            }
            object.add("loDescendants", loDescendants);
            object.add("leDescendants", leDescendants);
        }
        if(head instanceof HarmTreeLeaf){
            head = htr.findOne(head.getId());
            object.addProperty("type", HarmTreeLeaf.class.getSimpleName());
            object.addProperty("attributeDefinition",((HarmTreeLeaf) head).getAttributeDefinition().getName());
            object.addProperty("riskSource",((HarmTreeLeaf) head).getRiskSource());
            object.addProperty("threatType",((HarmTreeLeaf) head).getThreatType());
        }
        object.addProperty("displayNotation", head.getDisplayNotation());
        return object;
    }
    
    private HarmTreeElement createHarmTreeRecursively(JsonObject object){
        if(object.get("type").getAsString().equals(HarmTreeVertex.class.getSimpleName())){
            HarmTreeVertex vertex = new HarmTreeVertex();
            vertex.setName(object.get("name").getAsString());
            vertex.setDescription(object.get("description").getAsString());
            vertex.setSeverity(object.get("severity").getAsDouble());
            for(JsonElement e:object.get("descendants").getAsJsonArray()){
                vertex.getDescendants().add((HarmTreeLogicalNode)createHarmTreeRecursively(e.getAsJsonObject()));
            }
            vertex.setDisplayNotation(object.get("displayNotation").getAsString());
            return vertex;
        }
        if(object.get("type").getAsString().equals(HarmTreeLogicalNode.class.getSimpleName())){
            HarmTreeLogicalNode node = new HarmTreeLogicalNode();
            node.setLogicalRequirement(object.get("logicalRequirement").getAsString());
            for(JsonElement e:object.get("loDescendants").getAsJsonArray()){
                node.getDescendants().add((HarmTreeLogicalNode)createHarmTreeRecursively(e.getAsJsonObject()));
            }
            for(JsonElement e:object.get("leDescendants").getAsJsonArray()){
                node.getLeafs().add((HarmTreeLeaf)createHarmTreeRecursively(e.getAsJsonObject()));
            }
            node.setDisplayNotation(object.get("displayNotation").getAsString());
            return node;
        }
        if(object.get("type").getAsString().equals(HarmTreeLeaf.class.getSimpleName())){
            HarmTreeLeaf node = new HarmTreeLeaf();
            node.setAttributeDefinition(adr.findByName(object.get("attributeDefinition").getAsString()));
            node.setRiskSource(object.get("riskSource").getAsString());
            node.setThreatType(object.get("threatType").getAsString());
            node.setDisplayNotation(object.get("displayNotation").getAsString());
            return node;
        }
        return null;
    }
    
}
