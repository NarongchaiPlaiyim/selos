package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_fee_payment_method")
public class FeePaymentMethod implements Serializable{

    private static final long serialVersionUID = -3283807845532052122L;

	@Id
    @Column(name = "id")
    private int id;

    @Column (name = "brms_code")
    private String brmsCode;

    @Column (name = "description", length = 200)
    private String description;

    @Column (name = "active")
    private int active;
    
    @Column (name="include_in_agreementsign",columnDefinition="int default 0")
    private boolean includeInAgreementSign;
    
    @Column (name="debit_from_customer",columnDefinition="int default 0")
    private boolean debitFromCustomer;
    
    public FeePaymentMethod(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
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
    
    public boolean isIncludeInAgreementSign() {
		return includeInAgreementSign;
	}
    public void setIncludeInAgreementSign(boolean includeInAgreementSign) {
		this.includeInAgreementSign = includeInAgreementSign;
	}
    public boolean isDebitFromCustomer() {
		return debitFromCustomer;
	}
    public void setDebitFromCustomer(boolean debitFromCustomer) {
		this.debitFromCustomer = debitFromCustomer;
	}
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("brmsCode", brmsCode)
                .append("description", description)
                .append("active", active)
                .append("includeInAgreementSign",includeInAgreementSign)
                .append("debitFromCustomer",debitFromCustomer)
                .toString();
    }
}
