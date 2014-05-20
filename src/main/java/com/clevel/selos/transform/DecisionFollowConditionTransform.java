package com.clevel.selos.transform;

import com.clevel.selos.dao.master.FollowConditionDAO;
import com.clevel.selos.dao.working.DecisionFollowConditionDAO;
import com.clevel.selos.model.db.working.DecisionFollowCondition;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.DecisionFollowConditionView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DecisionFollowConditionTransform extends Transform {
    @Inject
    private DecisionFollowConditionDAO decisionFollowConditionDAO;
    @Inject
    private FollowConditionDAO followConditionDAO;

    @Inject
    private FollowConditionTransform followConditionTransform;

    @Inject
    public DecisionFollowConditionTransform() {
    }

    public DecisionFollowCondition transformToModel(DecisionFollowConditionView decisionFollowConditionView, WorkCase workCase) {
        if (decisionFollowConditionView == null) {
            return null;
        }

        DecisionFollowCondition decisionFollowCondition;
        if (decisionFollowConditionView.getId() != 0) {
            decisionFollowCondition = decisionFollowConditionDAO.findById(decisionFollowConditionView.getId());
        } else {
            decisionFollowCondition = new DecisionFollowCondition();
        }

        if (decisionFollowConditionView.getConditionView() != null && decisionFollowConditionView.getConditionView().getId() != 0) {
            decisionFollowCondition.setFollowCondition(followConditionDAO.findById(decisionFollowConditionView.getConditionView().getId()));
        } else {
            decisionFollowCondition.setFollowCondition(null);
        }
        decisionFollowCondition.setWorkCase(workCase);
        decisionFollowCondition.setDetail(decisionFollowConditionView.getDetail());
        decisionFollowCondition.setFollowDate(decisionFollowConditionView.getFollowDate());
        return decisionFollowCondition;
    }

    public List<DecisionFollowCondition> transformToModel(List<DecisionFollowConditionView> decisionFollowConditionViewList, WorkCase workCase) {
        List<DecisionFollowCondition> decisionFollowConditionList = new ArrayList<DecisionFollowCondition>();
        if(decisionFollowConditionViewList != null && decisionFollowConditionViewList.size() > 0){
            for (DecisionFollowConditionView decisionFollowConditionView : decisionFollowConditionViewList) {
                decisionFollowConditionList.add(transformToModel(decisionFollowConditionView, workCase));
            }
        }
        return decisionFollowConditionList;
    }

    public DecisionFollowConditionView transformToView(DecisionFollowCondition decisionFollowCondition) {
        DecisionFollowConditionView decisionFollowConditionView = new DecisionFollowConditionView();
        if (decisionFollowCondition == null) {
            return decisionFollowConditionView;
        }
        decisionFollowConditionView.setId(decisionFollowCondition.getId());
        decisionFollowConditionView.setConditionView(followConditionTransform.transformToView(decisionFollowCondition.getFollowCondition()));
        decisionFollowConditionView.setDetail(decisionFollowCondition.getDetail());
        decisionFollowConditionView.setFollowDate(decisionFollowCondition.getFollowDate());
        return decisionFollowConditionView;
    }

    public List<DecisionFollowConditionView> transformToView(List<DecisionFollowCondition> decisionFollowConditionList) {
        List<DecisionFollowConditionView> decisionFollowConditionViewList = new ArrayList<DecisionFollowConditionView>();
        if (decisionFollowConditionList == null) {
            return decisionFollowConditionViewList;
        }

        for (DecisionFollowCondition decisionFollowCondition : decisionFollowConditionList) {
            decisionFollowConditionViewList.add(transformToView(decisionFollowCondition));
        }
        return decisionFollowConditionViewList;
    }
}
