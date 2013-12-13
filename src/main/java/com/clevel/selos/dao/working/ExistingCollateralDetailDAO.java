package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCollateralDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingCollateralDetailDAO extends GenericDAO<ExistingCollateralDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingCollateralDetailDAO() {

    }


}
