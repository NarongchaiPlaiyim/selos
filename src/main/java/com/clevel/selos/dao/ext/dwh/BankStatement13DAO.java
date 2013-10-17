package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement13;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement13DAO extends GenericDAO<BankStatement13,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement13DAO() {
    }

}
