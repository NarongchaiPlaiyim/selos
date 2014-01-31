package com.clevel.selos.businesscontrol.util.bpm;

import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.working.WorkCase;
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
    WorkCaseDAO workCaseDAO;
    @Inject
    ActionDAO actionDAO;

    public void assignChecker(long workCasePreScreenId, String queueName, String checkerId, long actionCode) throws Exception{
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(actionCode);
        log.debug("assignChecker : workCasePreScreenId : {}, queueName : {}, checkerId : {}, actionCode : {}", workCasePrescreen, queueName, checkerId, actionCode);
        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getName());
            fields.put("BDMCheckerUserName", checkerId);

            log.debug("dispatch case for [Assign to Checker]..., Action_Code : {}, Action_Name : {}, BDMCheckerUserName : {}", action.getId(), action.getName(), checkerId);

            if (workCasePrescreen != null) {
                execute(queueName, workCasePrescreen.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void assignToABDM(long workCaseId, String queueName, String abdmUserId, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getName());
            fields.put("ABDMUserName", abdmUserId);

            log.debug("dispatch case for [Assign to ABDM]..., Action_Code : {}, Action_Name : {}, BDMCheckerUserName : {}", action.getId(), action.getName(), abdmUserId);

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void closeSales(long workCasePreScreenId, String queueName, long actionCode) throws Exception{
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getName());

            log.debug("dispatch case for [Close Sales]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCasePrescreen != null) {
                execute(queueName, workCasePrescreen.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void cancelCase(long workCasePreScreenId, long workCaseId, String queueName, long actionCode) throws Exception{
        String wobNumber = "";
        if(Long.toString(workCasePreScreenId) != null && workCasePreScreenId != 0){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            if(workCasePrescreen != null){
                wobNumber = workCasePrescreen.getWobNumber();
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        } else {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null){
                wobNumber = workCase.getWobNumber();
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase.");
            }
        }

        if(wobNumber != null && wobNumber != ""){
            Action action = actionDAO.findById(actionCode);
            if(action != null){
                HashMap<String,String> fields = new HashMap<String, String>();
                fields.put("Action_Code", Long.toString(action.getId()));
                fields.put("Action_Name", action.getName());

                log.debug("dispatch case for [Cancel Case]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

                execute(queueName, wobNumber, fields);
            } else {
                throw new Exception("An exception occurred, Can not find Action.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find WorkCase.");
        }
    }

    public void returnMaker(long workCasePreScreenId, String queueName, long actionCode) throws Exception{
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getName());

            log.debug("dispatch case for [Return BDM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCasePrescreen != null) {
                execute(queueName, workCasePrescreen.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void submitMaker(long workCasePreScreenId, String queueName, long actionCode) throws Exception{
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getName());

            log.debug("dispatch case for [Submit BDM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCasePrescreen != null) {
                execute(queueName, workCasePrescreen.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void submitZM(long workCaseId, String queueName, String zmUserId, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getName());
            fields.put("ZMUserName", zmUserId);

            log.debug("dispatch case for [Submit ZM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    private void execute(String queueName, String wobNumber, HashMap<String, String> fields) throws Exception{
        log.debug("BPM Execute ::: queueName : {}, wobNumber : {}, fields : {}", queueName, wobNumber, fields);
        bpmInterface.dispatchCase(queueName, wobNumber, fields);
    }
}
