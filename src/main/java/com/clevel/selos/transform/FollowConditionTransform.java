package com.clevel.selos.transform;

import com.clevel.selos.dao.master.FollowConditionDAO;
import com.clevel.selos.model.db.master.FollowCondition;
import com.clevel.selos.model.view.FollowConditionView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FollowConditionTransform extends Transform {
    @Inject
    private FollowConditionDAO followConditionDAO;

    @Inject
    public FollowConditionTransform() {
    }

    public FollowCondition transformToModel(FollowConditionView followConditionView) {
        if (followConditionView == null) {
            return null;
        }

        FollowCondition followCondition;
        if (followConditionView.getId() != 0) {
            followCondition = followConditionDAO.findById(followConditionView.getId());
        } else {
            followCondition = new FollowCondition();
        }
        followCondition.setActive(followConditionView.getActive());
        followCondition.setName(followConditionView.getName());
        followCondition.setDescription(followConditionView.getDescription());
        return followCondition;
    }

    public List<FollowCondition> transformToModel(List<FollowConditionView> followConditionViewList) {
        List<FollowCondition> followConditionList = new ArrayList<FollowCondition>();
        if (followConditionViewList == null) {
            return followConditionList;
        }

        for (FollowConditionView followConditionView : followConditionViewList) {
            followConditionList.add(transformToModel(followConditionView));
        }
        return followConditionList;
    }

    public FollowConditionView transformToView(FollowCondition followCondition) {
        FollowConditionView followConditionView = new FollowConditionView();
        if (followCondition == null) {
            return followConditionView;
        }
        followConditionView.setId(followCondition.getId());
        followConditionView.setActive(followCondition.getActive());
        followConditionView.setName(followCondition.getName());
        followConditionView.setDescription(followCondition.getDescription());
        return followConditionView;
    }

    public List<FollowConditionView> transformToView(List<FollowCondition> followConditionList) {
        List<FollowConditionView> followConditionViewList = new ArrayList<FollowConditionView>();
        if (followConditionList == null) {
            return followConditionViewList;
        }

        for (FollowCondition followCondition : followConditionList) {
            followConditionViewList.add(transformToView(followCondition));
        }
        return followConditionViewList;
    }
}
