package com.clevel.selos.model.view.insurance;

import java.io.Serializable;

public class InsuranceCompanyView implements Serializable {
    private int company;

    public InsuranceCompanyView() {

    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }
}
