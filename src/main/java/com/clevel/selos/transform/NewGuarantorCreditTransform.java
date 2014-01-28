package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.db.working.NewGuarantorRelCredit;
import com.clevel.selos.model.view.NewCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGuarantorCreditTransform extends Transform {

    @Inject
    public NewGuarantorCreditTransform() {}


    public List<NewGuarantorRelCredit> transformsToModelForGuarantor(List<NewCreditDetailView> newCreditDetailViewList, List<NewCreditDetail> newCreditDetailList, NewGuarantorDetail newGuarantorDetail, User user) {

        List<NewGuarantorRelCredit> newGuarantorRelCreditList = new ArrayList<NewGuarantorRelCredit>();
        NewGuarantorRelCredit newGuarantorRelCredit;

        for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
            newGuarantorRelCredit = new NewGuarantorRelCredit();
//            if (newGuarantorRelCredit.getId() != 0) {
//                newGuarantorRelCredit.setId(newCreditDetailView.getId());
//                newGuarantorRelCredit.setCreateDate(newCreditDetailView.getCreateDate());
//                newGuarantorRelCredit.setCreateBy(newCreditDetailView.getCreateBy());
//            } else { // id = 0 create new
//                newGuarantorRelCredit.setCreateDate(new Date());
//                newGuarantorRelCredit.setCreateBy(user);
//            }

            newGuarantorRelCredit.setModifyDate(new Date());
            newGuarantorRelCredit.setModifyBy(user);
            for (int i = 0; i < newCreditDetailList.size(); i++) {
                NewCreditDetail newCreditDetailAdd = newCreditDetailList.get(i);
                log.info("newCreditDetailAdd id is " + newCreditDetailAdd.getId() + " detail seq  is " + newCreditDetailAdd.getSeq());
                log.info("guarantor choose seq  is " + newCreditDetailView.getSeq());

                if (newCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()) {
                    newGuarantorRelCredit.setNewCreditDetail(newCreditDetailAdd);
                    log.info("newGuarantorRelCredit id is " + newGuarantorRelCredit.getNewCreditDetail().getId());
                }
            }
            newGuarantorRelCredit.setNewCreditFacility(newGuarantorRelCredit.getNewCreditFacility());
            newGuarantorRelCredit.setNewGuarantorDetail(newGuarantorDetail);
            newGuarantorRelCreditList.add(newGuarantorRelCredit);
        }

        return newGuarantorRelCreditList;
    }


}
