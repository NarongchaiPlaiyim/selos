package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement12;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement12DAO extends GenericDAO<BankStatement12,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement12DAO() {
    }

}
