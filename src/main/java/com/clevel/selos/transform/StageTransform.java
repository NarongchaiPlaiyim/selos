package com.clevel.selos.transform;

import com.clevel.selos.dao.master.StageDAO;
import com.clevel.selos.model.db.master.Stage;
import com.clevel.selos.model.view.StageView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StageTransform extends Transform {
    @Inject
    private StageDAO stageDAO;

    @Inject
    public StageTransform() {
    }

    public Stage transformToModel(StageView stageView) {
        if (stageView == null) {
            return null;
        }

        Stage stage;
        if (stageView.getId() != 0) {
            stage = stageDAO.findById(stageView.getId());
        } else {
            stage = new Stage();
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

    public Map<Integer, StageView> transformToCache(List<Stage> stageList) {
        if(stageList == null || stageList.size() == 0)
            return null;

        Map<Integer, StageView> _tmpMap = new ConcurrentHashMap<Integer, StageView>();
        for(Stage stage : stageList){
            StageView stageView = transformToView(stage);
            _tmpMap.put(stageView.getId(), stageView);
        }
        return _tmpMap;
    }
}
