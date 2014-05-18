package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.WarningCode;
import com.clevel.selos.model.db.working.Customer;

import java.io.Serializable;

public class CustomerCSIView implements Serializable {
    private long id;
    Customer customer;
    WarningCode warningCode;
    String warningDate;
    String matchedType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WarningCode getWarningCode() {
        return warningCode;
    }

    public void setWarningCode(WarningCode warningCode) {
        this.warningCode = warningCode;
    }

    public String getWarningDate() {
        return warningDate;
    }

    public void setWarningDate(String warningDate) {
        this.warningDate = warningDate;
    }

    public String getMatchedType() {
        return matchedType;
    }

    public void setMatchedType(String matchedType) {
        this.matchedType = matchedType;
    }
}
