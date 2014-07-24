package com.clevel.selos.dao.report;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.report.OfferLetter;
import org.slf4j.Logger;

import javax.inject.Inject;

public class OfferLetterDAO extends GenericDAO<OfferLetter, String> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public OfferLetterDAO() {
    }
}
