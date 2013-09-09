package com.clevel.selos.controller;


import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.NcbRecord;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "fullappNcb")
public class FullappNcb implements Serializable {


    @Inject
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    private List<AccountStatus> accountStatusList;
    private AccountStatus accountStatus;

    private List<AccountType> accountTypeList;
    private AccountType accountType;

    //test
    private NcbRecord ncbRecord;
    private List<NcbRecord> ncbRecordList;

    @Inject
    private AccountStatusDAO  accountStatusDAO;

    @Inject
    private AccountTypeDAO accountTypeDAO;

    public FullappNcb() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        if(ncbRecord == null){
            ncbRecord = new NcbRecord();
        }

        if(ncbRecordList == null){
            ncbRecordList = new ArrayList<NcbRecord>();
        }

        if(accountStatusList == null){
            accountStatusList = new ArrayList<AccountStatus>();
        }

        if(accountTypeList == null){
            accountTypeList = new ArrayList<AccountType>();
        }

        accountStatusList = accountStatusDAO.findAll();
        accountTypeList   = accountTypeDAO.findAll();
    }

    // onclick add button
    public void onAdd(){
        //*** Reset form ***//
    }



    public List<AccountStatus> getAccountStatusList() {
        return accountStatusList;
    }

    public void setAccountStatusList(List<AccountStatus> accountStatusList) {
        this.accountStatusList = accountStatusList;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<AccountType> getAccountTypeList() {
        return accountTypeList;
    }

    public void setAccountTypeList(List<AccountType> accountTypeList) {
        this.accountTypeList = accountTypeList;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public NcbRecord getNcbRecord() {
        return ncbRecord;
    }

    public void setNcbRecord(NcbRecord ncbRecord) {
        this.ncbRecord = ncbRecord;
    }

    public List<NcbRecord> getNcbRecordList() {
        return ncbRecordList;
    }

    public void setNcbRecordList(List<NcbRecord> ncbRecordList) {
        this.ncbRecordList = ncbRecordList;
    }
}
