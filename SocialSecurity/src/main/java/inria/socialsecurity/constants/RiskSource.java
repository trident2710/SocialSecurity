/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

import inria.crawlerv2.driver.AttributeVisibility;

/**
 * Represents the risk source.
 *
 * @see Documentation page 18
 * @author adychka
 */
public enum RiskSource {
    A1("user friends"), //user friends 
    A2("user friends of friends"), //friends of friends
    A4("strangers"); // strangers

    
    private String explain;
    
    private RiskSource(String explain){
        this.explain = explain;
    }
    
    public String getExplain(){
        return explain;
    }
    
    public String getValue() {
        return name();
    }
    
    public static RiskSource getForAttributeVisibility(AttributeVisibility visibility){
        switch(visibility){
            case FRIEND: return A1;
            case FRIEND_OF_FRIEND: return A2;
            case PUBLIC: return A4;
            default: return null;
        }
    }
    
    public RiskSource[] getWeaker(){
        RiskSource[] res = new RiskSource[ordinal()+1];
        System.arraycopy(RiskSource.values(), 0, res, 0, ordinal()+1);
        return res;
    }
}
