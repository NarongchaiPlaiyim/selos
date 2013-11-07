package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name ="wrk_collateral_detail")
public class CollateralDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_COLL_DETAIL_ID", sequenceName="SEQ_WRK_COLL_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_COLL_DETAIL_ID")
    private long id;

    @Column(name="no")
    private int no;

    @Column(name="job_id")
    private String jobID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="appraisal_date")
    private Date appraisalDate;

    @Column(name="aad_decision")
    private String AADDecision;

    @Column(name="aad_decision_reason")
    private String AADDecisionReason;

    @Column(name="aad_decision_reason_detail")
    private String AADDecisionReasonDetail;

    @Column(name="usage")
    private String usage;

    @Column(name="type_of_usage")
    private String typeOfUsage;

    @Column(name="mortgage_condition")
    private String mortgageCondition;

    @Column(name="mortgage_condition_detail")
    private String mortgageConditionDetail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "appraisal_id")
    private Appraisal appraisal;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
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

    public String getAADDecision() {
        return AADDecision;
    }

    public void setAADDecision(String AADDecision) {
        this.AADDecision = AADDecision;
    }

    public String getAADDecisionReason() {
        return AADDecisionReason;
    }

    public void setAADDecisionReason(String AADDecisionReason) {
        this.AADDecisionReason = AADDecisionReason;
    }

    public String getAADDecisionReasonDetail() {
        return AADDecisionReasonDetail;
    }

    public void setAADDecisionReasonDetail(String AADDecisionReasonDetail) {
        this.AADDecisionReasonDetail = AADDecisionReasonDetail;
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

    public Appraisal getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(Appraisal appraisal) {
        this.appraisal = appraisal;
    }

    public CollateralDetail() {
    }

}


