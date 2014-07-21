package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_reason")
public class Reason implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "reasontype_id")
    private ReasonType reasonType;

    @Column(name = "code")
    private String code;

    @Column(name = "description", length = 400)
    private String description;

    @Column(name = "active")
    private int active;

    @OneToOne
    @JoinColumn(name = "reject_group_id")
    private UWRejectGroup uwRejectGroup;

    public Reason() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReasonType getReasonType() {
        return reasonType;
    }

    public void setReasonType(ReasonType reasonType) {
        this.reasonType = reasonType;
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

    public UWRejectGroup getUwRejectGroup() {
        return uwRejectGroup;
    }

    public void setUwRejectGroup(UWRejectGroup uwRejectGroup) {
        this.uwRejectGroup = uwRejectGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("reasonType", reasonType)
                .append("code", code)
                .append("description", description)
                .append("active", active)
                .append("uwRejectGroup", uwRejectGroup)
                .toString();
    }
}
