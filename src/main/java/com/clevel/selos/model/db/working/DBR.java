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
@Table(name = "wrk_dbr")
public class DBR implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_DBR_ID", sequenceName = "SEQ_WRK_DBR_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DBR_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToMany(mappedBy = "dbr")
    private List<DBRDetail> dbrDetails;

    @Column(name = "income_factor")
    private BigDecimal incomeFactor;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "monthly_income_adjust")
    private BigDecimal monthlyIncomeAdjust;

    @Column(name = "monthly_income_per_month")
    private BigDecimal monthlyIncomePerMonth;

    @Column(name = "net_monthly_income")
    private BigDecimal netMonthlyIncome;

    @Column(name = "current_dbr")
    private BigDecimal currentDBR;

    @Column(name = "dbr_before_request")
    private BigDecimal dbrBeforeRequest;

    @Column(name = "dbr_interest")
    private BigDecimal dbrInterest;

    @Column(name ="final_dbr")
    private BigDecimal finalDBR;

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

    @Column(name = "totalMonthDebtBorrowerStart")
    private BigDecimal totalMonthDebtBorrowerStart;

    @Column(name = "totalMonthDebtBorrowerFinal")
    private BigDecimal totalMonthDebtBorrowerFinal;

    @Column(name = "TOTAL_MONTH_DEBT_RELATED_WC")
    private BigDecimal totalMonthDebtRelatedWc;

    public DBR() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public List<DBRDetail> getDbrDetails() {
        return dbrDetails;
    }

    public void setDbrDetails(List<DBRDetail> dbrDetails) {
        this.dbrDetails = dbrDetails;
    }

    public BigDecimal getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(BigDecimal incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyIncomeAdjust() {
        return monthlyIncomeAdjust;
    }

    public void setMonthlyIncomeAdjust(BigDecimal monthlyIncomeAdjust) {
        this.monthlyIncomeAdjust = monthlyIncomeAdjust;
    }

    public BigDecimal getMonthlyIncomePerMonth() {
        return monthlyIncomePerMonth;
    }

    public void setMonthlyIncomePerMonth(BigDecimal monthlyIncomePerMonth) {
        this.monthlyIncomePerMonth = monthlyIncomePerMonth;
    }

    public BigDecimal getNetMonthlyIncome() {
        return netMonthlyIncome;
    }

    public void setNetMonthlyIncome(BigDecimal netMonthlyIncome) {
        this.netMonthlyIncome = netMonthlyIncome;
    }

    public BigDecimal getCurrentDBR() {
        return currentDBR;
    }

    public void setCurrentDBR(BigDecimal currentDBR) {
        this.currentDBR = currentDBR;
    }

    public BigDecimal getDbrBeforeRequest() {
        return dbrBeforeRequest;
    }

    public void setDbrBeforeRequest(BigDecimal dbrBeforeRequest) {
        this.dbrBeforeRequest = dbrBeforeRequest;
    }

    public BigDecimal getDbrInterest() {
        return dbrInterest;
    }

    public void setDbrInterest(BigDecimal dbrInterest) {
        this.dbrInterest = dbrInterest;
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

    public BigDecimal getFinalDBR() {
        return finalDBR;
    }

    public void setFinalDBR(BigDecimal finalDBR) {
        this.finalDBR = finalDBR;
    }

    public BigDecimal getTotalMonthDebtBorrowerStart() {
        return totalMonthDebtBorrowerStart;
    }

    public void setTotalMonthDebtBorrowerStart(BigDecimal totalMonthDebtBorrowerStart) {
        this.totalMonthDebtBorrowerStart = totalMonthDebtBorrowerStart;
    }

    public BigDecimal getTotalMonthDebtBorrowerFinal() {
        return totalMonthDebtBorrowerFinal;
    }

    public void setTotalMonthDebtBorrowerFinal(BigDecimal totalMonthDebtBorrowerFinal) {
        this.totalMonthDebtBorrowerFinal = totalMonthDebtBorrowerFinal;
    }

    public BigDecimal getTotalMonthDebtRelatedWc() {
        return totalMonthDebtRelatedWc;
    }

    public void setTotalMonthDebtRelatedWc(BigDecimal totalMonthDebtRelatedWc) {
        this.totalMonthDebtRelatedWc = totalMonthDebtRelatedWc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("dbrDetails", dbrDetails)
                .append("incomeFactor", incomeFactor)
                .append("monthlyIncome", monthlyIncome)
                .append("monthlyIncomeAdjust", monthlyIncomeAdjust)
                .append("monthlyIncomePerMonth", monthlyIncomePerMonth)
                .append("netMonthlyIncome", netMonthlyIncome)
                .append("currentDBR", currentDBR)
                .append("dbrBeforeRequest", dbrBeforeRequest)
                .append("dbrInterest", dbrInterest)
                .append("finalDBR", finalDBR)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
