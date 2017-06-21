/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.constants;

/**
 * defining standart data source names i.e. for each data source there is specific format of the file 
 * + specific attribute names
 * should be appended by adding new social networks
 * @author adychka
 */
public enum DefaultDataSourceName {
    FACEBOOK("facebook"), //defining the name for the format of data obtained from facebook account
    TWITTER("twitter"); //defining the name for the format of data obtained from twiiter account
    
    final String name;

    public String getName() {
        return name;
    }
    
    private DefaultDataSourceName(String name){
        this.name = name;
    }
}
