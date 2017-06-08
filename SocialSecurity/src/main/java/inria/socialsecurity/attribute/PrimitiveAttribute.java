/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.attribute;

/**
 *
 * @author adychka
 */
public class PrimitiveAttribute extends Attribute{
    
    public PrimitiveAttribute(String name,AttributeValue value) {
        super(name);
        this.value = value;
    }
    
    private AttributeValue value;

    public AttributeValue getValue() {
        return value;
    }

    public void setValue(AttributeValue value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return ""+value.toString();
    }
}
