package com.clevel.selos.ws;

import com.clevel.selos.dao.history.CaseCreationHistoryDAO;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;

@WebService
public class CaseCreation implements WSCaseCreation {
    @Inject
    Logger log;
    @Inject
    CaseCreationHistoryDAO caseCreationHistoryDAO;
    @Inject
    WSDataPersist wsDataPersist;

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
        //todo: update log
        log.debug("newCase. ()");
        CaseCreationResponse caseCreationResponse = null;

        //handle all un-expected exception
        try {
            //validate duplicate CA
            if (caseCreationHistoryDAO.isExist(caNumber)) {
                return new CaseCreationResponse(2,"Duplicate CA found in system!");
            }

            //validate all input parameter
            //todo: validate all input parameter here


            Date now = new Date();

            //todo: create new case in BPM and update caseCreationHistory status


            //all validation passed including new case creation in BPM.
            CaseCreationHistory caseCreationHistory = new CaseCreationHistory(jobName,caNumber,oldCaNumber,accountNo1,customerId,customerName,citizenId,requestType,customerType,
                    bdmId,hubCode,regionCode,uwId,appInDateBDM,finalApproved,parallel,pending,caExist,caEnd,accountNo2,accountNo3,accountNo4,
                    accountNo5,accountNo6,accountNo7,accountNo8,accountNo9,accountNo10,appInDateUW,now,"SUCCEED","");
            wsDataPersist.addNewCase(caseCreationHistory);

            // return succeed
            caseCreationResponse = new CaseCreationResponse(0,"Create new case success.");
        } catch (Exception e) {
            log.error("Exception while creating new case!.",e);
            caseCreationResponse = new CaseCreationResponse(12,"System exception!");
        }

        log.debug("{}",caseCreationResponse);
        return caseCreationResponse;
    }
}
