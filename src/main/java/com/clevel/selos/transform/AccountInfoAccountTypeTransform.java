package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.model.db.master.BankAccountType;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

public class AccountInfoAccountTypeTransform extends Transform {
    @Inject
    private BankAccountTypeDAO accountTypeDAO;

    @Inject
    public AccountInfoAccountTypeTransform() {
    }

    public BankAccountType transformToModel(final int id){
        BankAccountType accountType;
        try {
            accountType = accountTypeDAO.findById(id);
        } catch (EntityNotFoundException e) {
            log.debug("{}",e);
            accountType = new BankAccountType();
        }
        return accountType;
    }
}
