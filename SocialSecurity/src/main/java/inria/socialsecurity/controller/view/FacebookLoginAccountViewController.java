/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.view;

import inria.socialsecurity.entity.snaccount.FacebookLoginAccount;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import java.util.Collection;
import java.util.Collections;
import javax.validation.Valid;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author adychka
 */
@Controller
@RequestMapping("/snaccount/**")
public class FacebookLoginAccountViewController {
    
    @Autowired
    FacebookLoginAccountRepository flar;
    
    
    /**
     * get the page displaying the list of harm trees in db
     * @param account
     * @param model
     * @return jsp view
     */
    @RequestMapping(value = "page_add", method = RequestMethod.GET)
    public String getFacebookLoginAccountAddForm(@ModelAttribute("account") FacebookLoginAccount account,Model model) {
        account = new FacebookLoginAccount();
        model.addAttribute("accounts", flar.findAll());
        return "snaccount/snaccount-add";
    }
    
    /**
     * get the page displaying the list of harm trees in db
     * @param id
     * @param model
     * @return jsp view
     */
    @RequestMapping(value = "page_update/{id}", method = RequestMethod.GET)
    public String getFacebookLoginAccountUpdateForm(@PathVariable("id") Long id,Model model) {
        FacebookLoginAccount a = flar.findOne(id);
        Collection<FacebookLoginAccount> col = IteratorUtil.asCollection(flar.findAll());
        col.remove(a);
        model.addAttribute("account",a);
        model.addAttribute("accounts",col);
        return "snaccount/snaccount-update";
    }
    
    /**
     * create new facebook login 
     * @param account
     * @param result
     * @param model
     * @return jsp view
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String createFacebookLoginAccount(@ModelAttribute("account") @Valid FacebookLoginAccount account, BindingResult result, Model model) {
        flar.save(account);
        return "redirect:/settings#sec_crawl_acc";
    }
    
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String updateFacebookLoginAccount(@ModelAttribute("account") @Valid FacebookLoginAccount account, BindingResult result, Model model) {
        FacebookLoginAccount a = flar.findOne(account.getId());
        a.setLogin(account.getLogin());
        a.setPassword(account.getPassword());
        a.setFriend(account.getFriend());
        flar.save(a);
        return "redirect:/settings#sec_crawl_acc";
    }
    
    
    @RequestMapping(value = "delete/{id}",method = RequestMethod.GET)
    public String deleteFacebookLoginAccount(@PathVariable Long id){
        flar.delete(id);
        return "redirect:/settings#sec_crawl_acc";
    }
}
