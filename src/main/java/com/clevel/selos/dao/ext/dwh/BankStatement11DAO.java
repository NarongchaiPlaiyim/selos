package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement11;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement11DAO extends GenericDAO<BankStatement11,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement11DAO() {
    }

}
