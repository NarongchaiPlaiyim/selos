package com.clevel.selos.controller;

import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.working.DBRDAO;
import com.clevel.selos.dao.working.DBRDetailDAO;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.DBRDetailView;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.model.view.NcbView;
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
@ManagedBean(name = "dbrInfo")
public class DBRInfo extends BaseController implements Serializable {
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

    @Inject
    DBRDAO dbrdao;

    @Inject
    DBRDetailDAO dbrDetailDAO;

    @Inject
    AccountTypeDAO loanTypeDAO;

    // *** Content ***///
    private DBRView dbrView;
    private DBRDetailView dbrDetailView;
    private List<AccountType> loanTypes;
    private List<NcbView> ncbViews;

    public DBRInfo(){

    }

    @PostConstruct
    public void onCreation() {
        dbrDetailView = new DBRDetailView();
        dbrView = new DBRView();
        ncbViews = new ArrayList<NcbView>(); // HardCode
    }

    public DBRView getDbrView() {
        return dbrView;
    }

    public void setDbrView(DBRView dbrView) {
        this.dbrView = dbrView;
    }

    public DBRDetailView getDbrDetailView() {
        return dbrDetailView;
    }

    public void setDbrDetailView(DBRDetailView dbrDetailView) {
        this.dbrDetailView = dbrDetailView;
    }

    public List<AccountType> getLoanTypes() {
        return loanTypes;
    }

    public void setLoanTypes(List<AccountType> loanTypes) {
        this.loanTypes = loanTypes;
    }

    public List<NcbView> getNcbViews() {
        return ncbViews;
    }

    public void setNcbViews(List<NcbView> ncbViews) {
        this.ncbViews = ncbViews;
    }
}
