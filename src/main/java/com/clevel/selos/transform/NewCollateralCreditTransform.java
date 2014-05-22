package com.clevel.selos.transform;


import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.NewCollateralCreditDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.ProposeCreditDetailView;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralCreditTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public NewCollateralCreditTransform() {}
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    NewCollateralCreditDAO newCollateralRelationDAO;

    public NewCollateralCredit transformsToModel(ProposeCreditDetailView proposeCreditDetailView, List<NewCreditDetail> newCreditDetailList, NewCollateral newCollateralDetail,NewCreditFacility newCreditFacility,ProposeType proposeType, User user){
        if(proposeCreditDetailView != null){
            NewCollateralCredit newCollateralCredit = new NewCollateralCredit();
            /*if(proposeCreditDetailView.getId() != 0){
                newCollateralCredit = newCollateralRelationDAO.findById(proposeCreditDetailView.getId());
            } else {
                newCollateralCredit.setCreateDate(new Date());
                newCollateralCredit.setCreateBy(user);
            }*/
            newCollateralCredit.setCreateDate(new Date());
            newCollateralCredit.setCreateBy(user);
            newCollateralCredit.setModifyDate(new Date());
            newCollateralCredit.setModifyBy(user);

            if ("N".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                NewCreditDetail newCreditDetailAdd = findNewCreditDetail(newCreditDetailList, proposeCreditDetailView);
                if (newCreditDetailAdd != null) {
                    newCollateralCredit.setNewCreditDetail(newCreditDetailAdd);
                }
            } else if ("E".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                if(proposeCreditDetailView.getId() != 0){
                    ExistingCreditDetail existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditDetailView.getId());
                    if(existingCreditDetail != null){
                        if (existingCreditDetail.getId() ==  proposeCreditDetailView.getId()) {
                            newCollateralCredit.setExistingCreditDetail(existingCreditDetail);
                        }
                    }
                }
            }
            newCollateralCredit.setNewCreditFacility(newCreditFacility);
            newCollateralCredit.setNewCollateral(newCollateralDetail);
            newCollateralCredit.setProposeType(proposeType);

            return newCollateralCredit;
        } else {
            return null;
        }
    }

    public List<NewCollateralCredit> transformsToModelList(List<ProposeCreditDetailView> proposeCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewCollateral newCollateralDetail,NewCreditFacility newCreditFacility,ProposeType proposeType, User user){
        List<NewCollateralCredit> newCollateralCreditList = new ArrayList<NewCollateralCredit>();
        if (proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0) {
            for (ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList) {
                NewCollateralCredit newCollateralCredit = transformsToModel(proposeCreditDetailView, newCreditDetailList, newCollateralDetail, newCreditFacility, proposeType, user);
                newCollateralCreditList.add(newCollateralCredit);
            }
        }
        return newCollateralCreditList;
    }

    public List<NewCollateralCredit> transformsToModelForCollateral(List<ProposeCreditDetailView> proposeCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewCollateral newCollateralDetail,NewCreditFacility newCreditFacility,ProposeType proposeType, User user) {
       log.info("proposeCreditDetailViewList size :: {}",proposeCreditDetailViewList.size());
        List<NewCollateralCredit> newCollateralCreditList = new ArrayList<NewCollateralCredit>();
        NewCollateralCredit newCollateralRelCredit;
        NewCreditDetail newCreditDetailAdd;

        for (ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList) {
            log.debug("Start... transformToModelForCollateral : proposeCreditDetailView : {}", proposeCreditDetailView);
                newCollateralRelCredit = new NewCollateralCredit();
                newCollateralRelCredit.setModifyDate(new Date());
                newCollateralRelCredit.setModifyBy(user);
                newCollateralRelCredit.setCreateDate(new Date());
                newCollateralRelCredit.setCreateBy(user);

                if ("N".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    newCreditDetailAdd = findNewCreditDetail(newCreditDetailList, proposeCreditDetailView);
                    if (newCreditDetailAdd != null) {
                        newCollateralRelCredit.setNewCreditDetail(newCreditDetailAdd);
                    }
                } else if ("E".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    ExistingCreditDetail existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditDetailView.getId());
                    if (existingCreditDetail.getId() ==  proposeCreditDetailView.getId()) {
                        newCollateralRelCredit.setExistingCreditDetail(existingCreditDetail);
                    }
                }
                newCollateralRelCredit.setNewCreditFacility(newCreditFacility);
                newCollateralRelCredit.setNewCollateral(newCollateralDetail);
                newCollateralRelCredit.setProposeType(proposeType);
                newCollateralCreditList.add(newCollateralRelCredit);
        }
        return newCollateralCreditList;
    }

    public NewCreditDetail findNewCreditDetail(List<NewCreditDetail> newCreditDetailList, ProposeCreditDetailView proposeCreditDetailView) {
        NewCreditDetail newCreditDetailReturn = null;
        if(newCreditDetailList != null && newCreditDetailList.size() > 0){
            for (NewCreditDetail newCreditDetailAdd : newCreditDetailList) {
                if(proposeCreditDetailView != null){
                    if (proposeCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()) {
                        newCreditDetailReturn = newCreditDetailAdd;
                        return newCreditDetailReturn;
                    }
                }
            }
        }
        return newCreditDetailReturn;
    }
}
