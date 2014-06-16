package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class ReturnInfoView implements Serializable {
    private static final long serialVersionUID = -7436809341917602008L;

    private long id;
    private Date dateOfReturn;
    private UserView returnFromUser;
    private StepView returnFromStep;
    private Date dateOfReply;
    private String returnCode;
    private String description;
    private String reason;
    private String reasonDetail;
    private String replyDetail;
    private int challenge;
    private int acceptChallenge;
    private int reasonId;
    private User createBy;
    private Date createDate;
    private User modifyBy;
    private Date modifyDate;

    //for return dialog
    private boolean canEdit;

    public ReturnInfoView() {
        reset();
    }

    public void reset() {
        this.returnFromUser = new UserView();
        this.returnFromStep = new StepView();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public UserView getReturnFromUser() {
        return returnFromUser;
    }

    public void setReturnFromUser(UserView returnFromUser) {
        this.returnFromUser = returnFromUser;
    }

    public StepView getReturnFromStep() {
        return returnFromStep;
    }

    public void setReturnFromStep(StepView returnFromStep) {
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

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
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
                .append("reasonId", reasonId)
                .append("createBy", createBy)
                .append("createDate", createDate)
                .append("modifyBy", modifyBy)
                .append("modifyDate", modifyDate)
                .append("canEdit", canEdit)
                .toString();
    }
}
