package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 3/20/14
 * Time: 9:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "wrk_case_owner")
public class WorkCaseOwner implements Serializable
{
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "role_id")
    private Integer roleid;
    @Column(name = "user_id")
    private String userid;
    @Column(name = "workcase_id")
    private Integer workCaseId;
    @Column(name = "step_id")
    private Integer stepId;
    @Column(name = "workcase_prescreen_id")
    private Integer workCasePrescreenId;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "modify_by")
    private String modifyBy;
    @Column(name = "create_date")
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Integer getWorkCasePrescreenId() {
        return workCasePrescreenId;
    }

    public void setWorkCasePrescreenId(Integer workCasePrescreenId) {
        this.workCasePrescreenId = workCasePrescreenId;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(Integer workCaseId) {
        this.workCaseId = workCaseId;
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("roleid", roleid).
                append("userid", userid).
                append("workCaseId", workCaseId).
                append("stepId", stepId).
                toString();
    }

}
