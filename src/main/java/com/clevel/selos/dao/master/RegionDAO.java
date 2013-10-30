package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Region;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RegionDAO extends GenericDAO<Region, Integer> {
    @Inject
    private Logger log;

    @Inject
    public RegionDAO() {
    }
}
