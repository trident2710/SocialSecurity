/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 *
 * @author adychka
 */
public enum BasicComplexAttributeName {
    FULL_NAME("full_name","full name",new PrimitiveAttributeName[]{PrimitiveAttributeName.FIRST_NAME,PrimitiveAttributeName.LAST_NAME}),
    BIRTHDAY("birthday","birthday",new PrimitiveAttributeName[]{PrimitiveAttributeName.BIRTH_DAY,PrimitiveAttributeName.BIRTH_MONTH,PrimitiveAttributeName.BIRTH_YEAR}),
    HOME_ADDRESS("home_address","home address",new PrimitiveAttributeName[]{PrimitiveAttributeName.HOME_BUILDING,PrimitiveAttributeName.HOME_CITY,PrimitiveAttributeName.HOME_COUNTRY,PrimitiveAttributeName.HOME_FLAT,PrimitiveAttributeName.HOME_INDEX,PrimitiveAttributeName.HOME_STREET,PrimitiveAttributeName.HOME_STREET_TYPE});

    String name;
    String displayName;
    PrimitiveAttributeName[] subnames;

    private BasicComplexAttributeName(String name,String displayName,PrimitiveAttributeName[] subnames){
        this.name = name;
        this.displayName = displayName;
        this.subnames = subnames;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    

    public PrimitiveAttributeName[] getSubnames() {
        return subnames;
    }
    
    
}
