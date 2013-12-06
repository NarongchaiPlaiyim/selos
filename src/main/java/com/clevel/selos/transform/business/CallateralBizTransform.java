package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.view.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CallateralBizTransform extends BusinessTransform {

    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;

    public CollateralDetailView transformCallteral(AppraisalDataResult appraisalDataResult) {
        CollateralDetailView collateralDetailView = new CollateralDetailView();
        if(appraisalDataResult!=null){
            AppraisalData appraisalData = appraisalDataResult.getAppraisalData();
            collateralDetailView.setJobID(appraisalData.getJobId());
            collateralDetailView.setAppraisalDate(appraisalData.getAppraisalDate());
            collateralDetailView.setAADDecision(appraisalData.getAadDecision());
            collateralDetailView.setAADDecisionReason(appraisalData.getAadDecisionReason());
            collateralDetailView.setAADDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
            collateralDetailView.setMortgageCondition(appraisalData.getMortgageCondition());
            collateralDetailView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

            List<HeadCollateralData> headCollateralDataList = appraisalData.getHeadCollateralDataList();
            List<CollateralHeaderDetailView> collateralHeaderDetailViewList = new ArrayList<CollateralHeaderDetailView>();
            if(headCollateralDataList!=null && headCollateralDataList.size()>0){
                for(HeadCollateralData headCollateralData: headCollateralDataList){
                    CollateralHeaderDetailView collateralHeaderDetailView = new CollateralHeaderDetailView();
                    collateralHeaderDetailView.setTitleDeed(headCollateralData.getTitleDeed());
                    collateralHeaderDetailView.setCollateralLocation(headCollateralData.getCollateralLocation());
                    collateralHeaderDetailView.setAppraisalValue(headCollateralData.getAppraisalValue());
                    CollateralType collateralType = collateralTypeDAO.findByCollateralCode(headCollateralData.getHeadCollType());
                    collateralHeaderDetailView.setHeadCollType(collateralType);
                    if(collateralType!=null && collateralType.getId()!=0){
                        SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,headCollateralData.getSubCollType());
                    }
                    //TODO: add field : subCollType

                    List<SubCollateralData> subCollateralDataList = headCollateralData.getSubCollateralDataList();
                    List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();
                    if(subCollateralDataList!=null && subCollateralDataList.size()>0){
                        for(SubCollateralData subCollateralData: subCollateralDataList){
                            SubCollateralDetailView subCollateralDetailView = new SubCollateralDetailView();
                            if(collateralType!=null && collateralType.getId()!=0){
                                SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,subCollateralData.getSubCollType());
                                subCollateralDetailView.setSubCollateralType(subCollateralType);
                            }
                            subCollateralDetailView.setAddress(subCollateralData.getAddress());
                            subCollateralDetailView.setTitleDeed(subCollateralData.getTitleDeed());
                            subCollateralDetailView.setCollateralOwner(subCollateralData.getCollateralOwner());
                            subCollateralDetailView.setAppraisalValue(subCollateralData.getAppraisalValue());
                            //TODO: add field : usage, typeOfUsage
                            subCollateralDetailViewList.add(subCollateralDetailView);
                        }
                    }
                    collateralHeaderDetailView.setSubCollateralDetailViewList(subCollateralDetailViewList);
                    collateralHeaderDetailViewList.add(collateralHeaderDetailView);
                }
            }
            collateralDetailView.setCollateralHeaderDetailViewList(collateralHeaderDetailViewList);
        }
        return collateralDetailView;
    }
}
