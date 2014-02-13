package com.clevel.selos.transform;


import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.NewCollateralCreditDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.view.ProposeCreditDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollateralCreditTransform extends Transform {

    @Inject
    public NewCollateralCreditTransform() {
    }

    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    NewCollateralCreditDAO newCollateralRelationDAO;


    public List<NewCollateralCredit> transformsToModelForCollateral(List<ProposeCreditDetailView> proposeCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewCollateral newCollateralDetail, User user) {

        List<NewCollateralCredit> newCollateralCreditList = new ArrayList<NewCollateralCredit>();
        NewCollateralCredit newCollateralRelCredit;

        for (ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList) {
            log.debug("Start... transformToModelForCollateral : proposeCreditDetailView : {}", proposeCreditDetailView);

            newCollateralRelCredit = new NewCollateralCredit();

            //newCollateralRelCredit = newCollateralRelationDAO.findById(Long.parseLong(Integer.toString(proposeCreditDetailView.getId())));
            newCollateralRelCredit.setModifyDate(DateTime.now().toDate());
            newCollateralRelCredit.setModifyBy(user);
            newCollateralRelCredit.setCreateDate(DateTime.now().toDate());
            newCollateralRelCredit.setCreateBy(user);

            for (NewCreditDetail newCreditDetailAdd : newCreditDetailList) {
                log.info("newCreditDetailAdd id is {} detail seq is {}", newCreditDetailAdd.getId(), newCreditDetailAdd.getSeq());
                log.info("guarantor choose seq is {}", proposeCreditDetailView.getSeq());
                log.info("proposeCreditDetailView::: getTypeOfStep :: {}", proposeCreditDetailView.getTypeOfStep());

                if ("N".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    if (proposeCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()) {
                        log.info("newCreditDetailAdd id is " + newCreditDetailAdd.getId() + " detail seq  is " + newCreditDetailAdd.getSeq());
                        log.info("guarantor choose seq  is " + proposeCreditDetailView.getSeq());
                        newCollateralRelCredit.setNewCreditDetail(newCreditDetailAdd);
                        log.info("newCollateralRelCredit newCreditDetailAdd id toSet is " + newCollateralRelCredit.getNewCreditDetail().getId());
                    }
                } else if ("E".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    ExistingCreditDetail existingCreditDetail = existingCreditDetailDAO.findById((long) proposeCreditDetailView.getSeq());
                    if (existingCreditDetail.getId() == (long) proposeCreditDetailView.getSeq()) {
                        log.info("guarantor choose seq  is :: {}", proposeCreditDetailView.getSeq());
                        newCollateralRelCredit.setExistingCreditDetail(existingCreditDetail);
                        log.info("newGuarantorCredit existingCreditDetail id toSet is " + newCollateralRelCredit.getExistingCreditDetail().getId());

                    }
                }

                newCollateralRelCredit.setNewCollateral(newCollateralDetail);
                newCollateralCreditList.add(newCollateralRelCredit);
            }
        }
        return newCollateralCreditList;
    }


}
