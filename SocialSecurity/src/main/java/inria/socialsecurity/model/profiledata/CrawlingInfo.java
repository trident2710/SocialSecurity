/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.profiledata;

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
    private final ProfileData pd;
    private final Integer depth;
    private final String url;
    private final FacebookLoginAccount account;

    public CrawlingInfo(ProfileData pd, Integer depth, String url,FacebookLoginAccount account) {
        this.pd = pd;
        this.depth = depth;
        this.url = url;
        this.account = account;
    }

    public ProfileData getPd() {
        return pd;
    }

    public Integer getDepth() {
        return depth;
    }

    public String getUrl() {
        return url;
    }

    public FacebookLoginAccount getAccount() {
        return account;
    }
    
    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
    
    
}
