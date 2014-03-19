package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Field;
import org.slf4j.Logger;

import javax.inject.Inject;

public class FieldDAO extends GenericDAO<Field,Long> {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    public FieldDAO() {
    }
}
