package com.clevel.selos.businesscontrol.util.bpm;

import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BPMExecutor implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    BPMInterface bpmInterface;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    ActionDAO actionDAO;
    @Inject
    ProductGroupDAO productGroupDAO;
    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    CustomerDAO customerDAO;

    public void assignChecker(long workCasePreScreenId, String queueName, String checkerId, long actionCode, String remark) throws Exception{
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(actionCode);
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);
        List<Customer> customerList = customerDAO.getBorrowerByWorkCaseId(0, workCasePreScreenId);

        log.debug("assignChecker : workCasePreScreenId : {}, queueName : {}, checkerId : {}, actionCode : {}", workCasePrescreen, queueName, checkerId, actionCode);
        if(action != null && prescreen != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("BDMCheckerUserName", checkerId);
            fields.put("ProductGroup", prescreen.getProductGroup().getName());
            //Send only 1st Borrower
            if(customerList != null && customerList.size() > 0){
                String borrowerName = customerList.get(0).getNameTh();
                if(customerList.get(0).getLastNameTh() != null){
                    borrowerName = borrowerName + " " + customerList.get(0).getLastNameTh();
                }
                fields.put("BorrowerName", borrowerName);
            }
            /*for(Customer item : customerList){
                fields.put("BorrowerName", item.getNameTh());
            }*/
            if(!Util.isEmpty(remark)){
                fields.put("Remark", remark);
            }

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
            fields.put("Action_Name", action.getDescription());
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
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);

        if(action != null && prescreen != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ProductGroup", prescreen.getProductGroup().getName());

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

    public void cancelCase(long workCasePreScreenId, long workCaseId, String queueName, long actionCode, String reason, String remark) throws Exception{
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

        if(wobNumber != null && !wobNumber.equalsIgnoreCase("")){
            Action action = actionDAO.findById(actionCode);
            if(action != null){
                HashMap<String,String> fields = new HashMap<String, String>();
                fields.put("Action_Code", Long.toString(action.getId()));
                fields.put("Action_Name", action.getDescription());
                fields.put("Remarks", remark);
                fields.put("Reason", reason);

                log.debug("dispatch case for [Cancel Case]..., Action_Code : {}, Action_Name : {}, Remark : {}, Reason : {}", action.getId(), action.getName(), remark, reason);

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
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);

        if(action != null && prescreen != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

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
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ProductGroup", prescreen.getProductGroup().getName());

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

    public void submitZM(long workCaseId, String queueName, String zmUserId, String rgmUserId, String ghUserId, String cssoUserId,
                         BigDecimal totalCommercial, BigDecimal totalRetail, String resultCode,
                         String productGroup, String deviationCode, int requestType, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ProductGroup", productGroup);
            fields.put("ZMUserName", zmUserId);
            if(!Util.isEmpty(rgmUserId)){
                fields.put("RGMUserName", rgmUserId);
            }
            if(!Util.isEmpty(ghUserId)){
                fields.put("GHUserName", ghUserId);
            }
            if(!Util.isEmpty(cssoUserId)){
                fields.put("CSSOUserName", cssoUserId);
            }
            fields.put("TotalCommercial", totalCommercial.toString());
            fields.put("TotalRetail", totalRetail.toString());
            fields.put("ResultCode", resultCode);
            if(!Util.isEmpty(deviationCode)){
                fields.put("DeviationCode", deviationCode);
            }
            fields.put("RequestType", String.valueOf(requestType));

            log.debug("dispatch case for [Submit ZM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitRM(long workCaseId, String queueName, String zmDecisionFlag, String zmPricingRequestFlag, BigDecimal totalCommercial, BigDecimal totalRetail, String resultCode, String deviationCode, int requestType, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ZMDecisionFlag", zmDecisionFlag);
            fields.put("ZMPricingRequestFlag", zmPricingRequestFlag);
            fields.put("TotalCommercial", totalCommercial.toString());
            fields.put("TotalRetail", totalRetail.toString());
            fields.put("ResultCode", resultCode);
            if(!Util.isEmpty(deviationCode)){
                fields.put("DeviationCode", deviationCode);
            }
            fields.put("RequestType", String.valueOf(requestType));

            log.debug("dispatch case for [Submit RM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitGH(long workCaseId, String queueName, String rgmDecisionFlag, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("RGMDecisionFlag", rgmDecisionFlag);
            log.debug("dispatch case for [Submit GH]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitCSSO(long workCaseId, String queueName, String rgmDecisionFlag, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("GHDecisionFlag", rgmDecisionFlag);
            log.debug("dispatch case for [Submit CSSO]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitUWFromCSSO(long workCaseId, String queueName, String cssoDecisionFlag, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("CSSODecisionFlag", cssoDecisionFlag);
            log.debug("dispatch case for [Submit UW]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitUWFromZM(long workCaseId, String queueName, String zmDecisionFlag, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ZMDecisionFlag", zmDecisionFlag);
            log.debug("dispatch case for [Submit UW]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitUW2(long workCaseId, String queueName, String uw2Name, String uw2DOALevel, String decisionFlag, String haveRG001, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("UW2UserName", uw2Name);
            fields.put("UW2DOALevel", uw2DOALevel);
            fields.put("UW1DecisionFlag", decisionFlag);
            fields.put("UWRG001Flag", haveRG001);

            log.debug("dispatch case for [Submit UW2]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitCA(long workCaseId, String queueName, String decisionFlag, String haveRG001, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("UW2DecisionFlag", decisionFlag);
            fields.put("UWRG001Flag", haveRG001);

            log.debug("dispatch case for [Submit UW2]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void requestAppraisal(String appNumber, String borrowerName, String productGroup, int requestType, String bdmUserName) throws Exception{
        boolean success = bpmInterface.createParallelCase(appNumber, borrowerName, productGroup, requestType, bdmUserName);
        if(!success){
            log.debug("create workcase appraisal item failed.");
            throw new Exception("exception while launch new case for appraisal");
        }
    }

    public void submitAADCommittee(String appNumber, String aadCommitteeUserId, Date appointmentDate, long appraisalLocationCode, String queueName, long actionCode, String wobNumber) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("AppointmentDate", appointmentDate.toString());
            fields.put("AppraisalLocationCode", Long.toString(appraisalLocationCode));
            fields.put("AADCommitteeUserName", aadCommitteeUserId);

            log.debug("dispatch case for [Submit AAD Committee]..., Action_Code : {}, Action_Name : {}");

            execute(queueName, wobNumber, fields);
        }
    }

    public void returnBDM(long workCaseId, String queueName, long actionCode, boolean hasRG001) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);
        String uwRG001Flag = "N";
        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            if(hasRG001){
                uwRG001Flag = "Y";
            } else {
                uwRG001Flag = "N";
            }
            fields.put("UWRG001Flag", uwRG001Flag);

            log.debug("dispatch case for [Return BDM]..., Action_Code : {}, Action_Name : {}, UWRG001Flag : {}", action.getId(), action.getName(), uwRG001Flag);

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void submitUW1(long workCaseId, String queueName, long actionCode) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Action action = actionDAO.findById(actionCode);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Submit BDM to UW1]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }
    
    public void executeBPM(long workCaseId, String queueName, long actionCode, String reason, String remark) throws Exception {
    	WorkCase workCase = null;
    	if (workCaseId > 0)
    		workCase = workCaseDAO.findById(workCaseId);
    	if (workCase == null) {
    		throw new Exception("An exception occurred, Can not find WorkCase.");
    	}
    	
    	String wobNumber = workCase.getWobNumber();
    	if (Util.isEmpty(wobNumber)) {
    		throw new Exception("An exception occurred, Can not find WOB number for WorkCase.");
    	}
    	Action action = null;
    	if (actionCode >= 0) 
    		action = actionDAO.findById(actionCode);
    	if (action == null) {
    		throw new Exception("An exception occurred, Can not find Action.");
    	}
    	
    	HashMap<String, String> fields = new HashMap<String, String>();
    	fields.put("Action_Code", Long.toString(actionCode));
        fields.put("Action_Name", action.getDescription());
        if (!Util.isEmpty(remark))
        	fields.put("Remarks", remark);
        if (!Util.isEmpty(reason))
        	fields.put("Reason", reason);

        log.debug("dispatch case for [Cancel Case]..., Action_Code : {}, Action_Name : {}, Remark : {}, Reason : {}", action.getId(), action.getName(), remark, reason);
        execute(queueName, wobNumber, fields);
    }

    public void updateBorrowerProductGroup(String borrowerName, String productGroup, String queueName, String wobNumber) throws Exception{
        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put("BorrowerName", borrowerName);
        fields.put("ProductGroup", productGroup);

        log.debug("updateBorrowerProductGroup : fields : {}", fields);
        bpmInterface.updateCase(queueName, wobNumber, fields);
    }

    public void selectCase(long actionCode, String queueName, String wobNumber) throws Exception{
        Action action = null;
        if (actionCode >= 0)
            action = actionDAO.findById(actionCode);
        if (action == null) {
            throw new Exception("An exception occurred, Can not find Action.");
        }

        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put("Action_Code", Long.toString(actionCode));
        fields.put("Action_Name", action.getDescription());

        log.debug("dispatch case for [Select Case]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());
        execute(queueName, wobNumber, fields);
    }
    
    private void execute(String queueName, String wobNumber, HashMap<String, String> fields) throws Exception{
        log.debug("BPM Execute ::: queueName : {}, wobNumber : {}, fields : {}", queueName, wobNumber, fields);
        bpmInterface.dispatchCase(queueName, wobNumber, fields);
    }

    public void batchDispatchCaseFromRoster(String rosterName, String[] arrayOfWobNo, HashMap<String, String> fields)
    {
        log.debug("BPM Exrcure batchDispatchCaseFromRoster. RoseterNaem : {}, WObNo : {} , Fields:{}",rosterName,arrayOfWobNo,fields);
        bpmInterface.batchDispatchCaseFromRoster(rosterName,arrayOfWobNo,fields);
    }
}
