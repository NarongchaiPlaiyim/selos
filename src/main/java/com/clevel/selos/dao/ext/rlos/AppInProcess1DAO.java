package com.clevel.selos.dao.ext.rlos;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.ext.rlos.AppInProcess1;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppInProcess1DAO extends GenericDAO<AppInProcess1, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AppInProcess1DAO() {
    }

}
