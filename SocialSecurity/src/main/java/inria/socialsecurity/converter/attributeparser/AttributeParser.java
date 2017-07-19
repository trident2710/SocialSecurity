/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter.attributeparser;

import inria.socialsecurity.constants.CrawlResultPerspective;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author adychka
 */
public interface AttributeParser<T> {
    Map<String,Map<AttributeDefinition,Object>> parsefromSourceSet(Set<T> sourceSet);
    Map<AttributeDefinition,Object> parseFromSource(T source);
}
