package com.clevel.selos.transform;

import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.view.StepView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StepTransform extends Transform {
    @Inject
    private StepDAO stepDAO;

    @Inject
    private StageTransform stageTransform;

    @Inject
    public StepTransform() {
    }

    public Step transformToModel(StepView stepView) {
        Step step = new Step();
        if (stepView == null) {
            return step;
        }
        if (stepView.getId() != 0) {
            step = stepDAO.findById(stepView.getId());
        }
        step.setName(stepView.getName());
        step.setDescription(stepView.getDescription());
        step.setStage(stageTransform.transformToModel(stepView.getStageView()));
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
}
