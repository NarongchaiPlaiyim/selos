package com.clevel.selos.model.view.insurance.model;

import java.io.Serializable;

public class InsuranceModel implements Serializable {
    private int company;

    public InsuranceModel() {

    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }
}
