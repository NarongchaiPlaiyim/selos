package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_warning_code")
public class WarningCode implements Serializable {
    @Id
    @Column(name="id")
    private int id;

    @Column(name="code",length = 4)
    private String code;

    @Column(name="warning_group",length = 50)
    private String warningGroup;

    @Column(name="definition_en",length = 100)
    private String definitionEn;

    @Column(name="definition_th",length = 100)
    private String definitionTh;

    @Column(name="active")
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWarningGroup() {
        return warningGroup;
    }

    public void setWarningGroup(String warningGroup) {
        this.warningGroup = warningGroup;
    }

    public String getDefinitionEn() {
        return definitionEn;
    }

    public void setDefinitionEn(String definitionEn) {
        this.definitionEn = definitionEn;
    }

    public String getDefinitionTh() {
        return definitionTh;
    }

    public void setDefinitionTh(String definitionTh) {
        this.definitionTh = definitionTh;
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
                .append("code", code)
                .append("warningGroup", warningGroup)
                .append("definitionEn", definitionEn)
                .append("definitionTh", definitionTh)
                .append("active", active)
                .toString();
    }
}
