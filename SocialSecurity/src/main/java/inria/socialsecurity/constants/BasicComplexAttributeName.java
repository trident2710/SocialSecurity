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
    FULL_NAME("full_name",new PrimitiveAttributeName[]{PrimitiveAttributeName.FIRST_NAME,PrimitiveAttributeName.LAST_NAME}),
    BIRTHDAY("birthday",new PrimitiveAttributeName[]{PrimitiveAttributeName.BIRTH_DAY,PrimitiveAttributeName.BIRTH_MONTH,PrimitiveAttributeName.BIRTH_YEAR}),
    HOME_ADDRESS("home_address",new PrimitiveAttributeName[]{PrimitiveAttributeName.HOME_BUILDING,PrimitiveAttributeName.HOME_CITY,PrimitiveAttributeName.HOME_COUNTRY,PrimitiveAttributeName.HOME_FLAT,PrimitiveAttributeName.HOME_INDEX,PrimitiveAttributeName.HOME_STREET,PrimitiveAttributeName.HOME_STREET_TYPE});

    String name;
    PrimitiveAttributeName[] subnames;

    private BasicComplexAttributeName(String name,PrimitiveAttributeName[] subnames){
        this.name = name;
        this.subnames = subnames;
    }

    public String getName() {
        return name;
    }

    public PrimitiveAttributeName[] getSubnames() {
        return subnames;
    }
    
    
}
