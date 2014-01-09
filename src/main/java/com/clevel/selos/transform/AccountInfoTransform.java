package com.clevel.selos.transform;

import com.clevel.selos.dao.working.AccountInfoDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AccountInfo;
import com.clevel.selos.model.db.working.AccountInfoDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AccountInfoDetailView;
import com.clevel.selos.model.view.AccountInfoView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class AccountInfoTransform extends Transform {
    private AccountInfo accountInfo;
    private AccountInfoView accountInfoView;
    private List<AccountInfoDetailView> accountInfoDetailViewList;
    private List<AccountInfoDetail> accountInfoDetailList;

    @Inject
    AccountInfoDetailTransform accountInfoDetailTransform;
    @Inject
    private AccountInfoDAO accountInfoDAO;
    @Inject
    public AccountInfoTransform() {

    }

    public AccountInfo transformToModel(final AccountInfoView accountInfoView,final WorkCase workCase,final User user){
        accountInfo = new AccountInfo();

        if(accountInfoView.getId() != 0){
            accountInfo = accountInfoDAO.findById(accountInfoView.getId());
            accountInfo.setModifyDate(new Date());
            accountInfo.setModifyBy(user);
            accountInfo.setLastReviewDate(new Date());
            accountInfo.setApprovedType(accountInfoView.getApprovedType());
            return accountInfo;
        } else {
            accountInfo.setWorkCase(workCase);
            accountInfo.setModifyDate(new Date());
            accountInfo.setModifyBy(user);
            accountInfo.setCreateDate(new Date());
            accountInfo.setCreateBy(user);
            accountInfo.setExtendedReviewDate(new Date());
            accountInfo.setLastReviewDate(new Date());
            accountInfo.setApprovedType(accountInfoView.getApprovedType());
            return accountInfo;
        }
    }

    public AccountInfoView transformToView(final AccountInfo accountInfo){
        accountInfoView = new AccountInfoView();
        accountInfoView.setId(accountInfo.getId());
        accountInfoView.setModifyDate(accountInfo.getModifyDate());
        accountInfoView.setModifyBy(accountInfo.getModifyBy());

        accountInfoView.setCreateDate(accountInfo.getCreateDate());
        accountInfoView.setCreateBy(accountInfo.getCreateBy());

        accountInfoView.setApprovedType(accountInfo.getApprovedType());

        accountInfoDetailList = accountInfo.getAccountInfoDetailList();
        accountInfoDetailViewList = accountInfoDetailTransform.transformToView(accountInfoDetailList);

        accountInfoView.setAccountInfoDetailViewList(accountInfoDetailViewList);

        return accountInfoView;
    }
}
