package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.model.db.master.BankAccountType;

import javax.inject.Inject;

public class AccountInfoAccountTypeTransform extends Transform {
    @Inject
    private BankAccountTypeDAO accountTypeDAO;
    private BankAccountType accountType;

    @Inject
    public AccountInfoAccountTypeTransform() {

    }

    public BankAccountType transformToModel(final int id){
        accountType = new BankAccountType();
        accountType = accountTypeDAO.findById(id);
        return accountType;
    }
}
