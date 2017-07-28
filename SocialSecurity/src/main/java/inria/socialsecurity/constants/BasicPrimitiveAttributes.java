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
    FIRST_NAME(true),
    LAST_NAME(true),
    BIRTHDAY(false),
    GENDER(false),
    GENDER_INTERESTS(false),
    POLITICAL_VIEW(false),
    RELIGIOUS_VIEW(false),
    LANGUAGES(false),
    PHONES(true),
    ADDRESS(false),
    EMAIL_ADDRESS(true),
    WORK_IDS(false),
    EDUCATION_IDS(false);

    private final boolean unique;
    
    private BasicPrimitiveAttributes(boolean isUnique) {
        this.unique = isUnique;
    }

    public String getValue() {
        return this.name().toLowerCase();
    }

    public String getDisplayName() {
        return this.name().toLowerCase().replace("_", " ");
    }

    public boolean isUnique() {
        return unique;
    }
    
}
