/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.neo4j.shell.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 *
 * @author adychka
 */
@Component
@Scope("singleton")
public class SettingsLoader {
  /**
   * where geckodriver is located
   */
    private String geckoPath = "geckodriver";
  /**
   * where phantomjs is located
   */
    private String phantomPath = "phantomjs";
    
    private int neo4jPort = 7474; // db connection port
    private String neo4jUrl = "localhost"; //db connection ip address

    private String neo4jLogin = "neo4j"; //db connection login
    private String neo4jPassword = "neo4j"; //db connection password
    
  /**
   * system property for providing the path to geckodriver
   */
    public static final String GECKODRIVER_PROPERTY = "webdriver.gecko.driver";
  
  /**
   * system property for providing the path to phantomjs
   */
    public static final String PHANTOMJS_PROPERTY = "phantomjs.binary.path";
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @PostConstruct
    public void init(){
        try {
            loadProperties();
            System.setProperty(GECKODRIVER_PROPERTY, geckoPath);
            System.setProperty(PHANTOMJS_PROPERTY, phantomPath);
        } catch (IOException ex) {
            Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadProperties() throws IOException{
        
    File file = new File("resources.properties");
    System.out.println(file.getAbsolutePath());
    
        JsonObject obj = new JsonParser().parse(new InputStreamReader(resourceLoader.getResource("properties").getInputStream())).getAsJsonObject();
        geckoPath= obj.get("geckodriver_path").getAsString();
        phantomPath= obj.get("phantom_path").getAsString();
        neo4jPort = Integer.parseInt(obj.get("neo4j_port").getAsString());
        neo4jUrl = obj.get("neo4j_url").getAsString();
        neo4jLogin = obj.get("neo4j_login").getAsString();
        neo4jPassword= obj.get("neo4j_pass").getAsString();
        System.out.println("load properties");
    }

    public String getGeckoPath() {
        return geckoPath;
    }

    public String getPhantomPath() {
        return phantomPath;
    }

    public int getNeo4jPort() {
        return neo4jPort;
    }

    public String getNeo4jUrl() {
        return neo4jUrl;
    }

    public String getNeo4jLogin() {
        return neo4jLogin;
    }

    public String getNeo4jPassword() {
        return neo4jPassword;
    }
    
    
}
