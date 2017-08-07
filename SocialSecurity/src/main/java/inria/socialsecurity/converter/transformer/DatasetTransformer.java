/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import java.util.Map;
import java.util.Set;

/**
 * defines interface for parsing the attributes dataset for some source
 * @author adychka
 */
public interface DatasetTransformer<T> {
    /**
     * parse for selected input set
     * @param sourceSet
     * @return 
     */
    Map<String,Map<String,String>> parsefromSourceSet(Set<T> sourceSet);
    
    /**
     * parse for selected input
     * @param source
     * @return 
     */
    Map<String,String> parseFromSource(T source);
}
