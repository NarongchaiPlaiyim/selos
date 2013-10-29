package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BankStatement;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatementDAO extends GenericDAO<BankStatement, Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatementDAO() {

    }
}
