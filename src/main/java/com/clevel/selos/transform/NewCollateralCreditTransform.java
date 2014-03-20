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

    public List<NewCollateralCredit> transformsToModelForCollateral(List<ProposeCreditDetailView> proposeCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewCollateral newCollateralDetail,NewCreditFacility newCreditFacility,ProposeType proposeType, User user) {
       log.info("proposeCreditDetailViewList size :: {}",proposeCreditDetailViewList.size());
        List<NewCollateralCredit> newCollateralCreditList = new ArrayList<NewCollateralCredit>();
        NewCollateralCredit newCollateralRelCredit;
        NewCreditDetail newCreditDetailAdd;

        for (ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList) {
            log.debug("Start... transformToModelForCollateral : proposeCreditDetailView : {}", proposeCreditDetailView);

                newCollateralRelCredit = new NewCollateralCredit();
                newCollateralRelCredit.setModifyDate(DateTime.now().toDate());
                newCollateralRelCredit.setModifyBy(user);
                newCollateralRelCredit.setCreateDate(DateTime.now().toDate());
                newCollateralRelCredit.setCreateBy(user);

                log.info("proposeCreditDetailView::: getTypeOfStep :: {}", proposeCreditDetailView.getTypeOfStep());

                if ("N".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    log.info("guarantor choose seq  is " + proposeCreditDetailView.getSeq());
                    newCreditDetailAdd = findNewCreditDetail(newCreditDetailList, proposeCreditDetailView);
                    if (newCreditDetailAdd != null) {
                        newCollateralRelCredit.setNewCreditDetail(newCreditDetailAdd);
                        log.info("newCollateralRelCredit newCreditDetailAdd id toSet is " + newCollateralRelCredit.getNewCreditDetail().getId());
                    }
                } else if ("E".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    ExistingCreditDetail existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditDetailView.getId());
                    if (existingCreditDetail.getId() ==  proposeCreditDetailView.getId()) {
                        log.debug("guarantor choose id  is :: {}", proposeCreditDetailView.getId());
                        log.debug("guarantor choose seq  is :: {}", proposeCreditDetailView.getSeq());
                        newCollateralRelCredit.setExistingCreditDetail(existingCreditDetail);
                        log.info("newCollateralRelCredit existingCreditDetail id toSet is " + newCollateralRelCredit.getExistingCreditDetail().getId());
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

        for (NewCreditDetail newCreditDetailAdd : newCreditDetailList) {
            log.info("newCreditDetailAdd seq is :: {}", newCreditDetailAdd.getSeq());
            log.info("guarantor choose seq is {}", proposeCreditDetailView.getSeq());
            if (proposeCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()) {
                newCreditDetailReturn = newCreditDetailAdd;
                return newCreditDetailReturn;
            }
        }
        return newCreditDetailReturn;
    }
}
