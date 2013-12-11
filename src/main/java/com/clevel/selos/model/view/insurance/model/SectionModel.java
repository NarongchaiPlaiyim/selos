package com.clevel.selos.model.view.insurance.model;

import java.io.Serializable;

public class SectionModel implements Serializable {
    private HeadCollModel headColl;
    private InsuranceModel insurance;

    public SectionModel() {
        init();
    }

    private void init(){
        headColl = new HeadCollModel();
        insurance = new InsuranceModel();
    }

    public HeadCollModel getHeadColl() {
        return headColl;
    }

    public void setHeadColl(HeadCollModel headColl) {
        this.headColl = headColl;
    }

    public InsuranceModel getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceModel insurance) {
        this.insurance = insurance;
    }
}
