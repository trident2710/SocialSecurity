/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.standart;

import java.util.Date;

/**
 *
 * @author adychka
 * This interface describes the set of methods which define the instance as
 * provider of the user private information properties used in 
 * the social security measurement algorithm. Should be implemented by each class 
 * which represents account information. 
 * @see FacebookAccount;
 */
public interface UserInformationProvider {
    String getFirstName();
    String getLastName();
    Date getDateOfBirth();
    String getPhoneNumber();
    
}
