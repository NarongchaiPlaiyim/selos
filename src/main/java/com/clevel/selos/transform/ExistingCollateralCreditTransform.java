package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditTypeDetail;
import com.clevel.selos.model.db.working.ExistingCollateralCredit;
import com.clevel.selos.model.db.working.ExistingCollateralDetail;
import com.clevel.selos.model.view.ExistingCreditTypeDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCollateralCreditTransform extends Transform {

    @Inject
    public ExistingCollateralCreditTransform() {
    }


    public List<ExistingCollateralCredit> transformsToModelForCollateral(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList,List<ExistingCreditDetail> existingCreditDetailList  ,ExistingCollateralDetail existingCollateralDetail,User user){

        List<ExistingCollateralCredit> existingCollateralCreditList = new ArrayList<ExistingCollateralCredit>();
        ExistingCreditTypeDetail existingCreditTypeDetail;

        ExistingCollateralCredit existingCollateralCredit;

        for (ExistingCreditTypeDetailView existingCreditTypeDetailView : existingCreditTypeDetailViewList) {
            existingCollateralCredit = new ExistingCollateralCredit();
            if (existingCollateralCredit.getId() != 0) {
                existingCollateralCredit.setId(existingCreditTypeDetailView.getId());
                existingCollateralCredit.setCreateDate(existingCreditTypeDetailView.getCreateDate());
                existingCollateralCredit.setCreateBy(existingCreditTypeDetailView.getCreateBy());
            } else { // id = 0 create new
                existingCollateralCredit.setCreateDate(new Date());
                existingCollateralCredit.setCreateBy(user);
            }

            existingCollateralCredit.setModifyDate(new Date());
            existingCollateralCredit.setModifyBy(user);
            for(int i=0;i<existingCreditDetailList.size();i++){
                ExistingCreditDetail  existingCreditDetail = existingCreditDetailList.get(i);
                log.info ("existingCreditDetail id is " + existingCreditDetail.getId() + " detail seq  is " + existingCreditDetail.getSeq());
                log.info ("  collateral choose seq  is " + existingCreditTypeDetailView.getSeq());

                if( existingCreditTypeDetailView.getSeq() == existingCreditDetail.getSeq()){
                    existingCollateralCredit.setExistingCreditDetail(existingCreditDetail);
                    log.info ("existingCollateralCredit id is " + existingCollateralCredit.getExistingCreditDetail().getId());
                }
            }

            existingCollateralCredit.setExistingCollateralDetail(existingCollateralDetail);
            existingCollateralCreditList.add(existingCollateralCredit);
        }

        return existingCollateralCreditList;
    }

    public List<ExistingCreditTypeDetailView> transformsToView(List<ExistingCollateralCredit> existingCollateralCreditList) {
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        ExistingCreditTypeDetailView existingCreditTypeDetailView;

        for( ExistingCollateralCredit existingCollateralCredit :existingCollateralCreditList){
            existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
            existingCreditTypeDetailView.setCreateDate(existingCollateralCredit.getExistingCreditDetail().getCreateDate());
            existingCreditTypeDetailView.setCreateBy(existingCollateralCredit.getExistingCreditDetail().getCreateBy());
            existingCreditTypeDetailView.setModifyDate(existingCollateralCredit.getExistingCreditDetail().getModifyDate());
            existingCreditTypeDetailView.setModifyBy(existingCollateralCredit.getExistingCreditDetail().getModifyBy());
            existingCreditTypeDetailView.setSeq(existingCollateralCredit.getExistingCreditDetail().getSeq());
            //existingCreditTypeDetailView.setNoFlag(Util.isTrue(existingCollateralCredit.getExistingCreditDetail().getNoFlag()));
            existingCreditTypeDetailView.setNo(existingCollateralCredit.getExistingCreditDetail().getNo());
            //existingCreditTypeDetailView.setType(existingCollateralCredit.getExistingCreditDetail().getType());
            //existingCreditTypeDetailView.setRequestType(existingCollateralCredit.getExistingCreditDetail().getRequestType());
            existingCreditTypeDetailView.setAccountNumber(existingCollateralCredit.getExistingCreditDetail().getAccountNumber());
            existingCreditTypeDetailView.setAccountName(existingCollateralCredit.getExistingCreditDetail().getAccountName());
            existingCreditTypeDetailView.setAccountSuf(existingCollateralCredit.getExistingCreditDetail().getAccountSuf());
            existingCreditTypeDetailView.setProductProgram(existingCollateralCredit.getExistingCreditDetail().getExistProductProgram().getName());
            existingCreditTypeDetailView.setCreditFacility(existingCollateralCredit.getExistingCreditDetail().getExistCreditType().getName());
            existingCreditTypeDetailView.setLimit(existingCollateralCredit.getExistingCreditDetail().getLimit());

            existingCreditTypeDetailViewList.add(existingCreditTypeDetailView);
        }

        return existingCreditTypeDetailViewList;
    }
}
