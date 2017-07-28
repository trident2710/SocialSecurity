/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.socialsecurity.constants.LogicalRequirement;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLeaf;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.HarmTreeRepository;
import java.util.Objects;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * converter from inner db storing format to the format accepted by the
 * cytoscape.js library for drawing the graphs in browser
 *
 * @author adychka
 * @see http://js.cytoscape.org/
 * @see HarmTreeElement
 */
public class HarmTreeToCytoscapeNotationConverter extends CytoscapeNotationConverter<HarmTreeVertex> {

    @Autowired
    HarmTreeRepository htr;
    @Autowired
    Session session; //neo4j session

    @Override
    public JsonElement convertFrom(HarmTreeVertex object) {
        session.clear();
        JsonObject res = createDefaultJsonObject();

        saveHarmTreeNode(object, res, object.getName());

        if (object.getDescendants() == null || object.getDescendants().isEmpty()) {
            return res;
        }
        object.getDescendants().stream().map((l) -> {
            saveHarmTreeEdge(object.getId(), l.getId(), res);
            return l;
        }).forEachOrdered((l) -> {
            formHarmTreeNotationObject(res, (HarmTreeLogicalNode) htr.findOne(l.getId()));
        });
        return res;
    }

    private void formHarmTreeNotationObject(JsonObject object, HarmTreeLogicalNode hte) {
        if(hte==null) return;
        
        saveHarmTreeLogicalNode(hte, object);
        hte.getLeafs().forEach((leaf) -> {
            leaf = (HarmTreeLeaf) htr.findOne(leaf.getId());
            saveHarmTreeEdge(hte.getId(), leaf.getId(), object);
            saveHarmTreeLeaf(leaf, object);
            
        });
        hte.getDescendants().forEach((element) -> {
            HarmTreeLogicalNode node = (HarmTreeLogicalNode) htr.findOne(element.getId());
            saveHarmTreeEdge(hte.getId(), element.getId(), object);
            formHarmTreeNotationObject(object, node);
        });
        
    }

    private void saveHarmTreeLogicalNode(HarmTreeLogicalNode htl, JsonObject source) {
        if(htl==null) return;
        saveHarmTreeNode(htl, source, htl.getLogicalRequirement());
    }

    private void saveHarmTreeLeaf(HarmTreeLeaf htl, JsonObject source) {
        if(htl==null) return;
        
        StringBuilder sb = new StringBuilder();
        sb.append("Risk source: ").append(htl.getRiskSource()).append("\n");
        sb.append("Threat type: ").append(htl.getThreatType()).append("\n");
        sb.append("Attribute: ").append(htl.getAttributeDefinition().getDisplayName()).append("\n");
        saveHarmTreeNode(htl, source, sb.toString());
    }

    private void saveHarmTreeNode(HarmTreeElement htl, JsonObject source, String label) {
        if(htl==null) return;
        
        JsonObject object = new JsonObject();
        object.addProperty("group", "nodes");
        object.addProperty("classes", htl.getClass().getSimpleName() + " " + HarmTreeElement.class.getSimpleName());
        JsonObject data = new JsonObject();
        data.addProperty("id", "" + htl.getId());
        data.addProperty("label", label);
        object.add("data", data);
        object.add("position", new JsonParser().parse(htl.getDisplayNotation()));

        source.get("elements").getAsJsonObject().get("nodes").getAsJsonArray().add(object);
    }

    private void saveHarmTreeEdge(Long from, Long to, JsonObject source) {
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
    public HarmTreeVertex convertTo(JsonElement destination) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
