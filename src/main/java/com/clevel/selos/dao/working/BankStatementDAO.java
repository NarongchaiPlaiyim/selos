package com.clevel.selos.dao.working;

import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.dao.GenericDAO;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatementDAO extends GenericDAO<BankStatement, Integer> {
    @Inject
    private Logger log;

    @Inject
    public BankStatementDAO(){

    }
}
