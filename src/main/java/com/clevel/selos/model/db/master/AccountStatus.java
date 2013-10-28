package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_account_status")
public class AccountStatus implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "dbr_flag")
    private int dbrFlag;
    @Column(name = "active")
    private int active;
    @Column(name = "ncb_code_ind", length = 5)
    private String ncbCodeInd;
    @Column(name = "ncb_code_jur", length = 5)
    private String ncbCodeJur;

    public AccountStatus() {
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

    public int getDbrFlag() {
        return dbrFlag;
    }

    public void setDbrFlag(int dbrFlag) {
        this.dbrFlag = dbrFlag;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getNcbCodeInd() {
        return ncbCodeInd;
    }

    public void setNcbCodeInd(String ncbCodeInd) {
        this.ncbCodeInd = ncbCodeInd;
    }

    public String getNcbCodeJur() {
        return ncbCodeJur;
    }

    public void setNcbCodeJur(String ncbCodeJur) {
        this.ncbCodeJur = ncbCodeJur;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("dbrFlag", dbrFlag).
                append("active", active).
                append("ncbCodeInd", active).
                append("ncbCodeJur", active).
                toString();
    }
}
