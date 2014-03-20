package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.view.RelationView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
        return relationView;
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
}
