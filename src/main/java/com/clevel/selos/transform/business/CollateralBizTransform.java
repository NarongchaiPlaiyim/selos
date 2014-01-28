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

public class CollateralBizTransform extends BusinessTransform {
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;

    private NewCollateralView newCollateralView;

    private List<NewCollateralHeadView> newCollateralHeadViewList;
    private NewCollateralHeadView newCollateralHeadView;

    private List<NewCollateralSubView> newCollateralSubViewList;
    private NewCollateralSubView newCollateralSubView;
    @Inject
    public CollateralBizTransform() {

    }

    public NewCollateralView transformCollteral(AppraisalDataResult appraisalDataResult) {
        newCollateralView = new NewCollateralView();
        if(appraisalDataResult!=null){
            AppraisalData appraisalData = appraisalDataResult.getAppraisalData();
            newCollateralView.setJobID(appraisalData.getJobId());
            newCollateralView.setAppraisalDate(appraisalData.getAppraisalDate());
            newCollateralView.setAadDecision(appraisalData.getAadDecision());
            newCollateralView.setAadDecisionReason(appraisalData.getAadDecisionReason());
            newCollateralView.setAadDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
            newCollateralView.setMortgageCondition(appraisalData.getMortgageCondition());
            newCollateralView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

            List<HeadCollateralData> headCollateralDataList = appraisalData.getHeadCollateralDataList();
            newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
            if(headCollateralDataList!=null && headCollateralDataList.size()>0){
                for(HeadCollateralData headCollateralData: headCollateralDataList){
                    newCollateralHeadView = new NewCollateralHeadView();
                    newCollateralHeadView.setTitleDeed(headCollateralData.getTitleDeed());
                    newCollateralHeadView.setCollateralLocation(headCollateralData.getCollateralLocation());
                    newCollateralHeadView.setAppraisalValue(headCollateralData.getAppraisalValue());
                    CollateralType collateralType = collateralTypeDAO.findByCollateralCode(headCollateralData.getHeadCollType());
                    newCollateralHeadView.setHeadCollType(collateralType);
                    if(collateralType!=null && collateralType.getId()!=0){
                        SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,headCollateralData.getSubCollType());
                    }
                    //TODO: add field : subCollType

                    List<SubCollateralData> subCollateralDataList = headCollateralData.getSubCollateralDataList();
                    newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
                    if(subCollateralDataList!=null && subCollateralDataList.size()>0){
                        for(SubCollateralData subCollateralData: subCollateralDataList){
                            newCollateralSubView = new NewCollateralSubView();
                            if(collateralType!=null && collateralType.getId()!=0){
                                SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,subCollateralData.getSubCollType());
                                newCollateralSubView.setSubCollateralType(subCollateralType);
                            }
                            newCollateralSubView.setAddress(subCollateralData.getAddress());
                            newCollateralSubView.setTitleDeed(subCollateralData.getTitleDeed());
                            newCollateralSubView.setCollateralOwner(subCollateralData.getCollateralOwner());
                            newCollateralSubView.setAppraisalValue(subCollateralData.getAppraisalValue());
                            //TODO: add field : usage, typeOfUsage
                            newCollateralSubViewList.add(newCollateralSubView);
                        }
                    }
                    newCollateralHeadView.setNewCollateralSubViewList(newCollateralSubViewList);
                    newCollateralHeadViewList.add(newCollateralHeadView);
                }
            }
            newCollateralView.setNewCollateralHeadViewList(newCollateralHeadViewList);
        }
        return newCollateralView;
    }
}
