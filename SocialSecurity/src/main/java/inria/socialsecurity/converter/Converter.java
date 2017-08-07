/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter;

/**
 * defines cutom converter form/to specified data types
 * S - source
 * D -destination
 * @author adychka
 */
public interface Converter<S,D> {
    D convertFrom(S source);
    S convertTo(D destination);
}
