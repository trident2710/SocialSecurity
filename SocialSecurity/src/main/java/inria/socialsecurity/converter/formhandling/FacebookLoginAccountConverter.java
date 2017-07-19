/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.formhandling;

import inria.socialsecurity.entity.snaccount.FacebookLoginAccount;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 *
 * @author adychka
 */
@Component
public class FacebookLoginAccountConverter implements Converter<String,FacebookLoginAccount>{
    @Autowired
    FacebookLoginAccountRepository flar;

    @Override
    public FacebookLoginAccount convert(String sid) {
        Long id = Long.parseLong(sid);
        if(id==-1)return null;

        return flar.findOne(id);
    }

    
    
}
