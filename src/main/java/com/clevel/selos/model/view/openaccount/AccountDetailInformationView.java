package com.clevel.selos.model.view.openaccount;

import com.clevel.selos.model.view.openaccount.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountDetailInformationView implements Serializable {
    private RequestAccountTypeModel requestAccountType;
    private String accountNumber;
    private BranchModel branch;
    private AccountTypeModel accountType;
    private ProductTypeModel productType;
    private String term;

    private String accountNameForShow;
    private List<AccountNameModel> accountNameModelList;

    private String purposeForShow;
    private List<PurposeModel> purposeModelList;
    private List<CreditTypeModel> creditTypeModelList;

    public AccountDetailInformationView() {
        init();
    }

    private void init(){
        requestAccountType = new RequestAccountTypeModel();
        branch = new BranchModel();
        accountType = new AccountTypeModel();
        productType = new ProductTypeModel();
        accountNameModelList = new ArrayList<AccountNameModel>();
        purposeModelList = new ArrayList<PurposeModel>();
        creditTypeModelList = new ArrayList<CreditTypeModel>();
    }

    public RequestAccountTypeModel getRequestAccountType() {
        return requestAccountType;
    }

    public void setRequestAccountType(RequestAccountTypeModel requestAccountType) {
        this.requestAccountType = requestAccountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BranchModel getBranch() {
        return branch;
    }

    public void setBranch(BranchModel branch) {
        this.branch = branch;
    }

    public AccountTypeModel getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeModel accountType) {
        this.accountType = accountType;
    }

    public ProductTypeModel getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeModel productType) {
        this.productType = productType;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<AccountNameModel> getAccountNameModelList() {
        return accountNameModelList;
    }

    public void setAccountNameModelList(List<AccountNameModel> accountNameModelList) {
        this.accountNameModelList = accountNameModelList;
    }

    public List<PurposeModel> getPurposeModelList() {
        return purposeModelList;
    }

    public void setPurposeModelList(List<PurposeModel> purposeModelList) {
        this.purposeModelList = purposeModelList;
    }

    public List<CreditTypeModel> getCreditTypeModelList() {
        return creditTypeModelList;
    }

    public void setCreditTypeModelList(List<CreditTypeModel> creditTypeModelList) {
        this.creditTypeModelList = creditTypeModelList;
    }

    public String getAccountNameForShow() {
        return accountNameForShow;
    }

    public void setAccountNameForShow(String accountNameForShow) {
        this.accountNameForShow = accountNameForShow;
    }

    public String getPurposeForShow() {
        return purposeForShow;
    }

    public void setPurposeForShow(String purposeForShow) {
        this.purposeForShow = purposeForShow;
    }
}
