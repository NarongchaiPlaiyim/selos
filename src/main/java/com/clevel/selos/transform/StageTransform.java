package com.clevel.selos.transform;

import com.clevel.selos.dao.master.StageDAO;
import com.clevel.selos.model.db.master.Stage;
import com.clevel.selos.model.view.StageView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StageTransform extends Transform {
    @Inject
    private StageDAO stageDAO;

    @Inject
    public StageTransform() {
    }

    public Stage transformToModel(StageView stageView) {
        Stage stage = new Stage();
        if (stageView == null) {
            return stage;
        }
        if (stageView.getId() != 0) {
            stage = stageDAO.findById(stageView.getId());
        }
        stage.setName(stageView.getName());
        stage.setDescription(stageView.getDescription());
        stage.setActive(stageView.getActive());
        return stage;
    }

    public List<Stage> transformToModel(List<StageView> stageViewList) {
        List<Stage> stageList = new ArrayList<Stage>();
        if (stageViewList == null) {
            return stageList;
        }
        for (StageView stageView : stageViewList) {
            stageList.add(transformToModel(stageView));
        }
        return stageList;
    }

    public StageView transformToView(Stage stage) {
        StageView stageView = new StageView();
        if (stage == null) {
            return stageView;
        }
        stageView.setId(stage.getId());
        stageView.setName(stage.getName());
        stageView.setDescription(stage.getDescription());
        stageView.setActive(stage.getActive());
        return stageView;
    }

    public List<StageView> transformToView(List<Stage> stageList) {
        List<StageView> stageViewList = new ArrayList<StageView>();
        if (stageList == null) {
            return stageViewList;
        }
        for (Stage stage : stageList) {
            stageViewList.add(transformToView(stage));
        }
        return stageViewList;
    }
}
