/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * Representation of the thret types.
 *
 * @see Documentation p.19
 * @author adychka
 */
public enum ThreatType {
    FE1("FE.1"),
    FE2("FE.2"),
    FE3("FE.3");

    private String value;

    public String getValue() {
        return value;
    }

    private ThreatType(String value) {
        this.value = value;
    }
}
