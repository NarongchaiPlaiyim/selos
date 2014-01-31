package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BAPaymentMethodDAO;
import com.clevel.selos.dao.working.BAPAInfoDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BasicInfoView;

import javax.inject.Inject;
import java.util.Date;

public class BasicInfoTransform extends Transform {
    @Inject
    OpenAccountTransform openAccountTransform;
    @Inject
    SBFScoreTransform sbfScoreTransform;
    @Inject
    OpenAccountTransform getOpenAccountTransform;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    BAPAInfoDAO bapaInfoDAO;
    @Inject
    BAPaymentMethodDAO baPaymentMethodDAO;

    @Inject
    public BasicInfoTransform() {
    }

    public BasicInfo transformToModel(BasicInfoView basicInfoView, WorkCase workCase, User user){
        BasicInfo basicInfo = new BasicInfo();

        basicInfo.setWorkCase(workCase);

        if(basicInfoView.getId() != 0){
            basicInfo = basicInfoDAO.findById(basicInfoView.getId());
            basicInfo.setCreateDate(basicInfoView.getCreateDate());
            basicInfo.setCreateBy(basicInfoView.getCreateBy());
        } else {
            basicInfo.setCreateDate(new Date());
            basicInfo.setCreateBy(user);
        }
        basicInfo.setModifyDate(new Date());
        basicInfo.setModifyBy(user);

        basicInfo.setNoUnpaidFeeInsurance(basicInfoView.isCharNoUnPaid() ? 1 : 0);
        basicInfo.setNoPendingClaimLG(basicInfoView.isCharNoPending() ? 1 : 0);

        basicInfo.setConstructionRequestLG(basicInfoView.isCharFCLG() ? 1 : 0);
        basicInfo.setAbleToGettingGuarantorJob(basicInfoView.isCharFCIns() ? 1 : 0);
        basicInfo.setNoClaimLGHistory(basicInfoView.isCharFCCom() ? 1 : 0);
        basicInfo.setNoRevokedLicense(basicInfoView.isCharFCAba() ? 1 : 0);
        basicInfo.setNoLateWorkDelivery(basicInfoView.isCharFCLate() ? 1 : 0);
        basicInfo.setAdequateOfCapitalResource(basicInfoView.isCharFCFund() ? 1 : 0);

        basicInfo.setApplySpecialProgram(basicInfoView.getSpProgram());
        basicInfo.setSpecialProgram(basicInfoView.getSpecialProgram());
        if(basicInfo.getSpecialProgram().getId() == 0){
            basicInfo.setSpecialProgram(null);
        }

        basicInfo.setRefinanceIN(basicInfoView.getRefIn());
        basicInfo.setRefinanceInValue(basicInfoView.getRefinanceIn());
        if(basicInfo.getRefinanceInValue().getCode() == 0){
            basicInfo.setRefinanceInValue(null);
        }

        basicInfo.setRefinanceOUT(basicInfoView.getRefOut());
        basicInfo.setRefinanceOutValue(basicInfoView.getRefinanceOut());
        if(basicInfo.getRefinanceOutValue().getCode() == 0){
            basicInfo.setRefinanceOutValue(null);
        }

        basicInfo.setRiskCustomerType(basicInfoView.getRiskType());
        if(basicInfo.getRiskCustomerType().getId() == 0){
            basicInfo.setRiskCustomerType(null);
        }

        basicInfo.setQualitativeType(basicInfoView.getQualitative());
        basicInfo.setExistingSMECustomer(basicInfoView.getExistingSME());
        basicInfo.setExistingSMECustomerSince(basicInfoView.getSince());
        basicInfo.setLastReviewDate(basicInfoView.getLastReviewDate());
        basicInfo.setExtendedReviewDate(basicInfoView.getExtReviewDate());

        basicInfo.setSbfScore(sbfScoreTransform.transformToModel(basicInfoView.getSbfScoreView()));
        if(basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getId() == 0){
            basicInfo.setSbfScore(null);
        }

        basicInfo.setRequestLoanWithSameName(basicInfoView.getLoan());
        basicInfo.setHaveLoanInOneYear(basicInfoView.getMoreOneYear());
        basicInfo.setPassAnnualReview(basicInfoView.getAnnual());

        basicInfo.setLoanRequestPattern(basicInfoView.getLoanRequestPattern());
        if(basicInfo.getLoanRequestPattern().getId() == 0){
            basicInfo.setLoanRequestPattern(null);
        }

        basicInfo.setReferralName(basicInfoView.getRefName());
        basicInfo.setReferralID(basicInfoView.getRefId());

        return basicInfo;
    }

    public BasicInfoView transformToView(BasicInfo basicInfo,WorkCase workCase){
        BasicInfoView basicInfoView = new BasicInfoView();

        basicInfoView.setId(basicInfo.getId());

        basicInfoView.setAppNo(workCase.getAppNumber());
        basicInfoView.setRefAppNo(basicInfo.getRefAppNumber());
        basicInfoView.setCaNo(basicInfo.getCaNumber());

        basicInfoView.setId(basicInfo.getId());

        basicInfoView.setRequestType(basicInfo.getRequestType());
        if(basicInfoView.getRequestType() == null){
            basicInfoView.setRequestType(new RequestType());
        }

        basicInfoView.setProductGroup(basicInfo.getProductGroup());
        if(basicInfoView.getProductGroup() == null){
            basicInfoView.setProductGroup(new ProductGroup());
        }

        basicInfoView.setCharNoUnPaid(basicInfo.getNoUnpaidFeeInsurance() != 0);
        basicInfoView.setCharNoPending(basicInfo.getNoPendingClaimLG() != 0);

        basicInfoView.setCharFCLG(basicInfo.getConstructionRequestLG() != 0);
        basicInfoView.setCharFCIns(basicInfo.getAbleToGettingGuarantorJob() != 0);
        basicInfoView.setCharFCCom(basicInfo.getNoClaimLGHistory() != 0);
        basicInfoView.setCharFCAba(basicInfo.getNoRevokedLicense() != 0);
        basicInfoView.setCharFCLate(basicInfo.getNoLateWorkDelivery() != 0);
        basicInfoView.setCharFCFund(basicInfo.getAdequateOfCapitalResource() != 0);

        basicInfoView.setSpProgram(basicInfo.getApplySpecialProgram());

        basicInfoView.setSpecialProgram(basicInfo.getSpecialProgram());
        if(basicInfoView.getSpecialProgram() == null){
            basicInfoView.setSpecialProgram(new SpecialProgram());
        }

        basicInfoView.setRefIn(basicInfo.getRefinanceIN());
        basicInfoView.setRefinanceIn(basicInfo.getRefinanceInValue());
        if(basicInfoView.getRefinanceIn() == null){
            basicInfoView.setRefinanceIn(new Bank());
        }

        basicInfoView.setRefOut(basicInfo.getRefinanceOUT());
        basicInfoView.setRefinanceOut(basicInfo.getRefinanceOutValue());
        if(basicInfoView.getRefinanceOut() == null){
            basicInfoView.setRefinanceOut(new Bank());
        }

        basicInfoView.setRiskType(basicInfo.getRiskCustomerType());
        if(basicInfoView.getRiskType() == null){
            basicInfoView.setRiskType(new RiskType());
        }

        basicInfoView.setQualitative(basicInfo.getQualitativeType());
        basicInfoView.setExistingSME(basicInfo.getExistingSMECustomer());
        basicInfoView.setSince(basicInfo.getExistingSMECustomerSince());
        basicInfoView.setLastReviewDate(basicInfo.getLastReviewDate());
        basicInfoView.setExtReviewDate(basicInfo.getExtendedReviewDate());

        basicInfoView.setSbfScoreView(sbfScoreTransform.transformToView(basicInfo.getSbfScore()));

        basicInfoView.setLoan(basicInfo.getRequestLoanWithSameName());
        basicInfoView.setMoreOneYear(basicInfo.getHaveLoanInOneYear());
        basicInfoView.setAnnual(basicInfo.getPassAnnualReview());

        basicInfoView.setLoanRequestPattern(basicInfo.getLoanRequestPattern());
        if(basicInfoView.getLoanRequestPattern() == null){
            basicInfoView.setLoanRequestPattern(new BorrowingType());
        }

        basicInfoView.setRefName(basicInfo.getReferralName());
        basicInfoView.setRefId(basicInfo.getReferralID());

        BAPAInfo bapaInfo = bapaInfoDAO.findByWorkCase(workCase);
        if (bapaInfo == null){
            basicInfoView.setApplyBA(0);
            basicInfoView.setBaPaymentMethod(new BAPaymentMethod());
        } else {
            basicInfoView.setApplyBA(bapaInfo.getApplyBA());
            if(bapaInfo.getId() != 0){
                BAPaymentMethod baPaymentMethod = baPaymentMethodDAO.findById(bapaInfo.getBaPaymentMethod());
                basicInfoView.setBaPaymentMethod(baPaymentMethod);
            } else {
                basicInfoView.setBaPaymentMethod(new BAPaymentMethod());
            }
        }

        basicInfoView.setCreateDate(basicInfo.getCreateDate());
        basicInfoView.setCreateBy(basicInfo.getCreateBy());
        basicInfoView.setModifyDate(basicInfo.getModifyDate());
        basicInfoView.setModifyBy(basicInfo.getModifyBy());

        return basicInfoView;
    }
}
