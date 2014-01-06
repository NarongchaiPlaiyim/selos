package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.AccountInfo;
import com.clevel.selos.model.db.working.AccountInfoDetail;
import com.clevel.selos.model.view.AccountInfoDetailView;

import javax.inject.Inject;

public class AccountInfoDetailTransform extends Transform {
    @Inject
    AccountInfoBranchTransform branchTransform;
    @Inject
    AccountInfoAccountTypeTransform accountTypeTransform;
    @Inject
    AccountInfoProductTypeTransform productTypeTransform;

    private AccountInfoDetail accountInfoDetail;

    private long id;
    @Inject
    public AccountInfoDetailTransform() {

    }

    public AccountInfoDetail transformToModel(final AccountInfoDetailView accountInfoDetailView, final AccountInfo accountInfo){
        accountInfoDetail = new AccountInfoDetail();
        accountInfoDetail.setAccountInfo(accountInfo);
        accountInfoDetail.setRequestAccountType(accountInfoDetailView.getReqAccountType());
        accountInfoDetail.setAccountNumber(accountInfoDetailView.getAccountNumber());

        //Branch
        id = accountInfoDetailView.getBranchView().getId();
        if(id != 0){
            accountInfoDetail.setBranch(branchTransform.transformToModel((int) id));
        } else {
            accountInfoDetail.setBranch(null);
        }

        //Account Type
        id = accountInfoDetailView.getAccountTypeView().getId();
        if(id != 0){
            accountInfoDetail.setAccountType(accountTypeTransform.transformToModel((int)id));
        } else {
            accountInfoDetail.setAccountType(null);
        }

        //Product Type
        id = accountInfoDetailView.getProductTypeView().getId();
        if(id != 0){
            accountInfoDetail.setProductType(productTypeTransform.transformToModel((int)id));
        } else {
            accountInfoDetail.setProductType(null);
        }

        //Account Name
        accountInfoDetail.setAccountNameList(null);

        //Purpose
        accountInfoDetail.setPurposeList(null);

        //Credit Type
        accountInfoDetail.setCreditTypeList(null);

        accountInfoDetail.setTerm(accountInfoDetailView.getTerm());
        accountInfoDetail.setOpenAccount(accountInfoDetailView.getOpenAccount());
        return accountInfoDetail;
    }

}
