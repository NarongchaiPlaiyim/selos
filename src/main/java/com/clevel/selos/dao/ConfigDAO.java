package com.clevel.selos.dao;

import com.clevel.selos.model.db.Config;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ConfigDAO extends GenericDAO<Config,String> {
    @Inject
    private Logger log;

    @Inject
    public ConfigDAO() {
    }
}
