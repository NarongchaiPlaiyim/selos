package com.clevel.selos.dao.system;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.system.SystemParameter;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SystemParameterDAO extends GenericDAO<SystemParameter,String> {
    @Inject
    private Logger log;

    @Inject
    public SystemParameterDAO() {
    }
}
