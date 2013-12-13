package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.view.ExistingCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCreditDetailTransform extends Transform {

    @Inject
    public ExistingCreditDetailTransform() {
    }

    public List<ExistingCreditDetail> transformsToModel(List<ExistingCreditDetailView> existingCreditDetailViewList, ExistingCreditFacility existingCreditFacility, User user){

        List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();
        ExistingCreditDetail existingCreditDetail;

        for (ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViewList) {
            existingCreditDetail = new ExistingCreditDetail();
            if (existingCreditDetailView.getId() != 0) {
                existingCreditDetail.setId(existingCreditDetailView.getId());
                existingCreditDetail.setCreateDate(existingCreditDetailView.getCreateDate());
                existingCreditDetail.setCreateBy(existingCreditDetailView.getCreateBy());
            } else { // id = 0 create new
                existingCreditDetail.setCreateDate(new Date());
                existingCreditDetail.setCreateBy(user);
            }

            existingCreditDetail.setSeq(existingCreditDetailView.getSeq());
            existingCreditDetail.setCreateDate(existingCreditDetail.getCreateDate());
            existingCreditDetail.setCreateBy(existingCreditDetail.getCreateBy());
            existingCreditDetail.setModifyDate(existingCreditDetail.getModifyDate());
            existingCreditDetail.setModifyBy(existingCreditDetail.getModifyBy());
            existingCreditDetail.setBorrowerType(existingCreditDetail.getBorrowerType());
            existingCreditDetail.setExistingCreditFrom(existingCreditDetail.getExistingCreditFrom());
            existingCreditDetail.setCreditType(existingCreditDetail.getCreditType());
            existingCreditDetail.setProductProgram(existingCreditDetail.getProductProgram());
            existingCreditDetail.setInstallment(existingCreditDetail.getInstallment());
            existingCreditDetail.setLimit(existingCreditDetail.getLimit());
            existingCreditDetail.setOutstanding(existingCreditDetail.getOutstanding());
            existingCreditDetail.setPceLimit(existingCreditDetail.getPceLimit());
            existingCreditDetail.setPcePercent(existingCreditDetail.getPcePercent());
            existingCreditDetail.setProductCode(existingCreditDetail.getProductCode());
            existingCreditDetail.setProjectCode(existingCreditDetail.getProjectCode());
            existingCreditDetailView.setTenor(existingCreditDetail.getTenor());
            existingCreditDetail.setTenor(existingCreditDetailView.getTenor());
            existingCreditDetail.setExistingCreditFacility(existingCreditFacility);

            existingCreditDetailList.add(existingCreditDetail);
        }

        return existingCreditDetailList;
    }

    public List<ExistingCreditDetailView> transformsToView(List<ExistingCreditDetail> existingCreditDetailList) {

        List<ExistingCreditDetailView> existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();
        ExistingCreditDetailView existingCreditDetailView;

        for (ExistingCreditDetail existingCreditDetail : existingCreditDetailList) {
            existingCreditDetailView = new ExistingCreditDetailView();

            existingCreditDetailView.setCreateDate(existingCreditDetail.getCreateDate());
            existingCreditDetailView.setCreateBy(existingCreditDetail.getCreateBy());
            existingCreditDetailView.setModifyDate(existingCreditDetail.getModifyDate());
            existingCreditDetailView.setModifyBy(existingCreditDetail.getModifyBy());
            existingCreditDetailView.setSeq(existingCreditDetail.getSeq());
            /*existingCreditDetailView.setBorrowerName(existingCreditDetail.getBorrowerName());
            existingCreditDetailView.setDisbursement(existingCreditDetail.getDisbursement());
            existingCreditDetailView.setFinalPrice(existingCreditDetail.getFinalPrice());*/
            existingCreditDetailView.setCreditType(existingCreditDetail.getCreditType());
            existingCreditDetailView.setProductProgram(existingCreditDetail.getProductProgram());
            /*existingCreditDetailView.setFrontEndFee(existingCreditDetail.getFrontEndFee());
            existingCreditDetailView.setHoldLimitAmount(existingCreditDetail.getHoldLimitAmount());*/
            existingCreditDetailView.setInstallment(existingCreditDetail.getInstallment());
            existingCreditDetailView.setLimit(existingCreditDetail.getLimit());
            //existingCreditDetailView.setLoanPurpose(existingCreditDetail.getLoanPurpose());
            existingCreditDetailView.setOutstanding(existingCreditDetail.getOutstanding());
            //existingCreditDetailView.setPCEAmount(existingCreditDetail.getPceAmount());
            existingCreditDetailView.setPceLimit(existingCreditDetail.getPceLimit());
            existingCreditDetailView.setPcePercent(existingCreditDetail.getPcePercent());
            existingCreditDetailView.setProductCode(existingCreditDetail.getProductCode());
            existingCreditDetailView.setProjectCode(existingCreditDetail.getProjectCode());
            /*existingCreditDetailView.setPurpose(existingCreditDetail.getPurpose());
            existingCreditDetailView.setReduceFrontEndFee(existingCreditDetail.getReduceFrontEndFee());
            existingCreditDetailView.setReducePriceFlag(existingCreditDetail.getReducePriceFlag());
            existingCreditDetailView.setRemark(existingCreditDetail.getRemark());
            existingCreditDetailView.setStandardInterest(existingCreditDetail.getStandardInterest());
            existingCreditDetailView.setStandardBasePrice(existingCreditDetail.getStandardBasePrice());
            existingCreditDetailView.setStandardPrice(existingCreditDetail.getStandardPrice());
            existingCreditDetailView.setSuggestInterest(existingCreditDetail.getSuggestInterest());
            existingCreditDetailView.setSuggestBasePrice(existingCreditDetail.getSuggestBasePrice());
            existingCreditDetailView.setSuggestPrice(existingCreditDetail.getSuggestPrice());*/
            existingCreditDetailView.setTenor(existingCreditDetail.getTenor());

            existingCreditDetailViewList.add(existingCreditDetailView);
        }

        return existingCreditDetailViewList;
    }
}
