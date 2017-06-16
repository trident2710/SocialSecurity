/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter;


/**
 * interface for parsing the java object to json
 * @author adychka
 */
public interface Converter<T,K> {
    K convertFrom(T object);
    T convertTo(K object);
}
