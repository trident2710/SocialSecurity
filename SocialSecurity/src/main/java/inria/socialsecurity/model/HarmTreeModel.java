/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model;

import com.google.gson.JsonElement;

/**
 * defines the model layer for the harm tree elements request logic contains the
 * methods for crud elements etc. should be implemented by the bean
 *
 * @author adychka
 * @see HarmTreeElement
 */
public interface HarmTreeModel {

    /**
     * update the display notation of the harmtree defined by the id of the
     * vertex
     *
     * @param id - of the vertex of the harm tree
     * @see http://js.cytoscape.org/
     * @param cytoscapeDisplayNotation - the display notation
     */
    public void updateHarmTreeNotationFromCytoscape(Long id, JsonElement cytoscapeDisplayNotation);

    /**
     * get the cytoscape display notation for the harm tree defined by the id of
     * it's vertex
     *
     * @param id of the harm tree vertex
     * @see http://js.cytoscape.org/
     * @return
     */
    public JsonElement getCytoscapeDisplayNotationForHarmTree(Long id);

    /**
     * update the harm tree element by id
     *
     * @param id of the harm tree element
     * @param data in json format which contains the fields named after the
     * fields of the harm tree element
     */
    public void updateHarmTreeElement(Long id, JsonElement data);

    /**
     * create and add the descendant to the specific parent
     *
     * @param parentId - id of the parent
     * @param properties in json format which contains the fields named after
     * the fields of the harm tree element
     */
    public void createHarmTreeDescendant(Long parentId, JsonElement properties);

    /**
     * delete the specific harm tree element
     *
     * @param id - id of the harm tree element
     */
    public void deleteHarmTreeElement(Long id);
}
