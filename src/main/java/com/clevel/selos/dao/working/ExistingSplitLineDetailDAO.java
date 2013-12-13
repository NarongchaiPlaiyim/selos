package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingSplitLineDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingSplitLineDetailDAO extends GenericDAO<ExistingSplitLineDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    public ExistingSplitLineDetailDAO() {

    }


}
