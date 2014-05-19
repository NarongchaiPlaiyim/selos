package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.PreDisbursementData;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.PreDisbursementDetail;
import com.clevel.selos.model.view.PreDisbursementDetailView;
import com.clevel.selos.model.view.PreDisbursementView;

import java.util.ArrayList;
import java.util.List;

public class PreDisbursementTransform extends Transform {

    public PreDisbursementView transformToPreDisbursementView(PreDisbursement preDisbursement){
    	PreDisbursementView preDisbursementView = new PreDisbursementView();
    	preDisbursementView.setRemark(preDisbursement.getRemark());
		return preDisbursementView;
    }

    public List<PreDisbursementDetailView> transformPreDisbursementDataList(String groupName, List<PreDisbursementData> preDisbursementDataList){
    	List<PreDisbursementDetailView> preDisbursementDetailViewList = new ArrayList<PreDisbursementDetailView>();
    	
    	for (PreDisbursementData preDisbursementData : preDisbursementDataList){
    		PreDisbursementDetailView preDisbursementDetailView = new PreDisbursementDetailView();
    		preDisbursementDetailView.setDescription(preDisbursementData.getDescription());
    		preDisbursementDetailView.setPreDisbursementData_id(preDisbursementData.getId());
    		preDisbursementDetailViewList.add(preDisbursementDetailView);
    	}
    	return preDisbursementDetailViewList;
    }
    
    public List<PreDisbursementDetailView> transformPreDisbursementDetailList(String groupName, List<PreDisbursementDetail> preDisbursementDetailList){
    	List<PreDisbursementDetailView> preDisbursementDetailViewList = new ArrayList<PreDisbursementDetailView>();
    	for (PreDisbursementDetail preDisbursementDetail : preDisbursementDetailList){
    		PreDisbursementDetailView preDisbursementDetailView = new PreDisbursementDetailView();
    		if (groupName.equals(preDisbursementDetail.getPreDisbursementData().getGroupName())){
    			//preDisbursementDetails.add(preDisbursementDetail);
    			preDisbursementDetailView.setId(preDisbursementDetail.getId());
    			preDisbursementDetailView.setDescription(preDisbursementDetail.getPreDisbursementData().getDescription());
    			preDisbursementDetailView.setValue(preDisbursementDetail.getValue());
    			preDisbursementDetailView.setPreDisbursementData_id(preDisbursementDetail.getPreDisbursementData().getId());
    			preDisbursementDetailView.setPreDisbursement_id(preDisbursementDetail.getPreDisbursement().getId());
    			preDisbursementDetailView.setSubmission_date(preDisbursementDetail.getSubmission_date());
    			preDisbursementDetailViewList.add(preDisbursementDetailView);
    		}
    	}    	
    	return preDisbursementDetailViewList;
    }

}
