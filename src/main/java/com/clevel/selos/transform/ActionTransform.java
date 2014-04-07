package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.ActionView;
import com.clevel.selos.model.db.master.Action;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ActionTransform extends Transform{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public ActionTransform(){
    }

    public ActionView transformToView(Action action){
        if(action == null)
            return null;

        ActionView actionView = new ActionView();
        actionView.setId(action.getId());
        actionView.setName(action.getName());
        actionView.setDescription(action.getDescription());
        actionView.setActive(action.getActive());
        actionView.setSubmitBPM(action.getSubmitBPM());
        return actionView;
    }

}
