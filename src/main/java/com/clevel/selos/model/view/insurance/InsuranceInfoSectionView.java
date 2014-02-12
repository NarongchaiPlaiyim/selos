package com.clevel.selos.model.view.insurance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.clevel.selos.model.view.NewCollateralHeadView;

public class InsuranceInfoSectionView implements Serializable {
    private NewCollateralHeadView newCollateralHeadView = new NewCollateralHeadView();
    
    public String getTitleDeed() {
        return this.newCollateralHeadView.getTitleDeed();
    }

    public void setTitleDeed(String titleDeed) {
    	this.newCollateralHeadView.setTitleDeed(titleDeed);
    }

    public BigDecimal getExistingCredit() {
        return this.newCollateralHeadView.getExistingCredit();
    }

    public void setExistingCredit(BigDecimal existingCredit) {
        this.newCollateralHeadView.setExistingCredit(existingCredit);
    }
    
    public int getCompany() {
        return this.newCollateralHeadView.getInsuranceCompany();
    }

    public void setCompany(int company) {
        this.newCollateralHeadView.setInsuranceCompany(company);
    }

	public NewCollateralHeadView getNewCollateralHeadView() {
		return newCollateralHeadView;
	}

	public void setNewCollateralHeadView(NewCollateralHeadView newCollateralHeadView) {
		this.newCollateralHeadView = newCollateralHeadView;
	}
}
