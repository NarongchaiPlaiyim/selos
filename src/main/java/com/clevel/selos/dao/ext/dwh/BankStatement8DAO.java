package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement8;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement8DAO extends GenericDAO<BankStatement8,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement8DAO() {
    }

}
