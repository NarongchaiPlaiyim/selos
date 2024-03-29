package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 3/20/14
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity(name = "com.clevel.selos.model.db.working.WorkCaseOwner")
@Table(name = "wrk_case_owner")
public class WorkCaseOwner {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_OWNER_ID", sequenceName = "SEQ_WRK_CASE_OWNER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_OWNER_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @Column(name = "times_of_criteria_checked", columnDefinition = "int default 0")
    private int timesOfCriteriaChecked;

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
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

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public int getTimesOfCriteriaChecked() {
        return timesOfCriteriaChecked;
    }

    public void setTimesOfCriteriaChecked(int timesOfCriteriaChecked) {
        this.timesOfCriteriaChecked = timesOfCriteriaChecked;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("step", step)
                .append("role", role)
                .append("user", user)
                .append("workCase", workCase)
                .append("workCasePrescreen", workCasePrescreen)
                .append("createBy", createBy)
                .append("createDate", createDate)
                .append("modifyBy", modifyBy)
                .append("modifyDate", modifyDate)
                .append("timesOfCriteriaChecked", timesOfCriteriaChecked)
                .toString();
    }
}
