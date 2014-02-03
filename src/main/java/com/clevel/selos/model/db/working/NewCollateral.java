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
@Table(name = "wrk_new_coll")
public class NewCollateral implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_ID", sequenceName = "SEQ_WRK_NEW_COLL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_ID")
    private long id;

    @Column(name = "propose_type")
    private String proposeType;

    @Column(name = "appraisal_request", nullable=false, columnDefinition="int default 0")
    private int appraisalRequest;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appraisal_date")
    private Date appraisalDate;

    @Column(name = "job_id")
    private String jobID;

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

    @Column(name = "uw_decision")
    private String uwDecision;

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
    private NewCreditFacility newCreditFacility;

    @OneToMany(mappedBy = "newCollateral", cascade = CascadeType.ALL)
    private List<NewCollateralHead> newCollateralHeadList;

    @OneToMany(mappedBy = "newCollateral", cascade = CascadeType.ALL)
    private List<NewCollateralCredit> newCollateralCreditList;

    /*** For Post - Insurance Premium Quote Process ***/
    @Column(name = "premium_amount", length = 14, scale = 2)
    private BigDecimal premiumAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProposeType() {
        return proposeType;
    }

    public void setProposeType(String proposeType) {
        this.proposeType = proposeType;
    }

    public int getAppraisalRequest() {
        return appraisalRequest;
    }

    public void setAppraisalRequest(int appraisalRequest) {
        this.appraisalRequest = appraisalRequest;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
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

    public String getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(String uwDecision) {
        this.uwDecision = uwDecision;
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

    public NewCreditFacility getNewCreditFacility() {
        return newCreditFacility;
    }

    public void setNewCreditFacility(NewCreditFacility newCreditFacility) {
        this.newCreditFacility = newCreditFacility;
    }

    public List<NewCollateralHead> getNewCollateralHeadList() {
        return newCollateralHeadList;
    }

    public void setNewCollateralHeadList(List<NewCollateralHead> newCollateralHeadList) {
        this.newCollateralHeadList = newCollateralHeadList;
    }

    public List<NewCollateralCredit> getNewCollateralCreditList() {
        return newCollateralCreditList;
    }

    public void setNewCollateralCreditList(List<NewCollateralCredit> newCollateralCreditList) {
        this.newCollateralCreditList = newCollateralCreditList;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("proposeType", proposeType)
                .append("appraisalRequest", appraisalRequest)
                .append("appraisalDate", appraisalDate)
                .append("jobID", jobID)
                .append("aadDecision", aadDecision)
                .append("aadDecisionReason", aadDecisionReason)
                .append("aadDecisionReasonDetail", aadDecisionReasonDetail)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
                .append("uwDecision", uwDecision)
                .append("uwRemark", uwRemark)
                .append("mortgageCondition", mortgageCondition)
                .append("mortgageConditionDetail", mortgageConditionDetail)
                .append("bdmComments", bdmComments)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("newCreditFacility", newCreditFacility)
                .append("newCollateralHeadList", newCollateralHeadList)
                .append("newCollateralCreditList", newCollateralCreditList)
                .toString();
    }


}