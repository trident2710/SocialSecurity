/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model;

import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.exception.ObjectNotFoundException;
import inria.socialsecurity.exception.WrongArgumentException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * model layer for the request processing attribute definition objects
 * @see AttributeDefinition
 * @author adychka
 */
public interface AttributeDefinitionModel {
    /**
     * @param id - complex attribute definition id
     * @return 
     * @throws ObjectNotFoundException if such object of such class does not exist
     */
    ComplexAttributeDefinition getComplexAttributeDefinitionById(Long id) throws ObjectNotFoundException;
    
    /**
     * @param id - primitive attribute definition id
     * @return 
     * @throws ObjectNotFoundException if such object of such class does not exist
     */
    PrimitiveAttributeDefinition getPrimitiveAttributeDefinitionById(Long id) throws ObjectNotFoundException;
    
    /**
     * 
     * @param id
     * @return
     * @throws ObjectNotFoundException if such object of such class does not exist
     */
    AttributeDefinition getAttributeDefinitionById(Long id) throws ObjectNotFoundException;
    
    /**
     * get all attribute definitons both complex and primitive
     * @return empty list if there are no attribute definitions
     */
    List<AttributeDefinition> getAllAttributeDefinitions();
    
    /**
     * get primitive attribute definitions
     * @see AttributeDefinitions
     * @return empty list if there are no attribute definitions
     */
    List<AttributeDefinition> getPrimitiveAttributeDefinitions();
    
    /**
     * get complex attribute definitions
     * @see ComplexAttributeDefinition
     * @return empty list if there are no attribute definitions
     */
    List<ComplexAttributeDefinition> getComlexAttributeDefinitions();
    
    /**
     * update attribute definition 
     * @param id - attribute id
     * @param request - http request
     * @throws WrongArgumentException
     * @throws ObjectNotFoundException
     */
    void updateAttributeDefinitionFromHttpRequest(Long id,HttpServletRequest request) throws WrongArgumentException,ObjectNotFoundException;
    

    /**
     * create new attribute definition 
     * @param request - http request
     * @return new saved ComplexAttributeDefinition object
     * @throws WrongArgumentException
     * @throws ObjectNotFoundException
     */
    AttributeDefinition createAttributeDefinitionFromHttpRequest(HttpServletRequest request) throws WrongArgumentException,ObjectNotFoundException;
    
    /**
     * delete attribute definition
     * @param id
     * @throws WrongArgumentException
     * @throws ObjectNotFoundException
     */
    void deleteAttributeDefinitionById(Long id) throws WrongArgumentException,ObjectNotFoundException;
    
   
    
}
