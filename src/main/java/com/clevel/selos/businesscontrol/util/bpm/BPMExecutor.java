package com.clevel.selos.businesscontrol.util.bpm;

import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;

public class BPMExecutor implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    BPMInterface bpmInterface;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    ActionDAO actionDAO;

    public void preScreenAssignToChecker(long workCasePreScreenId, String queueName, String checkerId, String actionCode) throws Exception{
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(Long.parseLong(actionCode));

        HashMap<String,String> fields = new HashMap<String, String>();
        fields.put("Action_Code", Long.toString(action.getId()));
        fields.put("Action_Name", action.getName());
        fields.put("BDMCheckerUserName", checkerId);

        if (workCasePrescreen != null) {
            execute(queueName, workCasePrescreen.getWobNumber(), fields);
        } else {
            throw new Exception("An exception occurred, Can not find workcaseprescreen.");
        }
    }

    public void cancelCase(){

    }

    private void execute(String queueName, String wobNumber, HashMap<String, String> fields) throws Exception{
        log.debug("BPM Execute ::: queueName : {}, wobNumber : {}, fields : {}", queueName, wobNumber, fields);
        bpmInterface.dispatchCase(queueName, wobNumber, fields);
    }
}
