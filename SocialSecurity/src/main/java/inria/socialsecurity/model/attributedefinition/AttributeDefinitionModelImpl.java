/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.model.attributedefinition;

import inria.socialsecurity.constants.DefaultDataSourceName;
import inria.socialsecurity.entity.attribute.AttributeDefinition;
import inria.socialsecurity.entity.attribute.ComplexAttributeDefinition;
import inria.socialsecurity.exception.ObjectNotFoundException;
import inria.socialsecurity.exception.WrongArgumentException;
import inria.socialsecurity.repository.AttributeDefinitionRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import static inria.socialsecurity.constants.param.AttributeDefinition.*;
import inria.socialsecurity.entity.attribute.PrimitiveAttributeDefinition;
import inria.socialsecurity.entity.attribute.Synonim;
import inria.socialsecurity.repository.SynonimRepository;

/**
 *
 * @author adychka
 */
public class AttributeDefinitionModelImpl implements AttributeDefinitionModel{
    
    //autowired bean for crud opertations for attributes
    @Autowired
    AttributeDefinitionRepository adr;
    
    @Autowired
    SynonimRepository sr;

    @Override
    public ComplexAttributeDefinition getComplexAttributeDefinitionById(Long id) throws ObjectNotFoundException {
        ComplexAttributeDefinition cad;
        try{
            cad = (ComplexAttributeDefinition) adr.findOne(id);
        } catch(ClassCastException ex){
            throw new ObjectNotFoundException();
        }
        
        if(cad==null) throw new ObjectNotFoundException(); 
        
        return cad;
    }

    @Override
    public PrimitiveAttributeDefinition getPrimitiveAttributeDefinitionById(Long id) throws ObjectNotFoundException {
        PrimitiveAttributeDefinition pad;
        try{
            pad = (PrimitiveAttributeDefinition)adr.findOne(id);
        } catch(ClassCastException ex){
            throw new ObjectNotFoundException();
        }
        
        if(pad == null) throw new ObjectNotFoundException();
        
        return pad;
    }

    @Override
    public List<AttributeDefinition> getAllAttributeDefinitions() {
        List<AttributeDefinition> res = new ArrayList<>();

        
        List<AttributeDefinition> p = adr.findPrimitiveAttributes();
        if(p!=null) res.addAll(p);
        List<ComplexAttributeDefinition> c = adr.findComplexAttributes();
        if(c!=null) res.addAll(c);
        
        return res;
    }

    @Override
    public List<AttributeDefinition> getPrimitiveAttributeDefinitions() {
        List<AttributeDefinition> res = adr.findPrimitiveAttributes();
        return res==null?new ArrayList<>():res;
    }

    @Override
    public List<ComplexAttributeDefinition> getComlexAttributeDefinitions() {
        List<ComplexAttributeDefinition> res = adr.findComplexAttributes();
        return res==null?new ArrayList<>():res;
    }
    
    @Override
    public void updateAttributeDefinitionFromHttpRequest(Long id, HttpServletRequest request) throws WrongArgumentException, ObjectNotFoundException {
        if (request.getParameter(NAME) == null
                || request.getParameter(NAME).isEmpty()
                || request.getParameter(ID) == null) 
            throw new WrongArgumentException();
        
        if(request.getParameter(PRIMITIVE_ATTRIBUTE+"0")!=null)
            updateComplexAttributeDefinitionFromHttpRequest(id, request);
        else
            updatePrimitiveAttributeDefinitionFromHttpRequest(id, request);
        
        
    }

    @Override
    public AttributeDefinition createAttributeDefinitionFromHttpRequest(HttpServletRequest request) throws WrongArgumentException {
         if (request.getParameter(NAME) == null
                || request.getParameter(NAME).isEmpty()) {
            throw new WrongArgumentException();
        }
         if(request.getParameter(PRIMITIVE_ATTRIBUTE+"0")!=null)
            return createComplexAttributeDefinitionFromHttpRequest(request);
        else
            return createPrimitiveAttributeDefinitionFromHttpRequest(request);
    }

    @Override
    public void deleteAttributeDefinitionById(Long id) throws ObjectNotFoundException {
        AttributeDefinition ad;
        try {
            ad = (AttributeDefinition) adr.findOne(id);
        }catch (ClassCastException ex) {
            throw new ObjectNotFoundException();
        }
        if(ad==null) throw new ObjectNotFoundException();
        
        adr.delete(ad);
    }


    private void updatePrimitiveAttributeDefinitionFromHttpRequest(Long id, HttpServletRequest request) throws WrongArgumentException, ObjectNotFoundException {
        if(request.getParameter(NAME)==null)
            throw new WrongArgumentException();
        
        System.out.println(request.getParameter(DATA_TYPE));
        if(request.getParameter(DATA_TYPE)==null)
            throw new WrongArgumentException();
        
        for(DefaultDataSourceName n:DefaultDataSourceName.values())
            if(request.getParameter(n.getName())==null)
                throw new WrongArgumentException();
        
        try{
            PrimitiveAttributeDefinition pad = (PrimitiveAttributeDefinition) adr.findOne(id);
            pad.setDisplayName(request.getParameter(NAME));
            pad.setName(pad.getDisplayName().toLowerCase().replace(" ", "_"));
            pad.setSynonims(null);
            adr.save(pad);
            
            for(DefaultDataSourceName n:DefaultDataSourceName.values()){
                Synonim s = new Synonim();
                s.setDataSourceName(n.getName());
                s.setAttributeName(request.getParameter(n.getName()));
                sr.save(s);
                pad.getSynonims().add(s);
            }
            adr.save(pad);
        } catch (ClassCastException ex){
            throw new ObjectNotFoundException();
        }
    }

    private void updateComplexAttributeDefinitionFromHttpRequest(Long id, HttpServletRequest request) throws WrongArgumentException, ObjectNotFoundException {
        try {
            ComplexAttributeDefinition cad = (ComplexAttributeDefinition) adr.findOne(id);
            String dispName = request.getParameter(NAME);
            String name = dispName.toLowerCase().replace(" ", "_");
            cad.setName(name);
            cad.setDisplayName(dispName);
            cad.setSubAttributes(null);
            adr.save(cad);
            //body contains the primitive attribute ids in form 
            //"primitiveAttributei":val where i from 0 to 10 (the quantity is dynamic)
            List<AttributeDefinition> pad = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if (request.getParameter(PRIMITIVE_ATTRIBUTE + i) != null) {
                    try {
                        pad.add(adr.findOne(Long.parseLong(request.getParameter(PRIMITIVE_ATTRIBUTE + i))));
                    } catch (NumberFormatException e) {
                        throw new WrongArgumentException();
                    }
                }
            }
            cad.setSubAttributes(pad);
            adr.save(cad);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException();
        } catch (ClassCastException ex) {
            throw new ObjectNotFoundException();
        }
    }

    private AttributeDefinition createPrimitiveAttributeDefinitionFromHttpRequest(HttpServletRequest request) throws WrongArgumentException {
        if(request.getParameter(NAME)==null)
            throw new WrongArgumentException();
        
        if(request.getParameter(DATA_TYPE)==null)
            throw new WrongArgumentException();
        
        for(DefaultDataSourceName n:DefaultDataSourceName.values())
            if(request.getParameter(n.getName())==null)
                throw new WrongArgumentException();
        
        PrimitiveAttributeDefinition atr = new PrimitiveAttributeDefinition();
        
        atr.setDisplayName(request.getParameter(NAME));
        atr.setName(atr.getDisplayName().toLowerCase().replace(" ", "_"));
        
        for(DefaultDataSourceName n:DefaultDataSourceName.values()){
            Synonim s = new Synonim();
            s.setDataSourceName(n.getName());
            s.setAttributeName(request.getParameter(n.getName()));
            sr.save(s);
            atr.getSynonims().add(s);
        }
        return adr.save(atr);    
    }

    private ComplexAttributeDefinition createComplexAttributeDefinitionFromHttpRequest(HttpServletRequest request) throws WrongArgumentException {
       
        if(request.getParameter(NAME)==null)
            throw new WrongArgumentException();
        
        ComplexAttributeDefinition cad = new ComplexAttributeDefinition();
        String dispName = request.getParameter(NAME);
        String name = dispName.toLowerCase().replace(" ", "_");
        cad.setName(name);
        cad.setDisplayName(dispName);
        int i = 0;
        //body contains the primitive attribute ids in form 
        //"primitiveAttributei":val where i from 1 to 10 (the quantity is dynamic)
        List<AttributeDefinition> pad = new ArrayList<>();
        while (true) {
            if (request.getParameter(PRIMITIVE_ATTRIBUTE + i) != null) {
                try {
                    pad.add(adr.findOne(Long.parseLong(request.getParameter(PRIMITIVE_ATTRIBUTE + i))));
                } catch (NumberFormatException e) {
                    throw new WrongArgumentException();
                }
            } else {
                break;
            }
            i++;
        }
        cad.setSubAttributes(pad);
        return adr.save(cad);
    }

    @Override
    public AttributeDefinition getAttributeDefinitionById(Long id) throws ObjectNotFoundException {
        AttributeDefinition ad = adr.findOne(id);
        if(ad == null) throw new ObjectNotFoundException();
        
        return adr.findOne(id);
    }



   
    
}
