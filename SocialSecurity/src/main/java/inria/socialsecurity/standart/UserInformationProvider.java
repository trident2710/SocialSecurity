/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.standart;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    /**
     * get the user first name
     * @return 
     */
    String getFirstName();
    /**
     * get the user last name
     * @return 
     */
    String getLastName();
    /**
     * get the user dateOfBirth in format dd/mm/yyyy
     * @return 
     */
    Date getDateOfBirth();
    
    /**
     * get user gender
     * @return 
     */
    Gender getGender();
    /**
     * get the list of phone numbers in format (+country code)phonenumber.
     * Example: "(+380)1234567"
     * @return 
     */
    List<String> getPhoneNumbers();
    /**
     * get the list of gender interests
     * @see Gender
     * @return 
     */
    List<Gender> getGenderInterests();
    /**
     * get addresses where user lived 
     * sorted from new to old
     * in format "Country|City|Street|StreetType|BuildingNo|Flat|Index"
     * Example: "Canada|Montreal|Kennedy|Str||||" if there are no building no, flat and index etc.
     * @return 
     */
    List<String> getAddresses();
    
    /**
     * get the list of works, the user worked in
     * key - work name
     * value - work address
     * @see getAddresses()
     * @return the map where the name is the key and its address is a value
     */
    Map<String,String> getWorkplaces();
    /**
     * get the list of educational institutions user studied in
     * @see EducationLevel
     * key - education level
     * value - the name of the institution
     * @return 
     */
    Map<EducationLevel,String> getEducation();
    /**
     * get the list of languages ehich user speaks
     * @return 
     */
    List<String> getLanguages();
    /**
     * get user political view
     * @return 
     */
    String getPoliticalView();
    /**
     * get user religion view
     * @return 
     */
    String getReligion();
    /**
     * get the user relationship status 
     * @return 
     */
    RelationshipStatus geRelationshipStatus();
    /**
     * get user interests
     * @return 
     */
    List<String> getInterests();
    
}
