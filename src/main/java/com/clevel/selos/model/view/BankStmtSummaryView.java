package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BankStmtSummaryView implements Serializable {

    private String interfaceResult;
    private String interfaceResultReason;

    private int seasonal;
    private BigDecimal TMBTotalIncomeGross;
    private BigDecimal TMBTotalIncomeNetBDM;
    private BigDecimal TMBTotalIncomeNetUW;

    private BigDecimal OthTotalIncomeGross;
    private BigDecimal OthTotalIncomeNetBDM;
    private BigDecimal OthTotalIncomeNetUW;

    private BigDecimal grdTotalIncomeGross;
    private BigDecimal grdTotalIncomeNetBDM;
    private BigDecimal grdTotalIncomeNetUW;
    private BigDecimal grdTotalTDChqRetAmount;
    private BigDecimal grdTotalTDChqRetPercent;

    private List<BankStmtView> bankStmtViewList;

    public String getInterfaceResult() {
        return interfaceResult;
    }

    public void setInterfaceResult(String interfaceResult) {
        this.interfaceResult = interfaceResult;
    }

    public String getInterfaceResultReason() {
        return interfaceResultReason;
    }

    public void setInterfaceResultReason(String interfaceResultReason) {
        this.interfaceResultReason = interfaceResultReason;
    }

    public int getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(int seasonal) {
        this.seasonal = seasonal;
    }

    public BigDecimal getTMBTotalIncomeGross() {
        return TMBTotalIncomeGross;
    }

    public void setTMBTotalIncomeGross(BigDecimal TMBTotalIncomeGross) {
        this.TMBTotalIncomeGross = TMBTotalIncomeGross;
    }

    public BigDecimal getTMBTotalIncomeNetBDM() {
        return TMBTotalIncomeNetBDM;
    }

    public void setTMBTotalIncomeNetBDM(BigDecimal TMBTotalIncomeNetBDM) {
        this.TMBTotalIncomeNetBDM = TMBTotalIncomeNetBDM;
    }

    public BigDecimal getTMBTotalIncomeNetUW() {
        return TMBTotalIncomeNetUW;
    }

    public void setTMBTotalIncomeNetUW(BigDecimal TMBTotalIncomeNetUW) {
        this.TMBTotalIncomeNetUW = TMBTotalIncomeNetUW;
    }

    public BigDecimal getOthTotalIncomeGross() {
        return OthTotalIncomeGross;
    }

    public void setOthTotalIncomeGross(BigDecimal othTotalIncomeGross) {
        OthTotalIncomeGross = othTotalIncomeGross;
    }

    public BigDecimal getOthTotalIncomeNetBDM() {
        return OthTotalIncomeNetBDM;
    }

    public void setOthTotalIncomeNetBDM(BigDecimal othTotalIncomeNetBDM) {
        OthTotalIncomeNetBDM = othTotalIncomeNetBDM;
    }

    public BigDecimal getOthTotalIncomeNetUW() {
        return OthTotalIncomeNetUW;
    }

    public void setOthTotalIncomeNetUW(BigDecimal othTotalIncomeNetUW) {
        OthTotalIncomeNetUW = othTotalIncomeNetUW;
    }

    public BigDecimal getGrdTotalIncomeGross() {
        return grdTotalIncomeGross;
    }

    public void setGrdTotalIncomeGross(BigDecimal grdTotalIncomeGross) {
        this.grdTotalIncomeGross = grdTotalIncomeGross;
    }

    public BigDecimal getGrdTotalIncomeNetBDM() {
        return grdTotalIncomeNetBDM;
    }

    public void setGrdTotalIncomeNetBDM(BigDecimal grdTotalIncomeNetBDM) {
        this.grdTotalIncomeNetBDM = grdTotalIncomeNetBDM;
    }

    public BigDecimal getGrdTotalIncomeNetUW() {
        return grdTotalIncomeNetUW;
    }

    public void setGrdTotalIncomeNetUW(BigDecimal grdTotalIncomeNetUW) {
        this.grdTotalIncomeNetUW = grdTotalIncomeNetUW;
    }

    public BigDecimal getGrdTotalTDChqRetAmount() {
        return grdTotalTDChqRetAmount;
    }

    public void setGrdTotalTDChqRetAmount(BigDecimal grdTotalTDChqRetAmount) {
        this.grdTotalTDChqRetAmount = grdTotalTDChqRetAmount;
    }

    public BigDecimal getGrdTotalTDChqRetPercent() {
        return grdTotalTDChqRetPercent;
    }

    public void setGrdTotalTDChqRetPercent(BigDecimal grdTotalTDChqRetPercent) {
        this.grdTotalTDChqRetPercent = grdTotalTDChqRetPercent;
    }

    public List<BankStmtView> getBankStmtViewList() {
        return bankStmtViewList;
    }

    public void setBankStmtViewList(List<BankStmtView> bankStmtViewList) {
        this.bankStmtViewList = bankStmtViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("interfaceResult", interfaceResult)
                .append("interfaceResultReason", interfaceResultReason)
                .append("seasonal", seasonal)
                .append("TMBTotalIncomeGross", TMBTotalIncomeGross)
                .append("TMBTotalIncomeNetBDM", TMBTotalIncomeNetBDM)
                .append("TMBTotalIncomeNetUW", TMBTotalIncomeNetUW)
                .append("OthTotalIncomeGross", OthTotalIncomeGross)
                .append("OthTotalIncomeNetBDM", OthTotalIncomeNetBDM)
                .append("OthTotalIncomeNetUW", OthTotalIncomeNetUW)
                .append("grdTotalIncomeGross", grdTotalIncomeGross)
                .append("grdTotalIncomeNetBDM", grdTotalIncomeNetBDM)
                .append("grdTotalIncomeNetUW", grdTotalIncomeNetUW)
                .append("grdTotalTDChqRetAmount", grdTotalTDChqRetAmount)
                .append("grdTotalTDChqRetPercent", grdTotalTDChqRetPercent)
                .append("bankStmtViewList", bankStmtViewList)
                .toString();
    }
}
