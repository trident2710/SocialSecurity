/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

import inria.crawlerv2.engine.account.Account;
import inria.socialsecurity.entity.snaccount.FacebookLoginAccount;
import inria.socialsecurity.entity.user.ProfileData;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * helper class contains data parsed from create new facebook data post request
 * @author adychka
 */
public final class CrawlingInfo {
    private ProfileData profileData;
    private Account[] accounts;

    public CrawlingInfo(ProfileData pd, Account[] accounts) {
        this.profileData = pd;
        this.accounts = accounts;
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
    
}
