package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_accountstatus")
public class AccountStatus implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "dbr_flag")
    private int dbrFlag;
    @Column(name = "active")
    private int active;
    @Column(name = "individual_code")
    private int individualCode;
    @Column(name = "juristic_code")
    private int juristicCode;

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

    public int getIndividualCode() {
        return individualCode;
    }

    public void setIndividualCode(int individualCode) {
        this.individualCode = individualCode;
    }

    public int getJuristicCode() {
        return juristicCode;
    }

    public void setJuristicCode(int juristicCode) {
        this.juristicCode = juristicCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("dbrFlag", dbrFlag).
                append("active", active).
                append("individualCode", individualCode).
                append("juristicCode", juristicCode).
                toString();
    }
}
