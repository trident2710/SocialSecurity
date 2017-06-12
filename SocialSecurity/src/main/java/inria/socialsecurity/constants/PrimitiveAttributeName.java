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
public enum PrimitiveAttributeName {
    FIRST_NAME("f_name","first name"),
    LAST_NAME("l_name","last name"),
    
    HOME_COUNTRY("h_country","home country"),
    HOME_CITY("h_city","home city"),
    HOME_STREET("h_street","home street"),
    HOME_STREET_TYPE("h_street_type","home street type"),
    HOME_BUILDING("h_building","home building"),
    HOME_FLAT("h_flat","home flat number"),
    HOME_INDEX("h_index","home index"),
    
    BIRTH_DAY("b_day","birth day"),
    BIRTH_MONTH("b_month","birth month"),
    BIRTH_YEAR("b_year","birth year"),
    
    EDUCATION_LEVEL("education_level","education level"),
    GENDER("gender","gender"),
    INTERESTS("interests","interests"),
    POLITICAL_VIEW("p_view","political_view"),
    RELIGIOUS_VIEW("r_view","religious view"),
    WORKPLACE("work","workplace name"),
    WORK_POSITION("w_position","workplace position"),
    PHONES("phones","phones"),
    EMAILS("emails","emails"),
    RELATIONSHIP_STATUS("relationship_status","relationship status");
    
    private final String value;
    private final String displayName;
    
    private  PrimitiveAttributeName(String value,String displayName){
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }
    
    public String getDisplayName(){
        return displayName;
    }
       
}
