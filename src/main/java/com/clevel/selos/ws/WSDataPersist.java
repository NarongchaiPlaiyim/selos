package com.clevel.selos.ws;

import com.clevel.selos.dao.history.CaseCreationHistoryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class WSDataPersist implements Serializable {
    @Inject
    @SELOS
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
        log.debug("addFailedHistory (message: {}, detail: {})", message, caseCreationHistory);
        caseCreationHistory.setStatus(ActionResult.FAILED);
        caseCreationHistory.setStatusDetail(message);
        addNewCase(caseCreationHistory);
    }

}
