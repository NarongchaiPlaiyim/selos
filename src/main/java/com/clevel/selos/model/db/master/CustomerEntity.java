package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_customer_entity")
public class CustomerEntity implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "active")
    private int active;
    @Column(name = "brms_code", length = 2)
    private String brmsCode;
    @Column(name = "default_qualitative")
    private int defaultQualitative;
    @Column(name = "change_quali_enable")
    private boolean changeQualtiEnable;


    public CustomerEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public int getDefaultQualitative() {
        return defaultQualitative;
    }

    public void setDefaultQualitative(int defaultQualitative) {
        this.defaultQualitative = defaultQualitative;
    }

    public boolean isChangeQualtiEnable() {
        return changeQualtiEnable;
    }

    public void setChangeQualtiEnable(boolean changeQualtiEnable) {
        this.changeQualtiEnable = changeQualtiEnable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("active", active)
                .append("defaultQualitative", defaultQualitative)
                .append("changeQualtiEnable", changeQualtiEnable)
                .toString();
    }
}
