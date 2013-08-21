package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.SettlementStatus;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SettlementStatusDAO extends GenericDAO<SettlementStatus,Integer> {
    @Inject
    private Logger log;

    @Inject
    public SettlementStatusDAO() {
    }
}
