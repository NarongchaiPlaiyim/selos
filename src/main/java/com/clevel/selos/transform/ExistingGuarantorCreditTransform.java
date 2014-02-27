package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditTypeDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingGuarantorCreditTransform extends Transform {

    @Inject
    public ExistingGuarantorCreditTransform() {
    }


    public List<ExistingGuarantorCredit> transformsToModelForGuarantor(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList,List<ExistingCreditDetail> existingCreditDetailList  ,ExistingGuarantorDetail existingGuarantorDetail,User user){

        List<ExistingGuarantorCredit> existingGuarantorCreditList = new ArrayList<ExistingGuarantorCredit>();
        ExistingCreditTypeDetail existingCreditTypeDetail;

        ExistingGuarantorCredit existingGuarantorCredit;

        for (ExistingCreditTypeDetailView existingCreditTypeDetailView : existingCreditTypeDetailViewList) {
            existingGuarantorCredit = new ExistingGuarantorCredit();
            if (existingGuarantorCredit.getId() != 0) {
                existingGuarantorCredit.setId(existingCreditTypeDetailView.getId());
                existingGuarantorCredit.setCreateDate(existingCreditTypeDetailView.getCreateDate());
                existingGuarantorCredit.setCreateBy(existingCreditTypeDetailView.getCreateBy());
            } else { // id = 0 create new
                existingGuarantorCredit.setCreateDate(new Date());
                existingGuarantorCredit.setCreateBy(user);
            }

            existingGuarantorCredit.setModifyDate(new Date());
            existingGuarantorCredit.setModifyBy(user);
            for(int i=0;i<existingCreditDetailList.size();i++){
                ExistingCreditDetail  existingCreditDetail = existingCreditDetailList.get(i);
                log.debug ("existingCreditDetail id is {} detail seqNo is {}",existingCreditDetail.getId(),existingCreditDetail.getNo());
                log.debug ("  guarantor choose seq  is {}",existingCreditTypeDetailView.getNo());

                if( existingCreditTypeDetailView.getNo() == existingCreditDetail.getNo()){
                    existingGuarantorCredit.setExistingCreditDetail(existingCreditDetail);
                    existingGuarantorCredit.setGuaranteeAmount(existingCreditTypeDetailView.getGuaranteeAmount());
                    log.debug ("existingGuarantorCredit id is {}",existingGuarantorCredit.getExistingCreditDetail().getId());
                }
            }

            existingGuarantorCredit.setExistingGuarantorDetail(existingGuarantorDetail);
            existingGuarantorCreditList.add(existingGuarantorCredit);
        }

        return existingGuarantorCreditList;
    }

    public List<ExistingCreditTypeDetailView> transformsToView(List<ExistingGuarantorCredit> existingGuarantorCreditList) {
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        ExistingCreditTypeDetailView existingCreditTypeDetailView;

        for( ExistingGuarantorCredit existingGuarantorCredit :existingGuarantorCreditList){
            existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
            existingCreditTypeDetailView.setCreateDate(existingGuarantorCredit.getExistingCreditDetail().getCreateDate());
            existingCreditTypeDetailView.setCreateBy(existingGuarantorCredit.getExistingCreditDetail().getCreateBy());
            existingCreditTypeDetailView.setModifyDate(existingGuarantorCredit.getExistingCreditDetail().getModifyDate());
            existingCreditTypeDetailView.setModifyBy(existingGuarantorCredit.getExistingCreditDetail().getModifyBy());
            existingCreditTypeDetailView.setSeq(existingGuarantorCredit.getExistingCreditDetail().getSeq());
            //existingCreditTypeDetailView.setNoFlag(Util.isTrue(existingGuarantorCredit.getExistingCreditDetail().getNoFlag()));
            existingCreditTypeDetailView.setNo(existingGuarantorCredit.getExistingCreditDetail().getNo());
            //existingCreditTypeDetailView.setType(existingGuarantorCredit.getExistingCreditDetail().getType());
            //existingCreditTypeDetailView.setRequestType(existingGuarantorCredit.getExistingCreditDetail().getRequestType());
            existingCreditTypeDetailView.setAccountNumber(existingGuarantorCredit.getExistingCreditDetail().getAccountNumber());
            existingCreditTypeDetailView.setAccountName(existingGuarantorCredit.getExistingCreditDetail().getAccountName());
            existingCreditTypeDetailView.setAccountSuf(existingGuarantorCredit.getExistingCreditDetail().getAccountSuf());
            existingCreditTypeDetailView.setProductProgram(existingGuarantorCredit.getExistingCreditDetail().getExistProductProgram().getName());
            existingCreditTypeDetailView.setCreditFacility(existingGuarantorCredit.getExistingCreditDetail().getExistCreditType().getName());
            existingCreditTypeDetailView.setGuaranteeAmount(existingGuarantorCredit.getGuaranteeAmount());
            existingCreditTypeDetailView.setLimit(existingGuarantorCredit.getExistingCreditDetail().getLimit());

            existingCreditTypeDetailViewList.add(existingCreditTypeDetailView);
        }

        return existingCreditTypeDetailViewList;
    }
}
