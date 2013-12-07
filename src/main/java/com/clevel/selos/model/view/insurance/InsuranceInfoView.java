package com.clevel.selos.model.view.insurance;

import java.io.Serializable;

public class InsuranceInfoView implements Serializable {
    private InsuranceInfoDetailView insuranceInfoDetailView;

    public InsuranceInfoView() {
        init();
    }

    private void init(){
        insuranceInfoDetailView = new InsuranceInfoDetailView();
    }

    public InsuranceInfoDetailView getInsuranceInfoDetailView() {
        return insuranceInfoDetailView;
    }

    public void setInsuranceInfoDetailView(InsuranceInfoDetailView insuranceInfoDetailView) {
        this.insuranceInfoDetailView = insuranceInfoDetailView;
    }
}
