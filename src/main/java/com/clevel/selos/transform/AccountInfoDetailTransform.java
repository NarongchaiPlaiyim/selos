package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AccountInfoCreditTypeView;
import com.clevel.selos.model.view.AccountInfoDetailView;
import com.clevel.selos.model.view.AccountInfoPurposeView;
import com.clevel.selos.model.view.AccountNameView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoDetailTransform extends Transform {
    @Inject
    AccountInfoBranchTransform branchTransform;
    @Inject
    AccountInfoAccountTypeTransform accountTypeTransform;
    @Inject
    AccountInfoProductTypeTransform productTypeTransform;

    private AccountInfoDetail accountInfoDetail;

    //Account Name
    private List<AccountInfoDetailAccountName> accountNameList;
    private AccountInfoDetailAccountName accountInfoDetailAccountName;

    //purpose
    private List<AccountInfoDetailPurpose> purposeList;
    private AccountInfoDetailPurpose accountInfoDetailPurpose;

    //Credit Type
    private List<AccountInfoDetailCreditType> creditTypeList;
    AccountInfoDetailCreditType accountInfoDetailCreditType;

    private List<AccountNameView> accountNameViewList;
    private List<AccountInfoPurposeView> accountInfoPurposeViewList;
    private List<AccountInfoCreditTypeView> accountInfoCreditTypeViewList;

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
        accountNameViewList = Util.safetyList(accountInfoDetailView.getAccountNameViewList());
        if(accountNameViewList.size() > 0){
            accountNameList = new ArrayList<AccountInfoDetailAccountName>();
            for(AccountNameView accountNameView : accountNameViewList){
                accountInfoDetailAccountName = new AccountInfoDetailAccountName();
                accountInfoDetailAccountName.setAccountInfoDetailAccountName(accountInfoDetail);
                accountInfoDetailAccountName.setAccountId(accountNameView.getId());
                accountInfoDetailAccountName.setAccountName(accountNameView.getName());
                accountNameList.add(accountInfoDetailAccountName);
            }
            accountInfoDetail.setAccountNameList(accountNameList);
        } else {
            accountInfoDetail.setAccountNameList(null);
        }


        //Purpose
        accountInfoPurposeViewList = Util.safetyList(accountInfoDetailView.getAccountInfoPurposeViewList());
        if(accountInfoPurposeViewList.size() > 0){
            purposeList = new ArrayList<AccountInfoDetailPurpose>();
            for(AccountInfoPurposeView purposeView : accountInfoPurposeViewList){
                if(purposeView.isSelected()){
                    accountInfoDetailPurpose = new AccountInfoDetailPurpose();
                    accountInfoDetailPurpose.setAccountInfoDetailPurpose(accountInfoDetail);
                    accountInfoDetailPurpose.setPurposeId(purposeView.getId());
                    accountInfoDetailPurpose.setPurposeName(purposeView.getName());
                    purposeList.add(accountInfoDetailPurpose);
                } else {
                    continue;
                }
            }
            accountInfoDetail.setPurposeList(purposeList);
        } else {
            accountInfoDetail.setPurposeList(null);
        }


        //Credit Type
        accountInfoCreditTypeViewList = Util.safetyList(accountInfoDetailView.getAccountInfoCreditTypeViewList());
        if(accountInfoCreditTypeViewList.size() > 0){
            creditTypeList = new ArrayList<AccountInfoDetailCreditType>();
            for(AccountInfoCreditTypeView creditTypeView : accountInfoCreditTypeViewList){
                if(creditTypeView.isSelected()){
                    accountInfoDetailCreditType = new AccountInfoDetailCreditType();
                    accountInfoDetailCreditType.setAccountInfoDetailCreditType(accountInfoDetail);
                    accountInfoDetailCreditType.setCreditTypeId(creditTypeView.getId());
                    accountInfoDetailCreditType.setProductProgram(creditTypeView.getProductProgram());
                    accountInfoDetailCreditType.setCreditFacility(creditTypeView.getCreditFacility());
                    accountInfoDetailCreditType.setLimit(creditTypeView.getLimit());
                    creditTypeList.add(accountInfoDetailCreditType);
                } else {
                    continue;
                }
            }
            accountInfoDetail.setCreditTypeList(creditTypeList);
        } else {
            accountInfoDetail.setCreditTypeList(null);
        }

        accountInfoDetail.setTerm(accountInfoDetailView.getTerm());
        accountInfoDetail.setOpenAccount(accountInfoDetailView.getOpenAccount());
        return accountInfoDetail;
    }

}
