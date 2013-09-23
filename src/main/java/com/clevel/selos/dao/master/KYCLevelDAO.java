package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.KYCLevel;
import org.slf4j.Logger;

import javax.inject.Inject;

public class KYCLevelDAO extends GenericDAO<KYCLevel,Integer> {
    @Inject
    private Logger log;

    @Inject
    public KYCLevelDAO() {
    }
}
