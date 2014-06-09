package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_user_sys_parameter")
public class UserSysParameter implements Serializable{
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "value")
    private String value;

    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "no_bdm_submit_flag", columnDefinition = "int default 0")
    private boolean noBDMSubmit;

    @Column(name = "pass_bu_approval_flag", columnDefinition = "int default 0")
    private boolean passBUApproval;

    @Column(name = "pass_fin_approval_step_flag", columnDefinition = "int default 0")
    private boolean passFinalApprovalStep;

    @Column(name = "active", columnDefinition = "int default 0")
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isNoBDMSubmit() {
        return noBDMSubmit;
    }

    public void setNoBDMSubmit(boolean noBDMSubmit) {
        this.noBDMSubmit = noBDMSubmit;
    }

    public boolean isPassBUApproval() {
        return passBUApproval;
    }

    public void setPassBUApproval(boolean passBUApproval) {
        this.passBUApproval = passBUApproval;
    }

    public boolean isPassFinalApprovalStep() {
        return passFinalApprovalStep;
    }

    public void setPassFinalApprovalStep(boolean passFinalApprovalStep) {
        this.passFinalApprovalStep = passFinalApprovalStep;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("code", code)
                .append("name", name)
                .append("description", description)
                .append("value", value)
                .append("fieldType", fieldType)
                .append("noBDMSubmit", noBDMSubmit)
                .append("passBUApproval", passBUApproval)
                .append("passFinalApprovalStep", passFinalApprovalStep)
                .append("active", active)
                .toString();
    }
}
