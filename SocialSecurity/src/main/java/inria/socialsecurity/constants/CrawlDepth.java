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
public enum CrawlDepth {
    SELF,
    FRIENDS,
    FRIENDS_OF_FRIENDS;
    
    int value;
    
    public int depth(){
        return ordinal()+1;
    }
}
