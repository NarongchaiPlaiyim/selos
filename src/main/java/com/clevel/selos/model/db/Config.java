package com.clevel.selos.model.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_CONFIG")
public class Config {
    @Id
    @Column(name = "CONFIG_NAME")
    private String name;
    @Column(name = "CONFIG_VALUE")
    private String value;
    @Column(name = "CONFIG_DESC")
    private String description;
    @Column(name = "CONFIG_ENABLE")
    private int enable;

    public Config() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("name", name).
                append("value", value).
                append("description", description).
                append("enable", enable).
                toString();
    }
}