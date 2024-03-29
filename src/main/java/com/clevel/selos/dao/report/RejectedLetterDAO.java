package com.clevel.selos.dao.report;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.report.RejectedLetter;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RejectedLetterDAO extends GenericDAO<RejectedLetter, String> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public RejectedLetterDAO() {
    }

}
