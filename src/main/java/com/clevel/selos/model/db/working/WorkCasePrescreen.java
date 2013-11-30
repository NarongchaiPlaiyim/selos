package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_case_prescreen")
public class WorkCasePrescreen implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_PRESCREEN_ID", sequenceName = "SEQ_WRK_CASE_PRESCREEN_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_PRESCREEN_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ca_number", length = 30, nullable = false)
    private String caNumber;

    @Column(name = "app_number", nullable = false)
    private String appNumber;

    @Column(name = "ref_app_number")
    private String refAppNumber;

    @Column(name = "wob_number")
    private String wobNumber;

    @Column(name = "case_lock")
    private int lock;

    @Column(name = "lock_user")
    private String lockUser;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_type_id")
    private CustomerEntity borrowerType;

    @OneToMany(mappedBy = "workCasePrescreen", fetch = FetchType.LAZY)
    private List<Customer> customerList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    @OneToOne
    @JoinColumn(name = "step_owner")
    private User stepOwner;

    public WorkCasePrescreen() {

    }

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

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getRefAppNumber() {
        return refAppNumber;
    }

    public void setRefAppNumber(String refAppNumber) {
        this.refAppNumber = refAppNumber;
    }

    public String getWobNumber() {
        return wobNumber;
    }

    public void setWobNumber(String wobNumber) {
        this.wobNumber = wobNumber;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getLockUser() {
        return lockUser;
    }

    public void setLockUser(String lockUser) {
        this.lockUser = lockUser;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
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

    public User getStepOwner() {
        return stepOwner;
    }

    public void setStepOwner(User stepOwner) {
        this.stepOwner = stepOwner;
    }
}
