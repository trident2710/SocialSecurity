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
    FRIEND,
    FRIEND_OF_FRIEND,
    STRANGER;    

    public static CrawlResultPerspective getWeakerFor(CrawlResultPerspective perspective){
        return perspective.ordinal()==CrawlResultPerspective.values().length-1?perspective:CrawlResultPerspective.values()[perspective.ordinal()+1];
    }
}
