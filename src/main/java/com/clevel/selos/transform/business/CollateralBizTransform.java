package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.view.ProposeCollateralInfoHeadView;
import com.clevel.selos.model.view.ProposeCollateralInfoSubView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CollateralBizTransform extends BusinessTransform {
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    @SELOS
    private Logger log;
    /*private NewCollateralView newCollateralView;

    private List<NewCollateralHeadView> newCollateralHeadViewList;
    private NewCollateralHeadView newCollateralHeadView;

    private List<NewCollateralSubView> newCollateralSubViewList;
    private NewCollateralSubView newCollateralSubView;*/

    @Inject
    private AppraisalData appraisalData;
    @Inject
    public CollateralBizTransform() {

    }

    /*public NewCollateralView transformCollateral(AppraisalDataResult appraisalDataResult) {
        log.debug("-- transformCollateral");
        if(!Util.isNull(appraisalDataResult)){
            AppraisalData appraisalData = appraisalDataResult.getAppraisalData();
            newCollateralView = new NewCollateralView();
            newCollateralView.setJobID(appraisalData.getJobId());
            log.debug("-- NewCollateralView.jobId[{}]", newCollateralView.getJobID());
            newCollateralView.setAppraisalDate(appraisalData.getAppraisalDate());
            newCollateralView.setAadDecision(appraisalData.getAadDecision());
            newCollateralView.setAadDecisionReason(appraisalData.getAadDecisionReason());
            newCollateralView.setAadDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
            newCollateralView.setMortgageCondition(appraisalData.getMortgageCondition());
            newCollateralView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

            List<HeadCollateralData> headCollateralDataList = appraisalData.getHeadCollateralDataList();
            newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
            if(!Util.isNull(headCollateralDataList) && headCollateralDataList.size()>0){
                for(HeadCollateralData headCollateralData: headCollateralDataList){
                    newCollateralHeadView = new NewCollateralHeadView();
                    newCollateralHeadView.setTitleDeed(headCollateralData.getTitleDeed());
                    newCollateralHeadView.setCollateralLocation(headCollateralData.getCollateralLocation());
                    newCollateralHeadView.setAppraisalValue(headCollateralData.getAppraisalValue());
                    CollateralType collateralType = collateralTypeDAO.findByCollateralCode(headCollateralData.getHeadCollType());
                    newCollateralHeadView.setHeadCollType(collateralType);
                    if(!Util.isNull(collateralType) && !Util.isZero(collateralType.getId())){
                        SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,headCollateralData.getSubCollType());
                        log.debug("-- SubCollateralType.id[{}]", subCollateralType.getId());
                        newCollateralHeadView.setSubCollType(subCollateralType);
                    }
                    //TODO: add field : subCollType  [HEAD]

                    List<SubCollateralData> subCollateralDataList = headCollateralData.getSubCollateralDataList();
                    newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
                    if(!Util.isNull(subCollateralDataList) && subCollateralDataList.size()>0){
                        for(SubCollateralData subCollateralData: subCollateralDataList){
                            newCollateralSubView = new NewCollateralSubView();
                            CollateralType collateralTypeSub = collateralTypeDAO.findByCollateralCode(subCollateralData.getHeadCollType());
                            if(!Util.isNull(collateralTypeSub) && !Util.isZero(collateralTypeSub.getId())){
                                SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralTypeSub,subCollateralData.getSubCollType());
                                newCollateralSubView.setSubCollateralType(subCollateralType);
                                newCollateralSubView.setHeadCollType(collateralTypeSub);
                            }
                            newCollateralSubView.setAddress(subCollateralData.getAddress());
                            newCollateralSubView.setTitleDeed(subCollateralData.getTitleDeed());
                            newCollateralSubView.setCollateralOwner(subCollateralData.getCollateralOwner());
                            newCollateralSubView.setAppraisalValue(subCollateralData.getAppraisalValue());
                            newCollateralSubView.setUsage(subCollateralData.getUsage());
                            newCollateralSubView.setTypeOfUsage(subCollateralData.getTypeOfUsage());
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
    }*/

    public ProposeCollateralInfoView transformAppraisalToProposeCollateralView(AppraisalDataResult appraisalDataResult) {
        ProposeCollateralInfoView proposeCollateralInfoView = new ProposeCollateralInfoView();
        if(!Util.isNull(appraisalDataResult)){
            AppraisalData appraisalData = appraisalDataResult.getAppraisalData();
            proposeCollateralInfoView.setJobID(appraisalData.getJobId());
            proposeCollateralInfoView.setAppraisalDate(appraisalData.getAppraisalDate());
            proposeCollateralInfoView.setAadDecision(appraisalData.getAadDecision());
            proposeCollateralInfoView.setAadDecisionReason(appraisalData.getAadDecisionReason());
            proposeCollateralInfoView.setAadDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
            proposeCollateralInfoView.setMortgageCondition(appraisalData.getMortgageCondition());
            proposeCollateralInfoView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

            List<HeadCollateralData> headCollateralDataList = appraisalData.getHeadCollateralDataList();
            List<ProposeCollateralInfoHeadView> proposeCollateralInfoHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();
            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
            if(Util.isSafetyList(headCollateralDataList)){
                for(HeadCollateralData headCollateralData: headCollateralDataList){
                    ProposeCollateralInfoHeadView proposeCollateralInfoHeadView = new ProposeCollateralInfoHeadView();
                    proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
                    proposeCollateralInfoHeadView.setTitleDeed(headCollateralData.getTitleDeed());
                    proposeCollateralInfoHeadView.setCollateralLocation(headCollateralData.getCollateralLocation());
                    proposeCollateralInfoHeadView.setAppraisalValue(headCollateralData.getAppraisalValue());

                    CollateralType collateralType = collateralTypeDAO.findByCollateralCode(headCollateralData.getHeadCollType());
                    proposeCollateralInfoHeadView.setHeadCollType(collateralType);

                    /*if(!Util.isNull(collateralType) && !Util.isZero(collateralType.getId())){
                        SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralType,headCollateralData.getSubCollType());
                        newCollateralHeadView.setSubCollType(subCollateralType);
                    }*/

                    List<SubCollateralData> subCollateralDataList = headCollateralData.getSubCollateralDataList();
                    if(Util.isSafetyList(subCollateralDataList)){
                        for(SubCollateralData subCollateralData: subCollateralDataList){
                            ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
                            CollateralType collateralTypeSub = collateralTypeDAO.findByCollateralCode(subCollateralData.getHeadCollType());
                            if(!Util.isNull(collateralTypeSub) && !Util.isZero(collateralTypeSub.getId())){
                                SubCollateralType subCollateralType = subCollateralTypeDAO.findByHeadAndSubColCode(collateralTypeSub,subCollateralData.getSubCollType());
                                proposeCollateralInfoSubView.setSubCollateralType(subCollateralType);
                            }
                            proposeCollateralInfoSubView.setAddress(subCollateralData.getAddress());
                            proposeCollateralInfoSubView.setTitleDeed(subCollateralData.getTitleDeed());
                            proposeCollateralInfoSubView.setAppraisalValue(subCollateralData.getAppraisalValue());
//                            proposeCollateralInfoSubView.setCollateralOwner(subCollateralData.getCollateralOwner());
//                            proposeCollateralInfoSubView.setUsage(subCollateralData.getUsage());
//                            proposeCollateralInfoSubView.setTypeOfUsage(subCollateralData.getTypeOfUsage());
                            UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
                            proposeCollateralInfoSubView.setSubId(uid.randomUUID().toString());
                            proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
                        }
                    }
                    proposeCollateralInfoHeadView.setProposeCollateralInfoSubViewList(proposeCollateralInfoSubViewList);
                    if(!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())){
                        proposeCollateralInfoHeadView.setHaveSubColl(true);
                    } else {
                        proposeCollateralInfoHeadView.setHaveSubColl(false);
                    }
                    proposeCollateralInfoHeadViewList.add(proposeCollateralInfoHeadView);
                }
            } else {
                proposeCollateralInfoHeadViewList.add(new ProposeCollateralInfoHeadView());
            }
            proposeCollateralInfoView.setProposeCollateralInfoHeadViewList(proposeCollateralInfoHeadViewList);
            proposeCollateralInfoView.setComs(true);
        }
        return proposeCollateralInfoView;
    }
}
