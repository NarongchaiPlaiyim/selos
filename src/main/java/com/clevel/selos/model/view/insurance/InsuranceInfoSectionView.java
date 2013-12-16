package com.clevel.selos.model.view.insurance;

import java.io.Serializable;

public class InsuranceInfoSectionView implements Serializable {
    private InsuranceInfoHeadCollView headColl;
    private InsuranceCompanyView insurance;

    public InsuranceInfoSectionView() {
        init();
    }

    private void init(){
        headColl = new InsuranceInfoHeadCollView();
        insurance = new InsuranceCompanyView();
    }

    public InsuranceInfoHeadCollView getHeadColl() {
        return headColl;
    }

    public void setHeadColl(InsuranceInfoHeadCollView headColl) {
        this.headColl = headColl;
    }

    public InsuranceCompanyView getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceCompanyView insurance) {
        this.insurance = insurance;
    }
}
