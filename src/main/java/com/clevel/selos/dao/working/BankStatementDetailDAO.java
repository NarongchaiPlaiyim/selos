package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BankStatementDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatementDetailDAO extends GenericDAO<BankStatementDetail, Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatementDetailDAO(){

    }
}
