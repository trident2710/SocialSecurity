/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity;



import inria.socialsecurity.attribute.ComplexAttribute;
import inria.socialsecurity.attribute.PrimitiveAttribute;
import inria.socialsecurity.attributeprovider.UserAttributeProvider;
import inria.socialsecurity.constants.PrimitiveAttributeName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author adychka
 */
@NodeEntity
public class FacebookAccount extends JsonStoringEntity implements UserAttributeProvider{
   
    
    @Relationship(type  = "FRIEND", direction = "BOTH")
    List<FacebookAccount> friends;
    
    public FacebookAccount(String jsonInfo){
        super(jsonInfo);   
    }

    @Override
    public PrimitiveAttribute getPrimitiveAttribute(AttributeDefinition attributeDefinition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComplexAttribute getComplexAttribute(ComplexAttributeDefinition cad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

   
    
   
}
