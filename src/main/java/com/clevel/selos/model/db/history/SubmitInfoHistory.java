package com.clevel.selos.model.db.history;

import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "his_submit_info")
public class SubmitInfoHistory implements Serializable {
    private static final long serialVersionUID = -7547065511627057306L;

    @Id
    @SequenceGenerator(name = "SEQ_HIS_SUBMIT_INFO_ID", sequenceName = "SEQ_HIS_SUBMIT_INFO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HIS_SUBMIT_INFO_ID")
    private long id;

    @Column(name = "app_number")
    private String appNumber;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "remark")
    private String remark;

    @OneToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    @OneToOne
    @JoinColumn(name = "to_user")
    private User toUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submit_date")
    private Date submitDate;

    @Column(name = "submit_type", columnDefinition = "int default 1")
    private int submitType;

    public SubmitInfoHistory(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public int getSubmitType() {
        return submitType;
    }

    public void setSubmitType(int submitType) {
        this.submitType = submitType;
    }
}
