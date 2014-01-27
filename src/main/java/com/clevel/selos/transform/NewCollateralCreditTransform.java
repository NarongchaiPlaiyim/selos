package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.view.NewCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralCreditTransform extends Transform {

    @Inject
    public NewCollateralCreditTransform() {
    }


    public List<NewCollateralCredit> transformsToModelForCollateral(List<NewCreditDetailView> newCreditDetailViewList,List<NewCreditDetail> newCreditDetailList  ,NewCollateral newCollateralDetail,User user){

       List<NewCollateralCredit> newCollateralCreditList = new ArrayList<NewCollateralCredit>();
       NewCollateralCredit newCollateralRelCredit;

        for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
            newCollateralRelCredit = new NewCollateralCredit();
//            if (newCollateralRelCredit.getId() != 0) {
//                newCollateralRelCredit.setId(newCreditDetailView.getId());
//                newCollateralRelCredit.setModifyDate(newCreditDetailView.getModifyDate());
//                newCollateralRelCredit.setModifyBy(newCreditDetailView.getModifyBy());
//            } else { // id = 0 create new
//                newCollateralRelCredit.setCreateDate(new Date());
//                newCollateralRelCredit.setCreateBy(user);
//            }

            newCollateralRelCredit.setModifyDate(new Date());
            newCollateralRelCredit.setModifyBy(user);
            for(int i=0; i < newCreditDetailList.size();i++){
                NewCreditDetail  newCreditDetailAdd = newCreditDetailList.get(i);
                log.info ("newCreditDetailAdd id is " + newCreditDetailAdd.getId() + " detail seq  is " + newCreditDetailAdd.getSeq());
                log.info ("collateral choose seq  is " + newCreditDetailView.getSeq());

                if( newCreditDetailView.getSeq() == newCreditDetailAdd.getSeq()){
                    newCollateralRelCredit.setNewCreditDetail(newCreditDetailAdd);
                    log.info ("newCollateralRelCredit id is " + newCollateralRelCredit.getNewCreditDetail().getId());
                }
            }

            newCollateralRelCredit.setNewCollateral(newCollateralDetail);
            newCollateralCreditList.add(newCollateralRelCredit);
        }

        return newCollateralCreditList;
    }


}
