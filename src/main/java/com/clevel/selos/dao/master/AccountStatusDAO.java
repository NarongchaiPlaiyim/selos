package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AccountStatus;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AccountStatusDAO extends GenericDAO<AccountStatus,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AccountStatusDAO() {
    }
}
