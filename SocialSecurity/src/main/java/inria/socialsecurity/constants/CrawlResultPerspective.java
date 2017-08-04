/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 *
 * @author adychka
 * defines from which perspective data was obtained
**/
public enum CrawlResultPerspective{
    FRIEND(RiskSource.A1),
    FRIEND_OF_FRIEND(RiskSource.A2),
    STRANGER(RiskSource.A4);    

    private final RiskSource riskSource;

    private CrawlResultPerspective(RiskSource riskSource) {
        this.riskSource = riskSource;
    }

    public RiskSource getRiskSource() {
        return riskSource;
    }
    
    public static CrawlResultPerspective getWeakerFor(CrawlResultPerspective perspective){
        return perspective.ordinal()==CrawlResultPerspective.values().length-1?perspective:CrawlResultPerspective.values()[perspective.ordinal()+1];
    }
    
    public static CrawlResultPerspective getForRiskSource(RiskSource s){
        for(CrawlResultPerspective p:CrawlResultPerspective.values()){
            if(p.getRiskSource()==s) return p;
        }
        return null;
    }
}
