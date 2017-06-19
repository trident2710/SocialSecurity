/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * the enum containing the types of education level used to unificate the data
 * obtained from the social networks
 *
 * @see FacebookAccount
 * @see User
 * @author adychka
 */
public enum EducationLevel {
    PRIMARY_SCHOOL("primary_school"),
    SECONDARY_SCHOOL("secondary_school"),
    COLLEGE("college"),
    UNIVERSITY("university"),
    OTHER("other");

    private final String value;

    private EducationLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
