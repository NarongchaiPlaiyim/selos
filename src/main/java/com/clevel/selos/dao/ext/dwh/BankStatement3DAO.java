package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement3;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement3DAO extends GenericDAO<BankStatement3,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement3DAO() {
    }

}
