package com.clevel.selos.model.view.master;

import com.clevel.selos.model.db.master.ReasonType;
import com.clevel.selos.model.db.master.UWRejectGroup;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ReasonView implements Serializable{
    private int id;
    private int reasonTypeId;
    private String code;
    private String description;
    private int active;
    private int uwRejectGroupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReasonTypeId() {
        return reasonTypeId;
    }

    public void setReasonTypeId(int reasonTypeId) {
        this.reasonTypeId = reasonTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getUwRejectGroupId() {
        return uwRejectGroupId;
    }

    public void setUwRejectGroupId(int uwRejectGroupId) {
        this.uwRejectGroupId = uwRejectGroupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("reasonTypeId", reasonTypeId)
                .append("code", code)
                .append("description", description)
                .append("active", active)
                .append("uwRejectGroupId", uwRejectGroupId)
                .toString();
    }
}
