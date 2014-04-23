package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.NewCreditTierDetailDAO;
import com.clevel.selos.integration.SELOS;
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
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditDetailTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public NewCreditDetailTransform() {}

    @Inject
    NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    private NewCreditTierTransform newCreditTierTransform;
    @Inject
    private DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    private ProductTransform productTransform;
    @Inject
    private LoanPurposeTransform loanPurposeTransform;
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;
    @Inject
    BaseRateDAO baseRateDAO;

    public List<NewCreditDetail> transformToModel(List<NewCreditDetailView> newCreditDetailViews, NewCreditFacility newCreditFacility ,User user , WorkCase workCase, ProposeType proposeType) {
        List<NewCreditDetail> newCreditDetailList = new ArrayList<NewCreditDetail>();
        NewCreditDetail newCreditDetail;

        for (NewCreditDetailView newCreditDetailView : newCreditDetailViews) {
            //---- Start Transform for NewCreditDetail ------//
            log.debug("Start.. transformToModel for newCreditDetailView : {}", newCreditDetailView);
            newCreditDetail = new NewCreditDetail();
            if (newCreditDetailView.getId() != 0) {
                //newCreditDetail.setId(newCreditDetailView.getId());
                newCreditDetail = newCreditDetailDAO.findById(newCreditDetailView.getId());
                newCreditDetail.setProposeCreditTierDetailList(new ArrayList<NewCreditTierDetail>());
                newCreditDetail.setModifyDate(DateTime.now().toDate());
                newCreditDetail.setModifyBy(user);
            } else { // id = 0 create new
                newCreditDetail.setCreateDate(new Date());
                newCreditDetail.setCreateBy(user);
            }

            newCreditDetail.setProposeType(proposeType);
            newCreditDetail.setWorkCase(workCase);
            newCreditDetail.setSeq(newCreditDetailView.getSeq());
            newCreditDetail.setGuaranteeAmount(newCreditDetailView.getGuaranteeAmount());
            newCreditDetail.setAccountNumber(newCreditDetailView.getAccountNumber());
            newCreditDetail.setAccountSuf(newCreditDetailView.getAccountSuf());
            newCreditDetail.setAccountName(newCreditDetailView.getAccountName());
            newCreditDetail.setRequestType(newCreditDetailView.getRequestType());
            newCreditDetail.setRefinance(newCreditDetailView.getRefinance());
            newCreditDetail.setUwDecision(newCreditDetailView.getUwDecision());
            newCreditDetail.setNoFlag(Util.returnNumForFlag(newCreditDetailView.isNoFlag()));
            newCreditDetail.setBorrowerName(newCreditDetailView.getBorrowerName());
            newCreditDetail.setDisbursementType(disbursementTypeTransform.transformToModel(newCreditDetailView.getDisbursementTypeView()));
            newCreditDetail.setCreditType(productTransform.transformToModel(newCreditDetailView.getCreditTypeView()));
            newCreditDetail.setProductProgram(productTransform.transformToModel(newCreditDetailView.getProductProgramView()));
            newCreditDetail.setFrontEndFee(newCreditDetailView.getFrontEndFee());
            newCreditDetail.setHoldLimitAmount(newCreditDetailView.getHoldLimitAmount());
            newCreditDetail.setInstallment(newCreditDetailView.getInstallment());
            newCreditDetail.setLimit(newCreditDetailView.getLimit());
            newCreditDetail.setLoanPurpose(loanPurposeTransform.transformToModel(newCreditDetailView.getLoanPurposeView()));
            newCreditDetail.setOutstanding(newCreditDetailView.getOutstanding());
            newCreditDetail.setPceAmount(newCreditDetailView.getPCEAmount());
            newCreditDetail.setPcePercent(newCreditDetailView.getPCEPercent());
            newCreditDetail.setProductCode(newCreditDetailView.getProductCode());
            newCreditDetail.setProjectCode(newCreditDetailView.getProjectCode());
            newCreditDetail.setPurpose(newCreditDetailView.getPurpose());
            newCreditDetail.setReduceFrontEndFee(Util.returnNumForFlag(newCreditDetailView.isReduceFrontEndFee()));
            newCreditDetail.setReducePriceFlag(Util.returnNumForFlag(newCreditDetailView.isReducePriceFlag()));
            newCreditDetail.setRemark(newCreditDetailView.getRemark());
            newCreditDetail.setUseCount(newCreditDetailView.getUseCount());
            newCreditDetail.setNewCreditFacility(newCreditFacility);
            newCreditDetail.setSetupCompleted(newCreditDetailView.getIsSetupCompleted());

            if(Util.safetyList(newCreditDetailView.getNewCreditTierDetailViewList()).size() > 0){
                log.debug("Start.. transformToModel for newCreditTierDetailViewList : {}", newCreditDetailView.getNewCreditTierDetailViewList());
                List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList(), newCreditDetail, user);
                log.debug("End.. transformToModel for newCreditTierDetailList : {}", newCreditTierDetailList);
                newCreditDetail.setProposeCreditTierDetailList(newCreditTierDetailList);
            }
            log.debug("End.. transformToModel for newCreditDetail : {}", newCreditDetail);
            newCreditDetailList.add(newCreditDetail);
        }
        return newCreditDetailList;
    }

    public List<NewCreditDetailView> transformToView(List<NewCreditDetail> newCreditDetailList) {
        List<NewCreditDetailView> newCreditDetailViewList = new ArrayList<NewCreditDetailView>();
        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
            newCreditDetailViewList.add(transformToView(newCreditDetail));
        }
        return newCreditDetailViewList;
    }

    public NewCreditDetailView transformToView(NewCreditDetail newCreditDetail) {
    	 NewCreditDetailView newCreditDetailView = new NewCreditDetailView();

         newCreditDetailView.setId(newCreditDetail.getId());
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
         newCreditDetailView.setUwDecision(newCreditDetail.getUwDecision());
         newCreditDetailView.setNoFlag(Util.isTrue(newCreditDetail.getNoFlag()));
         newCreditDetailView.setBorrowerName(newCreditDetail.getBorrowerName());
         newCreditDetailView.setDisbursementTypeView(disbursementTypeTransform.transformToView(newCreditDetail.getDisbursementType()));
         newCreditDetailView.setCreditTypeView(productTransform.transformToView(newCreditDetail.getCreditType()));
         newCreditDetailView.setProductProgramView(productTransform.transformToView(newCreditDetail.getProductProgram()));
         newCreditDetailView.setFrontEndFee(newCreditDetail.getFrontEndFee());
         newCreditDetailView.setHoldLimitAmount(newCreditDetail.getHoldLimitAmount());
         newCreditDetailView.setInstallment(newCreditDetail.getInstallment());
         newCreditDetailView.setLimit(newCreditDetail.getLimit());
         newCreditDetailView.setLoanPurposeView(loanPurposeTransform.transformToView(newCreditDetail.getLoanPurpose()));
         newCreditDetailView.setOutstanding(newCreditDetail.getOutstanding());
         newCreditDetailView.setPCEAmount(newCreditDetail.getPceAmount());
         newCreditDetailView.setPCEPercent(newCreditDetail.getPcePercent());
         newCreditDetailView.setProductCode(newCreditDetail.getProductCode());
         newCreditDetailView.setProjectCode(newCreditDetail.getProjectCode());
         newCreditDetailView.setPurpose(newCreditDetail.getPurpose());
         newCreditDetailView.setReduceFrontEndFee(Util.isTrue(newCreditDetail.getReduceFrontEndFee()));
         newCreditDetailView.setReducePriceFlag(Util.isTrue(newCreditDetail.getReducePriceFlag()));
         newCreditDetailView.setRemark(newCreditDetail.getRemark());
         newCreditDetailView.setUseCount(newCreditDetail.getUseCount());
         newCreditDetailView.setIsSetupCompleted(newCreditDetail.getSetupCompleted());

         List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierDetailDAO.findByNewCreditDetail(newCreditDetail);

         if (newCreditTierDetailList.size()>0)
         {
             List<NewCreditTierDetailView> newCreditTierDetailViewList = newCreditTierTransform.transformToView(newCreditTierDetailList);
             newCreditDetailView.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
         }
         return newCreditDetailView;
    }

    public NewCreditDetailView copyToNewView(NewCreditDetailView originalNewCreditDetailView, ProposeType proposeType, boolean isNewId) {
        NewCreditDetailView newCreditDetailView = new NewCreditDetailView();
        if (originalNewCreditDetailView != null) {
            newCreditDetailView.setId(isNewId ? 0 : originalNewCreditDetailView.getId());
            newCreditDetailView.setProposeType(proposeType);
            newCreditDetailView.setCreateBy(originalNewCreditDetailView.getCreateBy());
            newCreditDetailView.setCreateDate(originalNewCreditDetailView.getCreateDate());
            newCreditDetailView.setModifyBy(originalNewCreditDetailView.getModifyBy());
            newCreditDetailView.setModifyDate(originalNewCreditDetailView.getModifyDate());
            newCreditDetailView.setSeq(originalNewCreditDetailView.getSeq());
            newCreditDetailView.setGuaranteeAmount(originalNewCreditDetailView.getGuaranteeAmount());
            newCreditDetailView.setAccountNumber(originalNewCreditDetailView.getAccountNumber());
            newCreditDetailView.setAccountSuf(originalNewCreditDetailView.getAccountSuf());
            newCreditDetailView.setAccountName(originalNewCreditDetailView.getAccountName());
            newCreditDetailView.setRequestType(originalNewCreditDetailView.getRequestType());
            newCreditDetailView.setRefinance(originalNewCreditDetailView.getRefinance());
            newCreditDetailView.setUwDecision(originalNewCreditDetailView.getUwDecision());
            newCreditDetailView.setNoFlag(originalNewCreditDetailView.isNoFlag());
            newCreditDetailView.setBorrowerName(originalNewCreditDetailView.getBorrowerName());
            newCreditDetailView.setDisbursementTypeView(originalNewCreditDetailView.getDisbursementTypeView());
            newCreditDetailView.setCreditTypeView(originalNewCreditDetailView.getCreditTypeView());
            newCreditDetailView.setProductProgramView(originalNewCreditDetailView.getProductProgramView());
            newCreditDetailView.setFrontEndFee(originalNewCreditDetailView.getFrontEndFee());
            newCreditDetailView.setHoldLimitAmount(originalNewCreditDetailView.getHoldLimitAmount());
            newCreditDetailView.setInstallment(originalNewCreditDetailView.getInstallment());
            newCreditDetailView.setLimit(originalNewCreditDetailView.getLimit());
            newCreditDetailView.setLoanPurposeView(originalNewCreditDetailView.getLoanPurposeView());
            newCreditDetailView.setOutstanding(originalNewCreditDetailView.getOutstanding());
            newCreditDetailView.setPCEAmount(originalNewCreditDetailView.getPCEAmount());
            newCreditDetailView.setPCEPercent(originalNewCreditDetailView.getPCEPercent());
            newCreditDetailView.setProductCode(originalNewCreditDetailView.getProductCode());
            newCreditDetailView.setProjectCode(originalNewCreditDetailView.getProjectCode());
            newCreditDetailView.setPurpose(originalNewCreditDetailView.getPurpose());
            newCreditDetailView.setReduceFrontEndFee(originalNewCreditDetailView.isReduceFrontEndFee());
            newCreditDetailView.setReducePriceFlag(originalNewCreditDetailView.isReducePriceFlag());
            newCreditDetailView.setRemark(originalNewCreditDetailView.getRemark());
            newCreditDetailView.setUseCount(originalNewCreditDetailView.getUseCount());
            newCreditDetailView.setIsSetupCompleted(originalNewCreditDetailView.getIsSetupCompleted());
        }
        return newCreditDetailView;
    }

    public List<NewCreditDetailView> copyToNewViews(List<NewCreditDetailView> originalNewCreditDetailViews, ProposeType proposeType, boolean isNewId) {
        List<NewCreditDetailView> newCreditDetailViewList = new ArrayList<NewCreditDetailView>();
        if (originalNewCreditDetailViews != null && originalNewCreditDetailViews.size() > 0) {
            for (NewCreditDetailView originalCreditDetailView : originalNewCreditDetailViews) {
                newCreditDetailViewList.add(copyToNewView(originalCreditDetailView, proposeType, isNewId));
            }
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
