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
public enum Gender{
    MALE("male"), 
    FEMALE("female"),
    TRANSGENDER("transgender"),
    UNCERTAIN("uncertain"),
    OTHER("other");
    
    private final String value;
    
    private Gender(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
