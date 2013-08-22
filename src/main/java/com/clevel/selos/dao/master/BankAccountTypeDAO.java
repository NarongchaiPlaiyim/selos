package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BankAccountType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankAccountTypeDAO extends GenericDAO<BankAccountType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BankAccountTypeDAO() {
    }
}
