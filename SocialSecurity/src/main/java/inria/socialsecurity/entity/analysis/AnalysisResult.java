/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.analysis;

import java.util.List;

/**
 *
 * @author adychka
 */
public class AnalysisResult {
    Long harmTreeVertexId;
    List<Double> results;
    String message;
    public AnalysisResult(){
        
    }
    
    public AnalysisResult(Long harmTreeVertexId, List<Double> results){
        this.harmTreeVertexId = harmTreeVertexId;
        this.results = results;
    }
    
    public AnalysisResult(Long harmTreeVertexId, String message){
        this.harmTreeVertexId = harmTreeVertexId;
        this.message = message;
    }

    public Long getHarmTreeVertexId() {
        return harmTreeVertexId;
    }

    public void setHarmTreeVertexId(Long harmTreeVertexId) {
        this.harmTreeVertexId = harmTreeVertexId;
    }

    public List<Double> getResults() {
        return results;
    }

    public void setResults(List<Double> results) {
        this.results = results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
     
}
