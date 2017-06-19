/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * enum containig the relationship types used to unificate the data obtained
 * from the social networks
 *
 * @see FacebookAccount
 * @see User
 * @author adychka
 */
public enum RelationshipStatus {
    MARRIED("married"),
    SINGLE("single"),
    DIVORCED("divorced"),
    IN_COUPLE("in_couple"),
    COMPLEX("complex");

    private final String value;

    private RelationshipStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
