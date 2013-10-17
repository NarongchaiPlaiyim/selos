package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement4;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement4DAO extends GenericDAO<BankStatement4,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement4DAO() {
    }

}
