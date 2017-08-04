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
    FIRST_NAME(true,false),
    LAST_NAME(true,false),
    BIRTHDAY(false,false),
    GENDER(false,false),
    GENDER_INTERESTS(false,false),
    POLITICAL_VIEW(false,false),
    RELIGIOUS_VIEW(false,false),
    LANGUAGES(false,true),
    PHONES(true,true),
    ADDRESS(false,false),
    EMAIL_ADDRESS(true,false),
    WORK_IDS(false,true),
    EDUCATION_IDS(false,true);

    private final boolean unique;
    private final boolean list;
    
    private BasicPrimitiveAttributes(boolean isUnique,boolean isList) {
        this.unique = isUnique;
        this.list = isList;
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

    public boolean isList() {
        return list;
    }
    
}
