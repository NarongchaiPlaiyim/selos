package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountStatusDAO;
import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.view.BankAccountStatusView;

import javax.inject.Inject;

public class BankAccountStatusTransform extends Transform {

    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    BankAccountStatusDAO bankAccountStatusDAO;

    @Inject
    public BankAccountStatusTransform() {

    }

    public BankAccountStatusView getBankAccountStatusView(BankAccountStatus bankAccountStatus) {
        if (bankAccountStatus != null) {
            BankAccountStatusView bankAccountStatusView = new BankAccountStatusView();
            bankAccountStatusView.setId(bankAccountStatus.getId());
            bankAccountStatusView.setCode(bankAccountStatus.getCode());
            bankAccountStatusView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(bankAccountStatus.getBankAccountType()));
            bankAccountStatusView.setActive(bankAccountStatus.getActive());
            return bankAccountStatusView;
        }
        return null;
    }

    public BankAccountStatus getBankAccountStatus(BankAccountStatusView bankAccountStatusView){
        BankAccountStatus bankAccountStatus = new BankAccountStatus();
        if(bankAccountStatusView != null){
            if(bankAccountStatusView.getId() != 0){
                bankAccountStatus = bankAccountStatusDAO.findById(bankAccountStatusView.getId());
            }
        }
        return bankAccountStatus;
    }
}
