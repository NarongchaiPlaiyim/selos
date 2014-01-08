package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.OpenAccountProductDAO;
import com.clevel.selos.dao.master.OpenAccountPurposeDAO;
import com.clevel.selos.model.db.master.OpenAccountPurpose;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoDetailTransform extends Transform {

    @Inject
    private AccountInfoBranchTransform branchTransform;
    @Inject
    private AccountInfoAccountTypeTransform accountTypeTransform;
    @Inject
    private AccountInfoProductTypeTransform productTypeTransform;

    @Inject
    private OpenAccountPurposeDAO purposeDAO;
    private List<OpenAccountPurpose> openAccountPurposeList;

    private AccountInfoDetail accountInfoDetail;
    private List<AccountInfoDetailView> accountInfoDetailViewList;
    private AccountInfoDetailView accountInfoDetailView;

    //Account Name
    private List<AccountInfoDetailAccountName> accountNameList;
    private AccountInfoDetailAccountName accountInfoDetailAccountName;

    //purpose
    private List<AccountInfoDetailPurpose> purposeList;
    private AccountInfoDetailPurpose accountInfoDetailPurpose;

    //Credit Type
    private List<AccountInfoDetailCreditType> creditTypeList;
    private AccountInfoDetailCreditType accountInfoDetailCreditType;

    private List<AccountNameView> accountNameViewList;
    private List<AccountInfoPurposeView> accountInfoPurposeViewList;
    private List<AccountInfoCreditTypeView> accountInfoCreditTypeViewList;

    private AccountInfoBranchView branchView;
    private AccountInfoAccountTypeView accountTypeView;
    private AccountInfoProductTypeView productTypeView;

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

    public List<AccountInfoDetailView> transformToView(final List<AccountInfoDetail> accountInfoDetailList){
        accountInfoDetailViewList = new ArrayList<AccountInfoDetailView>();
        String value;
        for(AccountInfoDetail infoDetail : accountInfoDetailList){
            accountInfoDetailView = new AccountInfoDetailView();
            accountInfoDetailView.setReqAccountType(infoDetail.getRequestAccountType());

            id = accountInfoDetailView.getReqAccountType();
            if(1 == id){
                accountInfoDetailView.setReqAccountTypeForShow("Existing");
            } else {
                accountInfoDetailView.setReqAccountTypeForShow("New");
            }

            value = accountInfoDetailView.getAccountNumber();
            if(value == null || "".equals(value)){
                accountInfoDetailView.setAccountNumberForShow(" - ");
            } else {
                accountInfoDetailView.setAccountNumberForShow(value);
            }

            //branch
            branchView = new AccountInfoBranchView();
            branchView.setId(infoDetail.getBranch().getId());
            branchView.setName(infoDetail.getBranch().getName());
            accountInfoDetailView.setBranchView(branchView);

            //account type
            accountTypeView = new AccountInfoAccountTypeView();
            accountTypeView.setId(infoDetail.getAccountType().getId());
            accountTypeView.setName(infoDetail.getAccountType().getName());
            accountInfoDetailView.setAccountTypeView(accountTypeView);

            //product type
            productTypeView = new AccountInfoProductTypeView();
            productTypeView.setId(infoDetail.getProductType().getId());
            productTypeView.setName(infoDetail.getProductType().getName());
            accountInfoDetailView.setProductTypeView(productTypeView);

            //account name
            accountInfoDetailView.setAccountNameViewList(null);
            accountInfoDetailView.setAccountNameViewListForShow("-");//for show

            //purpose
            purposeList = safetyList(infoDetail.getPurposeList());
            accountInfoDetailView.setAccountInfoPurposeViewList(loadPurpose(purposeList));

//            accountInfoDetailView.setAccountInfoPurposeViewListForShow("-");

            //open account
            accountInfoDetailView.setOpenAccount(infoDetail.getOpenAccount());

            //Credit Type
            accountInfoDetailView.setAccountInfoCreditTypeViewList(null);

            accountInfoDetailViewList.add(accountInfoDetailView);
        }
        return accountInfoDetailViewList;
    }

    private List<AccountInfoPurposeView> loadPurpose(List<AccountInfoDetailPurpose> purposeList){
        openAccountPurposeList = safetyList(purposeDAO.findAll());
        accountInfoPurposeViewList = new ArrayList<AccountInfoPurposeView>();
        long id;
        AccountInfoPurposeView purposeView;
        pointer :
        for(OpenAccountPurpose purposeMST : openAccountPurposeList){
            purposeView = new AccountInfoPurposeView();
            id = purposeMST.getId();
            purposeView.setId(id);
            purposeView.setName(purposeMST.getName());
            purposeView.setSelected(false);
            for (AccountInfoDetailPurpose purposeDB : purposeList){
                if(id == purposeDB.getPurposeId() ){
                    purposeView.setSelected(true);
                    purposeList.remove(purposeDB);
                    break;
                } else {
                    continue;
                }
            }
            accountInfoPurposeViewList.add(purposeView);
        }
        return accountInfoPurposeViewList;
    }
    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}
