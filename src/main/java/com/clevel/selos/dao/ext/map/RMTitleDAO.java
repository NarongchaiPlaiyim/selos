package com.clevel.selos.dao.ext.map;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.map.RMTitle;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RMTitleDAO extends GenericDAO<RMTitle, Integer> {
    @Inject
    private Logger log;

    @Inject
    public RMTitleDAO() {
    }
}
