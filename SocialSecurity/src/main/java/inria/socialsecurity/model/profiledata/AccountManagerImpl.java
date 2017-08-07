/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import inria.crawlerv2.engine.account.Account;
import inria.crawlerv2.engine.account.AccountManager;
import inria.socialsecurity.entity.snaccount.FacebookLoginAccount;
import inria.socialsecurity.repository.FacebookLoginAccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author adychka
 */
@Deprecated
public class AccountManagerImpl implements AccountManager{
    @Autowired
    FacebookLoginAccountRepository snar;
        
    private final List<Account> accounts = new ArrayList<>();
    @Override
    public List<Account> getAllAccounts() {
        if(accounts.isEmpty()) fetchAccounts();
        return accounts;
    }

    @Override
    public List<Account> getBannedAccounts() {
        if(accounts.isEmpty()) fetchAccounts();
        return accounts.stream()
                .filter(a->a.isBanned())
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> getWorkingAccounts() {
        if(accounts.isEmpty()) fetchAccounts();
        return accounts.stream()
                .filter(a->!a.isBanned())
                .collect(Collectors.toList());
    }

    @Override
    public Account getRandomWorkingAccount() {
        return getWorkingAccounts().get(new Random().nextInt(getWorkingAccounts().size()));
    }

    @Override
    public Account addAccount(String login, String password) {
        FacebookLoginAccount fla = new FacebookLoginAccount();
        fla.setLogin(login);
        fla.setPassword(password);
        fla.setIsClosed(Boolean.FALSE);
        snar.save(fla);

        if(accounts.isEmpty()) fetchAccounts();
        Account a = new Account(fla.getLogin(), fla.getPassword(), fla.getIsClosed());
        accounts.add(a);
        return a;
    }

    @Override
    public void removeAccount(Account account) {
        FacebookLoginAccount fla = snar.findByLoginAndPassword(account.getLogin(), account.getPassword());
        if(fla!=null) snar.delete(fla);
        accounts.remove(account);
    }

    @Override
    public void save() {
        //saves everything by default, not needed
    }
    
    public Account get(String login,String password){
        if(accounts.isEmpty()) fetchAccounts();
        for(Account a:accounts){
            if(a.getLogin().equals(login)&&a.getPassword().equals(password))return a;
        }
        return null;
    }
    
    public Account getOtherThan(Account account){
        if(accounts.isEmpty()) fetchAccounts();
        if(getWorkingAccounts().size()==1)
            return null;
        while (true) {            
            Account a = getRandomWorkingAccount();
            if(!a.equals(account)){
                return a;
            }
        }
    }

    private void fetchAccounts(){
        snar.findAll().forEach((a)->{
            accounts.add(new Account(a.getLogin(), a.getPassword(), a.getIsClosed()));
        });
    }
}
