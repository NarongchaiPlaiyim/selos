package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class AbstractWorkCase implements Serializable{

    @Column(name = "app_number", length = 16,nullable = false)
    protected String appNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_date")
    protected Date receivedDate;

    @OneToOne
    @JoinColumn(name = "at_user_team_id")
    protected UserTeam atUserTeam;

    @OneToOne
    @JoinColumn(name = "product_group_id")
    protected ProductGroup productGroup;

    @OneToOne
    @JoinColumn(name = "request_type_id")
    protected RequestType requestType;

    @OneToOne
    @JoinColumn(name = "from_user_id")
    protected User fromUser;

    @OneToOne
    @JoinColumn(name = "at_user_id")
    protected User atUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_date")
    protected Date appointmentDate;

    @ManyToOne
    @JoinColumn(name = "doa_level_id")
    protected AuthorizationDOA authorizationDOA;

    /*@Temporal(TemporalType.TIMESTAMP)
    @JoinColumn(name = "sla_end_date")
    protected Date slaEndDate;*/

    @Column(name = "total_time_at_user", length = 1, columnDefinition = "int default 0")
    protected int totalTimeAtUser;

    @Column(name = "total_time_at_process", length = 1, columnDefinition = "int default 0")
    protected int totalTimeAtProcess;

    @Column(name = "wob_number")
    protected String wobNumber;

    @Column(name = "case_lock", length = 1, columnDefinition = "int default 0")
    protected int lock;

    @Column(name = "lock_user")
    protected String lockUser;

    @OneToOne
    @JoinColumn(name = "step_id")
    protected Step step;

    @OneToOne
    @JoinColumn(name = "status_id")
    protected Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    protected Date createDate;

    @OneToOne
    @JoinColumn(name = "create_by")
    protected User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "complete_date")
    protected Date completeDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    protected Date modifyDate;

    @OneToOne
    @JoinColumn(name = "modify_by")
    protected User modifyBy;

    @Column(name = "bpm_active", length = 1, columnDefinition = "int default 1")
    protected int bpmActive;

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

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
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

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public UserTeam getAtUserTeam() {
        return atUserTeam;
    }

    public void setAtUserTeam(UserTeam atUserTeam) {
        this.atUserTeam = atUserTeam;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType request_type) {
        this.requestType = request_type;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getAtUser() {
        return atUser;
    }

    public void setAtUser(User atUser) {
        this.atUser = atUser;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AuthorizationDOA getAuthorizationDOA() {
        return authorizationDOA;
    }

    public void setAuthorizationDOA(AuthorizationDOA authorizationDOA) {
        this.authorizationDOA = authorizationDOA;
    }

   /* public Date getSlaEndDate() {
        return slaEndDate;
    }

    public void setSlaEndDate(Date slaEndDate) {
        this.slaEndDate = slaEndDate;
    }*/

    public int getTotalTimeAtUser() {
        return totalTimeAtUser;
    }

    public void setTotalTimeAtUser(int totalTimeAtUser) {
        this.totalTimeAtUser = totalTimeAtUser;
    }

    public int getTotalTimeAtProcess() {
        return totalTimeAtProcess;
    }

    public void setTotalTimeAtProcess(int totalTimeAtProcess) {
        this.totalTimeAtProcess = totalTimeAtProcess;
    }

    public int getBpmActive() {
        return bpmActive;
    }

    public void setBpmActive(int bpmActive) {
        this.bpmActive = bpmActive;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
}
