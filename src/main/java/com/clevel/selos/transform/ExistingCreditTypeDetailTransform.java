package com.clevel.selos.transform;


import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.ExistingCreditTypeDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCreditTypeDetailTransform extends Transform {

    @Inject
    public ExistingCreditTypeDetailTransform() {
    }

    public List<ExistingCreditTypeDetail> transformToModelForGuarantor(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList,ExistingGuarantorDetail existingGuarantorDetail,User user){

        List<ExistingCreditTypeDetail> existingCreditTypeDetailList = new ArrayList<ExistingCreditTypeDetail>();
        ExistingCreditTypeDetail existingCreditTypeDetail;

        for (ExistingCreditTypeDetailView existingCreditTypeDetailView : existingCreditTypeDetailViewList) {
            existingCreditTypeDetail = new ExistingCreditTypeDetail();
            if (existingCreditTypeDetailView.getId() != 0) {
                existingCreditTypeDetail.setId(existingCreditTypeDetailView.getId());
                existingCreditTypeDetail.setCreateDate(existingCreditTypeDetailView.getCreateDate());
                existingCreditTypeDetail.setCreateBy(existingCreditTypeDetailView.getCreateBy());
            } else { // id = 0 create new
                existingCreditTypeDetail.setCreateDate(new Date());
                existingCreditTypeDetail.setCreateBy(user);
            }

            existingCreditTypeDetail.setSeq(existingCreditTypeDetailView.getSeq());
            existingCreditTypeDetail.setNo(Util.returnNumForFlag(existingCreditTypeDetailView.isNoFlag()));
            existingCreditTypeDetail.setType(existingCreditTypeDetailView.getType());
            existingCreditTypeDetail.setRequestType(existingCreditTypeDetailView.getRequestType());
            existingCreditTypeDetail.setAccountNumber(existingCreditTypeDetailView.getAccountNumber());
            existingCreditTypeDetail.setAccountName(existingCreditTypeDetailView.getAccountName());
            existingCreditTypeDetail.setAccountSuf(existingCreditTypeDetailView.getAccountSuf());
            existingCreditTypeDetail.setCreditFacility(existingCreditTypeDetailView.getCreditFacility());
            existingCreditTypeDetail.setGuaranteeAmount(existingCreditTypeDetailView.getGuaranteeAmount());
            existingCreditTypeDetail.setLimit(existingCreditTypeDetailView.getLimit());
            existingCreditTypeDetail.setProductProgram(existingCreditTypeDetailView.getProductProgram());
            existingCreditTypeDetail.setUseCount(existingCreditTypeDetailView.getUseCount());
            existingCreditTypeDetail.setExistingGuarantorDetail(existingGuarantorDetail);

            existingCreditTypeDetailList.add(existingCreditTypeDetail);
        }

        return existingCreditTypeDetailList;
    }

    public List<ExistingCreditTypeDetail> transformToModelForCollateral(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList,ExistingCollateralDetail existingCollateralDetail,User user){

        List<ExistingCreditTypeDetail> existingCreditTypeDetailList = new ArrayList<ExistingCreditTypeDetail>();
        ExistingCreditTypeDetail existingCreditTypeDetail;

        for (ExistingCreditTypeDetailView existingCreditTypeDetailView : existingCreditTypeDetailViewList) {
            existingCreditTypeDetail = new ExistingCreditTypeDetail();
            if (existingCreditTypeDetailView.getId() != 0) {
                existingCreditTypeDetail.setId(existingCreditTypeDetailView.getId());
                existingCreditTypeDetail.setCreateDate(existingCreditTypeDetailView.getCreateDate());
                existingCreditTypeDetail.setCreateBy(existingCreditTypeDetailView.getCreateBy());
            } else { // id = 0 create new
                existingCreditTypeDetail.setCreateDate(new Date());
                existingCreditTypeDetail.setCreateBy(user);
            }

            existingCreditTypeDetail.setSeq(existingCreditTypeDetailView.getSeq());
            existingCreditTypeDetail.setNo(Util.returnNumForFlag(existingCreditTypeDetailView.isNoFlag()));
            existingCreditTypeDetail.setType(existingCreditTypeDetailView.getType());
            existingCreditTypeDetail.setRequestType(existingCreditTypeDetailView.getRequestType());
            existingCreditTypeDetail.setAccountNumber(existingCreditTypeDetailView.getAccountNumber());
            existingCreditTypeDetail.setAccountName(existingCreditTypeDetailView.getAccountName());
            existingCreditTypeDetail.setAccountSuf(existingCreditTypeDetailView.getAccountSuf());
            existingCreditTypeDetail.setCreditFacility(existingCreditTypeDetailView.getCreditFacility());
            existingCreditTypeDetail.setGuaranteeAmount(existingCreditTypeDetailView.getGuaranteeAmount());
            existingCreditTypeDetail.setLimit(existingCreditTypeDetailView.getLimit());
            existingCreditTypeDetail.setProductProgram(existingCreditTypeDetailView.getProductProgram());
            existingCreditTypeDetail.setUseCount(existingCreditTypeDetailView.getUseCount());
            existingCreditTypeDetail.setExistingCollateralDetail(existingCollateralDetail);

            existingCreditTypeDetailList.add(existingCreditTypeDetail);
        }

        return existingCreditTypeDetailList;
    }

    public List<ExistingCreditTypeDetailView> transformToView(List<ExistingCreditTypeDetail> existingCreditTypeDetailList) {
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        ExistingCreditTypeDetailView existingCreditTypeDetailView;

        for( ExistingCreditTypeDetail existingCreditTypeDetail :existingCreditTypeDetailList){
            existingCreditTypeDetailView = new ExistingCreditTypeDetailView();
            existingCreditTypeDetailView.setCreateDate(existingCreditTypeDetail.getCreateDate());
            existingCreditTypeDetailView.setCreateBy(existingCreditTypeDetail.getCreateBy());
            existingCreditTypeDetailView.setModifyDate(existingCreditTypeDetail.getModifyDate());
            existingCreditTypeDetailView.setModifyBy(existingCreditTypeDetail.getModifyBy());
            existingCreditTypeDetailView.setSeq(existingCreditTypeDetail.getSeq());
            existingCreditTypeDetailView.setNoFlag(Util.isTrue(existingCreditTypeDetail.getNo()));
            existingCreditTypeDetailView.setType(existingCreditTypeDetail.getType());
            existingCreditTypeDetailView.setRequestType(existingCreditTypeDetail.getRequestType());
            existingCreditTypeDetailView.setAccountNumber(existingCreditTypeDetail.getAccountNumber());
            existingCreditTypeDetailView.setAccountName(existingCreditTypeDetail.getAccountName());
            existingCreditTypeDetailView.setAccountSuf(existingCreditTypeDetail.getAccountSuf());
            existingCreditTypeDetailView.setCreditFacility(existingCreditTypeDetail.getCreditFacility());
            existingCreditTypeDetailView.setGuaranteeAmount(existingCreditTypeDetail.getGuaranteeAmount());
            existingCreditTypeDetailView.setLimit(existingCreditTypeDetail.getLimit());
            existingCreditTypeDetailView.setProductProgram(existingCreditTypeDetail.getProductProgram());
            existingCreditTypeDetailView.setUseCount(existingCreditTypeDetail.getUseCount());
            existingCreditTypeDetailViewList.add(existingCreditTypeDetailView);
        }

        return existingCreditTypeDetailViewList;
    }
}
