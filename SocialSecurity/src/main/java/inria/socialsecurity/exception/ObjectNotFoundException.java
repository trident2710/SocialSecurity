/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * means the error when the object cannot be found in the data source this can
 * be caused by different reasons i.e. object with this id does not exist or it
 * exists but has the other type, which means that it cannot be accessed
 *
 * @author adychka
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such object")
public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException() {
        super("This object can not be found or not exist");
    }
}
