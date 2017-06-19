/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import inria.socialsecurity.converter.HarmTreeToCytoscapeNotationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author adychka
 */
@Configuration
public class ConverterConfiguration {

    /**
     * bean for converting data from inner db representation to the format
     * accepted by the js library 'cytoscape' for drawing graphs in web browser
     *
     * @see http://js.cytoscape.org/
     * @return
     */
    @Bean
    public HarmTreeToCytoscapeNotationConverter getHarmTreeToCytoscapeNotationConverter() {
        return new HarmTreeToCytoscapeNotationConverter();
    }
}
