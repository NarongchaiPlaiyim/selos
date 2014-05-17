package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.relation.StepToStatusDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.StepToStatus;
import com.clevel.selos.model.view.StepView;
import com.clevel.selos.transform.ActionTransform;
import com.clevel.selos.transform.StepTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

public class StepStatusControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private StepToStatusDAO stepToStatusDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private ActionDAO actionDAO;

    @Inject
    private StepTransform stepTransform;
    @Inject
    private ActionTransform actionTransform;

    @Inject
    public StepStatusControl(){

    }

    public HashMap<String, Integer> getStepStatusByStepStatusRole(long stepId, long statusId){
        User user = getCurrentUser();
        List<StepToStatus> stepToStatusList = stepToStatusDAO.getActionListByRole(stepId, statusId, user.getRole().getId());
        HashMap<String, Integer> stepToStatusMap = new HashMap<String, Integer>();

        if(stepToStatusList != null){
            for(StepToStatus item : stepToStatusList){
                stepToStatusMap.put(item.getAction().getName(), 1);
            }
        }

        return stepToStatusMap;
    }

    public List<SelectItem> getStepSelectItemList(){
        List<Step> stepList = stepDAO.findActiveAll();
        List<SelectItem> stepSelectItemList = stepTransform.transformToSelectItem(stepList);
        return stepSelectItemList;
    }

    public List<SelectItem> getActionSelectItemList(){
        List<Action> actionList = actionDAO.findActiveAll();
        List<SelectItem> actionSelectItemList = actionTransform.transformToSelectItem(actionList);
        return actionSelectItemList;
    }
}
