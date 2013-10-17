package com.clevel.selos.controller;

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
import java.math.BigDecimal;
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

    enum ModeForButton{ADD, EDIT}
    private ModeForButton modeForButton;

    private int seasonal;
    private Date expectedSubmissionDate;

    /*Abbreviation variables
        - BSS : Bank Statement Summary
        - TCR : Trade Cheque Return
     */
    private List<BankStmtView> tmbBankStmtSumList;
    private BigDecimal totalTmbBSSIncomeGross;
    private BigDecimal totalTmbBSSIncomeNetBDM;
    private BigDecimal totalTmbBSSIncomeNetUW;

    private List<BankStmtView> otherBankStmtSumList;
    private BigDecimal totalOtherBSSIncomeGross;
    private BigDecimal totalOtherBSSIncomeNetBDM;
    private BigDecimal totalOtherBSSIncomeNetUW;

    private BigDecimal grandTotalIncomeGross;
    private BigDecimal grandTotalIncomeNetBDM;
    private BigDecimal grandTotalIncomeNetUW;
    private BigDecimal grandTotalTCRAmount;
    private BigDecimal grandTotalTCRPercent;

    private List<BankStmtView> srcOfCollateralProofList;
    private BigDecimal grandTotalAverageAmount;

    public BankStatementSummary(){
    }

    @PostConstruct
    public void onCreation() {
        expectedSubmissionDate = new Date();

        totalTmbBSSIncomeGross = BigDecimal.ZERO;
        totalTmbBSSIncomeNetBDM = BigDecimal.ZERO;
        totalTmbBSSIncomeNetUW = BigDecimal.ZERO;

        totalOtherBSSIncomeGross = BigDecimal.ZERO;
        totalOtherBSSIncomeNetBDM = BigDecimal.ZERO;
        totalOtherBSSIncomeNetUW = BigDecimal.ZERO;

        grandTotalIncomeGross = BigDecimal.ZERO;
        grandTotalIncomeNetBDM = BigDecimal.ZERO;
        grandTotalIncomeNetUW = BigDecimal.ZERO;
        grandTotalTCRAmount = BigDecimal.ZERO;
        grandTotalTCRPercent = BigDecimal.ZERO;

        grandTotalAverageAmount = BigDecimal.ZERO;
    }

    public void onLoadSummary() {
        log.debug("onLoadSummary()");
    }

    public void onSaveSummary() {
        log.debug("onSaveSummary() seasonal: {}, expectedSubmissionDate: {}", seasonal, expectedSubmissionDate);
    }

    public String onLinkToAddTmbBankDetail() {
        log.debug("Link to TMB Bank Statement Detail with params[isTmbBank: true, seasonal: {}, expectedSubmissionDate: {}]", seasonal, expectedSubmissionDate);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isTmbBank", true);
        map.put("seasonal", seasonal);
        map.put("expectedSubmissionDate", expectedSubmissionDate);

        FacesUtil.getExternalContext().getFlash().put("bankStmtSumParameters", map);

        return "bankStatementDetail?faces-redirect=true";
    }

    public String onLinkToAddOtherBankDetail() {
        log.debug("Link to TMB Bank Statement Detail with params[isTmbBank: false, seasonal: {}, expectedSubmissionDate: {}]", seasonal, expectedSubmissionDate);
        return "bankStatementDetail";
    }

    //---------- Getter/Setter ----------//
    public int getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(int seasonal) {
        this.seasonal = seasonal;
    }

    public Date getExpectedSubmissionDate() {
        return expectedSubmissionDate;
    }

    public void setExpectedSubmissionDate(Date expectedSubmissionDate) {
        this.expectedSubmissionDate = expectedSubmissionDate;
    }

    public List<BankStmtView> getTmbBankStmtSumList() {
        return tmbBankStmtSumList;
    }

    public void setTmbBankStmtSumList(List<BankStmtView> tmbBankStmtSumList) {
        this.tmbBankStmtSumList = tmbBankStmtSumList;
    }

    public List<BankStmtView> getOtherBankStmtSumList() {
        return otherBankStmtSumList;
    }

    public void setOtherBankStmtSumList(List<BankStmtView> otherBankStmtSumList) {
        this.otherBankStmtSumList = otherBankStmtSumList;
    }

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public BigDecimal getTotalTmbBSSIncomeGross() {
        return totalTmbBSSIncomeGross;
    }

    public void setTotalTmbBSSIncomeGross(BigDecimal totalTmbBSSIncomeGross) {
        this.totalTmbBSSIncomeGross = totalTmbBSSIncomeGross;
    }

    public BigDecimal getTotalTmbBSSIncomeNetBDM() {
        return totalTmbBSSIncomeNetBDM;
    }

    public void setTotalTmbBSSIncomeNetBDM(BigDecimal totalTmbBSSIncomeNetBDM) {
        this.totalTmbBSSIncomeNetBDM = totalTmbBSSIncomeNetBDM;
    }

    public BigDecimal getTotalTmbBSSIncomeNetUW() {
        return totalTmbBSSIncomeNetUW;
    }

    public void setTotalTmbBSSIncomeNetUW(BigDecimal totalTmbBSSIncomeNetUW) {
        this.totalTmbBSSIncomeNetUW = totalTmbBSSIncomeNetUW;
    }

    public BigDecimal getTotalOtherBSSIncomeGross() {
        return totalOtherBSSIncomeGross;
    }

    public void setTotalOtherBSSIncomeGross(BigDecimal totalOtherBSSIncomeGross) {
        this.totalOtherBSSIncomeGross = totalOtherBSSIncomeGross;
    }

    public BigDecimal getTotalOtherBSSIncomeNetBDM() {
        return totalOtherBSSIncomeNetBDM;
    }

    public void setTotalOtherBSSIncomeNetBDM(BigDecimal totalOtherBSSIncomeNetBDM) {
        this.totalOtherBSSIncomeNetBDM = totalOtherBSSIncomeNetBDM;
    }

    public BigDecimal getTotalOtherBSSIncomeNetUW() {
        return totalOtherBSSIncomeNetUW;
    }

    public void setTotalOtherBSSIncomeNetUW(BigDecimal totalOtherBSSIncomeNetUW) {
        this.totalOtherBSSIncomeNetUW = totalOtherBSSIncomeNetUW;
    }

    public BigDecimal getGrandTotalIncomeGross() {
        return grandTotalIncomeGross;
    }

    public void setGrandTotalIncomeGross(BigDecimal grandTotalIncomeGross) {
        this.grandTotalIncomeGross = grandTotalIncomeGross;
    }

    public BigDecimal getGrandTotalIncomeNetBDM() {
        return grandTotalIncomeNetBDM;
    }

    public void setGrandTotalIncomeNetBDM(BigDecimal grandTotalIncomeNetBDM) {
        this.grandTotalIncomeNetBDM = grandTotalIncomeNetBDM;
    }

    public BigDecimal getGrandTotalIncomeNetUW() {
        return grandTotalIncomeNetUW;
    }

    public void setGrandTotalIncomeNetUW(BigDecimal grandTotalIncomeNetUW) {
        this.grandTotalIncomeNetUW = grandTotalIncomeNetUW;
    }

    public BigDecimal getGrandTotalTCRAmount() {
        return grandTotalTCRAmount;
    }

    public void setGrandTotalTCRAmount(BigDecimal grandTotalTCRAmount) {
        this.grandTotalTCRAmount = grandTotalTCRAmount;
    }

    public BigDecimal getGrandTotalTCRPercent() {
        return grandTotalTCRPercent;
    }

    public void setGrandTotalTCRPercent(BigDecimal grandTotalTCRPercent) {
        this.grandTotalTCRPercent = grandTotalTCRPercent;
    }

    public List<BankStmtView> getSrcOfCollateralProofList() {
        return srcOfCollateralProofList;
    }

    public void setSrcOfCollateralProofList(List<BankStmtView> srcOfCollateralProofList) {
        this.srcOfCollateralProofList = srcOfCollateralProofList;
    }

    public BigDecimal getGrandTotalAverageAmount() {
        return grandTotalAverageAmount;
    }

    public void setGrandTotalAverageAmount(BigDecimal grandTotalAverageAmount) {
        this.grandTotalAverageAmount = grandTotalAverageAmount;
    }
}
