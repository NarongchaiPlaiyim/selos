package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class BRMSResult implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BRMS_RESULT_ID", sequenceName="SEQ_WRK_BRMS_RESULT_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BRMS_RESULT_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePreScreen;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name="step_id")
    private Step step;

    @Column(name="rule_name")
    private String ruleName;

    @Column(name="rule_order")
    private String ruleOrder;

    @Column(name="rule_color")
    private String ruleColor;

    @Column(name="rule_type")
    private String ruleType;

    @Column(name="personal_id")
    private String personalId;

    @Column(name="deviation_flag")
    private String deviationFlag;

    @Column(name="reject_group_code")
    private String rejectGroupCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCasePrescreen getWorkCasePreScreen() {
        return workCasePreScreen;
    }

    public void setWorkCasePreScreen(WorkCasePrescreen workCasePreScreen) {
        this.workCasePreScreen = workCasePreScreen;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(String ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public String getRuleColor() {
        return ruleColor;
    }

    public void setRuleColor(String ruleColor) {
        this.ruleColor = ruleColor;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getDeviationFlag() {
        return deviationFlag;
    }

    public void setDeviationFlag(String deviationFlag) {
        this.deviationFlag = deviationFlag;
    }

    public String getRejectGroupCode() {
        return rejectGroupCode;
    }

    public void setRejectGroupCode(String rejectGroupCode) {
        this.rejectGroupCode = rejectGroupCode;
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
}
