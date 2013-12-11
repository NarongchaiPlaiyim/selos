package com.clevel.selos.model.view.insurance;

import com.clevel.selos.model.view.insurance.model.SectionModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InsuranceInfoView implements Serializable {
    private String jobID;
    private BigDecimal premium;
    private List<SectionModel> sectionList;

    public InsuranceInfoView() {
        init();
    }

    private void init(){
        premium = BigDecimal.ZERO;
        sectionList = new ArrayList<SectionModel>();
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

    public List<SectionModel> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionModel> sectionList) {
        this.sectionList = sectionList;
    }
}
