package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AccountInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AccountInfoView;

import javax.inject.Inject;

public class AccountInfoTransform extends Transform {
    private AccountInfo accountInfo = null;

    @Inject
    public AccountInfoTransform() {

    }

    public AccountInfo transformToModel(final AccountInfoView accountInfoView,final WorkCase workCase,final User user){
        accountInfo = new AccountInfo();
        accountInfo.setWorkCase(workCase);
        return accountInfo;
    }
}
