/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.attribute;

/**
 * represents the attribute 
 * attributes can be complex (address, date of birth) 
 * or primitive (first name, last name, day! of birth etc.)
 *
 * @author adychka
 */
public abstract class Attribute {
    private String name;

    public Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
