package com.clevel.selos.model.db.master;

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

    @Column(name="group",length = 50)
    private String group;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
}
