package com.clevel.selos.transform;


import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewGuarantorCredit;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.view.ProposeCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGuarantorCreditTransform extends Transform {

    @Inject
    public NewGuarantorCreditTransform() {
    }

    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;

    public List<NewGuarantorCredit> transformsToModelForGuarantor(List<ProposeCreditDetailView> newCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewGuarantorDetail newGuarantorDetail, User user) {

        List<NewGuarantorCredit> newGuarantorCreditList = new ArrayList<NewGuarantorCredit>();
        NewGuarantorCredit newGuarantorCredit;

        for (ProposeCreditDetailView proposeCreditDetailView : newCreditDetailViewList) {
            newGuarantorCredit = new NewGuarantorCredit();
//            if (newGuarantorCredit.getId() != 0) {
//                newGuarantorCredit.setCreateDate(newCreditDetailView.getCreateDate());
//                newGuarantorCredit.setCreateBy(newCreditDetailView.getCreateBy());
//            } else { // id = 0 create new
//                newGuarantorCredit.setCreateDate(new Date());
//                newGuarantorCredit.setCreateBy(user);
//            }
            newGuarantorCredit.setCreateDate(new Date());
            newGuarantorCredit.setCreateBy(user);
            newGuarantorCredit.setModifyDate(new Date());
            newGuarantorCredit.setModifyBy(user);

            for (int i = 0; i < newCreditDetailList.size(); i++) {
                NewCreditDetail newCreditDetailAdd = newCreditDetailList.get(i);
                log.info("newCreditDetailAdd id is " + newCreditDetailAdd.getId() + " detail seq  is " + newCreditDetailAdd.getSeq());
                log.info("guarantor choose seq  is " + proposeCreditDetailView.getSeq());

                log.info("proposeCreditDetailView::: {}", proposeCreditDetailView.getTypeOfStep());


                if (proposeCreditDetailView.getTypeOfStep() == "E") {
                    ExistingCreditDetail existingCreditDetail = existingCreditDetailDAO.findById((long) proposeCreditDetailView.getSeq());
                    newGuarantorCredit.setExistingCreditDetail(existingCreditDetail);
                    newGuarantorCredit.setGuaranteeAmount(proposeCreditDetailView.getGuaranteeAmount());
                } else if (proposeCreditDetailView.getTypeOfStep() == "N") {
                    if (proposeCreditDetailView.getId() == newCreditDetailAdd.getId()) {
                        newGuarantorCredit.setNewCreditDetail(newCreditDetailAdd);
                        log.info("newGuarantorCredit id is " + newGuarantorCredit.getNewCreditDetail().getId());
                        newGuarantorCredit.setGuaranteeAmount(proposeCreditDetailView.getGuaranteeAmount());
                    }
                }

            }

            newGuarantorCredit.setNewGuarantorDetail(newGuarantorDetail);
            newGuarantorCreditList.add(newGuarantorCredit);
        }

        return newGuarantorCreditList;
    }


}
