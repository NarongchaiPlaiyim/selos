package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.view.CollateralHeaderDetailView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.model.view.SubCollateralDetailView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProposeCollateralInfoTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;

    public ProposeCollateralInfoView transformsCOMSToModelView(AppraisalData appraisalData){

        log.info("transformsCOMSToModelView begin");
        ProposeCollateralInfoView collateralDetailView = new ProposeCollateralInfoView();
        collateralDetailView.setJobID(appraisalData.getJobId());
        collateralDetailView.setAppraisalDate(appraisalData.getAppraisalDate());
        collateralDetailView.setAadDecision(appraisalData.getAadDecision());
        collateralDetailView.setAadDecisionReason(appraisalData.getAadDecisionReason());
        collateralDetailView.setAadDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
        collateralDetailView.setUsage(appraisalData.getUsage());
        collateralDetailView.setTypeOfUsage(appraisalData.getTypeOfUsage());
        collateralDetailView.setMortgageCondition(appraisalData.getMortgageCondition());
        collateralDetailView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

        List<CollateralHeaderDetailView> collateralHeaderDetailViewList = new ArrayList<CollateralHeaderDetailView>();
    /*    List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();

        for(HeadCollateralData headCollateralData : appraisalData.getHeadCollateralDataList())
        {
            CollateralHeaderDetailView collateralHeaderDetailView = convertCollateralHeader(headCollateralData);

            for(SubCollateralData subCollateralData : headCollateralData.getSubCollateralDataList()){
                SubCollateralDetailView subCollateralDetailView = convertSubCollateral(subCollateralData);
                subCollateralDetailViewList.add(subCollateralDetailView);
            }

            collateralHeaderDetailView.setSubCollateralDetailViewList(subCollateralDetailViewList);
            collateralHeaderDetailViewList.add(collateralHeaderDetailView);
        }

        collateralDetailView.setCollateralHeaderDetailViewList(collateralHeaderDetailViewList);
        log.info("convertCollateral end");
        return collateralDetailView;*/

        /*List<SubCollateralData> SubCollateralDataList = appraisalData.getSubCollateralDataList();
        List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();

        for(int i= 0;i<appraisalData.getSubCollateralDataList().size();i++){

            SubCollateralDetailView subCollateralDetailView = convertSubCollateral(SubCollateralDataList.get(i));
            subCollateralDetailView.setNo(i+1);
            subCollateralDetailView.getSubCollateralType().setCollateralType(collateralHeaderDetailView.getHeadCollType());
            SubCollateralType subCollateralTypeResult = subCollateralTypeDAO.findByBySubColCode(subCollateralDetailView.getSubCollateralType());
            subCollateralDetailView.setSubCollateralType(subCollateralTypeResult);
            subCollateralDetailViewList.add(subCollateralDetailView);
        }
        collateralHeaderDetailView.setSubCollateralDetailViewList(subCollateralDetailViewList);

        collateralHeaderDetailView.setNo(1);
        collateralHeaderDetailViewList.add(collateralHeaderDetailView);
        collateralHeaderDetailView.setNo(2);
        collateralHeaderDetailViewList.add(collateralHeaderDetailView);*/

        return collateralDetailView;

    }

    private CollateralHeaderDetailView convertCollateralHeader(HeadCollateralData headCollateralData ){
        log.info("convertCollateralHeader begin");
        CollateralHeaderDetailView collateralHeaderDetailView = new CollateralHeaderDetailView();

        collateralHeaderDetailView.setTitleDeed(headCollateralData.getTitleDeed());
        double appraisalValue = Double.parseDouble(headCollateralData.getAppraisalValue());
        collateralHeaderDetailView.setAppraisalValue(new BigDecimal(appraisalValue));
        collateralHeaderDetailView.setCollateralLocation(headCollateralData.getCollateralLocation());
        CollateralType headCollType = new CollateralType();
        if(headCollateralData.getHeadCollType()== null || headCollateralData.getHeadCollType().equals("")){
            headCollType.setCode("00");
        }else{
            headCollType.setCode(headCollateralData.getHeadCollType());
        }

        headCollType = collateralTypeDAO.findByCollateralCode(headCollType);
        collateralHeaderDetailView.setHeadCollType(headCollType);
        log.info("convertCollateralHeader end");
        return collateralHeaderDetailView;
    }

    private SubCollateralDetailView convertSubCollateral(SubCollateralData subCollateralData ){
        log.info("convertSubCollateral begin");
        SubCollateralDetailView subCollateralDetailView = new SubCollateralDetailView();

        subCollateralDetailView.setTitleDeed(subCollateralData.getTitleDeed());
        subCollateralDetailView.setAppraisalValue(subCollateralData.getAppraisalValue());
        subCollateralDetailView.setAddress(subCollateralData.getAddress());
        subCollateralDetailView.setLandOffice(subCollateralData.getLandOffice());
        subCollateralDetailView.setCollateralOwnerAAD(subCollateralData.getCollateralOwner());
        SubCollateralType subCollType = new SubCollateralType();

        if(subCollateralData.getCollateralType()==null || subCollateralData.getCollateralType().equals("")){
            subCollType.setCode("00");
        }else{
            subCollType.setCode(subCollateralData.getCollateralType());
        }

        subCollateralDetailView.setSubCollateralType(subCollType);
        log.info("convertSubCollateral end");
        return subCollateralDetailView;
    }
}
