/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.attributeprovider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author adychka
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "no such object")
public class ObjectNotFoundException extends Exception{

    public ObjectNotFoundException(){
        super("This object can not be found or not exist");
    }
}
