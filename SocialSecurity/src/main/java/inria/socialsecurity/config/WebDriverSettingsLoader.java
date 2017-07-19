/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 *
 * @author adychka
 */
@Component
public class WebDriverSettingsLoader {
  /**
   * where geckodriver is located
   */
    public static final String GECKODRIVER_PATH = "/users/adychka/Documents/SocialSecurity/webapp/SocialSecurity/geckodriver";
  /**
   * where phantomjs is located
   */
    public static final String PHANTOMJS_PATH = "/users/adychka/Documents/SocialSecurity/webapp/SocialSecurity/phantomjs";
  
  /**
   * system property for providing the path to geckodriver
   */
    public static final String GECKODRIVER_PROPERTY = "webdriver.gecko.driver";
  
  /**
   * system property for providing the path to phantomjs
   */
    public static final String PHANTOMJS_PROPERTY = "phantomjs.binary.path";
    
    @PostConstruct
    public void init(){
        System.setProperty(GECKODRIVER_PROPERTY, GECKODRIVER_PATH);
        System.setProperty(PHANTOMJS_PROPERTY, PHANTOMJS_PATH);
    }
}
