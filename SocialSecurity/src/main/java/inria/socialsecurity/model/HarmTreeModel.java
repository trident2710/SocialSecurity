/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model;

import com.google.gson.JsonElement;

/**
 *
 * @author adychka
 */
public interface HarmTreeModel {
    public void updateHarmTree(Long id,JsonElement displayNotation);
    public JsonElement getDisplayNotationForHarmTree(Long id);
    public void updateHarmTreeNode(Long id,JsonElement data);
    public void createHarmTreeDescendant(Long parentId,JsonElement properties);
    public void deleteHarmTreeElement(Long id);
}
