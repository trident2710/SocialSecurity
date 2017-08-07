/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import com.google.gson.JsonObject;
import inria.crawlerv2.engine.CrawlingInstanceSettings;
import inria.crawlerv2.engine.account.Account;
import java.util.concurrent.Callable;

/**
 * defines the factory for creating the crawling instances based on task
 * need to make ProfileData testable
 * @see ProfileData
 * @author adychka
 */
public interface CrawlingEngineFactory {
    
    /**
     * create crawling callable returning the collected attributes in form of json object
     * @param settings see CrawlingInstanceSettings
     * @param target - target profile url in form https://facebook.com/profile.php?id=12435
     * @param account - facebook account used to log in facebook.com
     * @return 
     */
    Callable<JsonObject> createCrawlingCallable(CrawlingInstanceSettings settings,String target, Account account);
    
    /**
     * create crawling callable returning the collected attributes and their visibility options in form of json object
     * @param settings see CrawlingInstanceSettings
     * @param target - target profile url in form https://facebook.com/profile.php?id=12435
     * @param account - facebook account used to log in facebook.com
     * @return 
     */
    Callable<JsonObject> createCrawlingVisibilityCallable(CrawlingInstanceSettings settings,String target, Account account);
}
