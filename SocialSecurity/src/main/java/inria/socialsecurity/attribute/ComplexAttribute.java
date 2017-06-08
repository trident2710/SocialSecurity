/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.attribute;

import java.util.List;

/**
 *
 * @author adychka
 */
public class ComplexAttribute extends Attribute{
    
    public ComplexAttribute(String name,List<PrimitiveAttribute> primitiveAttributes) {
        super(name);
        this.primitiveAttributes = primitiveAttributes;
    }
    
    
    private List<PrimitiveAttribute> primitiveAttributes;

    public List<PrimitiveAttribute> getPrimitiveAttributes() {
        return primitiveAttributes;
    }

    public void setPrimitiveAttributes(List<PrimitiveAttribute> primitiveAttributes) {
        this.primitiveAttributes = primitiveAttributes;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(PrimitiveAttribute attribute:primitiveAttributes){
            sb.append(attribute.toString());
            sb.append("|");
        }
        return sb.toString();
    }
    
}
