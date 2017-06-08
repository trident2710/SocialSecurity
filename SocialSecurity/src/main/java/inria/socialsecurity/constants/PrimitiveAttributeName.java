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
    FIRST_NAME("f_name"),
    LAST_NAME("l_name"),
    
    HOME_COUNTRY("h_country"),
    HOME_CITY("h_city"),
    HOME_STREET("h_street"),
    HOME_STREET_TYPE("h_street_type"),
    HOME_BUILDING("h_building"),
    HOME_FLAT("h_flat"),
    HOME_INDEX("h_index"),
    
    DAY_OF_BIRTH("b_day"),
    MONTH_OF_BIRTH("b_month"),
    YEAR_OF_BIRTH("b_year"),
    
    EDUCATION_LEVEL("education_level"),
    GENDER("gender"),
    INTERESTS("interests"),
    POLITICAL_VIEW("p_view"),
    RELIGIOUS_VIEW("r_view"),
    WORKPLACE("work"),
    WORK_POSITION("w_position"),
    PHONES("phones"),
    EMAILS("emails"),
    RELATIONSHIP_STATUS("relationship_status");
    
    private final String value;
    
    private  PrimitiveAttributeName(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
       
}
