package com.clevel.selos.model.db.history;

import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "his_return_info")
public class ReturnInfoHistory implements Serializable {
    private static final long serialVersionUID = 6730969926562436772L;

    @Id
    @SequenceGenerator(name = "SEQ_HIS_RETURN_INFO_ID", sequenceName = "SEQ_HIS_RETURN_INFO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HIS_RETURN_INFO_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "date_return")
    private Date dateOfReturn;

    @OneToOne
    @JoinColumn(name = "return_from_user_id")
    private User returnFromUser;

    @OneToOne
    @JoinColumn(name = "return_from_step_id")
    private Step returnFromStep;

    @Column(name = "date_of_reply")
    private Date dateOfReply;

    @Column(name = "return_code")
    private String returnCode;

    @Column(name = "description")
    private String description;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reasonDetail")
    private String reasonDetail;

    @Column(name = "replyDetail")
    private String replyDetail;

    @Column(name = "challenge", length = 1, nullable = false, columnDefinition = "int default 0")
    private int challenge;

    @Column(name = "accept_challenge", length = 1, nullable = false, columnDefinition = "int default 0")
    private int acceptChallenge;

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

    @Column(name = "canEdit", length = 1, nullable = false, columnDefinition = "int default 0")
    private int canEdit;

    @Column(name = "reasonId", columnDefinition = "int default 0")
    private int reasonId;

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

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public User getReturnFromUser() {
        return returnFromUser;
    }

    public void setReturnFromUser(User returnFromUser) {
        this.returnFromUser = returnFromUser;
    }

    public Step getReturnFromStep() {
        return returnFromStep;
    }

    public void setReturnFromStep(Step returnFromStep) {
        this.returnFromStep = returnFromStep;
    }

    public Date getDateOfReply() {
        return dateOfReply;
    }

    public void setDateOfReply(Date dateOfReply) {
        this.dateOfReply = dateOfReply;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }

    public String getReplyDetail() {
        return replyDetail;
    }

    public void setReplyDetail(String replyDetail) {
        this.replyDetail = replyDetail;
    }

    public int getChallenge() {
        return challenge;
    }

    public void setChallenge(int challenge) {
        this.challenge = challenge;
    }

    public int getAcceptChallenge() {
        return acceptChallenge;
    }

    public void setAcceptChallenge(int acceptChallenge) {
        this.acceptChallenge = acceptChallenge;
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

    public int getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(int canEdit) {
        this.canEdit = canEdit;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("dateOfReturn", dateOfReturn)
                .append("returnFromUser", returnFromUser)
                .append("returnFromStep", returnFromStep)
                .append("dateOfReply", dateOfReply)
                .append("returnCode", returnCode)
                .append("description", description)
                .append("reason", reason)
                .append("reasonDetail", reasonDetail)
                .append("replyDetail", replyDetail)
                .append("challenge", challenge)
                .append("acceptChallenge", acceptChallenge)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("canEdit", canEdit)
                .append("reasonId", reasonId)
                .toString();
    }
}
