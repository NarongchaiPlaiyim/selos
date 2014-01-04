package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.model.db.master.BankBranch;

import javax.inject.Inject;

public class AccountInfoBranchTransform extends Transform {
    @Inject
    private BankBranchDAO bankBranchDAO;
    private BankBranch branch;

    @Inject
    public AccountInfoBranchTransform() {

    }

    public BankBranch transformToModel(final int id){
        branch = new BankBranch();
        branch = bankBranchDAO.findById(id);
        return branch;
    }
}
