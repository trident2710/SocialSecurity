/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.controller;

import inria.socialsecurity.entity.harmtree.HarmTreeVertex;
import inria.socialsecurity.repository.HarmTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * view controller for displaing the pages for manipulations with the harm trees
 *
 * @see HarmTreeElement
 * @author adychka
 */
@Controller
@RequestMapping("/harmtrees*")
public class HarmTreeController {

    static final String ATR_HARMT = "harm_trees";
    static final String ATR_HRMT_ID = "id";

    /**
     * autowired repository for crud the harm tree elements
     */
    @Autowired
    HarmTreeRepository htr;

    /**
     * get the page displaying the list of harm trees in db
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/harmtrees-all", method = RequestMethod.GET)
    public String showAll(Model model) {
        model.addAttribute(ATR_HARMT, htr.getTreeVertices());
        return "harmtree_all";
    }

    /**
     * add new harm tree and redirect to the page for updating this harm tree
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/harmtrees-add", method = RequestMethod.GET)
    public String addHarmTree(Model model) {
        model.addAttribute(ATR_HARMT, htr.getTreeVertices());
        HarmTreeVertex t = new HarmTreeVertex();
        t.setName("new harm tree");
        t.setDescription("description");
        return "redirect:harmtrees-update?id=" + htr.save(t).getId();
    }

    /**
     * get the page for updating the harm tree with the specified id
     *
     * @see HarmTreeVertex
     * @param id id of hte harm tree vertex
     * @param model
     * @return
     */
    @RequestMapping(value = "/harmtrees-update", method = RequestMethod.GET)
    public String showUpdatePage(@RequestParam String id, Model model) {
        model.addAttribute(ATR_HRMT_ID, id);
        return "harmtree_update";
    }

    @RequestMapping(value = "/harmtrees-specific", method = RequestMethod.DELETE)
    public ResponseEntity deleteAttribute(@RequestParam String id, Model model) {
        Long idValue;
        try {
            idValue = Long.parseLong(id);
            HarmTreeVertex htln = (HarmTreeVertex) htr.findOne(idValue);
            htr.delete(htln);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (ClassCastException ce) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
