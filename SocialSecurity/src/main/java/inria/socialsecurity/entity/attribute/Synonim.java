/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.attribute;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * defining the synonim for the attribute definition
 * i.e. how does this attribute name in different data sources
 * for example, in facebook the name could be stored under the name "name"
 * and in twiiter, under the name "user_name"
 * @author adychka
 */
@NodeEntity
public class Synonim {
    
    @GraphId
    private Long id;
    
    /**
     * name of the source of data
     * @see DefaultDataSourceName
     */
    @Property
    private String dataSourceName; 
    
    /**
     * name of variable in data source
     */
    @Property
    private String attributeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    
}
