package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewGuarantorCredit;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.view.NewCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGuarantorCreditTransform extends Transform {

    @Inject
    public NewGuarantorCreditTransform() {
    }


    public List<NewGuarantorCredit> transformsToModelForGuarantor(List<NewCreditDetailView> newCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewGuarantorDetail newGuarantorDetail, User user) {

        List<NewGuarantorCredit> newGuarantorCreditList = new ArrayList<NewGuarantorCredit>();
        NewGuarantorCredit newGuarantorCredit;

        for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
            newGuarantorCredit = new NewGuarantorCredit();
            if (newGuarantorCredit.getId() != 0) {
                newGuarantorCredit.setCreateDate(newCreditDetailView.getCreateDate());
                newGuarantorCredit.setCreateBy(newCreditDetailView.getCreateBy());
            } else { // id = 0 create new
                newGuarantorCredit.setCreateDate(new Date());
                newGuarantorCredit.setCreateBy(user);
            }
            newGuarantorCredit.setModifyDate(new Date());
            newGuarantorCredit.setModifyBy(user);
            for (int i = 0; i < newCreditDetailList.size(); i++) {
                NewCreditDetail newCreditDetailAdd = newCreditDetailList.get(i);
                log.info("newCreditDetailAdd id is " + newCreditDetailAdd.getId() + " detail seq  is " + newCreditDetailAdd.getSeq());
                log.info("guarantor choose seq  is " + newCreditDetailView.getSeq());

                if (newCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()) {
                    newGuarantorCredit.setNewCreditDetail(newCreditDetailAdd);
                    log.info("newGuarantorCredit id is " + newGuarantorCredit.getNewCreditDetail().getId());
                    newGuarantorCredit.setGuaranteeAmount(newCreditDetailView.getGuaranteeAmount());
                }
            }

            newGuarantorCredit.setNewGuarantorDetail(newGuarantorDetail);
            newGuarantorCreditList.add(newGuarantorCredit);
        }

        return newGuarantorCreditList;
    }


}
