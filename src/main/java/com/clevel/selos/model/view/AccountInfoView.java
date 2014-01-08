package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountInfoView implements Serializable {
    private long id;
    private int approvedType;
    private List<AccountInfoDetailView> accountInfoDetailViewList;
    private AccountInfoDetailView accountInfoDetailViewSelected;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public AccountInfoView(){
        init();
    }

    public void reset(){
        init();
    }

    private void init(){
        accountInfoDetailViewList = new ArrayList<AccountInfoDetailView>();
        accountInfoDetailViewSelected = new AccountInfoDetailView();
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

    public AccountInfoDetailView getAccountInfoDetailViewSelected() {
        return accountInfoDetailViewSelected;
    }

    public void setAccountInfoDetailViewSelected(AccountInfoDetailView accountInfoDetailViewSelected) {
        this.accountInfoDetailViewSelected = accountInfoDetailViewSelected;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("approvedType", approvedType)
                .append("accountInfoDetailViewList", accountInfoDetailViewList)
                .append("accountInfoDetailViewSelected", accountInfoDetailViewSelected)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
