package com.clevel.selos.dao.system;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.system.Config;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ConfigDAO extends GenericDAO<Config,String> {
    @Inject
    private Logger log;

    @Inject
    public ConfigDAO() {
    }
}
