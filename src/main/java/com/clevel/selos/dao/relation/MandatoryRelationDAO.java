package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.relation.MandatoryRelation;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandatoryRelationDAO extends GenericDAO<MandatoryRelation,Long> {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    public MandatoryRelationDAO() {
    }
}
