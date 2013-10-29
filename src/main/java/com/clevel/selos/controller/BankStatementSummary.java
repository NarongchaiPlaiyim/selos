package com.clevel.selos.controller;

import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BankStmtView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "bankStatementSummary")
public class BankStatementSummary {
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

    //Dialog
    enum ModeForButton {
        ADD, EDIT
    }

    private ModeForButton modeForButton;

    //View
    private BankStmtSummaryView summaryView;
    private List<BankStmtView> tmbBankStmtViewList;
    private List<BankStmtView> otherBankStmtViewList;

    public BankStatementSummary() {
    }

    @PostConstruct
    public void onCreation() {
        onLoadSummary();
    }

    public void onLoadSummary() {
        log.debug("onLoadSummary()");
        summaryView = new BankStmtSummaryView();
        //todo: get TMB Bank statement list, Other Bank statement list and calculate total and grand total
    }

    public void onSaveSummary() {
        log.debug("onSaveSummary()");
    }

    public String onLinkToAddTmbBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: true, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        passParamsToBankStmtDetail(true, summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        return "bankStatementDetail?faces-redirect=true";
    }

    public String onLinkToAddOtherBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: false, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        passParamsToBankStmtDetail(false, summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        return "bankStatementDetail?faces-redirect=true";
    }

    private void passParamsToBankStmtDetail(boolean isTmbBank, int seasonal, Date expectedSubmissionDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isTmbBank", isTmbBank);
        map.put("seasonal", seasonal);
        map.put("expectedSubmissionDate", expectedSubmissionDate);
        FacesUtil.getFlash().put("bankStmtSumParams", map);
    }

    public BankStmtSummaryView getSummaryView() {
        return summaryView;
    }

    public void setSummaryView(BankStmtSummaryView summaryView) {
        this.summaryView = summaryView;
    }

    public List<BankStmtView> getTmbBankStmtViewList() {
        return tmbBankStmtViewList;
    }

    public void setTmbBankStmtViewList(List<BankStmtView> tmbBankStmtViewList) {
        this.tmbBankStmtViewList = tmbBankStmtViewList;
    }

    public List<BankStmtView> getOtherBankStmtViewList() {
        return otherBankStmtViewList;
    }

    public void setOtherBankStmtViewList(List<BankStmtView> otherBankStmtViewList) {
        this.otherBankStmtViewList = otherBankStmtViewList;
    }
}
