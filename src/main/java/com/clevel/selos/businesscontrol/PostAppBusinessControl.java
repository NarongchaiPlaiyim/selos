package com.clevel.selos.businesscontrol;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.util.Util;

@Stateless
public class PostAppBusinessControl extends BusinessControl {
	private static final long serialVersionUID = 1881119889067519324L;
	@Inject
    @SELOS
    private Logger log;
	@Inject
    private BPMExecutor bpmExecutor;
	@Inject
	private ActionDAO actionDAO;
	@Inject
	private ReasonDAO reasonDAO;
	@Inject
	private WorkCaseDAO workCaseDAO;
	
	public void submitCA(long workCaseId, String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1015, -1, remark);
	}
	public void returnToBDM(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1005, reasonId, remark);
	}
	public void returnToUW2(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1033, reasonId, remark);
	}
	public void returnToDataEntry(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1034, reasonId, remark);
	}
	public void returnToContactCenter(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1037, reasonId, remark);
	}
	public void returnToLARBC(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1038, reasonId, remark);
	}
	public void cancelCA(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1003, reasonId, remark);
	}
	public void cancelDisbursement(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1040, reasonId, remark);
	}
	public void requestPriceReduction(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1028, -1, remark);
	}
	public void generateAgreement(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1036, -1, remark);
	}
	public void regenerateAgreement(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1039, -1, remark);
	}
	public void dataEntryComplete(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1035, -1, remark);
	}
	
	private void _executeBPM(long workCaseId,String queueName,String wobNumber,long actionId,int reasonId,String remark) throws Exception {
		WorkCase workCase = workCaseDAO.findById(workCaseId);
		Action action = actionDAO.findById(actionId);
		
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("Action_Code", Long.toString(action.getId()));
        fields.put("Action_Name", action.getDescription());
        if (reasonId > 0) {
        	Reason reason = reasonDAO.findById(reasonId);
        	String reasonStr = reason.getCode() != null ? reason.getCode() : "";
        	reasonStr = reason.getDescription() != null ? reasonStr + " - " + reason.getDescription() : reasonStr;
        	fields.put("Reason", reasonStr);
        }
        if (!Util.isEmpty(remark))
        	fields.put("Remarks", remark);
        
        String stepCode = workCase.getStep().getCode();
        //Add additional field
        if (!Util.isEmpty(stepCode)) {
        	if ("3008".equals(stepCode)) { //Check Doc
        		_checkDoc(workCase, fields);
        	} else if ("3029".equals(stepCode)) { //Generate Agreement
        		_generateAgreement(workCase, fields);
        	} else if ("3033".equals(stepCode)) { //Confirm mortgage registration
        		_confirmMortgage(workCase, fields);
        	} else if ("3035".equals(stepCode)) { //Confirm agreement sign fee
        		_confirmAgreement(workCase, fields);
        	} else if ("3037".equals(stepCode) || "3038".equals(stepCode)) { //review signed agreement
        		_reviewSign(workCase, fields);
        	} else if ("3046".equals(stepCode)) { //regenerate agreement
        		_regenAgreement(workCase, fields);
        	} else if ("3023".equals(stepCode)) { // create update customer profile
        		_createCustProfile(workCase, fields);
        	} else if ("3049".equals(stepCode)) { //setup limit
        		_setupLimit(workCase, fields);
        	}
        }
        bpmExecutor.execute(queueName, wobNumber, fields);
	}
	
	private void _checkDoc(WorkCase workCase, HashMap<String,String> fields) {
		String formCFlag = "N";
		fields.put("FormCFlag", formCFlag);
	}
	private void _generateAgreement(WorkCase workCase, HashMap<String,String> fields) {
		String appointDateStr = "";
		String mortgageRequired = "N";
		
		fields.put("AppointmentDate",appointDateStr);
		fields.put("MortgageRequired", mortgageRequired);
	}
	private void _confirmMortgage(WorkCase workCase, HashMap<String, String> fields) {
		String appointDateStr = "";
		fields.put("AppointmentDate",appointDateStr);
	}
	private void _confirmAgreement(WorkCase workCase, HashMap<String, String> fields) {
		String collectFee = "N";
		fields.put("CollecFeeRequired", collectFee);
	}
	private void _reviewSign(WorkCase workCase, HashMap<String, String> fields) {
		String pledgeRequired = "N";
		fields.put("PledgeRequired", pledgeRequired);
	}
	private void _regenAgreement(WorkCase workCase, HashMap<String, String> fields) {
		String appointDateStr = "";
		fields.put("AppointmentDate", appointDateStr);
	}
	private void _createCustProfile(WorkCase workCase, HashMap<String, String> fields) {
		String accOpenRequired = "N";
		fields.put("AccOpenRequired", accOpenRequired);
	}
	private void _setupLimit(WorkCase workCase, HashMap<String, String> fields) {
		String disbursementRequired = "N";
		String basicCheckRequired = "N";
		fields.put("DisbursementRequired", disbursementRequired);
		fields.put("BasicConditionCheckRequired", basicCheckRequired);
	}
	
}
