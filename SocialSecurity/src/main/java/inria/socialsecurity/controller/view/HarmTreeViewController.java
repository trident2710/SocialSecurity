/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller.view;

import static inria.socialsecurity.constants.param.HarmTree.*;
import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.model.HarmTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * view controller for displaing the pages for manipulations with the harm trees
 *
 * @see HarmTreeElement
 * @author adychka
 */
@Controller
@RequestMapping("/harmtrees/**")
public class HarmTreeViewController {



    /**
     * autowired repository for model layer
     */
    @Autowired
    HarmTreeModel htm;

    /**
     * get the page displaying the list of harm trees in db
     * @param model
     * @return jsp view
     */
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public String getHarmTreeAllPage(Model model) {
        model.addAttribute(HARM_TREES, htm.getHarmTrees());
        return "harmtree/harmtree_all";
    }

    /**
     * add new harm tree and redirect to the page for updating this harm tree
     * @param model
     * @return jsp view
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String getHarmTreeAddPage(Model model) {
        HarmTreeVertex t = htm.createHarmTree("new harm tree", "description");
        return "redirect:harmtrees/update/" + t.getId();
    }

    /**
     * get the page for updating the harm tree with the specified id
     *
     * @see HarmTreeVertex
     * @param id id of hte harm tree vertex
     * @param model
     * @return
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String getHarmTreeUpdatePage(@PathVariable String id, Model model) {
        model.addAttribute(ID, id);
        return "harmtree/harmtree_update";
    }

    
}
