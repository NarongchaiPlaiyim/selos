package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.view.ProposeCollateralInfoHeadView;
import com.clevel.selos.model.view.ProposeCollateralInfoSubView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CallateralBizTransform extends BusinessTransform {

    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;

    public ProposeCollateralInfoView transformCallteral(AppraisalDataResult appraisalDataResult) {
        ProposeCollateralInfoView collateralDetailView = new ProposeCollateralInfoView();
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
            List<ProposeCollateralInfoHeadView> collateralHeaderDetailViewList = new ArrayList<ProposeCollateralInfoHeadView>();
            if(headCollateralDataList!=null && headCollateralDataList.size()>0){
                for(HeadCollateralData headCollateralData: headCollateralDataList){
                    ProposeCollateralInfoHeadView collateralHeaderDetailView = new ProposeCollateralInfoHeadView();
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
                    List<ProposeCollateralInfoSubView> subCollateralDetailViewList = new ArrayList<ProposeCollateralInfoSubView>();
                    if(subCollateralDataList!=null && subCollateralDataList.size()>0){
                        for(SubCollateralData subCollateralData: subCollateralDataList){
                            ProposeCollateralInfoSubView subCollateralDetailView = new ProposeCollateralInfoSubView();
                            if(collateralType!=null && collateralType.getId()!=0){
                                SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,subCollateralData.getSubCollType());
                                subCollateralDetailView.setSubCollateralType(subCollateralType);
                            }
                            subCollateralDetailView.setAddress(subCollateralData.getAddress());
                            subCollateralDetailView.setTitleDeed(subCollateralData.getTitleDeed());
//                            subCollateralDetailView.setCollateralOwner(subCollateralData.getCollateralOwner());
                            subCollateralDetailView.setAppraisalValue(subCollateralData.getAppraisalValue());
                            //TODO: add field : usage, typeOfUsage
                            subCollateralDetailViewList.add(subCollateralDetailView);
                        }
                    }
                    collateralHeaderDetailView.setProposeCollateralInfoSubViewList(subCollateralDetailViewList);
                    collateralHeaderDetailViewList.add(collateralHeaderDetailView);
                }
            }
            collateralDetailView.setProposeCollateralInfoHeadViewList(collateralHeaderDetailViewList);
        }
        return collateralDetailView;
    }
}
