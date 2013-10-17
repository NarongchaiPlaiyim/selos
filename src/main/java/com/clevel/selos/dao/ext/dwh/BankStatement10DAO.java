package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement10;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatement10DAO extends GenericDAO<BankStatement10,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement10DAO() {
    }

}
