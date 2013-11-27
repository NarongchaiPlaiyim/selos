package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_bankstatement_summary")
public class BankStatementSummary implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BANKSTMT_SUM_ID", sequenceName = "SEQ_WRK_BANKSTMT_SUM_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BANKSTMT_SUM_ID")
    private long id;

    @Column(name = "seasonal_flag")
    private int seasonal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expected_submit_date")
    private Date expectedSubmitDate;

    @Column(name = "tmb_total_income_gross")
    private BigDecimal TMBTotalIncomeGross;

    @Column(name = "tmb_total_income_bdm")
    private BigDecimal TMBTotalIncomeNetBDM;

    @Column(name = "tmb_total_income_uw")
    private BigDecimal TMBTotalIncomeNetUW;

    @Column(name = "other_total_income_gross")
    private BigDecimal othTotalIncomeGross;

    @Column(name = "other_total_income_bdm")
    private BigDecimal othTotalIncomeNetBDM;

    @Column(name = "other_total_income_uw")
    private BigDecimal othTotalIncomeNetUW;

    @Column(name = "grand_total_income_gross")
    private BigDecimal grdTotalIncomeGross;

    @Column(name = "grand_total_income_bdm")
    private BigDecimal grdTotalIncomeNetBDM;

    @Column(name = "grand_total_income_uw")
    private BigDecimal grdTotalIncomeNetUW;

    @Column(name = "grand_td_chq_return_amt")
    private BigDecimal grdTotalTDChqRetAmount;

    @Column(name = "grand_td_chq_return_percent")
    private BigDecimal grdTotalTDChqRetPercent;

    @Column(name = "grand_avg_os_balance_amt")
    private BigDecimal grdTotalAvgOSBalanceAmount;

    @Column(name = "count_refresh")
    private int countRefresh;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToMany(mappedBy = "bankStatementSummary", cascade = CascadeType.ALL)
    private List<BankStatement> bankStmtList;

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
        return othTotalIncomeGross;
    }

    public void setOthTotalIncomeGross(BigDecimal othTotalIncomeGross) {
        this.othTotalIncomeGross = othTotalIncomeGross;
    }

    public BigDecimal getOthTotalIncomeNetBDM() {
        return othTotalIncomeNetBDM;
    }

    public void setOthTotalIncomeNetBDM(BigDecimal othTotalIncomeNetBDM) {
        this.othTotalIncomeNetBDM = othTotalIncomeNetBDM;
    }

    public BigDecimal getOthTotalIncomeNetUW() {
        return othTotalIncomeNetUW;
    }

    public void setOthTotalIncomeNetUW(BigDecimal othTotalIncomeNetUW) {
        this.othTotalIncomeNetUW = othTotalIncomeNetUW;
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

    public BigDecimal getGrdTotalAvgOSBalanceAmount() {
        return grdTotalAvgOSBalanceAmount;
    }

    public void setGrdTotalAvgOSBalanceAmount(BigDecimal grdTotalAvgOSBalanceAmount) {
        this.grdTotalAvgOSBalanceAmount = grdTotalAvgOSBalanceAmount;
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

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public List<BankStatement> getBankStmtList() {
        return bankStmtList;
    }

    public void setBankStmtList(List<BankStatement> bankStmtList) {
        this.bankStmtList = bankStmtList;
    }

    public int getCountRefresh() {
        return countRefresh;
    }

    public void setCountRefresh(int countRefresh) {
        this.countRefresh = countRefresh;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("seasonal", seasonal)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("TMBTotalIncomeGross", TMBTotalIncomeGross)
                .append("TMBTotalIncomeNetBDM", TMBTotalIncomeNetBDM)
                .append("TMBTotalIncomeNetUW", TMBTotalIncomeNetUW)
                .append("othTotalIncomeGross", othTotalIncomeGross)
                .append("othTotalIncomeNetBDM", othTotalIncomeNetBDM)
                .append("othTotalIncomeNetUW", othTotalIncomeNetUW)
                .append("grdTotalIncomeGross", grdTotalIncomeGross)
                .append("grdTotalIncomeNetBDM", grdTotalIncomeNetBDM)
                .append("grdTotalIncomeNetUW", grdTotalIncomeNetUW)
                .append("grdTotalTDChqRetAmount", grdTotalTDChqRetAmount)
                .append("grdTotalTDChqRetPercent", grdTotalTDChqRetPercent)
                .append("grdTotalAvgOSBalanceAmount", grdTotalAvgOSBalanceAmount)
                .append("countRefresh", countRefresh)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("workCase", workCase)
                .append("workCasePrescreen", workCasePrescreen)
                .append("bankStmtList", bankStmtList)
                .toString();
    }
}
