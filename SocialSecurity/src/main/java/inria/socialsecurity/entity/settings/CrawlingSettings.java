/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import inria.crawlerv2.driver.WebDriverOption;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * entity for storing the settings needed for FBCrawler
 * @see inria.crawlerv2.engine.CrawlingEngineSettings
 * @author adychka
 */
@NodeEntity
public class CrawlingSettings {
    @JsonIgnore
    @GraphId
    Long id;
    
    @Property
    Integer longWaitMillis = 10000;
    
    @Property
    Integer shortWaitMillis = 5000;
    
    @Property
    Integer waitForElemLoadSec = 5;
    
    @Property
    Integer requestDelay = 20000;
    
    @Property
    Integer changeAccountAfter = 10;
    
    @Property
    Integer delayBeforeRunInMillis = 0;
    
    @Property
    Integer maxFriendsToCollect = 10000;
    
    @Property
    String webDriverOption = WebDriverOption.PHANTOM.name();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLongWaitMillis() {
        return longWaitMillis;
    }

    public void setLongWaitMillis(Integer longWaitMillis) {
        this.longWaitMillis = longWaitMillis;
    }

    public Integer getShortWaitMillis() {
        return shortWaitMillis;
    }

    public void setShortWaitMillis(Integer shortWaitMillis) {
        this.shortWaitMillis = shortWaitMillis;
    }

    public Integer getWaitForElemLoadSec() {
        return waitForElemLoadSec;
    }

    public void setWaitForElemLoadSec(Integer waitForElemLoadSec) {
        this.waitForElemLoadSec = waitForElemLoadSec;
    }

    public Integer getRequestDelay() {
        return requestDelay;
    }

    public void setRequestDelay(Integer requestDelay) {
        this.requestDelay = requestDelay;
    }

    public Integer getChangeAccountAfter() {
        return changeAccountAfter;
    }

    public void setChangeAccountAfter(Integer changeAccountAfter) {
        this.changeAccountAfter = changeAccountAfter;
    }

    public Integer getDelayBeforeRunInMillis() {
        return delayBeforeRunInMillis;
    }

    public void setDelayBeforeRunInMillis(Integer delayBeforeRun) {
        this.delayBeforeRunInMillis = delayBeforeRun;
    }

    public WebDriverOption getWebDriverOption() {
        return webDriverOption.equals(WebDriverOption.GECKO.name())?WebDriverOption.GECKO:WebDriverOption.PHANTOM;
    }

    public void setWebDriverOption(WebDriverOption webDriverOption) {
        this.webDriverOption = webDriverOption.name();
    }

    public Integer getMaxFriendsToCollect() {
        return maxFriendsToCollect;
    }

    public void setMaxFriendsToCollect(Integer maxFriendsToCollect) {
        this.maxFriendsToCollect = maxFriendsToCollect;
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
    
    public void check(){
        if(longWaitMillis <0)
            throw new RuntimeException("wrong longVaitMillis value");
        if(shortWaitMillis <0)
            throw new RuntimeException("wrong shortVaitMillis value");
        if(waitForElemLoadSec <0)
            throw new RuntimeException("wrong waitForElemLoadSec value");
        if(requestDelay <0)
            throw new RuntimeException("wrong requestDelay value");
        if(changeAccountAfter <0)
            throw new RuntimeException("wrong changeAccountAfter value");
        if(delayBeforeRunInMillis <0)
            throw new RuntimeException("wrong delayBeforeRunInMillis value");
        if(maxFriendsToCollect <0)
            throw new RuntimeException("wrong maxFriendsToCollect value");
        if(webDriverOption == null)
            throw new RuntimeException("wrong webDriverOption value");
    }
    
   
}
