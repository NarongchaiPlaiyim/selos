package com.clevel.selos.model.db.audit;

import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "adt_isa_activity")
public class IsaActivity implements Serializable {

    @Id
    @SequenceGenerator(name = "SEQ_ADT_ISA_ID", sequenceName = "SEQ_ADT_ISA_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADT_ISA_ID")
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "action", nullable = false)
    private String action;
    @Column(name = "action_desc", length = 1000)
    private String actionDesc;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_date", nullable = false)
    private Date actionDate;
    @Column(name = "result", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionResult actionResult;
    @Column(name = "result_desc", length = 1000)
    private String resultDesc;
    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    //Basic_Security_Checklist_v2.3
    //User Account Management Activities (Add/Change/Delete User Account, etc.) Page 12.
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    @Column(name = "old_data", length = 1000)
    private String oldData;

    @Column(name = "new_data", length = 1000)
    private String newData;

    public IsaActivity(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String ipAddress, Date modifyDate, User modifyBy, String oldData, String newData) {
        this.userId = userId;
        this.action = action;
        this.actionDesc = actionDesc;
        this.actionDate = actionDate;
        this.actionResult = actionResult;
        this.resultDesc = resultDesc;
        this.ipAddress = ipAddress;
        this.modifyDate = modifyDate;
        this.modifyBy = modifyBy;
        this.oldData = oldData;
        this.newData = newData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("userId", userId)
                .append("action", action)
                .append("actionDesc", actionDesc)
                .append("actionDate", actionDate)
                .append("actionResult", actionResult)
                .append("resultDesc", resultDesc)
                .append("ipAddress", ipAddress)
                .append("modifyDate", modifyDate)
                .append("modifyBy", modifyBy)
                .append("oldData", oldData)
                .append("newData", newData)
                .toString();
    }
}
