package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.view.NewCollateralHeadDetailView;
import com.clevel.selos.model.view.NewCollateralInfoView;
import com.clevel.selos.model.view.NewSubCollateralDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CollateralBizProposeTransform extends BusinessTransform {

    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;

    public NewCollateralInfoView transformCollateral(AppraisalDataResult appraisalDataResult) {
        NewCollateralInfoView collateralDetailView = new NewCollateralInfoView();
        if(appraisalDataResult!=null){
            AppraisalData appraisalData = appraisalDataResult.getAppraisalData();
            collateralDetailView.setJobID(appraisalData.getJobId());
            collateralDetailView.setAppraisalDate(appraisalData.getAppraisalDate());
            collateralDetailView.setAadDecision(appraisalData.getAadDecision());
            collateralDetailView.setAadDecisionReason(appraisalData.getAadDecisionReason());
            collateralDetailView.setAadDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
            collateralDetailView.setMortgageCondition(appraisalData.getMortgageCondition());
            collateralDetailView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

            List<HeadCollateralData> headCollateralDataList = appraisalData.getHeadCollateralDataList();
            List<NewCollateralHeadDetailView> collateralHeaderDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
            if(headCollateralDataList!=null && headCollateralDataList.size()>0){
                for(HeadCollateralData headCollateralData: headCollateralDataList){
                    NewCollateralHeadDetailView collateralHeaderDetailView = new NewCollateralHeadDetailView();
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
                    List<NewSubCollateralDetailView> subCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
                    if(subCollateralDataList!=null && subCollateralDataList.size()>0){
                        for(SubCollateralData subCollateralData: subCollateralDataList){
                            NewSubCollateralDetailView subCollateralDetailView = new NewSubCollateralDetailView();
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
                    collateralHeaderDetailView.setNewSubCollateralDetailViewList(subCollateralDetailViewList);
                    collateralHeaderDetailViewList.add(collateralHeaderDetailView);
                }
            }
            collateralDetailView.setNewCollateralHeadDetailViewList(collateralHeaderDetailViewList);
        }
        return collateralDetailView;
    }
}
