/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter;

/**
 * interface for converting data from one format to another
 *
 * @author adychka
 * @param <T>  input data class
 * @param <K> output data class
 */
public interface Converter<T, K> {

    /**
     * convert from
     *
     * @param object - data
     * @return converted data
     */
    K convertFrom(T object);

    /**
     * convert to
     *
     * @param object -data
     * @return converted data
     */
    T convertTo(K object);
}
