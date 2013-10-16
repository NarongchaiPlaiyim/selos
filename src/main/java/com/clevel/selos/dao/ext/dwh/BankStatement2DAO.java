package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement2;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement2DAO extends GenericDAO<BankStatement2,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement2DAO() {
    }

}
