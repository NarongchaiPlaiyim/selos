package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BankAccountType;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BankAccountTypeDAO extends GenericDAO<BankAccountType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BankAccountTypeDAO() {
    }
}
