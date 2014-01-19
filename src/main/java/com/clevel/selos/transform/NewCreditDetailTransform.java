package com.clevel.selos.transform;

import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.model.view.NewCreditTierDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditDetailTransform extends Transform {

    @Inject
    public NewCreditDetailTransform() {}
    @Inject
    NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    NewCreditTierTransform newCreditTierTransform;

/*    public List<NewCreditDetail> getNewCreditDetailForGuarantor(List<NewCreditDetailView> newCreditGrtViews, List<NewCreditDetail> newCreditDetailAll) {
        List<NewCreditDetail> newCreditListReturn = new ArrayList<NewCreditDetail>();
        for (NewCreditDetailView newCreditDetailView : newCreditGrtViews) {
            for (NewCreditDetail newCreditDetail : newCreditDetailAll) {
                if (newCreditDetailView.getSeq() == newCreditDetail.getSeq()) {
                    newCreditDetail.setGuaranteeAmount(newCreditDetailView.getGuaranteeAmount());
                    newCreditListReturn.add(newCreditDetail);
                }
            }
        }

        return newCreditListReturn;
    }

    public List<NewCreditDetail> getNewCreditDetailForCollateral(List<NewCreditDetailView> newCreditCollViews, List<NewCreditDetail> newCreditDetailAll) {
        List<NewCreditDetail> newCreditListReturn = new ArrayList<NewCreditDetail>();
        for (NewCreditDetailView newCreditDetailView : newCreditCollViews) {
            for (NewCreditDetail newCreditDetail : newCreditDetailAll) {
                if (newCreditDetailView.getSeq() == newCreditDetail.getSeq()) {
                    newCreditListReturn.add(newCreditDetail);
                }
            }
        }

        return newCreditListReturn;
    }*/


    public List<NewCreditDetail> transformToModel(List<NewCreditDetailView> newCreditDetailViews, NewCreditFacility newCreditFacility ,User user) {
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
            newCreditDetail.setGuaranteeAmount(newCreditDetailView.getGuaranteeAmount());
            newCreditDetail.setAccountNumber(newCreditDetailView.getAccountNumber());
            newCreditDetail.setAccountSuf(newCreditDetailView.getAccountSuf());
            newCreditDetail.setAccountName(newCreditDetailView.getAccountName());
            newCreditDetail.setRequestType(newCreditDetailView.getRequestType());
            newCreditDetail.setRefinance(newCreditDetailView.getRefinance());
            newCreditDetail.setApproved(newCreditDetailView.getApproved());
            newCreditDetail.setNoFlag(Util.returnNumForFlag(newCreditDetailView.isNoFlag()));
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
            newCreditDetail.setReduceFrontEndFee(Util.returnNumForFlag(newCreditDetailView.isReduceFrontEndFee()));
            newCreditDetail.setReducePriceFlag(Util.returnNumForFlag(newCreditDetailView.isReducePriceFlag()));
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

            newCreditDetailView.setCreateBy(newCreditDetail.getCreateBy());
            newCreditDetailView.setCreateDate(newCreditDetail.getCreateDate());
            newCreditDetailView.setModifyBy(newCreditDetail.getModifyBy());
            newCreditDetailView.setModifyDate(newCreditDetail.getModifyDate());
            newCreditDetailView.setSeq(newCreditDetail.getSeq());
            newCreditDetailView.setGuaranteeAmount(newCreditDetail.getGuaranteeAmount());
            newCreditDetailView.setAccountNumber(newCreditDetail.getAccountNumber());
            newCreditDetailView.setAccountSuf(newCreditDetail.getAccountSuf());
            newCreditDetailView.setAccountName(newCreditDetail.getAccountName());
            newCreditDetailView.setRequestType(newCreditDetail.getRequestType());
            newCreditDetailView.setRefinance(newCreditDetail.getRefinance());
            newCreditDetailView.setApproved(newCreditDetail.getApproved());
            newCreditDetailView.setNoFlag(Util.isTrue(newCreditDetail.getNoFlag()));
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
            newCreditDetailView.setReduceFrontEndFee(Util.isTrue(newCreditDetail.getReduceFrontEndFee()));
            newCreditDetailView.setReducePriceFlag(Util.isTrue(newCreditDetail.getReducePriceFlag()));
            newCreditDetailView.setRemark(newCreditDetail.getRemark());
            newCreditDetailView.setStandardInterest(newCreditDetail.getStandardInterest());
            newCreditDetailView.setStandardBasePrice(newCreditDetail.getStandardBasePrice());
            newCreditDetailView.setStandardPrice(newCreditDetail.getStandardPrice());
            newCreditDetailView.setSuggestInterest(newCreditDetail.getSuggestInterest());
            newCreditDetailView.setSuggestBasePrice(newCreditDetail.getSuggestBasePrice());
            newCreditDetailView.setSuggestPrice(newCreditDetail.getSuggestPrice());
            newCreditDetailView.setTenor(newCreditDetail.getTenor());

            if (newCreditDetail.getProposeCreditTierDetailList() != null) {
                List<NewCreditTierDetailView> newCreditTierDetailViewList = newCreditTierTransform.transformToView(newCreditDetail.getProposeCreditTierDetailList());
                newCreditDetailView.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            }

            newCreditDetailViewList.add(newCreditDetailView);
        }

        return newCreditDetailViewList;
    }

}
