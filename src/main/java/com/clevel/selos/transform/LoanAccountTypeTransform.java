package com.clevel.selos.transform;


import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.LoanAccountTypeView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LoanAccountTypeTransform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public LoanAccountTypeTransform() {

    }

    public LoanAccountTypeView getLoanAccountTypeView(AccountType loanAccountType) {
        LoanAccountTypeView loanAccountTypeView = new LoanAccountTypeView();
        if (loanAccountType == null) {
            return loanAccountTypeView;
        }
        loanAccountTypeView.setId(loanAccountType.getId());
        loanAccountTypeView.setName(loanAccountType.getName());
        loanAccountTypeView.setCalculateType(loanAccountType.getCalculateType());
        return loanAccountTypeView;
    }

    public List<LoanAccountTypeView> getLoanAccountTypeViews(List<AccountType> loanAccountTypes) {
        List<LoanAccountTypeView> loanAccountTypeViews = new ArrayList<LoanAccountTypeView>();
        if (loanAccountTypes == null && loanAccountTypes.isEmpty()) {
            return loanAccountTypeViews;
        }
        for (AccountType loanAccountType : loanAccountTypes) {
            loanAccountTypeViews.add(getLoanAccountTypeView(loanAccountType));
        }
        return loanAccountTypeViews;
    }

    public AccountType getLoanType(LoanAccountTypeView loanAccountTypeView) {
        AccountType loanAccountType = new AccountType();
        if (loanAccountTypeView == null) {
            return loanAccountType;
        }
        loanAccountType.setId(loanAccountTypeView.getId());
        loanAccountType.setName(loanAccountTypeView.getName());

        return loanAccountType;

    }
}
