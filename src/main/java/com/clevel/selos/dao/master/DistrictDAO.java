package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.District;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DistrictDAO extends GenericDAO<District,Integer> {
    @Inject
    private Logger log;

    @Inject
    public DistrictDAO() {
    }
}
