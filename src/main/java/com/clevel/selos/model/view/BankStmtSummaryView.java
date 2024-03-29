package com.clevel.selos.model.view;

import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.util.DateTimeUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BankStmtSummaryView implements Serializable {

    private List<ActionStatusView> actionStatusViewList;

    private long id;
    private int seasonal;
    private Date expectedSubmitDate;
    private int countRefresh;
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
    private BigDecimal grdTotalAvgOSBalanceAmount;

    private BigDecimal grdTotalBorrowerIncomeGross;
    private BigDecimal grdTotalBorrowerIncomeNetBDM;
    private BigDecimal grdTotalBorrowerIncomeNetUW;

    private List<BankStmtView> tmbBankStmtViewList;
    private List<BankStmtView> othBankStmtViewList;

    private User modifyBy;
    private Date modifyDate;

    public BankStmtSummaryView() {
    }

    public void reset() {
        this.setSeasonal(RadioValue.NOT_SELECTED.value());
        this.expectedSubmitDate = DateTimeUtil.getCurrentDateTH();
        this.TMBTotalIncomeGross = BigDecimal.ZERO;
        this.TMBTotalIncomeNetBDM = BigDecimal.ZERO;
        this.TMBTotalIncomeNetUW = BigDecimal.ZERO;
        this.OthTotalIncomeGross = BigDecimal.ZERO;
        this.OthTotalIncomeNetBDM = BigDecimal.ZERO;
        this.OthTotalIncomeNetUW = BigDecimal.ZERO;
        this.grdTotalIncomeGross = BigDecimal.ZERO;
        this.grdTotalIncomeNetBDM = BigDecimal.ZERO;
        this.grdTotalIncomeNetUW = BigDecimal.ZERO;
        this.grdTotalTDChqRetAmount = BigDecimal.ZERO;
        this.grdTotalTDChqRetPercent = BigDecimal.ZERO;
        this.grdTotalAvgOSBalanceAmount = BigDecimal.ZERO;
        this.grdTotalBorrowerIncomeGross = BigDecimal.ZERO;
        this.grdTotalBorrowerIncomeNetBDM = BigDecimal.ZERO;
        this.grdTotalBorrowerIncomeNetUW = BigDecimal.ZERO;
    }

    public List<ActionStatusView> getActionStatusViewList() {
        return actionStatusViewList;
    }

    public void setActionStatusViewList(List<ActionStatusView> actionStatusViewList) {
        this.actionStatusViewList = actionStatusViewList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(int seasonal) {
        this.seasonal = seasonal;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public int getCountRefresh() {
        return countRefresh;
    }

    public void setCountRefresh(int countRefresh) {
        this.countRefresh = countRefresh;
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

    public List<BankStmtView> getTmbBankStmtViewList() {
        return tmbBankStmtViewList;
    }

    public void setTmbBankStmtViewList(List<BankStmtView> tmbBankStmtViewList) {
        this.tmbBankStmtViewList = tmbBankStmtViewList;
    }

    public List<BankStmtView> getOthBankStmtViewList() {
        return othBankStmtViewList;
    }

    public void setOthBankStmtViewList(List<BankStmtView> othBankStmtViewList) {
        this.othBankStmtViewList = othBankStmtViewList;
    }

    public BigDecimal getGrdTotalAvgOSBalanceAmount() {
        return grdTotalAvgOSBalanceAmount;
    }

    public void setGrdTotalAvgOSBalanceAmount(BigDecimal grdTotalAvgOSBalanceAmount) {
        this.grdTotalAvgOSBalanceAmount = grdTotalAvgOSBalanceAmount;
    }

    public BigDecimal getGrdTotalBorrowerIncomeGross() {
        return grdTotalBorrowerIncomeGross;
    }

    public void setGrdTotalBorrowerIncomeGross(BigDecimal grdTotalBorrowerIncomeGross) {
        this.grdTotalBorrowerIncomeGross = grdTotalBorrowerIncomeGross;
    }

    public BigDecimal getGrdTotalBorrowerIncomeNetBDM() {
        return grdTotalBorrowerIncomeNetBDM;
    }

    public void setGrdTotalBorrowerIncomeNetBDM(BigDecimal grdTotalBorrowerIncomeNetBDM) {
        this.grdTotalBorrowerIncomeNetBDM = grdTotalBorrowerIncomeNetBDM;
    }

    public BigDecimal getGrdTotalBorrowerIncomeNetUW() {
        return grdTotalBorrowerIncomeNetUW;
    }

    public void setGrdTotalBorrowerIncomeNetUW(BigDecimal grdTotalBorrowerIncomeNetUW) {
        this.grdTotalBorrowerIncomeNetUW = grdTotalBorrowerIncomeNetUW;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionStatusViewList", actionStatusViewList)
                .append("id", id)
                .append("seasonal", seasonal)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("countRefresh", countRefresh)
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
                .append("grdTotalAvgOSBalanceAmount", grdTotalAvgOSBalanceAmount)
                .append("grdTotalBorrowerIncomeGross", grdTotalBorrowerIncomeGross)
                .append("grdTotalBorrowerIncomeNetBDM", grdTotalBorrowerIncomeNetBDM)
                .append("grdTotalBorrowerIncomeNetUW", grdTotalBorrowerIncomeNetUW)
                .append("tmbBankStmtViewList", tmbBankStmtViewList)
                .append("othBankStmtViewList", othBankStmtViewList)
                .toString();
    }
}
