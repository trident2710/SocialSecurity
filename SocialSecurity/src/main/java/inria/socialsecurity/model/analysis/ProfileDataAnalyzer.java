/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.analysis;

import inria.socialsecurity.constants.RiskSource;
import inria.socialsecurity.constants.ThreatType;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.entity.user.ProfileData;
import inria.socialsecurity.model.analysis.HarmTreeEvaluator.HarmTreeNotValidException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author adychka
 */
public interface ProfileDataAnalyzer {
    List<Double> calculateLikelihoodForHarmTree(HarmTreeVertex vertex,ProfileData data) throws HarmTreeNotValidException;
    
    Map.Entry<String,Double> calculateAccuracyFor(ProfileData data, RiskSource source, ThreatType type, AttributeDefinition definition);
    
    
}
