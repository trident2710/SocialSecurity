/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import inria.crawlerv2.engine.account.Account;
import inria.socialsecurity.entity.user.ProfileData;

/**
 * helper class contains data parsed from create new facebook data post request
 * @author adychka
 */
public class CrawlingInfo {
    private ProfileData profileData;
    private Account[] accounts; //T, T' and T'' accounts (see socumentatiion)
    private boolean shouldCollectFF;

    public CrawlingInfo(ProfileData pd, Account[] accounts,boolean shouldCollectFF) {
        this.profileData = pd;
        this.accounts = accounts;
        this.shouldCollectFF = shouldCollectFF;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public boolean isShouldCollectFF() {
        return shouldCollectFF;
    }

    public void setShouldCollectFF(boolean shouldCollectFF) {
        this.shouldCollectFF = shouldCollectFF;
    }
    
    
    
}
