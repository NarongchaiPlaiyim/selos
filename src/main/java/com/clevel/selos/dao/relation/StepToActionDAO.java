package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.relation.StepToAction;
import org.slf4j.Logger;

import javax.inject.Inject;

public class StepToActionDAO extends GenericDAO<StepToAction, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public StepToActionDAO() {
    }
}
