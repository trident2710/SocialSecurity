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
import inria.socialsecurity.model.analysis.HarmTreeValidator.HarmTreeNotValidException;
import java.util.Map;
import java.util.Set;

/**
 * defines the actions needed to perform the analysis of collected data based on the harmtrees 
 * @author adychka
 */
public interface ProfileDataAnalyzer {
    
    /**
     * caluclates the harm tree likelihood. see documentation
     * @param vertex
     * @param data
     * @return
     * @throws inria.socialsecurity.model.analysis.HarmTreeValidator.HarmTreeNotValidException 
     */
    Set<Double> calculateLikelihoodForHarmTree(HarmTreeVertex vertex,ProfileData data) throws HarmTreeNotValidException;
    
    /**
     * calculates the attribute value accuracy by defining the biggest value group
     * for example, if there are 60% men and 40% women in friends, it means that the target is man with 60% accuracy
     * see documentation
     * @param data
     * @param source
     * @param type
     * @param definition
     * @return 
     */
    Map.Entry<String,Double> calculateAccuracyFor(ProfileData data, RiskSource source, ThreatType type, AttributeDefinition definition);
     
}
