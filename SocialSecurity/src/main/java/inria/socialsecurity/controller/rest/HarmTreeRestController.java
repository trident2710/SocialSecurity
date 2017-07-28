/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.socialsecurity.constants.LogicalRequirement;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.exception.ObjectNotFoundException;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.model.harmtree.HarmTreeModel;
import inria.socialsecurity.repository.HarmTreeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * rest controller for crud harm tree elements
 *
 * @see HarmTreeElement
 * @author adychka
 */
@RestController
@RequestMapping("/rest/harmtrees/**")
public class HarmTreeRestController {



    @Autowired
    HarmTreeRepository htr; //hepository for the harm tree elements 

    @Autowired
    HarmTreeModel harmTreeModel; //bean - model layer for harm tree elements crud operations etc.

    /**
     * get the possible values for the risk source and threat type
     *
     * @see RiskSource
     * @see ThreatType
     * @return json string in format {"riskSources":[],"threatTypes":[]}
     */
    @RequestMapping(value = "leaf/settings", method = RequestMethod.GET)
    public String getHarmTreeLeafSettings() {
        JsonObject object = new JsonObject();
        JsonArray threatTypes = new JsonArray();
        JsonArray riskSources = new JsonArray();
        JsonArray logicalRequirements = new JsonArray();

        for (RiskSource r : RiskSource.values()) {
            riskSources.add(r.getValue());
        }
        
        for (ThreatType r : ThreatType.values()) {
            threatTypes.add(r.getValue());
        }
        
        for (LogicalRequirement l : LogicalRequirement.values()) {
            logicalRequirements.add(l.getName());
        }
        object.add("riskSources", riskSources);
        object.add("threatTypes", threatTypes);
        object.add("logicalRequirements", logicalRequirements);

        return object.toString();
    }

    /**
     * get the list of harm tree vertices
     *
     * @see HarmTreeVertex
     * @return
     */
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<HarmTreeVertex> getHarmTrees() {
        return htr.getTreeVertices();
    }

    /**
     * get the harm tree node by id
     *
     * @param id harm tree element id
     * @return harm tree element
     * @see HarmTreeElement
     * @throws WrongArgumentException if id is malformed
     */
    @RequestMapping(value = "node/{id}", method = RequestMethod.GET)
    public HarmTreeElement getHarmTreeElement(@PathVariable String id) throws WrongArgumentException {
        try {
            return htr.findOne(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        }
    }

    /**
     * update the harm tree element by id
     *
     * @param id
     * @param body contains the fields named after HarmTreeElement fields
     * @see HarmTreeElement
     * @return
     * @throws WrongArgumentException
     */
    @RequestMapping(value = "node/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateHarmTreeElement(@PathVariable String id, @RequestBody String body) throws WrongArgumentException {
        harmTreeModel.updateHarmTreeElement(Long.parseLong(id), new JsonParser().parse(body));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * delete the harm tree element by id Important: deletes the descendants
     * recursively!
     *
     * @param id
     * @return
     * @throws WrongArgumentException
     */
    @RequestMapping(value = "node/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteHarmTreeElement(@PathVariable String id) throws WrongArgumentException {
        harmTreeModel.deleteHarmTreeElement(Long.parseLong(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * create the descendant for the harm tree element
     *
     * @param parentId - id of paraent node
     * @param body - child params
     * @return
     * @throws WrongArgumentException
     */
    @RequestMapping(value = "descendant/{parentId}", method = RequestMethod.POST)
    public ResponseEntity createHarmTreeElementDescendant(@PathVariable String parentId, @RequestBody String body) throws WrongArgumentException {
        harmTreeModel.createHarmTreeDescendant(Long.parseLong(parentId), new JsonParser().parse(body));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * get the cytoscape.js accepted display notation for the harm tree vertex
     * by id
     *
     * @param id of the harm tree vertex
     * @return
     * @throws WrongArgumentException if id is malformed
     * @throws ObjectNotFoundException if such harm tree vertex not found or
     * found object is of wrong type
     */
    @RequestMapping(value = "cytoscape/{id}", method = RequestMethod.GET)
    public String getHarmTreeCytoscapeNotation(@PathVariable String id) throws WrongArgumentException, ObjectNotFoundException {
        try {
            return harmTreeModel.getCytoscapeDisplayNotationForHarmTree(Long.parseLong(id)).toString();
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        } catch (ClassCastException e) {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * update the display notation for the the harm tree vertex by id
     *
     * @param id
     * @param body - cytoscape.js display notation
     * @see http://js.cytoscape.org/
     * @return
     * @throws WrongArgumentException
     */
    @RequestMapping(value = "cytoscape/{id}", method = RequestMethod.POST)
    public ResponseEntity updateHarmTree(@PathVariable String id, @RequestBody String body) throws WrongArgumentException {
        harmTreeModel.updateHarmTreeNotationFromCytoscape(Long.parseLong(id), new JsonParser().parse(body));
        return new ResponseEntity(HttpStatus.OK);
    }
    
    /**
     * delete harm tree
     * @param id
     * @param model
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteHarmTree(@PathVariable String id, Model model) throws WrongArgumentException {
        Long idValue;
        try {
            idValue = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        }
        harmTreeModel.deleteHarmTree(idValue);
    }

}
