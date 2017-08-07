/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.analysis;

import java.util.Set;

/**
 * pojo representing the result of analysis for the specified harmtree. See documentation 
 * @see HarmTreeEement
 * 
 * @author adychka
 */
public class AnalysisReportItem {
    
    private boolean isValid;
    private String errMsg;
    private String harmTreeName;
    private Double severity;
    private Set<Double> likelihood;
    private Set<Double> score;
    private Double worstCase;
    private Double bestCase;

    public String getHarmTreeName() {
        return harmTreeName;
    }

    public void setHarmTreeName(String harmTreeName) {
        this.harmTreeName = harmTreeName;
    }

    public Double getSeverity() {
        return severity;
    }

    public void setSeverity(Double severity) {
        this.severity = severity;
    }

    public Set<Double> getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(Set<Double> likelihood) {
        this.likelihood = likelihood;
    }

    public Set<Double> getScore() {
        return score;
    }

    public void setScore(Set<Double> score) {
        this.score = score;
    }

    public Double getWorstCase() {
        return worstCase;
    }

    public void setWorstCase(Double worstCase) {
        this.worstCase = worstCase;
    }

    public Double getBestCase() {
        return bestCase;
    }

    public void setBestCase(Double bestCase) {
        this.bestCase = bestCase;
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }  
}
