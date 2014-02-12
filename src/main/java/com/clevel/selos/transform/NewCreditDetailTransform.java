package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.NewCreditTierDetailDAO;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.model.view.NewCreditTierDetailView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
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
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;
    @Inject
    BaseRateDAO baseRateDAO;

    public List<NewCreditDetail> transformToModel(List<NewCreditDetailView> newCreditDetailViews, NewCreditFacility newCreditFacility ,User user , WorkCase workCase) {
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

            newCreditDetail.setProposeType(ProposeType.P.type());
            newCreditDetail.setWorkCase(workCase);
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
            newCreditDetail.setDisbursementType(newCreditDetailView.getDisbursement());
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
            newCreditDetailView.setProposeType(newCreditDetail.getProposeType());
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
            newCreditDetailView.setDisbursement(newCreditDetail.getDisbursementType());
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
            newCreditDetailView.setStandardPrice(toGetPricing(newCreditDetail.getStandardBasePrice(),newCreditDetail.getStandardInterest()));

            List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierDetailDAO.findByNewCreditDetail(newCreditDetail);

            if (newCreditTierDetailList.size()>0)
            {
                List<NewCreditTierDetailView> newCreditTierDetailViewList = newCreditTierTransform.transformToView(newCreditTierDetailList);
                newCreditDetailView.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            }

            newCreditDetailViewList.add(newCreditDetailView);
        }

        return newCreditDetailViewList;
    }

   public String toGetPricing(BaseRate baseRate ,BigDecimal price){
      String priceToShow = "";

       if (price.doubleValue() < 0)
       {
           priceToShow = baseRate.getName() + " " + price;
       }else{
           priceToShow = baseRate.getName() + " + " + price;
       }
        log.info("priceToShow :: {}",priceToShow);
       return priceToShow;
   }

}
