/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * enum containig the primitive attributes which can be collected from the
 * social network account used to unificate the data obtained from the social
 * networks
 *
 * @see FacebookAccount
 * @see User
 * @author adychka
 */
public enum BasicPrimitiveAttributes {
    FIRST_NAME,
    LAST_NAME,
    BIRTHDAY,
    GENDER,
    GENDER_INTERESTS,
    POLITICAL_VIEW,
    RELIGIOUS_VIEW,
    LANGUAGES,
    PHONES,
    ADDRESS,
    EMAIL_ADDRESS,
    WORK_IDS,
    EDUCATION_IDS;

    private BasicPrimitiveAttributes() {
    }

    public String getValue() {
        return this.name().toLowerCase();
    }

    public String getDisplayName() {
        return this.name().toLowerCase().replace("_", " ");
    }


    
    

}
