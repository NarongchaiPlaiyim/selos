package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement1;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement1DAO extends GenericDAO<BankStatement1,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement1DAO() {
    }

}
