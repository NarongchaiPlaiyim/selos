package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_approval_history")
public class ApprovalHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_APPROVAL_HISTORY_ID", sequenceName = "SEQ_WRK_APPROVAL_HISTORY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_APPROVAL_HISTORY_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "submit_date")
    private Date submitDate;

    @Column(name = "comments")
    private String comments;

    @Column(name = "approve_decision", columnDefinition = "int default 0")
    private int approveDecision;

    @Column(name = "is_submit", columnDefinition = "int default 0")
    private int isSubmit;

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

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getApproveDecision() {
        return approveDecision;
    }

    public void setApproveDecision(int approveDecision) {
        this.approveDecision = approveDecision;
    }

    public int getSubmit() {
        return isSubmit;
    }

    public void setSubmit(int submit) {
        isSubmit = submit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("step", step)
                .append("user", user)
                .append("submitDate", submitDate)
                .append("comments", comments)
                .append("approveDecision", approveDecision)
                .append("isSubmit", isSubmit)
                .toString();
    }
}
