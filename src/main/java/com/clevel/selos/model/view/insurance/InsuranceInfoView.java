package com.clevel.selos.model.view.insurance;

import com.clevel.selos.model.view.ProposeCollateralInfoView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InsuranceInfoView implements Serializable {
    private List<InsuranceInfoSectionView> sectionList;
    private ProposeCollateralInfoView newCollateralView;

    public InsuranceInfoView() {
        init();
    }

    private void init(){
        sectionList = new ArrayList<InsuranceInfoSectionView>();
    }

    public String getJobID() {
        return getNewCollateralView().getJobID();
    }

    public void setJobID(String jobID) {
        getNewCollateralView().setJobID(jobID);
    }

    public BigDecimal getPremium() {
        return getNewCollateralView().getPremiumAmount();
    }

    public void setPremium(BigDecimal premiumAmount) {
        getNewCollateralView().setPremiumAmount(premiumAmount);
    }

    public List<InsuranceInfoSectionView> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<InsuranceInfoSectionView> sectionList) {
        this.sectionList = sectionList;
    }

	public ProposeCollateralInfoView getNewCollateralView() {
		return newCollateralView;
	}

	public void setNewCollateralView(ProposeCollateralInfoView newCollateralView) {
		this.newCollateralView = newCollateralView;
	}
}
