package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.AccountInfo;
import com.clevel.selos.model.db.working.AccountInfoDetail;
import com.clevel.selos.model.view.AccountInfoDetailView;

import javax.inject.Inject;

public class AccountInfoDetailTransform extends Transform {
    private AccountInfoDetail accountInfoDetail;

    @Inject
    public AccountInfoDetailTransform() {

    }

    public AccountInfoDetail transformToModel(final AccountInfoDetailView accountInfoDetailView, final AccountInfo accountInfo){
        accountInfoDetail = new AccountInfoDetail();
        accountInfoDetail.setAccountInfo(accountInfo);
        accountInfoDetail.setRequestAccountType(accountInfoDetailView.getReqAccountType());
        accountInfoDetail.setAccountNumber(accountInfoDetailView.getAccountNumber());

        //Object from master table
        accountInfoDetail.setBankBranch(null);
        accountInfoDetail.setAccountType(null);
        accountInfoDetail.setProductType(null);
        accountInfoDetail.setAccountNameList(null);
        accountInfoDetail.setPurposeList(null);
        accountInfoDetail.setCreditTypeList(null);

        accountInfoDetail.setTerm(accountInfoDetailView.getTerm());
        accountInfoDetail.setOpenAccount(accountInfoDetailView.getOpenAccount());
        return accountInfoDetail;
    }

}
