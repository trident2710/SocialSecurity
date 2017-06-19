/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * basic complex attribute definitions demonstrating how complex attributes
 * consist of primitive ones
 *
 * @see PrimitiveAttributeName
 * @author adychka
 */
public enum BasicComplexAttributeName {
    FULL_NAME("full_name", "full name", new PrimitiveAttributeName[]{PrimitiveAttributeName.FIRST_NAME, PrimitiveAttributeName.LAST_NAME}),
    BIRTHDAY("birthday", "birthday", new PrimitiveAttributeName[]{PrimitiveAttributeName.BIRTH_DAY, PrimitiveAttributeName.BIRTH_MONTH, PrimitiveAttributeName.BIRTH_YEAR}),
    HOME_ADDRESS("home_address", "home address", new PrimitiveAttributeName[]{PrimitiveAttributeName.HOME_BUILDING, PrimitiveAttributeName.HOME_CITY, PrimitiveAttributeName.HOME_COUNTRY, PrimitiveAttributeName.HOME_FLAT, PrimitiveAttributeName.HOME_INDEX, PrimitiveAttributeName.HOME_STREET, PrimitiveAttributeName.HOME_STREET_TYPE});

    String name; //the name of object in db i.e. the inner name inside of application
    String displayName; //the name, the users should see i.e. the name for users
    PrimitiveAttributeName[] subnames;

    private BasicComplexAttributeName(String name, String displayName, PrimitiveAttributeName[] subnames) {
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

    /**
     * get the primitive attributes this attribute consists of
     *
     * @return array of primitive attribute names
     */
    public PrimitiveAttributeName[] getSubnames() {
        return subnames;
    }

}
