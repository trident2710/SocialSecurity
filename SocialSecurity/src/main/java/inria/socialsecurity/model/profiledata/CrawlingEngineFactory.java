/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import com.google.gson.JsonObject;
import inria.crawlerv2.engine.CrawlingCallable;
import inria.crawlerv2.engine.CrawlingInstanceSettings;
import inria.crawlerv2.engine.account.Account;
import java.net.URI;
import java.util.concurrent.Callable;

/**
 *
 * @author adychka
 */
public interface CrawlingEngineFactory {
    Callable<JsonObject> createCrawlingCallable(CrawlingInstanceSettings settings,String target, Account account);
}
