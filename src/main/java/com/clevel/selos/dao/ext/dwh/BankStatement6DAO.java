package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement6;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement6DAO extends GenericDAO<BankStatement6,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement6DAO() {
    }

}
