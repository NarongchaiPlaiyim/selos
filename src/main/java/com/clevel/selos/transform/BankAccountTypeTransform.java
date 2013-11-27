package com.clevel.selos.transform;


import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.view.BankAccountTypeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BankAccountTypeTransform extends Transform {

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    public BankAccountTypeTransform() {
    }

    public BankAccountTypeView getBankAccountTypeView(BankAccountType bankAccountType) {
        if (bankAccountType != null) {
            BankAccountTypeView bankAccountTypeView = new BankAccountTypeView();
            bankAccountTypeView.setId(bankAccountType.getId());
            bankAccountTypeView.setName(bankAccountType.getName());
            bankAccountTypeView.setShortName(bankAccountType.getShortName());
            bankAccountTypeView.setOpenAccountFlag(bankAccountType.getOpenAccountFlag());
            bankAccountTypeView.setBankStatementFlag(bankAccountType.getBankStatementFlag());
            bankAccountTypeView.setOthBankStatementFlag(bankAccountType.getOthBankStatementFlag());
            return bankAccountTypeView;
        }
        return null;
    }

    public BankAccountType getBankAccountType(BankAccountTypeView bankAccountTypeView) {
        if (bankAccountTypeView != null) {
            BankAccountType bankAccountType = new BankAccountType();
            bankAccountType.setId(bankAccountTypeView.getId());
            bankAccountType.setName(bankAccountTypeView.getName());
            bankAccountType.setShortName(bankAccountTypeView.getShortName());
            bankAccountType.setBankStatementFlag(bankAccountTypeView.getBankStatementFlag());
            bankAccountType.setOthBankStatementFlag(bankAccountTypeView.getOthBankStatementFlag());
            bankAccountType.setOpenAccountFlag(bankAccountTypeView.getOpenAccountFlag());
            bankAccountType.setActive(bankAccountTypeView.getActive());
            return bankAccountType;
        }
        return null;
    }

    public List<BankAccountTypeView> getBankAccountTypeView(List<BankAccountType> bankAccountTypes) {
        List<BankAccountTypeView> bankAccountTypeViews = new ArrayList<BankAccountTypeView>();
        if (bankAccountTypes == null) {
            log.debug("getBankAccountTypeView() bankAccountTypes is null!");
            return bankAccountTypeViews;
        }

        for (BankAccountType bankAccountType : bankAccountTypes) {
            bankAccountTypeViews.add(getBankAccountTypeView(bankAccountType));
        }
        return bankAccountTypeViews;
    }
}
