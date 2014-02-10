package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.MortgageTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.insurance.InsuranceCompanyView;
import com.clevel.selos.model.view.insurance.InsuranceInfoHeadCollView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSectionView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.util.Util;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsuranceInfoTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    
    @Inject
    private NewCollateralTransform newCollateralTransform;
    
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    
    private List<InsuranceInfoView> insuranceInfoViewList;

    public List<InsuranceInfoView> transformsNewCollateralToView(List<NewCollateral> newCollateralList) {

    	List<InsuranceInfoView> insuranceInfoViewList = new ArrayList<InsuranceInfoView>();
    	InsuranceInfoView insuranceInfoView;

        for (NewCollateral newCollateral : newCollateralList) {
        	insuranceInfoView = new InsuranceInfoView();
        	insuranceInfoView.setNewCollateralView(this.newCollateralTransform.transformsCollateralToView(newCollateral));
        	//insuranceInfoView.setJobID(newCollateral.getJobID());
        	//insuranceInfoView.setPremium(newCollateral.getPremiumAmount());
        	log.debug("transformsNewCollateralToView : newCollateral id: " + newCollateral.getId());
        	List<NewCollateralHead> newCollateralHeadList = newCollateral.getNewCollateralHeadList();
        	List<InsuranceInfoSectionView> insuranceInfoSectionViewList = new ArrayList<InsuranceInfoSectionView>();
        	List<NewCollateralHeadView> newCollateralHeadViewList = this.newCollateralHeadTransform.transformToView(newCollateralHeadList);
        	
        	if (newCollateralHeadViewList != null){
        		for (NewCollateralHeadView newCollateralHeadView : newCollateralHeadViewList){
        			log.debug("transformsNewCollateralToView : newCollateralHead id: " + newCollateralHeadView.getId());
        			log.debug("transformsNewCollateralToView : newCollateralHead title: " + newCollateralHeadView.getTitleDeed());
        			
        			InsuranceInfoSectionView infoSectionView = new InsuranceInfoSectionView();
        			infoSectionView.setNewCollateralHeadView(newCollateralHeadView);
        			//infoSectionView.setHeadColl(this.transformNewCollateralHeadViewtoHeadColView(newCollateralHead));
        			//infoSectionView.setInsurance(this.transformNewCollateralHeadViewtoCompanyView(newCollateralHead));
        			insuranceInfoSectionViewList.add(infoSectionView);
        		}
        	}
        	insuranceInfoView.setSectionList(insuranceInfoSectionViewList);
        	insuranceInfoViewList.add(insuranceInfoView);
        }
    	return insuranceInfoViewList;
    }
    
    /*private InsuranceCompanyView transformNewCollateralHeadViewtoCompanyView(NewCollateralHead newCollateralHead){
    	InsuranceCompanyView insuranceCompanyView = new InsuranceCompanyView();
		insuranceCompanyView.setCompany(newCollateralHead.getInsuranceCompany());
		return insuranceCompanyView;
    }
    
    private InsuranceInfoHeadCollView transformNewCollateralHeadViewtoHeadColView(NewCollateralHead newCollateralHead){
    	InsuranceInfoHeadCollView infoHeadCollView = new InsuranceInfoHeadCollView();
    	infoHeadCollView.setTitleDeed(newCollateralHead.getTitleDeed());
		infoHeadCollView.setExitingCredit(newCollateralHead.getExistingCredit());
    	return infoHeadCollView;
    }*/
}
