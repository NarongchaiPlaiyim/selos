package com.clevel.selos.ws;

import com.clevel.selos.dao.history.CaseCreationHistoryDAO;

import com.clevel.selos.integration.IntegrationStatus;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class WSDataPersist {
    @Inject
    Logger log;
    @Inject
    CaseCreationHistoryDAO caseCreationHistoryDAO;

    @Inject
    public WSDataPersist() {
    }

    public void addNewCase(CaseCreationHistory caseCreationHistory) {
        log.debug("addNewCase. ({})", caseCreationHistory);
        caseCreationHistoryDAO.persist(caseCreationHistory);
    }

    public void addFailedCase(CaseCreationHistory caseCreationHistory, String message) {
        log.debug("addFailedHistory (message: {}, detail: {})",message,caseCreationHistory);
        caseCreationHistory.setStatus(IntegrationStatus.FAILED);
        caseCreationHistory.setStatusDetail(message);
        addNewCase(caseCreationHistory);
    }

}
