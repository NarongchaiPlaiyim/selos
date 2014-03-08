package com.clevel.selos.transform;


import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.ProposeCreditDetailView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewGuarantorCreditTransform extends Transform {

    @Inject
    public NewGuarantorCreditTransform() {
    }

    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;

    public List<NewGuarantorCredit> transformsToModelForGuarantor(List<ProposeCreditDetailView> newCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewGuarantorDetail newGuarantorDetail,NewCreditFacility newCreditFacility, User user) {
        log.info("newCreditDetailList  ::: {}", newCreditDetailList.size());
        List<NewGuarantorCredit> newGuarantorCreditList = new ArrayList<NewGuarantorCredit>();
        NewGuarantorCredit newGuarantorCredit;
        NewCreditDetail newCreditDetailAdd;

        for (ProposeCreditDetailView proposeCreditDetailView : newCreditDetailViewList) {

                newGuarantorCredit = new NewGuarantorCredit();
                newGuarantorCredit.setCreateDate(DateTime.now().toDate());
                newGuarantorCredit.setCreateBy(user);
                newGuarantorCredit.setModifyDate(DateTime.now().toDate());
                newGuarantorCredit.setModifyBy(user);

                log.info("proposeCreditDetailView::: getTypeOfStep :: {}", proposeCreditDetailView.getTypeOfStep());

                if ("N".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    log.info("guarantor choose seq  is " + proposeCreditDetailView.getSeq());
                    newCreditDetailAdd = findNewCreditDetail(newCreditDetailList, proposeCreditDetailView);
                    if (newCreditDetailAdd != null) {
                        newGuarantorCredit.setNewCreditDetail(newCreditDetailAdd);
                        log.info("newGuarantorCredit newCreditDetailAdd id toSet is " + newGuarantorCredit.getNewCreditDetail().getId());
                        newGuarantorCredit.setGuaranteeAmount(proposeCreditDetailView.getGuaranteeAmount());
                    }
                } else if ("E".equalsIgnoreCase(proposeCreditDetailView.getTypeOfStep())) {
                    log.info(" Existing ::  proposeCreditDetailView.getSeq() ::: {}", proposeCreditDetailView.getSeq());
                    ExistingCreditDetail existingCreditDetail = existingCreditDetailDAO.findById((long) proposeCreditDetailView.getSeq());
//                    log.info(" existingCreditDetail id ::: {}",existingCreditDetail.getId());
                    if (existingCreditDetail != null) {
                        newGuarantorCredit.setExistingCreditDetail(existingCreditDetail);
                        log.info("newGuarantorCredit existingCreditDetail id toSet is " + newGuarantorCredit.getExistingCreditDetail().getId());
                    }
                    newGuarantorCredit.setGuaranteeAmount(proposeCreditDetailView.getGuaranteeAmount());

                }

                newGuarantorCredit.setNewGuarantorDetail(newGuarantorDetail);
                newGuarantorCredit.setNewCreditFacility(newCreditFacility);
                newGuarantorCreditList.add(newGuarantorCredit);


        }

        return newGuarantorCreditList;
    }

    public NewCreditDetail findNewCreditDetail(List<NewCreditDetail> newCreditDetailList, ProposeCreditDetailView proposeCreditDetailView) {
        NewCreditDetail newCreditDetailReturn = null;

        for (NewCreditDetail newCreditDetailAdd : newCreditDetailList) {
            log.info("newCreditDetailAdd id is  :: {}", newCreditDetailAdd.getId());
            log.info("newCreditDetailAdd seq is :: {}", newCreditDetailAdd.getSeq());
            log.info("guarantor choose seq is {}", proposeCreditDetailView.getSeq());
            if (proposeCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()) {
                newCreditDetailReturn = newCreditDetailAdd;
                break;
            }
        }
        return newCreditDetailReturn;
    }

}
