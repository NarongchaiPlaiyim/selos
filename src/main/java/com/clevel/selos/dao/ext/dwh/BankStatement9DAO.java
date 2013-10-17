package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement9;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement9DAO extends GenericDAO<BankStatement9,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement9DAO() {
    }

}
