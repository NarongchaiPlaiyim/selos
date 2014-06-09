package com.clevel.selos.transform;

import com.clevel.selos.dao.master.StageDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.view.StepView;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StepTransform extends Transform {
    @Inject
    private StepDAO stepDAO;
    @Inject
    private StageDAO stageDAO;

    @Inject
    private StageTransform stageTransform;

    @Inject
    public StepTransform() {
    }

    public Step transformToModel(StepView stepView) {
        if (stepView == null) {
            return null;
        }

        Step step;
        if (stepView.getId() != 0) {
            step = stepDAO.findById(stepView.getId());
        } else {
            step = new Step();
        }

        if (stepView.getStageView() != null && stepView.getStageView().getId() != 0) {
            step.setStage(stageDAO.findById(stepView.getStageView().getId()));
        } else {
            step.setStage(null);
        }

        step.setName(stepView.getName());
        step.setDescription(stepView.getDescription());
        step.setDocCheck(stepView.getDocCheck());
        step.setCheckBRMS(stepView.getCheckBRMS());
        step.setActive(stepView.getActive());
        return step;
    }

    public List<Step> transformToModel(List<StepView> stepViewList) {
        List<Step> stepList = new ArrayList<Step>();
        if (stepViewList == null) {
            return stepList;
        }
        for (StepView stepView : stepViewList) {
            stepList.add(transformToModel(stepView));
        }
        return stepList;
    }

    public StepView transformToView(Step step) {
        StepView stepView = new StepView();
        if (step == null) {
            return stepView;
        }
        stepView.setId(step.getId());
        stepView.setName(step.getName());
        stepView.setDescription(step.getDescription());
        stepView.setStageView(stageTransform.transformToView(step.getStage()));
        stepView.setDocCheck(step.getDocCheck());
        stepView.setCheckBRMS(step.getCheckBRMS());
        stepView.setActive(step.getActive());
        return stepView;
    }

    public List<StepView> transformToView(List<Step> stepList) {
        List<StepView> stepViewList = new ArrayList<StepView>();
        if (stepList == null) {
            return stepViewList;
        }
        for (Step step : stepList) {
            stepViewList.add(transformToView(step));
        }
        return stepViewList;
    }

    public List<SelectItem> transformToSelectItem(List<Step> stepList) {
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(Step step : stepList){
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(step.getId());
            selectItem.setLabel(step.getName());
            selectItem.setDescription(step.getDescription());
            selectItemList.add(selectItem);
        }

        return selectItemList;
    }

    public StepView transformToView(SelectItem selectItem){
        StepView stepView = new StepView();
        stepView.setId((Long)selectItem.getValue());
        stepView.setName(selectItem.getLabel());
        stepView.setDescription(selectItem.getDescription());
        return stepView;
    }
}
