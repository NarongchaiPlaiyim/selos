package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.view.ActionView;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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

    public List<SelectItem> transformToSelectItem(List<Action> actionList){
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(Action action : actionList){
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(action.getId());
            selectItem.setLabel(action.getName());
            selectItem.setDescription(action.getDescription());
            selectItemList.add(selectItem);
        }
        return selectItemList;
    }

    public ActionView transformToView(SelectItem selectItem){
        if(selectItem == null)
            return null;

        ActionView actionView = new ActionView();
        actionView.setId((Long)selectItem.getValue());
        actionView.setName(selectItem.getLabel());
        actionView.setDescription(selectItem.getDescription());
        return actionView;
    }

}
