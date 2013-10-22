package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_bank_account_status")
public class BankAccountStatus implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "code")
    private int code;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private int active;

    @OneToOne
    @JoinColumn(name = "bank_account_type_id")
    private BankAccountType bankAccountType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("code", code)
                .append("description", description)
                .append("active", active)
                .append("bankAccountType", bankAccountType)
                .toString();
    }
}
