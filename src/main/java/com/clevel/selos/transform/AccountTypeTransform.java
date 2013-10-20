package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.AccountTypeView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AccountTypeTransform extends Transform {
    @Inject
    Logger log;

    @Inject
    public AccountTypeTransform() {
    }

    public AccountTypeView transformToView(AccountType accountType) {
        AccountTypeView accountTypeView = new AccountTypeView();
        if (accountType == null) {
            log.debug("transformToView() accountType is null!");
            return accountTypeView;
        }

        accountTypeView.setId(accountType.getId());
        accountTypeView.setAccountType(accountType.getName());
        return accountTypeView;
    }

    public List<AccountTypeView> transformToViewList(List<AccountType> accountTypes) {
        List<AccountTypeView> accountTypeViews = new ArrayList<AccountTypeView>();
        if (accountTypes == null) {
            log.debug("transformToViewList() accountTypes is null!");
            return accountTypeViews;
        }

        for (AccountType accountType : accountTypes) {
            accountTypeViews.add(transformToView(accountType));
        }
        return accountTypeViews;
    }
}
