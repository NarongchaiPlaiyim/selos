package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement7;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement7DAO extends GenericDAO<BankStatement7,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement7DAO() {
    }

}
