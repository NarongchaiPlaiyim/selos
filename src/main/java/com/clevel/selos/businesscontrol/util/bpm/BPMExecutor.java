package com.clevel.selos.businesscontrol.util.bpm;

import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class BPMExecutor implements Serializable {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    @ExceptionMessage
    private Message exceptionMessage;

    @Inject
    private BPMInterface bpmInterface;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private ActionDAO actionDAO;
    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private PrescreenDAO prescreenDAO;
    @Inject
    private CustomerDAO customerDAO;

    public void assignChecker(String queueName, String wobNumber, long actionCode, long workCasePreScreenId, String checkerId, String remark) throws Exception{
        Action action = actionDAO.findById(actionCode);
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);

        log.debug("assignChecker : workCasePreScreenId : {}, queueName : {}, checkerId : {}, actionCode : {}", workCasePreScreenId, queueName, checkerId, actionCode);
        if(action != null && prescreen != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("BDMCheckerUserName", checkerId);
            fields.put("ProductGroup", prescreen.getProductGroup().getName());
            if(!Util.isEmpty(remark))
                fields.put("Remarks", remark);

            log.debug("dispatch case for [Assign to Checker]..., Action_Code : {}, Action_Name : {}, BDMCheckerUserName : {}", action.getId(), action.getName(), checkerId);

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void assignToABDM(String queueName, String wobNumber, String abdmUserId, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ABDMUserName", abdmUserId);

            log.debug("dispatch case for [Assign to ABDM]..., Action_Code : {}, Action_Name : {}, BDMCheckerUserName : {}", action.getId(), action.getName(), abdmUserId);

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
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

    public void cancelCase(String queueName, String wobNumber, long actionCode, String reason, String remark) throws Exception{
        if(wobNumber != null && !wobNumber.equalsIgnoreCase("")){
            Action action = actionDAO.findById(actionCode);
            if(action != null){
                HashMap<String,String> fields = new HashMap<String, String>();
                fields.put("Action_Code", Long.toString(action.getId()));
                fields.put("Action_Name", action.getDescription());
                if(!Util.isNull(remark) && !Util.isEmpty(remark)) {
                    fields.put("Remarks", remark);
                }
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

    public void cancelRequestPriceReduction(String queueName, String wobNumber, String reason, String remark, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(!Util.isNull(action)){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            if(!Util.isEmpty(remark)) {
                fields.put("Remarks", remark);
            }
            fields.put("Reason", reason);

            log.debug("dispatch case for [Cancel Request Price Reduction]");
            execute(queueName, wobNumber, fields);
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

    //------ Submit Function Generic for BU -----------//
    public void submitForABDM(String queueName, String wobNumber, String remark, String reason, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(!Util.isNull(action)){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }

            log.debug("dispatch case for [submitCase]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getDescription());

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception("An exception occurred, Could not found an Action.");
        }
    }

    public void submitForBDM(String queueName, String wobNumber, String zmUserId, String rgmUserId, String ghUserId, String cssoUserId, String remark, String reason, BigDecimal totalCommercial, BigDecimal totalRetail, String resultCode, String productGroup, String deviationCode, int requestType, int appraisalRequestRequire, long actionCode) throws Exception{
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
            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }
            fields.put("TotalCommercial", totalCommercial.toString());
            fields.put("TotalRetail", totalRetail.toString());
            fields.put("ResultCode", resultCode);
            if(!Util.isEmpty(deviationCode)){
                fields.put("DeviationCode", deviationCode);
            }
            fields.put("RequestType", String.valueOf(requestType));
            fields.put("AppraisalReq", String.valueOf(appraisalRequestRequire));

            log.debug("dispatch case for [Submit ZM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception(exceptionMessage.get("exception.submit.workitem.notfound"));
        }
    }

    public void submitForZM(String queueName, String wobNumber, String rgmUserId, String ghUserId, String cssoUserId, String remark, String reason, String zmDecisionFlag, String zmPricingRequestFlag, BigDecimal totalCommercial, BigDecimal totalRetail, String resultCode, String deviationCode, int requestType, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        log.debug("submitForZM ::: action : {}", action);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            if(!Util.isEmpty(rgmUserId)){
                fields.put("RGMUserName", rgmUserId);
            }
            if(!Util.isEmpty(ghUserId)){
                fields.put("GHUserName", ghUserId);
            }
            if(!Util.isEmpty(cssoUserId)){
                fields.put("CSSOUserName", cssoUserId);
            }
            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }
            fields.put("ZMDecisionFlag", zmDecisionFlag);
            fields.put("ZMPricingRequestFlag", zmPricingRequestFlag);
            fields.put("TotalCommercial", totalCommercial.toString());
            fields.put("TotalRetail", totalRetail.toString());
            fields.put("ResultCode", resultCode);
            if(!Util.isEmpty(deviationCode)){
                fields.put("DeviationCode", deviationCode);
            }
            fields.put("RequestType", String.valueOf(requestType));

            log.debug("dispatch case for [submitForZM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Action [" + actionCode + "] could not found.");
        }
    }

    public void submitForZMFCash(String queueName, String wobNumber, String remark, String reason, String zmDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(!Util.isNull(action)){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ZMDecisionFlag", zmDecisionFlag);

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }

            log.debug("dispatch case for [submitForZMFCash]...");

            execute(queueName, wobNumber, fields);
        }
    }

    public void submitForRGM(String queueName, String wobNumber, String ghUserId, String cssoUserId, String remark, String reason, String rgmDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            if(!Util.isEmpty(ghUserId)){
                fields.put("GHUserName", ghUserId);
            }
            if(!Util.isEmpty(cssoUserId)){
                fields.put("CSSOUserName", cssoUserId);
            }
            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }
            fields.put("RGMDecisionFlag", rgmDecisionFlag);
            log.debug("dispatch case for [submitForRGM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Action [" + actionCode + "] could not found.");
        }
    }

    public void submitForGH(String queueName, String wobNumber, String cssoUserId, String remark, String reason, String ghDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            if(!Util.isEmpty(cssoUserId)){
                fields.put("CSSOUserName", cssoUserId);
            }
            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }
            fields.put("GHDecisionFlag", ghDecisionFlag);
            log.debug("dispatch case for [Submit CSSO]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Action [" + actionCode + "] could not found.");
        }
    }

    public void submitForCSSO(String queueName, String wobNumber, String remark, String reason, String cssoDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }
            fields.put("CSSODecisionFlag", cssoDecisionFlag);
            log.debug("dispatch case for [Submit UW]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Action [" + actionCode + "] could not found.");
        }
    }
    //------End Submit Function Generic for BU -----------//

    //------ Submit Function Generic for UW -------------//
    public void submitForUW(String queueName, String wobNumber, String remark, String reason, String uw2Name, String uw2DOALevel, String decisionFlag, String haveRG001, String appraisalRequired, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("UW2UserName", uw2Name);
            fields.put("UW2DOALevel", uw2DOALevel);
            fields.put("UW1DecisionFlag", decisionFlag);
            fields.put("UWRG001Flag", haveRG001);
            fields.put("AppraisalReq", appraisalRequired);

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }

            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }

            log.debug("dispatch case for [Submit UW2]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());
            execute(queueName, wobNumber, fields);
        }
    }

    public void submitForUW2(String queueName, String wobNumber, String remark, String reason, String decisionFlag, String haveRG001, String insuranceRequired, String approvalFlag, String tcgRequired, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("UW2DecisionFlag", decisionFlag);
            fields.put("UWRG001Flag", haveRG001);
            fields.put("InsuranceRequired", insuranceRequired);
            fields.put("ApprovalFlag", approvalFlag);
            fields.put("TCGRequired", tcgRequired);

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }

            log.debug("dispatch case for [Submit UW2]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Action [" + actionCode + "] could not found.");
        }
    }

    public void submitForBDMUW(String queueName, String wobNumber, String remark, String reason, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }
            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }

            log.debug("dispatch case for [submitForBDMUW]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Action [" + actionCode + "] could not found.");
        }
    }
    //------ End Submit Function Generic For UW ---------//

    public void submitZM(String queueName, String wobNumber, String zmUserId, String rgmUserId, String ghUserId, String cssoUserId,
                         BigDecimal totalCommercial, BigDecimal totalRetail, String resultCode,
                         String productGroup, String deviationCode, int requestType, int appraisalRequestRequire, long actionCode) throws Exception{
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
            fields.put("AppraisalReq", String.valueOf(appraisalRequestRequire));

            log.debug("dispatch case for [Submit ZM]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception(exceptionMessage.get("exception.submit.workitem.notfound"));
        }
    }

    public void submitRM(String queueName, String wobNumber, String zmDecisionFlag, String zmPricingRequestFlag, BigDecimal totalCommercial, BigDecimal totalRetail, String resultCode, String deviationCode, int requestType, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        log.debug("submitRM ::: action : {}", action);
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

            execute(queueName, wobNumber, fields);
        }else{
            throw new Exception("Exception while Submit Case, Could not find an Action");
        }
    }

    public void submitGH(String queueName, String wobNumber, String rgmDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("RGMDecisionFlag", rgmDecisionFlag);
            log.debug("dispatch case for [Submit GH]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }
    }

    public void submitCSSO(String queueName, String wobNumber, String rgmDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("GHDecisionFlag", rgmDecisionFlag);
            log.debug("dispatch case for [Submit CSSO]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }
    }

    public void submitRGMPriceReduce(String queueName, String wobNumber, String zmPriceRequestFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ZMDecisionFlag", "NA");
            fields.put("ZMPricingRequestFlag", zmPriceRequestFlag);
            log.debug("dispatch case for [Submit RGM Price Reduce]...");

            execute(queueName, wobNumber, fields);
        }
    }

    public void submitFCashZM(String queueName, String wobNumber, String zmDecisionFlag, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("ZMDecisionFlag", zmDecisionFlag);
            log.debug("dispatch case for [Submit FCASH ZM]...");

            execute(queueName, wobNumber, fields);
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

    public void submitUW2(long workCaseId, String queueName, String uw2Name, String uw2DOALevel, String decisionFlag, String haveRG001, String remark, long actionCode) throws Exception{
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
            fields.put("AppraisalReq", String.valueOf(workCase.getRequestAppraisalRequire()));

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }

            log.debug("dispatch case for [Submit UW2]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase PreScreen.");
            }
        }
    }

    public void submitCA(String wobNumber, String queueName, String decisionFlag, String haveRG001, String insuranceRequired, String approvalFlag, String tcgRequired, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("UW2DecisionFlag", decisionFlag);
            fields.put("UWRG001Flag", haveRG001);
            fields.put("InsuranceRequired", insuranceRequired);
            fields.put("ApprovalFlag", approvalFlag);
            fields.put("TCGRequired", tcgRequired);

            log.debug("dispatch case for [Submit UW2]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getName());

            execute(queueName, wobNumber, fields);
        }
    }

    public void requestAppraisal(String appNumber, String borrowerName, String productGroup, int requestType, String bdmUserName) throws Exception{
        boolean success = bpmInterface.createParallelCase(appNumber, borrowerName, productGroup, requestType, bdmUserName);
        if(!success){
            log.debug("create workcase appraisal item failed.");
            throw new Exception("exception while launch new case for appraisal");
        }
    }

    //Step after customerAcceptance
    public void requestAppraisal(String queueName, String wobNumber, long actionCode, String remark, String reason) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(!Util.isNull(action)){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            if(!Util.isEmpty(remark)){
                fields.put("Remarks", remark);
            }

            if(!Util.isEmpty(reason)){
                fields.put("Reason", reason);
            }

            log.debug("dispatch case for [Submit AAD Committee]...,");

            execute(queueName, wobNumber, fields);
        }
    }

    public void submitForAADAdmin(String aadCommitteeUserId, String appointmentDate, long appraisalLocationCode, String queueName, long actionCode, String wobNumber) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(action != null){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("AppointmentDate", appointmentDate.toString());
            fields.put("AppraisalLocationCode", Long.toString(appraisalLocationCode));
            fields.put("AADCommitteeUserName", aadCommitteeUserId);

            log.debug("dispatch case for [Submit AAD Committee]..., Action_Code : {}, Action_Name : {}, AppointmentDate : {}, AppraisalLocationCode : {}, AADCommitteeUserName : {}", actionCode, action.getDescription(), appointmentDate, appraisalLocationCode, aadCommitteeUserId);

            execute(queueName, wobNumber, fields);
        }
    }

    public void submitUW2FromCommittee(String queueName, String wobNumber, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);
        if(!Util.isNull(action)){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Submit to UW2 from AAD Committee]... Action_Code : {}, Action_Name : {}", actionCode, action.getDescription());

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

            log.debug("dispatch case for [Return BDM]..., Action_Code : {}, Action_Name : {}, UWRG001Flag : {}", action.getId(), action.getDescription(), uwRG001Flag);

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    /*public void submitCase(long workCaseId, long workCasePrescreenId, String queueName, long actionCode) throws Exception{
        WorkCase workCase = null;
        WorkCasePrescreen workCasePrescreen = null;
        if(workCaseId!=0){
            workCase = workCaseDAO.findById(workCaseId);
        } else {
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
        }

        Action action = actionDAO.findById(actionCode);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Submit BDM to UW1]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getDescription());

            if (workCase != null) {
                execute(queueName, workCase.getWobNumber(), fields);
            } else if (workCasePrescreen!=null){
                execute(queueName, workCasePrescreen.getWobNumber(), fields);
            } else {
                throw new Exception("An exception occurred, Can not find WorkCase.");
            }
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }*/

    public void submitCustomerAcceptance(String queueName, String wobNumber, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(!Util.isNull(action)){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Submit Customer Acceptance Pre-Approve]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getDescription());

            if(!Util.isEmpty(queueName)){
                execute(queueName, wobNumber, fields);
            } else {
                throw new Exception("An exception occurred, Could not found wobNumber.");
            }
        } else {
            throw new Exception("An exception occurred, Could not found Action Description.");
        }
    }

    public void submitPendingDecision(String queueName, String wobNumber, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(!Util.isNull(action)){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Submit Pending Decision Pre-Approve]...,");

            if(!Util.isEmpty(queueName) && !Util.isEmpty(wobNumber)){
                execute(queueName, wobNumber, fields);
            } else {
                throw new Exception("An exception occurred, Could not found QueueName or WobNumber");
            }
        } else {
            throw new Exception("An exception occurred, Could not found Action Description.");
        }
    }

    public void returnCase(String queueName, String wobNumber, String remark, String reason, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(!Util.isNull(action)){
            HashMap<String, String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());
            fields.put("Reason", reason);
            fields.put("Remarks", remark);

            log.debug("dispatch case for [Return Case]...,");

            if(!Util.isEmpty(queueName) && !Util.isEmpty(wobNumber)){
                execute(queueName, wobNumber, fields);
            } else {
                throw new Exception("An exception occurred, Could not found QueueName or WobNumber");
            }
        } else {
            throw new Exception("An exception occurred, Could not found Action Description.");
        }
    }

    public void restartCase(String queueName, long actionCode, String wobNumber) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Restart Case]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getDescription());

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception("An exception occurred, Can not find Action.");
        }
    }

    public void completeCase(String queueName, long actionCode, String wobNumber) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(action != null){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [Complete Case]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getDescription());

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception("An exception occurred, Could not found an Action.");
        }
    }

    public void submitCase(String queueName, String wobNumber, long actionCode) throws Exception{
        Action action = actionDAO.findById(actionCode);

        if(!Util.isNull(action)){
            HashMap<String,String> fields = new HashMap<String, String>();
            fields.put("Action_Code", Long.toString(action.getId()));
            fields.put("Action_Name", action.getDescription());

            log.debug("dispatch case for [submitCase]..., Action_Code : {}, Action_Name : {}", action.getId(), action.getDescription());

            execute(queueName, wobNumber, fields);
        } else {
            throw new Exception("An exception occurred, Could not found an Action.");
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
        HttpSession session = FacesUtil.getSession(false);
        int fetchType = Util.parseInt(session.getAttribute("fetchType"), 0);
        log.debug("updateBorrowerProductGroup : fields : {}, queueName : {}, wobNumber : {}, fetchType : {}", fields, queueName, wobNumber, fetchType);
        bpmInterface.updateCase(queueName, wobNumber, fields, fetchType);
    }

    public void updateProductGroup(String productGroup, String queueName, String wobNumber) throws Exception {
        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put("ProductGroup", productGroup);
        HttpSession session = FacesUtil.getSession(false);
        int fetchType = Util.parseInt(session.getAttribute("fetchType"), 0);
        log.debug("updateProductGroup : fields : {}, queueName : {}, wobNumber : {}, fetchType : {}", fields, queueName, wobNumber, fetchType);
        bpmInterface.updateCase(queueName, wobNumber, fields, fetchType);
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

    public void execute(String queueName, String wobNumber, HashMap<String, String> fields) throws Exception{
        HttpSession session = FacesUtil.getSession(false);
        int fetchType = Util.parseInt(session.getAttribute("fetchType"), 0);
        log.debug("BPM Execute ::: queueName : {}, wobNumber : {}, fields : {}, fetchType : {}", queueName, wobNumber, fields, fetchType);
        bpmInterface.dispatchCase(queueName, wobNumber, fields, fetchType);
    }

    public void batchDispatchCaseFromRoster(String rosterName, String[] arrayOfWobNo, HashMap<String, String> fields)
    {
        log.debug("BPM Exrcure batchDispatchCaseFromRoster. RoseterNaem : {}, WObNo : {} , Fields:{}",rosterName,arrayOfWobNo,fields);
        bpmInterface.batchDispatchCaseFromRoster(rosterName,arrayOfWobNo,fields);
    }
}
