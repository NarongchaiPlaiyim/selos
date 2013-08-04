package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AccountType;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AccountTypeDAO extends GenericDAO<AccountType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AccountTypeDAO() {
    }
}
