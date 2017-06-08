/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.AppConfiguration;
import inria.socialsecurity.test.config.Config;
import inria.socialsecurity.test.helper.CRUDAttributeDefinitionTest;
import inria.socialsecurity.test.helper.CRUDAttributeTest;
import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {Config.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AttributeTestCase {
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Autowired
    CRUDAttributeDefinitionTest crudadt;
    
    @Autowired
    CRUDAttributeTest crudat;
    
    
    
    @Test
    public void CRUDAttributeDefinition(){
        crudadt.basicComplexAttrCrud();
        crudadt.basicAttrCrud();
        crudat.checkPrimitiveAttrbute();
        crudat.checkComplexAttribute();
    }
    
    
   
    
   
}
