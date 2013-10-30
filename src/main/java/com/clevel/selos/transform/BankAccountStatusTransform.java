package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.view.BankAccountStatusView;

import javax.inject.Inject;

public class BankAccountStatusTransform extends Transform {

    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

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
}
