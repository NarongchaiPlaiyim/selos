package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoView implements Serializable {
    private long id;
    private int approvedType;
    private List<AccountInfoDetailView> accountInfoDetailViewList;

    public AccountInfoView(){
        init();
    }

    private void init(){
        accountInfoDetailViewList = new ArrayList<AccountInfoDetailView>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    public List<AccountInfoDetailView> getAccountInfoDetailViewList() {
        return accountInfoDetailViewList;
    }

    public void setAccountInfoDetailViewList(List<AccountInfoDetailView> accountInfoDetailViewList) {
        this.accountInfoDetailViewList = accountInfoDetailViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("approvedType", approvedType)
                .append("accountInfoDetailViewList", accountInfoDetailViewList)
                .toString();
    }
}
