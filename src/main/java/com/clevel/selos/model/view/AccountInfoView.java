package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class AccountInfoView {
    private long id;
    private int approvedType;
    private List<AccountInfoDetailView> accountInfoDetailViews;

    public AccountInfoView(){

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

    public List<AccountInfoDetailView> getAccountInfoDetailViews() {
        return accountInfoDetailViews;
    }

    public void setAccountInfoDetailViews(List<AccountInfoDetailView> accountInfoDetailViews) {
        this.accountInfoDetailViews = accountInfoDetailViews;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accountInfoDetailViews", accountInfoDetailViews)
                .toString();
    }
}
