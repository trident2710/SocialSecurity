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
    FIRST_NAME("f_name", "first name",DataType.STRING),
    LAST_NAME("l_name", "last name",DataType.STRING),
    HOME_COUNTRY("h_country", "home country",DataType.STRING),
    HOME_CITY("h_city", "home city",DataType.STRING),
    HOME_STREET("h_street", "home street",DataType.STRING),
    HOME_STREET_TYPE("h_street_type", "home street type",DataType.STRING),
    HOME_BUILDING("h_building", "home building",DataType.NUMBER),
    HOME_FLAT("h_flat", "home flat number",DataType.NUMBER),
    HOME_INDEX("h_index", "home index",DataType.NUMBER),
    BIRTH_DAY("b_day", "birth day",DataType.NUMBER),
    BIRTH_MONTH("b_month", "birth month",DataType.NUMBER),
    BIRTH_YEAR("b_year", "birth year",DataType.NUMBER),
    EDUCATION_LEVEL("education_level", "education level",DataType.STRING),
    GENDER("gender", "gender",DataType.STRING),
    INTERESTS("interests", "interests",DataType.STRING),
    POLITICAL_VIEW("p_view", "political_view",DataType.STRING),
    RELIGIOUS_VIEW("r_view", "religious view",DataType.STRING),
    WORKPLACE("work", "workplace name",DataType.STRING),
    WORK_POSITION("w_position", "workplace position",DataType.STRING),
    PHONES("phones", "phones",DataType.STRING),
    EMAILS("emails", "emails",DataType.STRING),
    RELATIONSHIP_STATUS("relationship_status", "relationship status",DataType.STRING);

    private final String value; //the attribute variable name used in the app
    private final String displayName; //the public name displayed to user 
    private final DataType dataType; //type of this field

    private BasicPrimitiveAttributes(String value, String displayName,DataType dt) {
        this.value = value;
        this.displayName = displayName;
        this.dataType = dt;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DataType getDataType() {
        return dataType;
    }
    
    

}
