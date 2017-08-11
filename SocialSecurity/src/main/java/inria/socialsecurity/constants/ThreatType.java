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
    FE1("get this attribute directly"),
    FE2("try to infer from friends");
    
    String explain;
    
    private ThreatType(String explain){
        this.explain = explain;
    }
    
    public String getValue() {
        return name();
    }
    
    public String getExplain(){
        return explain;
    }

}
