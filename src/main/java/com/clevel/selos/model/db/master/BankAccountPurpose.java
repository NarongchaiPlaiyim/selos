package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_bank_account_purpose")
public class BankAccountPurpose implements Serializable {
    private static final long serialVersionUID = 3401143155482524451L;
	@Id
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private int active;
    @Column(name="pledge_default",columnDefinition="int default 0")
    private boolean pledgeDefault;

    public BankAccountPurpose() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public boolean isPledgeDefault() {
		return pledgeDefault;
	}
    public void setPledgeDefault(boolean pledgeDefault) {
		this.pledgeDefault = pledgeDefault;
	}
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("active", active).
                append("pledgeDefault", pledgeDefault).
                toString();
    }
}
