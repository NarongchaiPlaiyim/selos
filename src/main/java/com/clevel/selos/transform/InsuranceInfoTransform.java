package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.InsuranceInfo;
import com.clevel.selos.model.db.working.ProposeCollateralInfo;
import com.clevel.selos.model.view.ProposeCollateralInfoHeadView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSectionView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSummaryView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InsuranceInfoTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    
    @Inject
    private ProposeLineTransform proposeLineTransform;

    public List<InsuranceInfoView> transformsNewCollateralToView(List<ProposeCollateralInfo> newCollateralList) {

    	List<InsuranceInfoView> insuranceInfoViewList = new ArrayList<InsuranceInfoView>();
    	InsuranceInfoView insuranceInfoView;
    	List<ProposeCollateralInfoView> newCollateralViewList = this.proposeLineTransform.transformProposeCollateralToViewList(newCollateralList, ProposeType.NA);
        for (ProposeCollateralInfoView newCollateralView : newCollateralViewList) {
        	insuranceInfoView = new InsuranceInfoView();
        	insuranceInfoView.setNewCollateralView(newCollateralView);
        	log.debug("transformsNewCollateralToView : newCollateralView id: " + newCollateralView.getId());
        	List<InsuranceInfoSectionView> insuranceInfoSectionViewList = new ArrayList<InsuranceInfoSectionView>();
        	List<ProposeCollateralInfoHeadView> newCollateralHeadViewList = newCollateralView.getProposeCollateralInfoHeadViewList();
        	if (newCollateralHeadViewList != null){
        		for (ProposeCollateralInfoHeadView newCollateralHeadView : newCollateralHeadViewList){
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
    
    public InsuranceInfoSummaryView transformsInsuranceInfoToView(InsuranceInfo insuranceInfo){
    	InsuranceInfoSummaryView infoSummaryView = new InsuranceInfoSummaryView();
    	infoSummaryView.setId(insuranceInfo.getId());
    	infoSummaryView.setTotalPremiumAmount(insuranceInfo.getTotalPremiumAmount());
    	infoSummaryView.setModifyBy(insuranceInfo.getModifyBy());
    	infoSummaryView.setModifyDate(insuranceInfo.getModifyDate());
    	return infoSummaryView;
    	
    }
}
