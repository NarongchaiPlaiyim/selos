package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CaseStatus;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.WorkflowStep;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_case_prescreen")
public class WorkCasePrescreen implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_CASE_PRESCREEN_ID", sequenceName = "SEQ_WRK_CASE_PRESCREEN_ID", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_WRK_CASE_PRESCREEN_ID")
    @Column(name="id", nullable=false)
    private long id;

    @Column(name="ca_number", length = 30, nullable=false)
    private String caNumber;

    @Column(name="fn_case_id")
    private String fnCaseId;

    @OneToOne
    @JoinColumn(name="workflowstep_id")
    private WorkflowStep workflowStep;

    @OneToOne
    @JoinColumn(name="casestatus_id")
    private CaseStatus caseStatus;

    @OneToMany(mappedBy="workCasePrescreen")
    private List<Customer> customerList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getFnCaseId() {
        return fnCaseId;
    }

    public void setFnCaseId(String fnCaseId) {
        this.fnCaseId = fnCaseId;
    }

    public WorkflowStep getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(WorkflowStep workflowStep) {
        this.workflowStep = workflowStep;
    }

    public CaseStatus getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(CaseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
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
                .append("caNumber", caNumber)
                .append("fnCaseId", fnCaseId)
                .append("workflowStep", workflowStep)
                .append("caseStatus", caseStatus)
                .append("customerList", customerList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
