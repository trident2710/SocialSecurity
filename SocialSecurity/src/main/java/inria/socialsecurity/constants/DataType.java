/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * defining the data types, i.e. how to read the property from 
 * the data source
 * @see PrimitiveAttributeDefinition
 * @author adychka
 */
public enum DataType {
    STRING("string",String.class),
    NUMBER("number",Integer.class);
    
    final String name;
    final Class cls;

    public String getName() {
        return name;
    }

    public Class getCls() {
        return cls;
    }
    
    
    
    private DataType(String name,Class cls){
        this.name = name;
        this.cls = cls;
    }
}
