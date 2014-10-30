package com.clevel.selos.ws;

import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.history.CaseCreationHistoryDAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.ecm.ECMInterfaceImpl;
import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMapping;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebService
public class CaseCreation implements WSCaseCreation, Serializable {
    @Inject
    @WS
    Logger log;
    @Inject
    ECMInterfaceImpl ecmInterface;
    @Inject
    CaseCreationHistoryDAO caseCreationHistoryDAO;
    @Inject
    WSDataPersist wsDataPersist;
    @Inject
    BPMInterface bpmInterface;
    @Inject
    STPExecutor stpExecutor;
    @Inject
    UserDAO userDAO;
    @Inject
    ReasonDAO reasonDAO;

    @Inject
    @NormalMessage
    Message normalMsg;
    @Inject
    @ValidationMessage
    Message msg;

    @Inject
    public CaseCreation() {
    }

    @Override
    @WebMethod
    public CaseCreationResponse newCase(@WebParam(name = "jobName") String jobName,
                                        @WebParam(name = "caNumber") String caNumber,
                                        @WebParam(name = "oldCaNumber") String oldCaNumber,
                                        @WebParam(name = "accountNo1") String accountNo1,
                                        @WebParam(name = "customerId") String customerId,
                                        @WebParam(name = "customerName") String customerName,
                                        @WebParam(name = "citizenId") String citizenId,
                                        @WebParam(name = "requestType") int requestType,
                                        @WebParam(name = "customerType") int customerType,
                                        @WebParam(name = "bdmId") String bdmId,
                                        @WebParam(name = "hubCode") String hubCode,
                                        @WebParam(name = "regionCode") String regionCode,
                                        @WebParam(name = "uwId") String uwId,
                                        @WebParam(name = "appInDateBDM") String appInDateBDM,
                                        @WebParam(name = "finalApproved") String finalApproved,
                                        @WebParam(name = "parallel") String parallel,
                                        @WebParam(name = "pending") String pending,
                                        @WebParam(name = "caExist") String caExist,
                                        @WebParam(name = "caEnd") String caEnd,
                                        @WebParam(name = "accountNo2") String accountNo2,
                                        @WebParam(name = "accountNo3") String accountNo3,
                                        @WebParam(name = "accountNo4") String accountNo4,
                                        @WebParam(name = "accountNo5") String accountNo5,
                                        @WebParam(name = "accountNo6") String accountNo6,
                                        @WebParam(name = "accountNo7") String accountNo7,
                                        @WebParam(name = "accountNo8") String accountNo8,
                                        @WebParam(name = "accountNo9") String accountNo9,
                                        @WebParam(name = "accountNo10") String accountNo10,
                                        @WebParam(name = "appInDateUW") String appInDateUW,
                                        @WebParam(name = "refAppNumber") String refAppNumber,
                                        @WebParam(name = "reason") String reason,
                                        @WebParam(name = "checkNCB") String checkNCB,
                                        @WebParam(name = "ssoId") String ssoId) {

        log.debug("newCase. (jobName: {}, caNumber: {}, oldCaNumber: {}, accountNo1: {}," +
                " customerId: {}, customerName: {}, citizenId: {}, requestType: {}," +
                " customerType: {}, bdmId: {}, hubCode: {}, regionCode: {}, uwId: {}," +
                " appInDateBDM: {}, finalApproved: {}, parallel: {}, pending: {}, caExist: {}," +
                " caEnd: {}, accountNo2: {}, accountNo3: {}, accountNo4: {}, accountNo5: {}," +
                " accountNo6: {}, accountNo7: {}, accountNo8: {}, accountNo9: {}, accountNo10: {}," +
                " appInDateUw: {}, refAppNumber: {}, reason: {}, checkNCB: {}, ssoId: {})",
                jobName, caNumber, oldCaNumber, accountNo1, customerId, customerName,
                citizenId, requestType, customerType, bdmId, hubCode, regionCode, uwId, appInDateBDM,
                finalApproved, parallel, pending, caExist, caEnd, accountNo2, accountNo3, accountNo4,
                accountNo5, accountNo6, accountNo7, accountNo8, accountNo9, accountNo10, appInDateUW,
                refAppNumber, reason, checkNCB, ssoId);

        Date now = new Date();
        CaseCreationHistory caseCreationHistory = new CaseCreationHistory(jobName, caNumber, oldCaNumber, accountNo1, customerId, customerName, citizenId, requestType, customerType,
                bdmId, hubCode, regionCode, uwId, appInDateBDM, finalApproved, parallel, pending, caExist, caEnd, accountNo2, accountNo3, accountNo4,
                accountNo5, accountNo6, accountNo7, accountNo8, accountNo9, accountNo10, appInDateUW, refAppNumber, reason, checkNCB, ssoId, now, ActionResult.WAITING, "", "");
        CaseCreationResponse response = new CaseCreationResponse();
        //handle all un-expected exception
        try {
            //validate duplicate CA
            if (requestType == 1 && caseCreationHistoryDAO.isExist(caNumber)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.DUPLICATE_CA, "(CA: " + caNumber + ")"));
                response.setValue(WSResponse.DUPLICATE_CA, msg.get(ValidationMapping.DUPLICATE_CA, "(CA: " + caNumber + ")"), "");
                log.debug("{}", response);
                return response;
            }

            //validate all input parameter
            if (ValidationUtil.isEmpty(jobName) || ValidationUtil.isGreaterThan(2, jobName)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(jobName)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(jobName)"), "");
                log.debug("{}", response);
                return response;
            } else if (!jobName.equalsIgnoreCase("NC")) {  //New case
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(jobName)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(jobName)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isEmpty(caNumber) || ValidationUtil.isGreaterThan(30, caNumber)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(caNumber)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(caNumber)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, oldCaNumber)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(oldCaNumber)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(oldCaNumber)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo1)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo1)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo1)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, customerId)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(customerId)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(customerId)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isEmpty(customerName) || ValidationUtil.isGreaterThan(150, customerName)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(customerName)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(customerName)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isEmpty(citizenId) || !ValidationUtil.isValueEqual(13, citizenId.length())) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(citizenId)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(citizenId)"), "");
                log.debug("{}", response);
                return response;
            } else if (!ValidationUtil.isNumeric(citizenId)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(citizenId)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(citizenId)"), "");
                log.debug("{}", response);
                return response;
            }
            if (requestType != CaseRequestTypes.NEW_CASE.value() && requestType != CaseRequestTypes.APPEAL_CASE.value() && requestType != CaseRequestTypes.RESUBMIT_CASE.value()) { //new credit, appeal/resubmit
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(requestType)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(requestType)"), "");
                log.debug("{}", response);
                return response;
            }
            if (customerType != BorrowerType.INDIVIDUAL.value() && customerType != BorrowerType.JURISTIC.value()) { //1-individual, 2-Juristic
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(customerType)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(customerType)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isEmpty(bdmId) || ValidationUtil.isGreaterThan(10, bdmId)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(bdmId)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(bdmId)"), "");
                log.debug("{}", response);
                return response;
            } else {
                //check for exist user
                try {
                    User user = null;
                    user = userDAO.findOneByCriteria(Restrictions.eq("id", bdmId), Restrictions.eq("userStatus", UserStatus.NORMAL));
                    if (user == null) {
                        wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.INVALID_BDM, bdmId));
                        response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.INVALID_BDM, bdmId), "");
                        log.debug("{}", response);
                        return response;
                    }
                } catch (Exception ex) {
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.INVALID_BDM, bdmId));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.INVALID_BDM, bdmId), "");
                    log.debug("{}", response);
                    return response;
                }
            }
            if (ValidationUtil.isEmpty(hubCode) || ValidationUtil.isGreaterThan(4, hubCode)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(hubCode)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(hubCode)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isEmpty(regionCode) || ValidationUtil.isGreaterThan(4, regionCode)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(regionCode)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(regionCode)"), "");
                log.debug("{}", response);
                return response;
            }

            if (ValidationUtil.isEmpty(appInDateBDM) || ValidationUtil.isGreaterThan(10, appInDateBDM)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(appInDateBDM)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(appInDateBDM)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(1, finalApproved)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(finalApproved)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(finalApproved)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isEmpty(parallel) || ValidationUtil.isGreaterThan(1, parallel)) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(parallel)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(parallel)"), "");
                log.debug("{}", response);
                return response;
            } else if (parallel != null && !parallel.equals("Y") && !parallel.equals("N")) {
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(parallel)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_DATA_INVALID, "(parallel)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(1, pending)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(pending)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(pending)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(1, caExist)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(caExist)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(caExist)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(1, caEnd)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(caEnd)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(caEnd)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo2)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo2)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo2)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo3)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo3)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo3)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo4)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo4)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo4)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo5)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo5)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo5)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo6)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo6)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo6)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo7)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo7)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo7)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo8)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo8)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo8)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo9)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo9)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo9)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(30, accountNo10)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo10)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(accountNo10)"), "");
                log.debug("{}", response);
                return response;
            }
            if (ValidationUtil.isNotNullAndGreaterThan(10, appInDateUW)) { //Optional
                wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(appInDateUW)"));
                response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(appInDateUW)"), "");
                log.debug("{}", response);
                return response;
            }

            if(requestType == CaseRequestTypes.APPEAL_CASE.getValue() || requestType == CaseRequestTypes.RESUBMIT_CASE.getValue()) { //check require for Appeal/ReSubmit
                if (ValidationUtil.isEmpty(uwId) || ValidationUtil.isGreaterThan(10, uwId)) {
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(uwId)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(uwId)"), "");
                    log.debug("{}", response);
                    return response;
                } else {
                    //check for exist user
                    try {
                        User user = null;
                        user = userDAO.findOneByCriteria(Restrictions.eq("id", uwId), Restrictions.eq("userStatus", UserStatus.NORMAL));
                        if (user == null) {
                            wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.INVALID_UW, uwId));
                            response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.INVALID_UW, uwId), "");
                            log.debug("{}", response);
                            return response;
                        }
                    } catch (Exception ex) {
                        wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.INVALID_UW, uwId));
                        response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.INVALID_UW, uwId), "");
                        log.debug("{}", response);
                        return response;
                    }
                }
                if (ValidationUtil.isEmpty(refAppNumber) || ValidationUtil.isGreaterThan(20, refAppNumber)) { //Required
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(refAppNumber)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(refAppNumber)"), "");
                    log.debug("{}", response);
                    return response;
                }
                if (ValidationUtil.isEmpty(reason) || ValidationUtil.isGreaterThan(20, reason)) { //Required
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reason)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reason)"), "");
                    log.debug("{}", response);
                    return response;
                } else {
                    //validate existing reason in master
                    List<Reason> reasonList = new ArrayList<Reason>();
                    if(requestType == CaseRequestTypes.APPEAL_CASE.getValue()){
                        reasonList = reasonDAO.getListByCodeAndReasonType(reason.trim(), ReasonTypeValue.APPEAL_REASON.value());
                    }else if(requestType == CaseRequestTypes.RESUBMIT_CASE.getValue()){
                        reasonList = reasonDAO.getListByCodeAndReasonType(reason.trim(), ReasonTypeValue.RESUBMIT_REASON.value());
                    }
                    if(reasonList==null || (reasonList!=null && reasonList.size()==0)){
                        wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.INVALID_REASON, reason));
                        response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.INVALID_REASON, reason), "");
                        log.debug("{}", response);
                        return response;
                    }
                }
                if (ValidationUtil.isEmpty(checkNCB) || ValidationUtil.isGreaterThan(1, checkNCB)) { //Required
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(checkNCB)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(checkNCB)"), "");
                    log.debug("{}", response);
                    return response;
                }

                //TODO: validate product program
                /*if (ValidationUtil.isEmpty(ssoId) || ValidationUtil.isGreaterThan(10, ssoId)) { //Required
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(ssoId)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(ssoId)"), "");
                    log.debug("{}", response);
                    return response;
                }*/
            } /*else {
                if (ValidationUtil.isNotNullAndGreaterThan(10, uwId)) { //Optional
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(uwId)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(uwId)"), "");
                    log.debug("{}", response);
                    return response;
                }
                if (ValidationUtil.isNotNullAndGreaterThan(20, refAppNumber)) { //Optional
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(refAppNumber)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(refAppNumber)"), "");
                    log.debug("{}", response);
                    return response;
                }
                if (ValidationUtil.isNotNullAndGreaterThan(20, reason)) { //Optional
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reason)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(reason)"), "");
                    log.debug("{}", response);
                    return response;
                }
                if (ValidationUtil.isNotNullAndGreaterThan(1, checkNCB)) { //Optional
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(checkNCB)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(checkNCB)"), "");
                    log.debug("{}", response);
                    return response;
                }
                if (ValidationUtil.isNotNullAndGreaterThan(10, ssoId)) { //Optional , required if Appeal/ReSubmit QuickLoan
                    wsDataPersist.addFailedCase(caseCreationHistory, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(ssoId)"));
                    response.setValue(WSResponse.VALIDATION_FAILED, msg.get(ValidationMapping.FIELD_LENGTH_INVALID, "(ssoId)"), "");
                    log.debug("{}", response);
                    return response;
                }
            }*/

            //generate ref number
            String applicationNumber = stpExecutor.getApplicationNumber("04");
            if(requestType == CaseRequestTypes.NEW_CASE.getValue()) {
                caseCreationHistory.setAppNumber(applicationNumber + "01");
            }else{
                String appendString = "01";
                if(!Util.isEmpty(refAppNumber)) {
                    appendString = refAppNumber.substring(refAppNumber.length()-2, refAppNumber.length());
                    int appendInt = Util.parseInt(appendString, 0) + 1;
                    if(appendInt > 9){
                        appendString = Integer.toString(appendInt);
                    }else{
                        appendString = "0".concat(Integer.toString(appendInt));
                    }
                }
                caseCreationHistory.setAppNumber(applicationNumber + appendString);
            }

            //all validation passed including new case creation in BPM.
            if (bpmInterface.createCase(caseCreationHistory)) {
                ECMCAPShare ecmcapShare = new ECMCAPShare();
                ecmcapShare.setCrsUKCANumber(caseCreationHistory.getAppNumber());
                ecmcapShare.setCrsCustName(caseCreationHistory.getCustomerName());
                ecmInterface.insert(ecmcapShare);

                log.debug("case creation success!");
                response.setValue(WSResponse.SUCCESS, normalMsg.get("ws.newCase.response.success"), caseCreationHistory.getAppNumber());
            } else {
                log.debug("case creation fail!");
                response.setValue(WSResponse.BPM_EXCEPTION, normalMsg.get("ws.newCase.response.failed"), caseCreationHistory.getAppNumber());
            }


        } catch (Exception e) {
            log.error("Exception while creating new case!", e);
            wsDataPersist.addFailedCase(caseCreationHistory, e.getMessage());
            response.setValue(WSResponse.SYSTEM_EXCEPTION, e.getMessage(), "");
        }

        log.debug("{}", response);
        return response;
    }

}
