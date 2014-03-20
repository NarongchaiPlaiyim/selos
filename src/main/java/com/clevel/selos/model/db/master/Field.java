package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_field")
public class Field {
    @Id
    @Column(name = "id")
    long id;
    @Column(name = "name_en")
    String nameEn;
    @Column(name = "name_th")
    String nameTh;
    @Column(name = "sys_ref_name")
    String sysRefName;
    @Column(name = "active")
    int active;

    public Field() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public String getSysRefName() {
        return sysRefName;
    }

    public void setSysRefName(String sysRefName) {
        this.sysRefName = sysRefName;
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
                .append("nameEn", nameEn)
                .append("nameTh", nameTh)
                .append("sysRefName", sysRefName)
                .append("active", active)
                .toString();
    }
}
