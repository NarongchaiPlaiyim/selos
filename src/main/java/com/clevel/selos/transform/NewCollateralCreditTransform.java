package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.db.working.NewCollateralRelCredit;
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


    public List<NewCollateralRelCredit> transformsToModelForCollateral(List<NewCreditDetailView> newCreditDetailViewList,List<NewCreditDetail> newCreditDetailList  ,NewCollateralDetail newCollateralDetail,User user){

       List<NewCollateralRelCredit> newCollateralCreditList = new ArrayList<NewCollateralRelCredit>();
       NewCollateralRelCredit newCollateralRelCredit;

        for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
            newCollateralRelCredit = new NewCollateralRelCredit();
            if (newCollateralRelCredit.getId() != 0) {
                newCollateralRelCredit.setId(newCreditDetailView.getId());
                newCollateralRelCredit.setCreateDate(newCreditDetailView.getCreateDate());
                newCollateralRelCredit.setCreateBy(newCreditDetailView.getCreateBy());
            } else { // id = 0 create new
                newCollateralRelCredit.setCreateDate(new Date());
                newCollateralRelCredit.setCreateBy(user);
            }

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

            newCollateralRelCredit.setNewCollateralDetail(newCollateralDetail);
            newCollateralCreditList.add(newCollateralRelCredit);
        }

        return newCollateralCreditList;
    }


}
