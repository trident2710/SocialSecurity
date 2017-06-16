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
@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "wrong argument")
public class WrongArgumentException extends Exception{
    public WrongArgumentException(){
        super("The argument passed has the wrong type or value");
    }
}
