/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * interface for converting data from one format to another
 *
 * @author adychka
 * @param <T>  input data class
 */
public abstract class CytoscapeNotationConverter<T> implements Converter<T,JsonElement>{
 
    /**
     * create empty object in form accepted by the cytoscape.js
     *
     * @return
     */
    protected JsonObject createDefaultJsonObject() {
        return new JsonParser().parse("{\n"
                + "	\"elements\": {\n"
                + "		\"nodes\": [],\n"
                + "		\"edges\": []\n"
                + "	}\n"
                + "}").getAsJsonObject();
    }

}
