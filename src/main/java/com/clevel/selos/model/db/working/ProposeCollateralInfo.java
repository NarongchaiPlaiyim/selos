package com.clevel.selos.model.db.working;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_coll")
public class ProposeCollateralInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_ID", sequenceName = "SEQ_WRK_NEW_COLL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_ID")
    private long id;

    @Column(name = "propose_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    @Column(name = "uw_decision", columnDefinition = "int default 0", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private DecisionType uwDecision;

    @Column(name = "appraisal_request", nullable=false, columnDefinition="int default 0")
    private int appraisalRequest;

    @Column(name = "coms")
    private int coms;

    @Column(name = "job_id")
    private String jobID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appraisal_date")
    private Date appraisalDate;

    @Column(name = "number_months_from_appr_date", columnDefinition = "int default 0")
    private int numberMonthsFromApprDate;

    @Column(name = "aad_decision")
    private String aadDecision;

    @Column(name = "aad_decision_reason")
    private String aadDecisionReason;

    @Column(name = "aad_decision_reason_detail")
    private String aadDecisionReasonDetail;

    @Column(name = "usage")
    private String usage;

    @Column(name = "type_of_usage")
    private String typeOfUsage;

    @Column(name = "uw_remark")
    private String uwRemark;

    @Column(name = "mortgage_condition")
    private String mortgageCondition;

    @Column(name = "mortgage_condition_detail")
    private String mortgageConditionDetail;

    @Column(name = "bdm_comments")
    private String bdmComments;

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
    @JoinColumn(name = "new_credit_facility_id")
    private ProposeLine proposeLine;

    @OneToMany(mappedBy = "proposeCollateral", cascade = CascadeType.ALL)
    private List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList;

    @OneToMany(mappedBy = "proposeCollateral", cascade = CascadeType.ALL)
    private List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList;

    /*** For Post - Insurance Premium Quote Process ***/
    @Column(name = "premium_amount", length = 14, scale = 2)
    private BigDecimal premiumAmount;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public int getAppraisalRequest() {
        return appraisalRequest;
    }

    public void setAppraisalRequest(int appraisalRequest) {
        this.appraisalRequest = appraisalRequest;
    }

    public int getComs() {
        return coms;
    }

    public void setComs(int coms) {
        this.coms = coms;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public int getNumberMonthsFromApprDate() {
        return numberMonthsFromApprDate;
    }

    public void setNumberMonthsFromApprDate(int numberMonthsFromApprDate) {
        this.numberMonthsFromApprDate = numberMonthsFromApprDate;
    }

    public String getAadDecision() {
        return aadDecision;
    }

    public void setAadDecision(String aadDecision) {
        this.aadDecision = aadDecision;
    }

    public String getAadDecisionReason() {
        return aadDecisionReason;
    }

    public void setAadDecisionReason(String aadDecisionReason) {
        this.aadDecisionReason = aadDecisionReason;
    }

    public String getAadDecisionReasonDetail() {
        return aadDecisionReasonDetail;
    }

    public void setAadDecisionReasonDetail(String aadDecisionReasonDetail) {
        this.aadDecisionReasonDetail = aadDecisionReasonDetail;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    public void setTypeOfUsage(String typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
    }

    public String getUwRemark() {
        return uwRemark;
    }

    public void setUwRemark(String uwRemark) {
        this.uwRemark = uwRemark;
    }

    public String getMortgageCondition() {
        return mortgageCondition;
    }

    public void setMortgageCondition(String mortgageCondition) {
        this.mortgageCondition = mortgageCondition;
    }

    public String getMortgageConditionDetail() {
        return mortgageConditionDetail;
    }

    public void setMortgageConditionDetail(String mortgageConditionDetail) {
        this.mortgageConditionDetail = mortgageConditionDetail;
    }

    public String getBdmComments() {
        return bdmComments;
    }

    public void setBdmComments(String bdmComments) {
        this.bdmComments = bdmComments;
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

    public ProposeLine getProposeLine() {
        return proposeLine;
    }

    public void setProposeLine(ProposeLine proposeLine) {
        this.proposeLine = proposeLine;
    }

    public List<ProposeCollateralInfoHead> getProposeCollateralInfoHeadList() {
        return proposeCollateralInfoHeadList;
    }

    public void setProposeCollateralInfoHeadList(List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList) {
        this.proposeCollateralInfoHeadList = proposeCollateralInfoHeadList;
    }

    public List<ProposeCollateralInfoRelation> getProposeCollateralInfoRelationList() {
        return proposeCollateralInfoRelationList;
    }

    public void setProposeCollateralInfoRelationList(List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList) {
        this.proposeCollateralInfoRelationList = proposeCollateralInfoRelationList;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("proposeType", proposeType)
                .append("uwDecision", uwDecision)
                .append("appraisalRequest", appraisalRequest)
                .append("coms", coms)
                .append("jobID", jobID)
                .append("appraisalDate", appraisalDate)
                .append("numberMonthsFromApprDate", numberMonthsFromApprDate)
                .append("aadDecision", aadDecision)
                .append("aadDecisionReason", aadDecisionReason)
                .append("aadDecisionReasonDetail", aadDecisionReasonDetail)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
                .append("uwRemark", uwRemark)
                .append("mortgageCondition", mortgageCondition)
                .append("mortgageConditionDetail", mortgageConditionDetail)
                .append("bdmComments", bdmComments)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("proposeLine", proposeLine)
                .append("proposeCollateralInfoHeadList", proposeCollateralInfoHeadList)
                .append("proposeCollateralInfoRelationList", proposeCollateralInfoRelationList)
                .append("premiumAmount", premiumAmount)
                .append("workCase", workCase)
                .toString();
    }
}