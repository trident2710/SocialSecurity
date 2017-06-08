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
public class AttributeValue {
    private String val;
    
    public AttributeValue(String val){
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    
    public boolean isInteger(){
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }
    
    public boolean isDouble(){
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }
    
    public boolean isCollection(){
        return val.split(";").length>1;
    }
    
    public Integer getAsInteger(){
        return Integer.parseInt(val);
    }
    
    public Double getAsDouble(){
        return Double.parseDouble(val);
    }
    
    public String[] getAsCollection(){
        return val.split(";");
    }
    
    public String getAsString(){
        return val;
    }
}
