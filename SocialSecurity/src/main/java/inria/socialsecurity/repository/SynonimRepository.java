/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.repository;

import inria.socialsecurity.entity.attribute.Synonim;
import java.util.List;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * defining repository for entities of class synonim 
 * @author adychka
 */
public interface SynonimRepository extends GraphRepository<Synonim>{
    /**
     * find entities with specific attribute name
     * @param attributeName
     * @return 
     */
    List<Synonim> findByAttributeName(String attributeName);
    /**
     * find entitites with specific data source name
     * @param dataSourceName
     * @see DefaultDataSourceName
     * @return 
     */
    List<Synonim> findByDataSourceName(String dataSourceName);
}
