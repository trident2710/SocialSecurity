/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.transformer;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author adychka
 */
public interface DatasetTransformer<T> {
    Map<String,Map<String,String>> parsefromSourceSet(Set<T> sourceSet);
    Map<String,String> parseFromSource(T source);
}
