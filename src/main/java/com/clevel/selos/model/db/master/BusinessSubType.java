package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_business_sub_type")
public class BusinessSubType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "active")
    private int active;
    @Column(name = "code")
    private String code;
    @Column(name = "main_code")
    private String mainCode;
    @Column(name = "extend_code")
    private String extendCode;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public BusinessSubType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getMainCode() {
        return mainCode;
    }

    public void setMainCode(String mainCode) {
        this.mainCode = mainCode;
    }

    public String getExtendCode() {
        return extendCode;
    }

    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("active", active).
                append("code", code).
                append("mainCode", mainCode).
                append("extendCode", extendCode).
                append("name", name).
                append("description", description).
                toString();
    }
}
