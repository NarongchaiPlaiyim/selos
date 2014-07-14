package com.clevel.selos.model.view.insurance;

import com.clevel.selos.model.view.ProposeCollateralInfoHeadView;

import java.io.Serializable;
import java.math.BigDecimal;

public class InsuranceInfoSectionView implements Serializable {
    private ProposeCollateralInfoHeadView newCollateralHeadView = new ProposeCollateralInfoHeadView();
    
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

	public ProposeCollateralInfoHeadView getNewCollateralHeadView() {
		return newCollateralHeadView;
	}

	public void setNewCollateralHeadView(ProposeCollateralInfoHeadView newCollateralHeadView) {
		this.newCollateralHeadView = newCollateralHeadView;
	}
}
