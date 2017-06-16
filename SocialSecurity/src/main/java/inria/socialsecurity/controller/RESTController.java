/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import inria.socialsecurity.attributeprovider.exception.ObjectNotFoundException;
import inria.socialsecurity.attributeprovider.exception.WrongArgumentException;
import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.converter.HarmTreeToCytoscapeNotationConverter;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.harmtree.HarmTreeElement;
import inria.socialsecurity.entity.harmtree.HarmTreeLogicalNode;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.model.HarmTreeModel;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.HarmTreeRepository;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author adychka
 */
@RestController
@RequestMapping("/rest/**")
public class RESTController {
    final static String PARAM_DISPLAY_DATA = "display_data";
    final static String PARAM_ID = "id";
    
    
    @Autowired
    AttributeDefinitionRepository adr;
    @Autowired
    HarmTreeRepository htr;
    @Autowired
    HarmTreeModel harmTreeModel;
    
   
    @RequestMapping(value = "/rest/attributes/all",method = RequestMethod.GET)
    public List<AttributeDefinition> getAttributes(){
        List<AttributeDefinition> lst = adr.findPrimitiveAttributes();
        lst.addAll(adr.findComplexAttributes());
        return lst;
    }
    
    @RequestMapping(value = "/rest/harmtrees/leaf/settings",method = RequestMethod.GET)
    public String getHarmTreeLeafSettings(){
        JsonObject object = new JsonObject();
        JsonArray threatTypes = new JsonArray();
        JsonArray riskSources = new JsonArray();
        
        for(RiskSource r:RiskSource.values()){
            riskSources.add(r.getValue());
        }
        for(ThreatType r:ThreatType.values()){
            threatTypes.add(r.getValue());
        }
        object.add("riskSources", riskSources);
        object.add("threatTypes", threatTypes);
        
        return object.toString();
    }
    
    @RequestMapping(value = "/rest/harmtrees/all",method = RequestMethod.GET)
    public List<HarmTreeVertex> getHarmTrees(){
        return htr.getTreeVertices();
    }
    
    @RequestMapping(value = "/rest/harmtrees/node/{id}",method = RequestMethod.GET)
    public HarmTreeElement getHarmTreeElement(@PathVariable String id) throws WrongArgumentException{
        try {
            return htr.findOne(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        }
    }
    
    @RequestMapping(value = "/rest/harmtrees/node/{id}",method = RequestMethod.PUT)
    public ResponseEntity updateHarmTreeElement(@PathVariable String id,@RequestBody String body) throws WrongArgumentException{
        harmTreeModel.updateHarmTreeNode(Long.parseLong(id), new JsonParser().parse(body));
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/rest/harmtrees/node/{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteHarmTreeElement(@PathVariable String id) throws WrongArgumentException{
        harmTreeModel.deleteHarmTreeElement(Long.parseLong(id));
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/rest/harmtrees/descendant/{parentId}",method = RequestMethod.POST)
    public ResponseEntity createHarmTreeElementDescendant(@PathVariable String parentId,@RequestBody String body) throws WrongArgumentException{
        harmTreeModel.createHarmTreeDescendant(Long.parseLong(parentId), new JsonParser().parse(body));
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/rest/harmtrees/cytoscape/{id}",method = RequestMethod.GET)
    public String getHarmTreeCytoscapeNotation(@PathVariable String id) throws WrongArgumentException, ObjectNotFoundException{
        try {
            return harmTreeModel.getDisplayNotationForHarmTree(Long.parseLong(id)).toString();
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        }
        catch (ClassCastException e) {
            throw new ObjectNotFoundException();
        }
    }
    
    @RequestMapping(value = "/rest/harmtrees/cytoscape/{id}",method = RequestMethod.POST)
    public ResponseEntity updateHarmTree(@PathVariable String id,@RequestBody String body) throws WrongArgumentException{
        harmTreeModel.updateHarmTree(Long.parseLong(id), new JsonParser().parse(body));
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
}
