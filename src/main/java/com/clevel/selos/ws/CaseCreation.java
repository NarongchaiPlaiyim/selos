package com.clevel.selos.ws;

import com.clevel.selos.dao.history.CaseCreationHistoryDAO;
import com.clevel.selos.integration.IntegrationStatus;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
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

        log.debug("newCase. ()");
        CaseCreationResponse caseCreationResponse = null;

        //handle all un-expected exception
        try {
            //validate duplicate CA
            if (caseCreationHistoryDAO.isExist(caNumber)) {
                return new CaseCreationResponse(2,msg.get("051"));
            }

            //validate all input parameter
            if(!ValidationUtil.isEqualRange(2,jobName.length())){
                return new CaseCreationResponse(2,msg.get("052"));
            }
            if(ValidationUtil.isGreaterThan(30,caNumber.length())){
                return new CaseCreationResponse(2,msg.get("053"));
            }
            if(ValidationUtil.isGreaterThan(30,oldCaNumber.length())){
                return new CaseCreationResponse(2,msg.get("054"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo1.length())){
                return new CaseCreationResponse(2,msg.get("055"));
            }
            if(ValidationUtil.isGreaterThan(30,customerId.length())){
                return new CaseCreationResponse(2,msg.get("056"));
            }
            if(ValidationUtil.isGreaterThan(150,customerName.length())){
                return new CaseCreationResponse(2,msg.get("057"));
            }
            if(!ValidationUtil.isEqualRange(13,citizenId.length())){
                return new CaseCreationResponse(2,msg.get("058"));
            }
//            if(!ValidationUtil.isEqualRange(1,requestType+"".length())){
//                return new CaseCreationResponse(2,msg.get("059"));
//            }
//            if(!ValidationUtil.isEqualRange(1,customerType+"".length())){
//                return new CaseCreationResponse(2,msg.get("060"));
//            }
            if(ValidationUtil.isGreaterThan(5,bdmId.length())){
                return new CaseCreationResponse(2,msg.get("061"));
            }
            if(ValidationUtil.isGreaterThan(4,hubCode.length())){
                return new CaseCreationResponse(2,msg.get("062"));
            }
            if(ValidationUtil.isGreaterThan(4,regionCode.length())){
                return new CaseCreationResponse(2,msg.get("063"));
            }
            if(ValidationUtil.isGreaterThan(5,uwId.length())){
                return new CaseCreationResponse(2,msg.get("064"));
            }
            if(ValidationUtil.isGreaterThan(10,appInDateBDM.length())){
                return new CaseCreationResponse(2,msg.get("065"));
            }
            if(!ValidationUtil.isEqualRange(1,finalApproved.length())){
                return new CaseCreationResponse(2,msg.get("066"));
            }
            if(!ValidationUtil.isEqualRange(1,parallel.length())){
                return new CaseCreationResponse(2,msg.get("067"));
            }
            if(ValidationUtil.isGreaterThan(1,pending.length())){
                return new CaseCreationResponse(2,msg.get("068"));
            }
            if(ValidationUtil.isGreaterThan(1,caExist.length())){
                return new CaseCreationResponse(2,msg.get("069"));
            }
            if(ValidationUtil.isGreaterThan(1,caEnd.length())){
                return new CaseCreationResponse(2,msg.get("070"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo2.length())){
                return new CaseCreationResponse(2,msg.get("071"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo3.length())){
                return new CaseCreationResponse(2,msg.get("072"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo4.length())){
                return new CaseCreationResponse(2,msg.get("073"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo5.length())){
                return new CaseCreationResponse(2,msg.get("074"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo6.length())){
                return new CaseCreationResponse(2,msg.get("075"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo7.length())){
                return new CaseCreationResponse(2,msg.get("076"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo8.length())){
                return new CaseCreationResponse(2,msg.get("077"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo9.length())){
                return new CaseCreationResponse(2,msg.get("078"));
            }
            if(ValidationUtil.isGreaterThan(30,accountNo10.length())){
                return new CaseCreationResponse(2,msg.get("079"));
            }
            if(ValidationUtil.isGreaterThan(10,appInDateUW.length())){
                return new CaseCreationResponse(2,msg.get("053"));
            }




            Date now = new Date();

            //todo: create new case in BPM and update caseCreationHistory status


            //all validation passed including new case creation in BPM.
            CaseCreationHistory caseCreationHistory = new CaseCreationHistory(jobName,caNumber,oldCaNumber,accountNo1,customerId,customerName,citizenId,requestType,customerType,
                    bdmId,hubCode,regionCode,uwId,appInDateBDM,finalApproved,parallel,pending,caExist,caEnd,accountNo2,accountNo3,accountNo4,
                    accountNo5,accountNo6,accountNo7,accountNo8,accountNo9,accountNo10,appInDateUW,now, IntegrationStatus.SUCCESS,"","REF001");
            wsDataPersist.addNewCase(caseCreationHistory);

            // return succeed
            caseCreationResponse = new CaseCreationResponse(0,"Create new case success.","REF001");
        } catch (Exception e) {
            log.error("Exception while creating new case!",e);
            caseCreationResponse = new CaseCreationResponse(12,"System exception!");
        }

        log.debug("{}",caseCreationResponse);
        return caseCreationResponse;
    }
}
