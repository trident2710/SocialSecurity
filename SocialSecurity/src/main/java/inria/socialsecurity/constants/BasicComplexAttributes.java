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
    FULL_NAME(new BasicPrimitiveAttributes[]{BasicPrimitiveAttributes.FIRST_NAME, BasicPrimitiveAttributes.LAST_NAME});

    BasicPrimitiveAttributes[] subnames;

    private BasicComplexAttributes(BasicPrimitiveAttributes[] subnames) {
        this.subnames = subnames;
    }

    public String getName() {
        return this.name().toLowerCase();
    }

    public String getDisplayName() {
        return this.name().toLowerCase().replace("_", " ");
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
