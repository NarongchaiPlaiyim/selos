package com.clevel.selos.businesscontrol;

import com.clevel.selos.controller.InsuranceInformation;
import com.clevel.selos.dao.master.StepLandingPageDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StepLandingPage;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.model.view.NewCollateralHeadView;
import com.clevel.selos.model.view.NewCollateralView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSectionView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSummaryView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.InsuranceInfoTransform;
import com.clevel.selos.transform.NewCollateralTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import com.clevel.selos.util.Util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Stateless
public class InsuranceInfoControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    BPMInterface bpmInterface;

    @Inject
    NewCollateralDAO newCollateralDAO;
    
    @Inject
    NewCollateralHeadDAO newCollateralHeadDAO;
    
    @Inject
    InsuranceInfoDAO insuranceInfoDAO;
    
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    NewCollateralTransform newCollateralTransform;
    
    @Inject
    InsuranceInfoTransform insuranceInfoTransform;
    
    @Inject
    public InsuranceInfoControl(){

    }
    
    public InsuranceInfoSummaryView getInsuranceInforSummaryView(long workCaseId){
    	InsuranceInfo insuranceInfo = insuranceInfoDAO.findInsuranceInfoByWorkCaseId(workCaseId);
    	InsuranceInfoSummaryView insuranceInfoSummaryView = new InsuranceInfoSummaryView();
    	if (insuranceInfo != null ){
    		insuranceInfoSummaryView = insuranceInfoTransform.transformsInsuranceInfoToView(insuranceInfo);
    	}
    	return insuranceInfoSummaryView;
    }
    
    
    public List<InsuranceInfoView> getInsuranceInfo(long workCaseId){
    	List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateralByTypeA(workCaseId);
    	return insuranceInfoTransform.transformsNewCollateralToView(newCollateralList);
    }    
    
    public void saveInsuranceInfo(List<InsuranceInfoView> insuranceInfoViewList, BigDecimal totalPremiumAmount, long workCaseId){
    	User user = getCurrentUser();
    	for (InsuranceInfoView insuranceInfoView : insuranceInfoViewList){
    		NewCollateral newCollateral = newCollateralDAO.findById(insuranceInfoView.getNewCollateralView().getId());
    		newCollateral.setPremiumAmount(insuranceInfoView.getPremium());
    		newCollateral.setModifyBy(user);
    		newCollateral.setModifyDate(new Date());
    		newCollateralDAO.persist(newCollateral);
    		List<InsuranceInfoSectionView> insuranceInfoSectionViewList = insuranceInfoView.getSectionList();
    		for (InsuranceInfoSectionView infoSectionView : insuranceInfoSectionViewList){
    			NewCollateralHead newCollateralHead = newCollateralHeadDAO.findById(infoSectionView.getNewCollateralHeadView().getId());
    			newCollateralHead.setInsuranceCompany(infoSectionView.getCompany());
    			newCollateralHead.setExistingCredit(infoSectionView.getExistingCredit());
    			newCollateralHead.setModifyBy(user);
    			newCollateralHead.setModifyDate(new Date());
    			newCollateralHeadDAO.persist(newCollateralHead);
    		}
    	}
    	InsuranceInfo insuranceInfo = insuranceInfoDAO.findInsuranceInfoByWorkCaseId(workCaseId);
    	if (insuranceInfo != null){
    		insuranceInfo.setTotalPremiumAmount(totalPremiumAmount);
    		insuranceInfo.setModifyBy(user);
    		insuranceInfo.setModifyDate(new Date());
    		insuranceInfoDAO.persist(insuranceInfo);
    	}else{ // New
    		insuranceInfo = new InsuranceInfo();
    		insuranceInfo.setCreateBy(user);
    		insuranceInfo.setCreateDate(new Date());
    		insuranceInfo.setTotalPremiumAmount(totalPremiumAmount);
    		insuranceInfo.setWorkCase(workCaseDAO.findById(workCaseId));
    		insuranceInfoDAO.persist(insuranceInfo);
    	}
    }
 
}
