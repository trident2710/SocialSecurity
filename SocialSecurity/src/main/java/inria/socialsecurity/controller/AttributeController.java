/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.exception.ObjectNotFoundException;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * controller bean containig the response logic for the attributes connects the
 * jsp views with the requests
 *
 * @see Attribute
 * @author adychka
 */
@Controller
@RequestMapping("/attributes*")
public class AttributeController {

    static final String ATR_PRIMITIVE_ATTRIBUTES = "primitive_attributes"; //@see PrimitiveAttribute
    static final String ATR_COMPLEX_ATTRIBUTES = "complex_attributes"; //@see ComplexAttribute
    static final String ATR_ATTRIBUTE = "attribute";
    static final String ATR_PRIM_ATTRS = "prim_attrs";
    static final String ATR_PRIM_NAME = "name";
    static final String ATR_PRIM_ID = "id";

    //autowired bean for crud opertations for attributes
    @Autowired
    AttributeDefinitionRepository adr;

    //get the page listing all attributes
    @RequestMapping(value = "/attributes-all", method = RequestMethod.GET)
    public String showAll(Model model) {
        model.addAttribute(ATR_PRIMITIVE_ATTRIBUTES, adr.findPrimitiveAttributes()); //add the primitive attributes to view
        model.addAttribute(ATR_COMPLEX_ATTRIBUTES, adr.findComplexAttributes()); //add the complex attributes to view
        return "attribute_all";
    }

    /**
     * get the update attribute page for the attribute with the passed id
     *
     * @param id - attribute id
     * @param model - view model
     * @return jsp page name
     * @throws WrongArgumentException if the attribute with such id doesn't
     * exist or belongs to other class
     * @throws ObjectNotFoundException if the id argument is malformed
     */
    @RequestMapping(value = "/attributes-update", method = RequestMethod.GET)
    public String showUpdatePage(@RequestParam String id, Model model) throws WrongArgumentException, ObjectNotFoundException {
        Long idValue;
        try {
            idValue = Long.parseLong(id);
            ComplexAttributeDefinition cad = (ComplexAttributeDefinition) adr.findOne(idValue);
            if (cad == null) {
                throw new ObjectNotFoundException();
            }

            model.addAttribute(ATR_ATTRIBUTE, cad);
            model.addAttribute(ATR_PRIM_ATTRS, adr.findPrimitiveAttributes());
            return "attribute_update";
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        } catch (ClassCastException ce) {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * update the existing attribute with the new arguments and redirect to all
     * attributes page
     *
     * @param request containing the post body
     * @return all attributes jsp page
     * @throws WrongArgumentException if id is not specified of malformed
     * @throws ObjectNotFoundException if such attribute does not exist or
     * belongs to the other class
     */
    @RequestMapping(value = "/attributes-update", method = RequestMethod.POST)
    public String updateAttribute(HttpServletRequest request) throws WrongArgumentException, ObjectNotFoundException {
        if (request.getParameter(ATR_PRIM_NAME) == null
                || request.getParameter(ATR_PRIM_NAME).isEmpty()
                || request.getParameter(ATR_PRIM_ID) == null) {
            throw new WrongArgumentException();
        }

        try {
            long id = Long.parseLong(request.getParameter(ATR_PRIM_ID));
            ComplexAttributeDefinition cad = (ComplexAttributeDefinition) adr.findOne(id);
            String dispName = request.getParameter(ATR_PRIM_NAME);
            String name = dispName.toLowerCase().replace(" ", "_");
            cad.setName(name);
            cad.setDisplayName(dispName);
            cad.setPrimitiveAttributes(null);
            adr.save(cad);
            //body contains the primitive attribute ids in form 
            //"primitiveAttributei":val where i from 1 to 10 (the quantity is dynamic)
            List<AttributeDefinition> pad = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if (request.getParameter("primitiveAttribut" + i) != null) {
                    try {
                        pad.add(adr.findOne(Long.parseLong(request.getParameter("primitiveAttribut" + i))));
                    } catch (NumberFormatException e) {
                        throw new WrongArgumentException();
                    }
                }
            }
            cad.setPrimitiveAttributes(pad);
            adr.save(cad);
            return "redirect:attributes-all";
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        } catch (ClassCastException ex) {
            throw new ObjectNotFoundException();
        }

    }

    /**
     * get the page for adding new complex attribute
     *
     * @param model
     * @return the jsp page name for adding the attribute
     */
    @RequestMapping(value = "/attributes-add", method = RequestMethod.GET)
    public String showAddPage(Model model) {
        model.addAttribute(ATR_PRIM_ATTRS, adr.findPrimitiveAttributes()); //pass the primitive attributes to view
        return "attribute_add";
    }

    /**
     * post the new values for creating the new complex attributes and if
     * succeed, redirect to the attribute list page
     *
     * @param request
     * @return the jsp page name for displaying all attributes
     * @throws WrongArgumentException if the primitive attribute id is malformed
     */
    @RequestMapping(value = "/attributes-add", method = RequestMethod.POST)
    public String createComplexAttribute(HttpServletRequest request) throws WrongArgumentException {
        if (request.getParameter(ATR_PRIM_NAME) == null
                || request.getParameter(ATR_PRIM_NAME).isEmpty()) {
            throw new WrongArgumentException();
        }

        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        String dispName = request.getParameter(ATR_PRIM_NAME);
        String name = dispName.toLowerCase().replace(" ", "_");
        cad.setName(name);
        cad.setDisplayName(dispName);
        int i = 1;
        //body contains the primitive attribute ids in form 
        //"primitiveAttributei":val where i from 1 to 10 (the quantity is dynamic)
        List<AttributeDefinition> pad = new ArrayList<>();
        while (true) {
            if (request.getParameter("primitiveAttribut" + i) != null) {
                try {
                    pad.add(adr.findOne(Long.parseLong(request.getParameter("primitiveAttribut" + i))));
                } catch (NumberFormatException e) {
                    throw new WrongArgumentException();
                }
            } else {
                break;
            }
            i++;
        }
        cad.setPrimitiveAttributes(pad);
        adr.save(cad);
        return "redirect:attributes-all";
    }

    /**
     * delete the complex attribute by id
     *
     * @param id - attribute id
     * @param model
     * @return http response entity 200 if succeeded
     * @throws WrongArgumentException if id is malformed
     * @throws ObjectNotFoundException if such attribute does not exist or
     * belongs to other class
     */
    @RequestMapping(value = "/attributes-specific", method = RequestMethod.DELETE)
    public ResponseEntity deleteAttribute(@RequestParam String id, Model model) throws WrongArgumentException, ObjectNotFoundException {
        Long idValue;
        try {
            idValue = Long.parseLong(id);
            ComplexAttributeDefinition cad = (ComplexAttributeDefinition) adr.findOne(idValue);
            adr.delete(cad);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        } catch (ClassCastException ce) {
            throw new ObjectNotFoundException();
        }
    }
}
