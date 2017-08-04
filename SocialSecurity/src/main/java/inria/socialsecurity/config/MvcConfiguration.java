/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import com.google.gson.JsonObject;
import inria.crawlerv2.engine.AttributeVisibilityCrawlerCallable;
import inria.crawlerv2.engine.CrawlingCallable;
import inria.crawlerv2.engine.CrawlingInstanceSettings;
import inria.crawlerv2.engine.account.Account;
import inria.crawlerv2.engine.account.AccountManager;
import inria.socialsecurity.constants.CrawlDepth;
import inria.socialsecurity.converter.FacebookProfileToCytoscapeNotationConverter;
import inria.socialsecurity.converter.transformer.AttributeMatrixToJsonConverter;
import inria.socialsecurity.converter.transformer.AttributesParser;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeMatrixTransformer;
import inria.socialsecurity.entity.settings.CrawlingSettings;
import inria.socialsecurity.model.DefaultDataProcessor;
import inria.socialsecurity.model.attributedefinition.AttributeDefinitionModel;
import inria.socialsecurity.model.attributedefinition.AttributeDefinitionModelImpl;
import inria.socialsecurity.model.harmtree.HarmTreeModel;
import inria.socialsecurity.model.harmtree.HarmTreeModelImpl;
import inria.socialsecurity.model.profiledata.AccountManagerImpl;
import inria.socialsecurity.model.profiledata.ProfileDataModelImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import inria.socialsecurity.model.profiledata.ProfileDataModel;
import inria.socialsecurity.repository.CrawlingSettingsRepository;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import inria.socialsecurity.converter.transformer.DatasetTransformer;
import inria.socialsecurity.converter.transformer.FacebookDatasetToAttributeVisibilityTransformer;
import inria.socialsecurity.converter.transformer.FacebookDatasetToTargetViewAttributeVisibilityTransformer;
import inria.socialsecurity.converter.transformer.FacebookTrueVisibilityToAttributeMatrixTransformer;
import inria.socialsecurity.converter.transformer.HarmTreeToJsonConverter;
import inria.socialsecurity.converter.transformer.MapToJsonConverter;
import inria.socialsecurity.model.analysis.HarmTreeValidator;
import inria.socialsecurity.model.analysis.ProfileDataAnalyzer;
import inria.socialsecurity.model.analysis.ProfileDataAnalyzerImpl;
import inria.socialsecurity.model.profiledata.CrawlingEngineFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * declaration of the beans representing the model level of web application i.e.
 * data processing and providing
 *
 * @author adychka
 */
@Configuration
public class MvcConfiguration {

    /**
     * model layer for harm tree processing requests contains CRUD methods etc.
     *
     * @return
     */
    @Bean
    public HarmTreeModel getHarmTreeModel() {
        return new HarmTreeModelImpl();
    }
    
    /**
     * model layer for attribute definition processing requests contains CRUD methods etc.
     *
     * @return
     */
    @Bean
    public AttributeDefinitionModel getAttributeDefinitionModel(){
        return new AttributeDefinitionModelImpl();
    }
    
    /**
     * model layer for profile data processing such as facebook profile data
     * used for perform CRUD and crawling profile attributes etc.
     * 
     * @return 
     */
    @Bean
    public ProfileDataModel getProfileDataModel(){
        return new ProfileDataModelImpl();
    }
    
    /**
     * for getting creadentials to log in while using crawler
     * @return 
     */
    @Bean
    public AccountManagerImpl getAccountManager(){
        return new AccountManagerImpl();
    }
    
    /**
     * for parsing the attribute matrix from the datasource 
     * @see AttributeDefinition
     * @see CrawlingEngine
     * @return 
     */
    @Bean
    public FacebookDatasetToAttributeMatrixTransformer getAttributeTransformer(){
        return new FacebookDatasetToAttributeMatrixTransformer();
    }
    
    /**
     * for parsing the attribute visibility matrix from the datasource 
     * @see AttributeDefinition
     * @see CrawlingEngine
     * @return 
     */
    public FacebookDatasetToAttributeVisibilityTransformer getVisibilityTransformer(){
        return new FacebookDatasetToAttributeVisibilityTransformer();
    }
    
    @Bean(name ="target respecting visibility")
    public FacebookDatasetToTargetViewAttributeVisibilityTransformer getTargetRespectiveVisibilityTransformer(){
        return new FacebookDatasetToTargetViewAttributeVisibilityTransformer();
    }
    
    @Bean
    public DefaultDataProcessor getDefaultDataProcessor(){
        return new DefaultDataProcessor();
    }
    
    @Bean
    public FacebookProfileToCytoscapeNotationConverter getFacebookProfileToCytoscapeNotationConverter(){
        return new FacebookProfileToCytoscapeNotationConverter();
    }
    
    @Bean
    public AttributeMatrixToJsonConverter getAttributeMatrixToJsonConverter(){
        return new AttributeMatrixToJsonConverter();
    }
    
    @Bean(name ="basic parser")
    AttributesParser getAttributesParser(){
        return new AttributesParser();
    }
    
    @Bean
    HarmTreeValidator getHarmTreeValidator(){
        return new HarmTreeValidator();
    }
    
    @Bean
    HarmTreeToJsonConverter getHarmTreeToJSonConverter(){
        return new HarmTreeToJsonConverter();
    }
    
    @Bean
    ProfileDataAnalyzer getProfileDataAnalyzer(){
        return new ProfileDataAnalyzerImpl();
    }
    
    @Bean
    CrawlingEngineFactory getCrawlingEngineFactory(){
        return new CrawlingEngineFactory() {
            @Override
            public Callable<JsonObject> createCrawlingCallable(CrawlingInstanceSettings settings, String target, Account account) {
                try {
                    return new CrawlingCallable(settings, new URI(target), account);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MvcConfiguration.class.getName()).log(Level.SEVERE, "wrong target", ex);
                }
                return null;
            }

            @Override
            public Callable<JsonObject> createCrawlingVisibilityCallable(CrawlingInstanceSettings settings, String target, Account account) {
                try {
                    return new AttributeVisibilityCrawlerCallable(account, new URI(target), settings);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MvcConfiguration.class.getName()).log(Level.SEVERE, "wrong target", ex);
                }
                return null;
            }
        };
    }
    
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(10000);
        return executor;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10000);
        return scheduler;
    }
    
    @Bean
    public FacebookTrueVisibilityToAttributeMatrixTransformer getFacebookTrueVisibilityToAttributeMatrixTransformer(){
        return new FacebookTrueVisibilityToAttributeMatrixTransformer();
    }
    
    @Bean
    public MapToJsonConverter getMapToJsonConverter(){
        return new MapToJsonConverter();
    }

    
    
}
