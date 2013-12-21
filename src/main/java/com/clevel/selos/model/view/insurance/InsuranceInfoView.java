package com.clevel.selos.model.view.insurance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InsuranceInfoView implements Serializable {
    private String jobID;
    private BigDecimal premium;
    private List<InsuranceInfoSectionView> sectionList;

    public InsuranceInfoView() {
        init();
    }

    private void init(){
        premium = BigDecimal.ZERO;
        sectionList = new ArrayList<InsuranceInfoSectionView>();
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public List<InsuranceInfoSectionView> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<InsuranceInfoSectionView> sectionList) {
        this.sectionList = sectionList;
    }
}
