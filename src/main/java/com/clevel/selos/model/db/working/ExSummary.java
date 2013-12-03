package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_ex_summary")
public class ExSummary implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EX_SUMMARY_ID", sequenceName = "SEQ_WRK_EX_SUMMARY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EX_SUMMARY_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    //User
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
    ///////////////////////////////////////////////////////////////

    /*//Borrower Characteristic & Business Information Summary
    //exSumCharacteristicView;
    @Column(name = "customer")
    private String customer;

    @Column(name = "current_dbr")
    private BigDecimal currentDBR;

    @Column(name = "final_dbr")
    private BigDecimal finalDBR;*/

    //for calculator
    @Column(name = "income")
    private BigDecimal income;

    @Column(name = "recommended_wc_need")
    private BigDecimal recommendedWCNeed;

    @Column(name = "actual_wc")
    private BigDecimal actualWC;

    /*@Column(name = "start_business_date")
    private Date startBusinessDate;

    @Column(name = "year_in_business")
    private String yearInBusiness;

    @Column(name = "sale_per_year_bdm")
    private BigDecimal salePerYearBDM;

    @Column(name = "sale_per_year_uw")
    private BigDecimal salePerYearUW;

    @Column(name = "group_sale_bdm")
    private BigDecimal groupSaleBDM;

    @Column(name = "group_sale_uw")
    private BigDecimal groupSaleUW;

    @Column(name = "group_exposure_bdm")
    private BigDecimal groupExposureBDM;

    @Column(name = "group_exposure_uw")
    private BigDecimal groupExposureUW;

    //exSumBusinessInfoView;
    @Column(name = "net_fix_asset")
    private BigDecimal netFixAsset;

    @Column(name = "no_of_employee", nullable = false, columnDefinition = "int default 0")
    private int noOfEmployee;

    @Column(name = "biz_province")
    private String bizProvince;

    @Column(name = "biz_type")
    private String bizType;

    @Column(name = "biz_group")
    private String bizGroup;

    @Column(name = "biz_code")
    private String bizCode;

    @Column(name = "biz_desc")
    private String bizDesc;

    @Column(name = "qualitative_class")
    private String qualitativeClass;

    @Column(name = "biz_size")
    private BigDecimal bizSize;

    @Column(name = "bdm")
    private BigDecimal BDM;

    @Column(name = "uw")
    private BigDecimal UW;

    @Column(name = "ar")
    private BigDecimal AR;

    @Column(name = "ap")
    private BigDecimal AP;

    @Column(name = "inv")
    private BigDecimal INV;

    //business operation activity
    @Column(name = "business_activity")
    private String businessOperationActivity;

    @Column(name = "business_permission")
    private String businessPermission;

    @Column(name = "expiry_date")
    private Date expiryDate;
    ///////////////////////////////////////////////////////////////

    //Account Movement
    //exSumAccMovementView;
    @Column(name = "od_limit")
    private BigDecimal odLimit;

    @Column(name = "utilization")
    private BigDecimal utilization;

    @Column(name = "swing")
    private BigDecimal swing;

    @Column(name = "over_limit_times", nullable = false, columnDefinition = "int default 0")
    private int overLimitTimes;

    @Column(name = "over_limit_days", nullable = false, columnDefinition = "int default 0")
    private int overLimitDays;

    @Column(name = "cheque_return", nullable = false, columnDefinition = "int default 0")
    private int chequeReturn;

    @Column(name = "cash_flow")
    private BigDecimal cashFlow;

    @Column(name = "cash_flow_limit")
    private BigDecimal cashFlowLimit;

    @Column(name = "trade_cheque_amount")
    private BigDecimal tradeChequeReturnAmount;

    @Column(name = "trade_cheque_percent")
    private BigDecimal tradeChequeReturnPercent;
    ///////////////////////////////////////////////////////////////

    //Collateral
    //exSumCollateralView;
    @Column(name = "cash_collateral_value")
    private BigDecimal cashCollateralValue;

    @Column(name = "core_asset_value")
    private BigDecimal coreAssetValue;

    @Column(name = "none_core_asset_value")
    private BigDecimal noneCoreAssetValue;

    @Column(name = "limit_approve")
    private BigDecimal limitApprove;

    @Column(name = "percent_ltv")
    private BigDecimal percentLTV;
    ///////////////////////////////////////////////////////////////*/

    //Business Overview and Support Decision
    @Column(name = "nature_of_business", length = 500)
    private String natureOfBusiness;

    @Column(name = "historical_of_change", length = 500)
    private String historicalAndReasonOfChange;

    @Column(name = "tmb_credit_history", length = 500)
    private String tmbCreditHistory;

    @Column(name = "support_reason", length = 500)
    private String supportReason;

    @Column(name = "rm008Code", nullable = false, columnDefinition = "int default -1")
    private int rm008Code;

    @Column(name = "rm008Remark", length = 500)
    private String rm008Remark;

    @Column(name = "rm204Code", nullable = false, columnDefinition = "int default -1")
    private int rm204Code;

    @Column(name = "rm204Remark", length = 500)
    private String rm204Remark;

    @Column(name = "rm020Code", nullable = false, columnDefinition = "int default -1")
    private int rm020Code;

    @Column(name = "rm020Remark", length = 500)
    private String rm020Remark;
    ///////////////////////////////////////////////////////////////

    //UW Decision and Explanation
    ///////////////////////////////////////////////////////////////
    @OneToOne
    @JoinColumn(name = "authority_id")
    private AuthorizationDOA approveAuthority;

    @Column(name = "uw_code")
    private String uwCode;

    @Column(name = "decision", nullable = false, columnDefinition = "int default -1")
    private int decision;

    @OneToMany(mappedBy = "exSummary")
    private List<ExSumDeviate> deviateCode;
    ///////////////////////////////////////////////////////////////

    //UW Comment
    @Column(name = "uw_comment", length = 500)
    private String uwComment;
    ///////////////////////////////////////////////////////////////

    //To Send to BRMS - calculate months from yearInBusiness fields.
    @Column(name ="year_business_month")
    private String yearInBusinessMonth;
    ///////////////////////////////////////////////////////////////

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

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public String getHistoricalAndReasonOfChange() {
        return historicalAndReasonOfChange;
    }

    public void setHistoricalAndReasonOfChange(String historicalAndReasonOfChange) {
        this.historicalAndReasonOfChange = historicalAndReasonOfChange;
    }

    public String getTmbCreditHistory() {
        return tmbCreditHistory;
    }

    public void setTmbCreditHistory(String tmbCreditHistory) {
        this.tmbCreditHistory = tmbCreditHistory;
    }

    public String getSupportReason() {
        return supportReason;
    }

    public void setSupportReason(String supportReason) {
        this.supportReason = supportReason;
    }

    public int getRm008Code() {
        return rm008Code;
    }

    public void setRm008Code(int rm008Code) {
        this.rm008Code = rm008Code;
    }

    public String getRm008Remark() {
        return rm008Remark;
    }

    public void setRm008Remark(String rm008Remark) {
        this.rm008Remark = rm008Remark;
    }

    public int getRm204Code() {
        return rm204Code;
    }

    public void setRm204Code(int rm204Code) {
        this.rm204Code = rm204Code;
    }

    public String getRm204Remark() {
        return rm204Remark;
    }

    public void setRm204Remark(String rm204Remark) {
        this.rm204Remark = rm204Remark;
    }

    public int getRm020Code() {
        return rm020Code;
    }

    public void setRm020Code(int rm020Code) {
        this.rm020Code = rm020Code;
    }

    public String getRm020Remark() {
        return rm020Remark;
    }

    public void setRm020Remark(String rm020Remark) {
        this.rm020Remark = rm020Remark;
    }

    public String getUwComment() {
        return uwComment;
    }

    public void setUwComment(String uwComment) {
        this.uwComment = uwComment;
    }

    public AuthorizationDOA getApproveAuthority() {
        return approveAuthority;
    }

    public void setApproveAuthority(AuthorizationDOA approveAuthority) {
        this.approveAuthority = approveAuthority;
    }

    public String getUwCode() {
        return uwCode;
    }

    public void setUwCode(String uwCode) {
        this.uwCode = uwCode;
    }

    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public List<ExSumDeviate> getDeviateCode() {
        return deviateCode;
    }

    public void setDeviateCode(List<ExSumDeviate> deviateCode) {
        this.deviateCode = deviateCode;
    }

    public String getYearInBusinessMonth() {
        return yearInBusinessMonth;
    }

    public void setYearInBusinessMonth(String yearInBusinessMonth) {
        this.yearInBusinessMonth = yearInBusinessMonth;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getRecommendedWCNeed() {
        return recommendedWCNeed;
    }

    public void setRecommendedWCNeed(BigDecimal recommendedWCNeed) {
        this.recommendedWCNeed = recommendedWCNeed;
    }

    public BigDecimal getActualWC() {
        return actualWC;
    }

    public void setActualWC(BigDecimal actualWC) {
        this.actualWC = actualWC;
    }
}
