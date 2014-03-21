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

    public List<InsuranceInfoView> transformsNewCollateralToView(List<NewCollateral> newCollateralList) {

    	List<InsuranceInfoView> insuranceInfoViewList = new ArrayList<InsuranceInfoView>();
    	InsuranceInfoView insuranceInfoView;
    	List<NewCollateralView> newCollateralViewList = this.newCollateralTransform.transformsCollateralToView(newCollateralList);
        for (NewCollateralView newCollateralView : newCollateralViewList) {
        	insuranceInfoView = new InsuranceInfoView();
        	insuranceInfoView.setNewCollateralView(newCollateralView);
        	log.debug("transformsNewCollateralToView : newCollateralView id: " + newCollateralView.getId());
        	List<InsuranceInfoSectionView> insuranceInfoSectionViewList = new ArrayList<InsuranceInfoSectionView>();
        	List<NewCollateralHeadView> newCollateralHeadViewList = newCollateralView.getNewCollateralHeadViewList();
        	if (newCollateralHeadViewList != null){
        		for (NewCollateralHeadView newCollateralHeadView : newCollateralHeadViewList){
        			log.debug("transformsNewCollateralToView : newCollateralHead id: " + newCollateralHeadView.getId());
        			log.debug("transformsNewCollateralToView : newCollateralHead title: " + newCollateralHeadView.getTitleDeed());
        			
        			InsuranceInfoSectionView infoSectionView = new InsuranceInfoSectionView();
        			infoSectionView.setNewCollateralHeadView(newCollateralHeadView);
        			insuranceInfoSectionViewList.add(infoSectionView);
        		}
        	}
        	insuranceInfoView.setSectionList(insuranceInfoSectionViewList);
        	insuranceInfoViewList.add(insuranceInfoView);
        }
    	return insuranceInfoViewList;
    }
}
