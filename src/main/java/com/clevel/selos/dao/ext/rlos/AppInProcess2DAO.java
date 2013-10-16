package com.clevel.selos.dao.ext.rlos;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.rlos.AppInProcess2;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppInProcess2DAO extends GenericDAO<AppInProcess2,Long> {
    @Inject
    private Logger log;

    @Inject
    public AppInProcess2DAO() {
    }

}
