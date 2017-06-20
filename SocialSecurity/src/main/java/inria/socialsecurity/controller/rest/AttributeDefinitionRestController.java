/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.rest;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.exception.ObjectNotFoundException;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.model.AttributeDefinitionModel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author adychka
 */
@RestController
@RequestMapping("/rest/attributes/**")
public class AttributeDefinitionRestController {
    
    @Autowired
    AttributeDefinitionModel adm; //repository for the attribute definitions
    
    /**
     * get all attributes i.e primitive and complex
     *
     * @return the list of attributes
     */
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<AttributeDefinition> getAttributes() {
        return adm.getAllAttributeDefinitions();
    }
    
    /**
     * update the existing attribute with the new arguments 
     * attributes page
     *
     * @param request containing the post body
     * @param id attribute definition id
     * @throws WrongArgumentException if id is not specified of malformed
     * @throws ObjectNotFoundException if such attribute does not exist or
     * belongs to the other class
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateAttribute(@PathVariable String id,HttpServletRequest request) throws WrongArgumentException, ObjectNotFoundException {
        Long idVal;
        try {
            idVal = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        }
        adm.updateAttributeDefinitionFromHttpRequest(idVal, request);
    }
    
     /**
     * post the new values for creating the new complex attributes and if
     * succeed, redirect to the attribute list page
     *
     * @param request
     * @throws WrongArgumentException if the primitive attribute id is malformed
     * @throws inria.socialsecurity.exception.ObjectNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST)
    public void createAttribute(HttpServletRequest request) throws WrongArgumentException, ObjectNotFoundException {
        
        adm.createAttributeDefinitionFromHttpRequest(request);
    }

    /**
     * delete the complex attribute by id
     *
     * @param id - attribute id
     * @throws WrongArgumentException if id is malformed
     * @throws ObjectNotFoundException if such attribute does not exist or
     * belongs to other class
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteAttribute(@PathVariable String id) throws WrongArgumentException, ObjectNotFoundException {
        Long idVal;
        try {
            idVal = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        }
        adm.deleteAttributeDefinitionById(idVal);
    }
}
