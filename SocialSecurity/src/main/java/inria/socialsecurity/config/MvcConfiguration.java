/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import inria.crawlerv2.engine.account.AccountManager;
import inria.socialsecurity.converter.attributeparser.AttributeParser;
import inria.socialsecurity.converter.attributeparser.FacebookCrawlingAttributeParser;
import inria.socialsecurity.entity.settings.CrawlingSettings;
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
import org.springframework.data.neo4j.repository.GraphRepositoryImpl;

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
     * for parsing the attributes from the datasource 
     * @see AttributeDefinition
     * @see CrawlingEngine
     * @return 
     */
    @Bean
    public FacebookCrawlingAttributeParser getAttributeParser(){
        return new FacebookCrawlingAttributeParser();
    }
}
