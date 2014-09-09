package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrk_case_prescreen")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WorkCasePrescreen extends AbstractWorkCase {

    private static final long serialVersionUID = 2014041200000001L;

    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_PRESCREEN_ID", sequenceName = "SEQ_WRK_CASE_PRESCREEN_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_PRESCREEN_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ca_number", length = 30, nullable = false)
    protected String caNumber;

    @Column(name = "ref_app_number")
    protected String refAppNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_type_id")
    private CustomerEntity borrowerType;

    @OneToMany(mappedBy = "workCasePrescreen", fetch = FetchType.LAZY)
    private List<Customer> customerList;

    @OneToOne
    @JoinColumn(name = "step_owner")
    private User stepOwner;

    @Column(name = "request_appraisal", columnDefinition = "int default 0")
    private int requestAppraisal;

    @Column(name = "parallel_appraisal_flag", columnDefinition = "int default 0")
    private int parallelAppraisalFlag;

    @Column(name = "ncb_reject_flag", columnDefinition = "int default 0")
    private int ncbRejectFlag;

    @OneToOne
    @JoinColumn(name = "appeal_resubmit_reason_id")
    private Reason appealResubmitReason;

    /*@Column(name = "request_type_id")
    private int requestTypeId;*/

    public WorkCasePrescreen() {

    }

    /*public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }*/

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

    public String getRefAppNumber() {
        return refAppNumber;
    }

    public void setRefAppNumber(String refAppNumber) {
        this.refAppNumber = refAppNumber;
    }

    public CustomerEntity getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(CustomerEntity borrowerType) {
        this.borrowerType = borrowerType;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public User getStepOwner() {
        return stepOwner;
    }

    public void setStepOwner(User stepOwner) {
        this.stepOwner = stepOwner;
    }

    public int getRequestAppraisal() {
        return requestAppraisal;
    }

    public void setRequestAppraisal(int requestAppraisal) {
        this.requestAppraisal = requestAppraisal;
    }

    public int getParallelAppraisalFlag() {
        return parallelAppraisalFlag;
    }

    public void setParallelAppraisalFlag(int parallelAppraisalFlag) {
        this.parallelAppraisalFlag = parallelAppraisalFlag;
    }

    public int getNcbRejectFlag() {
        return ncbRejectFlag;
    }

    public void setNcbRejectFlag(int ncbRejectFlag) {
        this.ncbRejectFlag = ncbRejectFlag;
    }

    public Reason getAppealResubmitReason() {
        return appealResubmitReason;
    }

    public void setAppealResubmitReason(Reason appealResubmitReason) {
        this.appealResubmitReason = appealResubmitReason;
    }
}
