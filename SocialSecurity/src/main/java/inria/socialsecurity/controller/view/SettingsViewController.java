/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.view;

import inria.crawlerv2.driver.WebDriverOption;
import inria.socialsecurity.entity.settings.CrawlingSettings;
import inria.socialsecurity.model.DefaultDataProcessor;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import inria.socialsecurity.repository.CrawlingSettingsRepository;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * controller for settings 
 * @author adychka
 */
@Controller
@RequestMapping("/settings/**")
public class SettingsViewController {
    
    @Autowired
    CrawlingSettingsRepository csr;
    
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Autowired
    FacebookLoginAccountRepository flar;
    
    @Autowired
    DefaultDataProcessor ddp;
    
    /**
     * get the page with settings
     * @param model
     * @return 
     */
    @RequestMapping(value = {""},method = RequestMethod.GET)
    public String getSettingsView(Model model) {
        model.addAttribute("web_driver_options",new String[]{WebDriverOption.PHANTOM.name(),WebDriverOption.GECKO.name()});
        model.addAttribute("primitive_attributes",adr.findPrimitiveAttributes());
        model.addAttribute("accounts", flar.findAll());
        if(!csr.findAll().iterator().hasNext()){
            csr.save(new CrawlingSettings());
        }
        model.addAttribute("crawling_settings", csr.findAll().iterator().next());
        return "basic/settings";
    }
    
    /**
     * update settings for crawling
     * @see CrawlingSettings
     * @param settings
     * @return 
     */
    @RequestMapping(value = {"crawling"},method = RequestMethod.POST)
    public String updateSettings(@ModelAttribute CrawlingSettings settings){
        settings.check();
        csr.deleteAll();
        csr.save(settings);
        return "redirect:/settings#sec_crawl";
    }
    
    @RequestMapping(value = {"restore/attributes"},method = RequestMethod.DELETE)
    public void restoreDefaultAttributeDefinitions(){
       ddp.deleteAllAttributeDefinitions();
       ddp.initAttributeDefinitions();
    }
}
