package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.relation.StepToStatusDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.StepToStatus;
import org.slf4j.Logger;

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
}
