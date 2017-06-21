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
 * @see BasicPrimitiveAttributes
 * @author adychka
 */
public enum BasicComplexAttributes {
    FULL_NAME("full_name", "full name", new BasicPrimitiveAttributes[]{BasicPrimitiveAttributes.FIRST_NAME, BasicPrimitiveAttributes.LAST_NAME}),
    BIRTHDAY("birthday", "birthday", new BasicPrimitiveAttributes[]{BasicPrimitiveAttributes.BIRTH_DAY, BasicPrimitiveAttributes.BIRTH_MONTH, BasicPrimitiveAttributes.BIRTH_YEAR}),
    HOME_ADDRESS("home_address", "home address", new BasicPrimitiveAttributes[]{BasicPrimitiveAttributes.HOME_BUILDING, BasicPrimitiveAttributes.HOME_CITY, BasicPrimitiveAttributes.HOME_COUNTRY, BasicPrimitiveAttributes.HOME_FLAT, BasicPrimitiveAttributes.HOME_INDEX, BasicPrimitiveAttributes.HOME_STREET, BasicPrimitiveAttributes.HOME_STREET_TYPE});

    String name; //the name of object in db i.e. the inner name inside of application
    String displayName; //the name, the users should see i.e. the name for users
    BasicPrimitiveAttributes[] subnames;

    private BasicComplexAttributes(String name, String displayName, BasicPrimitiveAttributes[] subnames) {
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
    public BasicPrimitiveAttributes[] getSubnames() {
        return subnames;
    }

}
