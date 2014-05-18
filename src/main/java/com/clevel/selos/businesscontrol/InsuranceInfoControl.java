package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.InsuranceInfoDAO;
import com.clevel.selos.dao.working.NewCollateralDAO;
import com.clevel.selos.dao.working.NewCollateralHeadDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.InsuranceInfo;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.view.insurance.InsuranceInfoSectionView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.transform.InsuranceInfoTransform;
import com.clevel.selos.transform.NewCollateralTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
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
    	List<InsuranceInfo> insuranceInfoList = insuranceInfoDAO.findInsuranceInfoByWorkCaseId(workCaseId);
    	if (insuranceInfoList.size() > 0){
    		insuranceInfoList.get(0).setTotalPremiumAmount(totalPremiumAmount);
    		insuranceInfoList.get(0).setModifyBy(user);
    		insuranceInfoList.get(0).setModifyDate(new Date());
    		insuranceInfoDAO.persist(insuranceInfoList.get(0));
    	}else{ // New
    		InsuranceInfo insuranceInfo = new InsuranceInfo();
    		insuranceInfo.setCreateBy(user);
    		insuranceInfo.setCreateDate(new Date());
    		insuranceInfo.setTotalPremiumAmount(totalPremiumAmount);
    		insuranceInfo.setWorkCase(workCaseDAO.findById(workCaseId));
    		insuranceInfoDAO.persist(insuranceInfo);
    	}
    }
 
}
