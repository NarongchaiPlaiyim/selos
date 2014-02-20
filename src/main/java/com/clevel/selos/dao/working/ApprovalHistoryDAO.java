package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ApprovalHistory;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ApprovalHistoryDAO extends GenericDAO<ApprovalHistory, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ApprovalHistoryDAO(){

    }


}
