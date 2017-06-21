/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.entity.attribute;

import inria.socialsecurity.constants.DefaultDataSourceName;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * defining the primitive attribute definition
 * i.e. the attribute which contains one value
 * for example, first name or last name or year of birth
 * @author adychka
 */
@NodeEntity
public class PrimitiveAttributeDefinition extends AttributeDefinition{
    
    @Property
    private String dataType;
    
    @Relationship(type = "HAS_SYNONIMS",direction = Relationship.OUTGOING)
    private List<Synonim> synonims;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<Synonim> getSynonims() {
        if(synonims == null)
            synonims = new ArrayList<>();
        return synonims;
    }

    public void setSynonims(List<Synonim> synonims) {
        this.synonims = synonims;
    }
    
    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /**
     * get synonim by the name of data source
     * @param name
     * @return 
     */
    public Synonim getSynonimByDataSourceName(DefaultDataSourceName name){
        if(this.synonims==null)
            return null;
        
        for(Synonim s:synonims){
            if(s.getDataSourceName().equals(name.getName()))
                return s;
        }
        return null;
    }
    
}
