package com.clevel.selos.transform;


import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.view.BankAccountTypeView;

import javax.inject.Inject;

public class BankAccountTypeTransform extends Transform {

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    public BankAccountTypeTransform(){

    }

    public BankAccountTypeView getBankAccountTypeView(BankAccountType bankAccountType){
        if(bankAccountType != null){
            BankAccountTypeView bankAccountTypeView = new BankAccountTypeView();
            bankAccountTypeView.setId(bankAccountType.getId());
            bankAccountTypeView.setName(bankAccountType.getName());
            bankAccountTypeView.setShortname(bankAccountType.getShortname());
            bankAccountTypeView.setOpenAccountFlag(bankAccountType.getOpenAccountFlag());
            bankAccountTypeView.setBankStatementFlag(bankAccountType.getBankStatementFlag());
            return bankAccountTypeView;
        }
        return null;
    }

    public BankAccountType getBankAccountType(BankAccountTypeView bankAccountTypeView){
        if(bankAccountTypeView != null){
            BankAccountType bankAccountType = new BankAccountType();
            bankAccountType.setId(bankAccountTypeView.getId());
            bankAccountType.setName(bankAccountTypeView.getName());
            bankAccountType.setShortname(bankAccountTypeView.getShortname());
            bankAccountType.setBankStatementFlag(bankAccountTypeView.getBankStatementFlag());
            bankAccountType.setOpenAccountFlag(bankAccountTypeView.getOpenAccountFlag());
            bankAccountType.setActive(bankAccountTypeView.getActive());

            return bankAccountType;
        }
        return null;
    }
}
