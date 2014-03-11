package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_fee_type")
public class FeeType {
    @Id
    @Column(name = "id")
    private long id;

    @Column (name = "brms_code")
    private String brmsCode;

    @Column (name = "description", length = 200)
    private String description;

    @Column (name = "frontend_fee", length = 1, columnDefinition = "int default 0")
    private boolean frontend;

    @Column (name = "active")
    private int active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFrontend() {
        return frontend;
    }

    public void setFrontend(boolean frontend) {
        this.frontend = frontend;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("brmsCode", brmsCode)
                .append("description", description)
                .append("active", active)
                .toString();
    }
}
