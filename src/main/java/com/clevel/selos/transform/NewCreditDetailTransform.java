package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.view.NewCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditDetailTransform extends Transform {

    @Inject
    public NewCreditDetailTransform() {
    }

    public List<NewCreditDetail> transformToModel(List<NewCreditDetailView> newCreditDetailViews, NewCreditFacility newCreditFacility, User user){

        List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();
        NewCreditDetail newCreditDetail;

        for (NewCreditDetailView newCreditDetailView : newCreditDetailViews) {
            newCreditDetail = new NewCreditDetail();
            if (newCreditDetailView.getId() != 0) {
                newCreditDetail.setId(newCreditDetailView.getId());
                newCreditDetail.setCreateDate(newCreditDetailView.getCreateDate());
                newCreditDetail.setCreateBy(newCreditDetailView.getCreateBy());
            } else { // id = 0 create new
                newCreditDetail.setCreateDate(new Date());
                newCreditDetail.setCreateBy(user);
            }

            newCreditDetail.setSeq(newCreditDetailView.getSeq());
            newCreditDetail.setBorrowerName(newCreditDetailView.getBorrowerName());
            newCreditDetail.setDisbursement(newCreditDetailView.getDisbursement());
            newCreditDetail.setFinalPrice(newCreditDetailView.getFinalPrice());
            newCreditDetail.setCreditType(newCreditDetailView.getCreditType());
            newCreditDetail.setProductProgram(newCreditDetailView.getProductProgram());
            newCreditDetail.setFrontEndFee(newCreditDetailView.getFrontEndFee());
            newCreditDetail.setHoldLimitAmount(newCreditDetailView.getHoldLimitAmount());
            newCreditDetail.setInstallment(newCreditDetailView.getInstallment());
            newCreditDetail.setLimit(newCreditDetailView.getLimit());
            newCreditDetail.setLoanPurpose(newCreditDetailView.getLoanPurpose());
            newCreditDetail.setOutstanding(newCreditDetailView.getOutstanding());
            newCreditDetail.setPceAmount(newCreditDetailView.getPCEAmount());
            newCreditDetail.setPcePercent(newCreditDetailView.getPCEPercent());
            newCreditDetail.setProductCode(newCreditDetailView.getProductCode());
            newCreditDetail.setProjectCode(newCreditDetailView.getProjectCode());
            newCreditDetail.setPurpose(newCreditDetailView.getPurpose());
            newCreditDetail.setReduceFrontEndFee(newCreditDetailView.getReduceFrontEndFee());
            newCreditDetail.setReducePriceFlag(newCreditDetailView.getReducePriceFlag());
            newCreditDetail.setRemark(newCreditDetailView.getRemark());
            newCreditDetail.setStandardInterest(newCreditDetailView.getStandardInterest());
            newCreditDetail.setStandardBasePrice(newCreditDetailView.getStandardBasePrice());
            newCreditDetail.setStandardPrice(newCreditDetailView.getStandardPrice());
            newCreditDetail.setSuggestInterest(newCreditDetailView.getSuggestInterest());
            newCreditDetail.setSuggestBasePrice(newCreditDetailView.getSuggestBasePrice());
            newCreditDetail.setSuggestPrice(newCreditDetailView.getSuggestPrice());
            newCreditDetail.setTenor(newCreditDetailView.getTenor());
            newCreditDetail.setNewCreditFacility(newCreditFacility);

            newCreditDetailList.add(newCreditDetail);
        }

        return newCreditDetailList;
    }

    public List<NewCreditDetailView> transformToView(List<NewCreditDetail> newCreditDetailList) {

        List<NewCreditDetailView> newCreditDetailViewList = new ArrayList<NewCreditDetailView>();
        NewCreditDetailView newCreditDetailView;

        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
            newCreditDetailView = new NewCreditDetailView();

            newCreditDetailView.setCreateDate(newCreditDetail.getCreateDate());
            newCreditDetailView.setCreateBy(newCreditDetail.getCreateBy());
            newCreditDetailView.setModifyDate(newCreditDetail.getModifyDate());
            newCreditDetailView.setModifyBy(newCreditDetail.getModifyBy());
            newCreditDetailView.setSeq(newCreditDetail.getSeq());
            newCreditDetailView.setBorrowerName(newCreditDetail.getBorrowerName());
            newCreditDetailView.setDisbursement(newCreditDetail.getDisbursement());
            newCreditDetailView.setFinalPrice(newCreditDetail.getFinalPrice());
            newCreditDetailView.setCreditType(newCreditDetail.getCreditType());
            newCreditDetailView.setProductProgram(newCreditDetail.getProductProgram());
            newCreditDetailView.setFrontEndFee(newCreditDetail.getFrontEndFee());
            newCreditDetailView.setHoldLimitAmount(newCreditDetail.getHoldLimitAmount());
            newCreditDetailView.setInstallment(newCreditDetail.getInstallment());
            newCreditDetailView.setLimit(newCreditDetail.getLimit());
            newCreditDetailView.setLoanPurpose(newCreditDetail.getLoanPurpose());
            newCreditDetailView.setOutstanding(newCreditDetail.getOutstanding());
            newCreditDetailView.setPCEAmount(newCreditDetail.getPceAmount());
            newCreditDetailView.setPCEPercent(newCreditDetail.getPcePercent());
            newCreditDetailView.setProductCode(newCreditDetail.getProductCode());
            newCreditDetailView.setProjectCode(newCreditDetail.getProjectCode());
            newCreditDetailView.setPurpose(newCreditDetail.getPurpose());
            newCreditDetailView.setReduceFrontEndFee(newCreditDetail.getReduceFrontEndFee());
            newCreditDetailView.setReducePriceFlag(newCreditDetail.getReducePriceFlag());
            newCreditDetailView.setRemark(newCreditDetail.getRemark());
            newCreditDetailView.setStandardInterest(newCreditDetail.getStandardInterest());
            newCreditDetailView.setStandardBasePrice(newCreditDetail.getStandardBasePrice());
            newCreditDetailView.setStandardPrice(newCreditDetail.getStandardPrice());
            newCreditDetailView.setSuggestInterest(newCreditDetail.getSuggestInterest());
            newCreditDetailView.setSuggestBasePrice(newCreditDetail.getSuggestBasePrice());
            newCreditDetailView.setSuggestPrice(newCreditDetail.getSuggestPrice());
            newCreditDetailView.setTenor(newCreditDetail.getTenor());

            newCreditDetailViewList.add(newCreditDetailView);
        }

        return newCreditDetailViewList;
    }
}