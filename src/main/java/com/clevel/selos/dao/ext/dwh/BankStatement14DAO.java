package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement14;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement14DAO extends GenericDAO<BankStatement14,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement14DAO() {
    }

}
