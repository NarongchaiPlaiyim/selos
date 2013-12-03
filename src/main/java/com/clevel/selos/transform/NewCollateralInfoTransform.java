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
import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.view.NewCollateralHeadDetailView;
import com.clevel.selos.model.view.NewCollateralInfoView;
import com.clevel.selos.model.view.NewSubCollateralDetailView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewCollateralInfoTransform extends Transform {
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

    public NewCollateralInfoView transformsCOMSToModelView(AppraisalData appraisalData) {

        log.info("transformsCOMSToModelView begin");
        NewCollateralInfoView collateralDetailView = new NewCollateralInfoView();
        collateralDetailView.setJobID(appraisalData.getJobId());
        collateralDetailView.setAppraisalDate(appraisalData.getAppraisalDate());
        collateralDetailView.setAadDecision(appraisalData.getAadDecision());
        collateralDetailView.setAadDecisionReason(appraisalData.getAadDecisionReason());
        collateralDetailView.setAadDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
        collateralDetailView.setUsage(appraisalData.getUsage());
        collateralDetailView.setTypeOfUsage(appraisalData.getTypeOfUsage());
        collateralDetailView.setMortgageCondition(appraisalData.getMortgageCondition());
        collateralDetailView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
    /*    List<NewSubCollateralDetailView> subCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();

        for(HeadCollateralData headCollateralData : appraisalData.getHeadCollateralDataList())
        {
            NewCollateralHeadDetailView collateralHeaderDetailView = convertCollateralHeader(headCollateralData);

            for(SubCollateralData subCollateralData : headCollateralData.getSubCollateralDataList()){
                NewSubCollateralDetailView subCollateralDetailView = convertSubCollateral(subCollateralData);
                subCollateralDetailViewList.add(subCollateralDetailView);
            }

            collateralHeaderDetailView.setNewSubCollateralDetailViewList(subCollateralDetailViewList);
            newCollateralHeadDetailViewList.add(collateralHeaderDetailView);
        }

        collateralDetailView.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViewList);
        log.info("convertCollateral end");
        return collateralDetailView;*/

        /*List<SubCollateralData> SubCollateralDataList = appraisalData.getSubCollateralDataList();
        List<NewSubCollateralDetailView> subCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();

        for(int i= 0;i<appraisalData.getSubCollateralDataList().size();i++){

            NewSubCollateralDetailView subCollateralDetailView = convertSubCollateral(SubCollateralDataList.get(i));
            subCollateralDetailView.setNo(i+1);
            subCollateralDetailView.getSubCollateralType().setCollateralType(collateralHeaderDetailView.getHeadCollType());
            SubCollateralType subCollateralTypeResult = subCollateralTypeDAO.findByBySubColCode(subCollateralDetailView.getSubCollateralType());
            subCollateralDetailView.setSubCollateralType(subCollateralTypeResult);
            subCollateralDetailViewList.add(subCollateralDetailView);
        }
        collateralHeaderDetailView.setNewSubCollateralDetailViewList(subCollateralDetailViewList);

        collateralHeaderDetailView.setNo(1);
        newCollateralHeadDetailViewList.add(collateralHeaderDetailView);
        collateralHeaderDetailView.setNo(2);
        newCollateralHeadDetailViewList.add(collateralHeaderDetailView);*/

        return collateralDetailView;

    }

    private NewCollateralHeadDetailView convertCollateralHeader(HeadCollateralData headCollateralData) {
        log.info("convertCollateralHeader begin");
        NewCollateralHeadDetailView newCollateralHeadDetailView = new NewCollateralHeadDetailView();

        newCollateralHeadDetailView.setTitleDeed(headCollateralData.getTitleDeed());
        double appraisalValue = Double.parseDouble(headCollateralData.getAppraisalValue());
        newCollateralHeadDetailView.setAppraisalValue(new BigDecimal(appraisalValue));
        newCollateralHeadDetailView.setCollateralLocation(headCollateralData.getCollateralLocation());
        CollateralType headCollType = new CollateralType();
        if (headCollateralData.getHeadCollType() == null || headCollateralData.getHeadCollType().equals("")) {
            headCollType.setCode("00");
        } else {
            headCollType.setCode(headCollateralData.getHeadCollType());
        }

        headCollType = collateralTypeDAO.findByCollateralCode(headCollType);
        newCollateralHeadDetailView.setHeadCollType(headCollType);
        log.info("convertCollateralHeader end");
        return newCollateralHeadDetailView;
    }

    private NewSubCollateralDetailView convertSubCollateral(SubCollateralData subCollateralData) {
        log.info("convertSubCollateral begin");
        NewSubCollateralDetailView newSubCollateralDetailView = new NewSubCollateralDetailView();

        newSubCollateralDetailView.setTitleDeed(subCollateralData.getTitleDeed());
        newSubCollateralDetailView.setAppraisalValue(subCollateralData.getAppraisalValue());
        newSubCollateralDetailView.setAddress(subCollateralData.getAddress());
        newSubCollateralDetailView.setLandOffice(subCollateralData.getLandOffice());
        newSubCollateralDetailView.setCollateralOwnerAAD(subCollateralData.getCollateralOwner());
        SubCollateralType subCollType = new SubCollateralType();

        if (subCollateralData.getCollateralType() == null || subCollateralData.getCollateralType().equals("")) {
            subCollType.setCode("00");
        } else {
            subCollType.setCode(subCollateralData.getCollateralType());
        }

        newSubCollateralDetailView.setSubCollateralType(subCollType);
        log.info("convertSubCollateral end");
        return newSubCollateralDetailView;
    }

    public List<NewCollateralDetail> transformsToModel() {

       return null;
    }

    public List<NewCollateralInfoView> transformsToView() {

        return null;
    }


}
