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
public enum LogicalRequirement {
    AND,
    OR,
    K_OUT_OF_N;
    
    public String getName(){
        return name().toLowerCase().replace("_", " ");
    }
    
    public static LogicalRequirement getForName(String name){
        for(LogicalRequirement req:LogicalRequirement.values()){
            if(name.equals(req.getName())) return req;
        }
        return null;
    }
}
