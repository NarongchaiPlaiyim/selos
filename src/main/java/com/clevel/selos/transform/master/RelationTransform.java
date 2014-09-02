package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.view.master.RelationView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RelationTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public RelationTransform() {
    }

    public Relation transformToModel(RelationView relationView) {
        Relation relation = new Relation();
        if (relationView == null) {
            log.debug("transformToModel() relationView is null!");
            return relation;
        }

        relation.setId(relationView.getId());
        relation.setActive(1);
        relation.setDescription(relationView.getDescription());
        return relation;
    }

    public RelationView transformToView(Relation relation) {
        RelationView relationView = new RelationView();
        if (relation == null) {
            log.debug("transformToView() relation is null!");
            return relationView;
        }

        relationView.setId(relation.getId());
        relationView.setDescription(relation.getDescription());
        relationView.setActive(relation.getActive());
        relationView.setBrmsCode(relation.getBrmsCode());
        relationView.setCanBeAttorney(relation.isCanBeAttorney());
        relationView.setCanBePOA(relation.isCanBePOA());
        return relationView;
    }

    public SelectItem transformToSelectItem(RelationView relationView){
        SelectItem selectItem = new SelectItem();
        if(relationView != null) {
            selectItem.setLabel(relationView.getDescription());
            selectItem.setValue(relationView.getId());
        }
        return selectItem;
    }

    public List<RelationView> transformToViewList(List<Relation> relations) {
        List<RelationView> relationViews = new ArrayList<RelationView>();
        if (relations == null) {
            log.debug("transformToViewList() relations is null!");
            return relationViews;
        }

        for (Relation relation : relations) {
            relationViews.add(transformToView(relation));
        }
        return relationViews;
    }

    public Map<Integer, RelationView> transformToCache(List<Relation> relationList){
        if(relationList == null || relationList.size() == 0)
            return null;
        Map<Integer, RelationView> _tmpMap = new ConcurrentHashMap<Integer, RelationView>();
        for(Relation relation : relationList){
            RelationView relationView = transformToView(relation);
            _tmpMap.put(relationView.getId(), relationView);
        }
        return _tmpMap;
    }
}
