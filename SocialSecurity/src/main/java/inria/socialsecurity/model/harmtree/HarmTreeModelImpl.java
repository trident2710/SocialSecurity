/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.harmtree;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import inria.socialsecurity.converter.HarmTreeToCytoscapeNotationConverter;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import java.util.List;
import org.neo4j.ogm.session.Session;
import org.neo4j.shell.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
public class HarmTreeModelImpl implements HarmTreeModel {

    @Autowired
    HarmTreeToCytoscapeNotationConverter converter;

    @Autowired
    HarmTreeRepository htr;

    @Autowired
    AttributeDefinitionRepository adr;

    @Autowired
    Session session;

    @Override
    public void updateHarmTreeNotationFromCytoscape(Long id, JsonElement displayNotation) {
        updateHarmTreeNodePositions(displayNotation);
    }

    @Override
    public JsonElement getCytoscapeDisplayNotationForHarmTree(Long id) {
        return converter.convertFrom((HarmTreeVertex) htr.findOne(id));
    }

    private void updateHarmTreeNodePositions(JsonElement displayNotation) {
        JsonArray nodes = displayNotation.getAsJsonObject()
                .get("elements").getAsJsonObject()
                .get("nodes").getAsJsonArray();

        for (JsonElement e : nodes) {
            Long id = e.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsLong();
            HarmTreeElement element = htr.findOne(id);
            element.setDisplayNotation(e.getAsJsonObject().get("position").getAsJsonObject().toString());
            htr.save(element);
        }
    }

    @Override
    public void updateHarmTreeElement(Long id, JsonElement properties) {
        System.out.println("update node");
        System.out.println(properties.toString());
        HarmTreeElement element = htr.findOne(id);
        if (element instanceof HarmTreeVertex) {
            HarmTreeVertex v = (HarmTreeVertex) element;
            v.setName(properties.getAsJsonObject().get("data").getAsJsonObject().get("name").getAsString());
            v.setDescription(properties.getAsJsonObject().get("data").getAsJsonObject().get("description").getAsString());
            v.setSeverity(properties.getAsJsonObject().get("data").getAsJsonObject().get("likelihood").getAsDouble());
            htr.save(v);
        }
        if (element instanceof HarmTreeLeaf) {
            HarmTreeLeaf l = (HarmTreeLeaf) element;
            l.setRiskSource(properties.getAsJsonObject().get("data").getAsJsonObject().get("riskSource").getAsString());
            l.setThreatType(properties.getAsJsonObject().get("data").getAsJsonObject().get("threatType").getAsString());
            l.setAttributeDefinition(null);
            htr.save(l);
            l.setAttributeDefinition(adr.findOne(Long.parseLong(properties.getAsJsonObject().get("data").getAsJsonObject().get("attributeDefinition").getAsString())));
            htr.save(l);
        }
        if (element instanceof HarmTreeLogicalNode) {
            HarmTreeLogicalNode n = (HarmTreeLogicalNode) element;
            n.setLogicalRequirement(properties.getAsJsonObject().get("data").getAsJsonObject().get("value").getAsString());
            htr.save(n);
        }
    }

    @Override
    public void createHarmTreeDescendant(Long parentId, JsonElement properties) {
        System.out.println("create node");
        System.out.println(properties.toString());
        HarmTreeElement parent = htr.findOne(parentId);
        
        switch (properties.getAsJsonObject().get("type").getAsInt()) {
            case 1:
                HarmTreeLeaf htl = new HarmTreeLeaf();
                htl.setThreatType(properties.getAsJsonObject().get("data").getAsJsonObject().get("threatType").getAsString());
                htl.setRiskSource(properties.getAsJsonObject().get("data").getAsJsonObject().get("riskSource").getAsString());
                htl.setDisplayNotation(parent.getDisplayNotation());
                htl.setAttributeDefinition(adr.findOne(
                        Long.parseLong(properties.getAsJsonObject().get("data").getAsJsonObject().get("attributeDefinition").getAsString())));
                htr.save(htl);
                ((HarmTreeLogicalNode) parent).addLeaf(htl);
                break;
            case 2:
                HarmTreeLogicalNode htln = new HarmTreeLogicalNode();
                htln.setDisplayNotation(parent.getDisplayNotation());
                htln.setLogicalRequirement(properties.getAsJsonObject().get("data").getAsJsonObject().get("value").getAsString());
                ((HarmTreeNode) parent).addDescendant(htln);
                htr.save(htln);
                break;
        }
        htr.save(parent);
    }

    @Override
    public void deleteHarmTreeElement(Long id) {
        HarmTreeElement element = htr.findOne(id);
        if (element instanceof HarmTreeLogicalNode) {
            System.out.println("delete logic");
            deleteWithDescendants((HarmTreeLogicalNode) element);
        }
        if (element instanceof HarmTreeLeaf) {
            System.out.println("delete leaf");
            htr.detachDelete(element.getId());
        }
    }

    @Override
    public void deleteHarmTree(Long id) {
        HarmTreeVertex htln;
        htln = (HarmTreeVertex) htr.findOne(id);
        if(!htln.getDescendants().isEmpty())
            deleteWithDescendants(htln.getDescendants().get(0));
        htr.detachDelete(htln.getId());
    }
    
    private void deleteWithDescendants(HarmTreeLogicalNode node) {
        node.getLeafs().forEach((elem) -> {
            htr.detachDelete(elem.getId());
        });
        node.getDescendants().forEach((elem) -> {
            deleteWithDescendants((HarmTreeLogicalNode) htr.findOne(elem.getId()));
        });
        htr.detachDelete(node.getId());
    }

    @Override
    public List<HarmTreeVertex> getHarmTrees() {
        return htr.getTreeVertices();
    }

    @Override
    public HarmTreeVertex createHarmTree(String name, String description) {
        HarmTreeVertex vertex = new HarmTreeVertex();
        vertex.setName(name);
        vertex.setDescription(description);
        JsonObject object = new JsonObject();
        object.addProperty("x", 300);
        object.addProperty("y", 300);
        vertex.setDisplayNotation(object.toString());
        return htr.save(vertex);
    }

}
