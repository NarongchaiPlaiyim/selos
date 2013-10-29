package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.LoanTypeView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LoanTypeTransform {

    @Inject
    public LoanTypeTransform() {

    }

    public LoanTypeView getLoanTypeView(AccountType loanType) {
        LoanTypeView loanTypeView = new LoanTypeView();
        if (loanType == null) {
            return loanTypeView;
        }
        loanTypeView.setId(loanType.getId());
        loanTypeView.setName(loanType.getName());
        return loanTypeView;
    }

    public List<LoanTypeView> getLoanTypeViews(List<AccountType> loanTypes) {
        List<LoanTypeView> loanTypeViews = new ArrayList<LoanTypeView>();
        if (loanTypes == null && loanTypes.isEmpty()) {
            return loanTypeViews;
        }
        for (AccountType loanType : loanTypes) {
            loanTypeViews.add(getLoanTypeView(loanType));
        }
        return loanTypeViews;
    }

    public AccountType getLoanType(LoanTypeView loanTypeView) {
        AccountType loanType = new AccountType();
        if (loanTypeView == null) {
            return loanType;
        }
        loanType.setId(loanTypeView.getId());
        loanType.setName(loanTypeView.getName());
        return loanType;

    }
}
