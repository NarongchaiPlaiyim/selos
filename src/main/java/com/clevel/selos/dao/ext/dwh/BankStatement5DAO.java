package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement5;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement5DAO extends GenericDAO<BankStatement5,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement5DAO() {
    }

}
