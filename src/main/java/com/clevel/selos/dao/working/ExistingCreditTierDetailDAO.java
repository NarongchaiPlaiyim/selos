package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCreditTierDetail;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingCreditTierDetailDAO extends GenericDAO<ExistingCreditTierDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    public ExistingCreditTierDetailDAO() {

    }


}
