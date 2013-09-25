package com.clevel.selos.ws;

import com.clevel.selos.dao.history.CaseCreationHistoryDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.IntegrationStatus;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;

@WebService
public class CaseCreation implements WSCaseCreation {
    @Inject
    @WS
    Logger log;
    @Inject
    CaseCreationHistoryDAO caseCreationHistoryDAO;
    @Inject
    WSDataPersist wsDataPersist;
    @Inject
    BPMInterface bpmInterface;

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
                             @WebParam(name = "appInDateUW") String appInDateUW) {

        log.debug("newCase. (jobName: {}, caNumber: {}, oldCaNumber: {}, accountNo1: {}," +
                " customerId: {}, customerName: {}, citizenId: {}, requestType: {}," +
                " customerType: {}, bdmId: {}, hubCode: {}, regionCode: {}, uwId: {}," +
                " appInDateBDM: {}, finalApproved: {}, parallel: {}, pending: {}, caExist: {}," +
                " caEnd: {}, accountNo2: {}, accountNo3: {}, accountNo4: {}, accountNo5: {}," +
                " accountNo6: {}, accountNo7: {}, accountNo8: {}, accountNo9: {}, accountNo10: {}," +
                " appInDateUw: {})",jobName,caNumber,oldCaNumber,accountNo1,customerId,customerName,
                citizenId,requestType,customerType,bdmId,hubCode,regionCode,uwId,appInDateBDM,
                finalApproved,parallel,pending,caExist,caEnd,accountNo2,accountNo3,accountNo4,
                accountNo5,accountNo6,accountNo7,accountNo8,accountNo9,accountNo10,appInDateUW);

        Date now = new Date();
        CaseCreationHistory caseCreationHistory = new CaseCreationHistory(jobName,caNumber,oldCaNumber,accountNo1,customerId,customerName,citizenId,requestType,customerType,
                bdmId,hubCode,regionCode,uwId,appInDateBDM,finalApproved,parallel,pending,caExist,caEnd,accountNo2,accountNo3,accountNo4,
                accountNo5,accountNo6,accountNo7,accountNo8,accountNo9,accountNo10,appInDateUW,now,IntegrationStatus.ADDED,"","");
        CaseCreationResponse response = new CaseCreationResponse();
        //handle all un-expected exception
        try {
            //validate duplicate CA
            if (caseCreationHistoryDAO.isExist(caNumber)) {
                addFailedHistory(caseCreationHistory, msg.get("051"));
                response.setValue(WSResponse.DUPLICATE_CA, msg.get("051"));
                return response;
            }

            //validate all input parameter
            if(!ValidationUtil.isValueEqual(2, jobName.length())){
                addFailedHistory(caseCreationHistory,msg.get("1002","(jobName)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(jobName)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,caNumber.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(jobName)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(jobName)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,oldCaNumber.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(caNumber)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(caNumber)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo1.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo1)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo1)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,customerId.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(customerId)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(customerId)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(150,customerName.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(customerName)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(customerName)"));
                return response;
            }
            if(!ValidationUtil.isValueEqual(13, citizenId.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(citizenId)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(citizenId)"));
                return response;
            }
//            if(!ValidationUtil.isValueEqual(1,requestType+"".length())){
//                return new CaseCreationResponse(2,msg.get("059"));
//            }
//            if(!ValidationUtil.isValueEqual(1,customerType+"".length())){
//                return new CaseCreationResponse(2,msg.get("060"));
//            }
            if(ValidationUtil.isGreaterThan(5,bdmId.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(bdmId)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(bdmId)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(4,hubCode.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(hubCode)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(hubCode)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(4,regionCode.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(regionCode)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(regionCode)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(5,uwId.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(uwId)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(uwId)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(10,appInDateBDM.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(appInDateBDM)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(appInDateBDM)"));
                return response;
            }
            if(!ValidationUtil.isValueEqual(1, finalApproved.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(finalApproved)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(finalApproved)"));
                return response;
            }
            if(!ValidationUtil.isValueEqual(1, parallel.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(parallel)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(parallel)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(1,pending.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(pending)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(pending)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(1,caExist.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(caExist)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(caExist)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(1,caEnd.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(caEnd)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(caEnd)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo2.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo2)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo2)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo3.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo3)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo3)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo4.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo4)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo4)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo5.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo5)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo5)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo6.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo6)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo6)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo7.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo7)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo7)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo8.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo8)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo8)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo9.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo9)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo9)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(30,accountNo10.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(accountNo10)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(accountNo10)"));
                return response;
            }
            if(ValidationUtil.isGreaterThan(10,appInDateUW.length())){
                addFailedHistory(caseCreationHistory, msg.get("1002", "(appInDateUW)"));
                response.setValue(WSResponse.VALIDATION_FAILED,msg.get("1002","(appInDateUW)"));
                return response;
            }


            //all validation passed including new case creation in BPM.
//            bpmInterface.createCase(caNumber,"BDMUsername"); //todo: how to get BDM Username for create case?

            addSuccessHistory(caseCreationHistory,"REF001"); //todo: generate ref number
            // return success
            response.setValue(WSResponse.SUCCESS,normalMsg.get("ws.newCase.response.success"));

        } catch (Exception e) {
            log.error("Exception while creating new case!", e);
            addFailedHistory(caseCreationHistory, e.getMessage());
            response.setValue(WSResponse.SYSTEM_EXCEPTION,e.getMessage());
        }

        log.debug("{}",response);
        return response;
    }

    public void addFailedHistory(CaseCreationHistory caseCreationHistory, String message) {
        log.debug("addFailedHistory (message: {}, detail: {})",message,caseCreationHistory);
        caseCreationHistory.setStatus(IntegrationStatus.FAILED);
        caseCreationHistory.setStatusDetail(message);
        wsDataPersist.addNewCase(caseCreationHistory);
    }

    public void addSuccessHistory(CaseCreationHistory caseCreationHistory,String appRefNumber) {
        log.debug("addSuccessHistory. (appRefNumber: {}, detail: {})",appRefNumber,caseCreationHistory);
        caseCreationHistory.setStatus(IntegrationStatus.SUCCESS);
        caseCreationHistory.setAppRefNumber(appRefNumber);
        wsDataPersist.addNewCase(caseCreationHistory);
    }
}
