package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class BasicInfoTransform extends Transform {
    @Inject
    BasicInfoAccountTransform basicInfoAccountTransform;

    @Inject
    public BasicInfoTransform() {
    }

    public BasicInfo transformToModel(BasicInfoView basicInfoView, WorkCase workCase, User user) {
        BasicInfo basicInfo = new BasicInfo();

        basicInfo.setWorkCase(workCase);

        if (basicInfoView.getId() != 0) {
            basicInfo.setId(basicInfoView.getId());
            basicInfo.setCreateDate(basicInfoView.getCreateDate());
            basicInfo.setCreateBy(basicInfoView.getCreateBy());
        } else {
            basicInfo.setCreateDate(new Date());
            basicInfo.setCreateBy(user);
        }
        basicInfo.setModifyDate(new Date());
        basicInfo.setModifyBy(user);

        basicInfo.setRequestType(basicInfoView.getRequestType());
        if (basicInfo.getRequestType().getId() == 0) {
            basicInfo.setRequestType(null);
        }

        basicInfo.setProductGroup(basicInfoView.getProductGroup());
        if (basicInfo.getProductGroup().getId() == 0) {
            basicInfo.setProductGroup(null);
        }

        basicInfo.setUnpaidFeeInsurance(basicInfoView.isCharUnPaid() ? 2 : 1);
        basicInfo.setNoPendingClaimLG(basicInfoView.isCharNoPending() ? 2 : 1);

        basicInfo.setConstructionRequestLG(basicInfoView.isCharFCLG() ? 2 : 1);
        basicInfo.setAbleToGettingGuarantorJob(basicInfoView.isCharFCIns() ? 2 : 1);
        basicInfo.setNoClaimLGHistory(basicInfoView.isCharFCCom() ? 2 : 1);
        basicInfo.setNoRevokedLicense(basicInfoView.isCharFCAba() ? 2 : 1);
        basicInfo.setNoLateWorkDelivery(basicInfoView.isCharFCLate() ? 2 : 1);
        basicInfo.setAdequateOfCapitalResource(basicInfoView.isCharFCFund() ? 2 : 1);

        basicInfo.setApplySpecialProgram(basicInfoView.getSpProgram());
        basicInfo.setSpecialProgram(basicInfoView.getSpecialProgram());
        if (basicInfo.getSpecialProgram().getId() == 0) {
            basicInfo.setSpecialProgram(null);
        }

        basicInfo.setRefinanceIN(basicInfoView.getRefIn());
        basicInfo.setRefinanceInValue(basicInfoView.getRefinanceIn());
        if (basicInfo.getRefinanceInValue().getCode() == 0) {
            basicInfo.setRefinanceInValue(null);
        }

        basicInfo.setRefinanceOUT(basicInfoView.getRefOut());
        basicInfo.setRefinanceOutValue(basicInfoView.getRefinanceOut());
        if (basicInfo.getRefinanceOutValue().getCode() == 0) {
            basicInfo.setRefinanceOutValue(null);
        }

        basicInfo.setRiskCustomerType(basicInfoView.getRiskType());
        if (basicInfo.getRiskCustomerType().getId() == 0) {
            basicInfo.setRiskCustomerType(null);
        }

        basicInfo.setQualitativeType(basicInfoView.getQualitative());
        basicInfo.setExistingSMECustomer(basicInfoView.getExistingSME());
        basicInfo.setExistingSMECustomerSince(basicInfoView.getSince());
        basicInfo.setLastReviewDate(basicInfoView.getLastReviewDate());
        basicInfo.setExtendedReviewDate(basicInfoView.getExtReviewDate());

        basicInfo.setSbfScore(basicInfoView.getSbfScore());
        if (basicInfo.getSbfScore().getId() == 0) {
            basicInfo.setSbfScore(null);
        }

        basicInfo.setRequestLoanWithSameName(basicInfoView.getLoan());
        basicInfo.setHaveLoanInOneYear(basicInfoView.getMoreOneYear());
        basicInfo.setPassAnnualReview(basicInfoView.getAnnual());

        basicInfo.setLoanRequestPattern(basicInfoView.getLoanRequestPattern());
        if (basicInfo.getLoanRequestPattern().getId() == 0) {
            basicInfo.setLoanRequestPattern(null);
        }

        basicInfo.setReferralName(basicInfoView.getRefName());
        basicInfo.setReferralID(basicInfoView.getRefId());

        basicInfo.setApplyBA(basicInfoView.getApplyBA());
        basicInfo.setBaPaymentMethod(basicInfoView.getBaPaymentMethod());
        if (basicInfo.getBaPaymentMethod().getId() == 0) {
            basicInfo.setBaPaymentMethod(null);
        }

        return basicInfo;
    }

    public BasicInfoView transformToView(BasicInfo basicInfo, WorkCase workCase) {
        BasicInfoView basicInfoView = new BasicInfoView();

        basicInfoView.setId(basicInfo.getId());

        basicInfoView.setAppNo(workCase.getAppNumber());
        basicInfoView.setRefAppNo(workCase.getRefAppNumber());
        basicInfoView.setCaNo(workCase.getCaNumber());

        basicInfoView.setId(basicInfo.getId());


        basicInfoView.setRequestType(basicInfo.getRequestType());
        if (basicInfoView.getRequestType() == null) {
            basicInfoView.setRequestType(new RequestType());
        }

        basicInfoView.setProductGroup(basicInfo.getProductGroup());
        if (basicInfoView.getProductGroup() == null) {
            basicInfoView.setProductGroup(new ProductGroup());
        }

        basicInfoView.setCharUnPaid(basicInfo.getUnpaidFeeInsurance() != 1);
        basicInfoView.setCharNoPending(basicInfo.getNoPendingClaimLG() != 1);

        basicInfoView.setCharFCLG(basicInfo.getConstructionRequestLG() != 1);
        basicInfoView.setCharFCIns(basicInfo.getAbleToGettingGuarantorJob() != 1);
        basicInfoView.setCharFCCom(basicInfo.getNoClaimLGHistory() != 1);
        basicInfoView.setCharFCAba(basicInfo.getNoRevokedLicense() != 1);
        basicInfoView.setCharFCLate(basicInfo.getNoLateWorkDelivery() != 1);
        basicInfoView.setCharFCFund(basicInfo.getAdequateOfCapitalResource() != 1);

        basicInfoView.setSpProgram(basicInfo.getApplySpecialProgram());

        basicInfoView.setSpecialProgram(basicInfo.getSpecialProgram());
        if (basicInfoView.getSpecialProgram() == null) {
            basicInfoView.setSpecialProgram(new SpecialProgram());
        }

        basicInfoView.setRefIn(basicInfo.getRefinanceIN());
        basicInfoView.setRefinanceIn(basicInfo.getRefinanceInValue());
        if (basicInfoView.getRefinanceIn() == null) {
            basicInfoView.setRefinanceIn(new Bank());
        }

        basicInfoView.setRefOut(basicInfo.getRefinanceOUT());
        basicInfoView.setRefinanceOut(basicInfo.getRefinanceOutValue());
        if (basicInfoView.getRefinanceOut() == null) {
            basicInfoView.setRefinanceOut(new Bank());
        }

        basicInfoView.setRiskType(basicInfo.getRiskCustomerType());
        if (basicInfoView.getRiskType() == null) {
            basicInfoView.setRiskType(new RiskType());
        }

        basicInfoView.setQualitative(basicInfo.getQualitativeType());
        basicInfoView.setExistingSME(basicInfo.getExistingSMECustomer());
        basicInfoView.setSince(basicInfo.getExistingSMECustomerSince());
        basicInfoView.setLastReviewDate(basicInfo.getLastReviewDate());
        basicInfoView.setExtReviewDate(basicInfo.getExtendedReviewDate());

        basicInfoView.setSbfScore(basicInfo.getSbfScore());
        if (basicInfoView.getSbfScore() == null) {
            basicInfoView.setSbfScore(new SBFScore());
        }

        basicInfoView.setLoan(basicInfo.getRequestLoanWithSameName());
        basicInfoView.setMoreOneYear(basicInfo.getHaveLoanInOneYear());
        basicInfoView.setAnnual(basicInfo.getPassAnnualReview());

        basicInfoView.setLoanRequestPattern(basicInfo.getLoanRequestPattern());
        if (basicInfoView.getLoanRequestPattern() == null) {
            basicInfoView.setLoanRequestPattern(new BorrowingType());
        }

        basicInfoView.setRefName(basicInfo.getReferralName());
        basicInfoView.setRefId(basicInfo.getReferralID());

        basicInfoView.setApplyBA(basicInfo.getApplyBA());
        basicInfoView.setBaPaymentMethod(basicInfo.getBaPaymentMethod());
        if (basicInfoView.getBaPaymentMethod() == null) {
            basicInfoView.setBaPaymentMethod(new BAPaymentMethod());
        }

        basicInfoView.setCreateDate(basicInfo.getCreateDate());
        basicInfoView.setCreateBy(basicInfo.getCreateBy());
        basicInfoView.setModifyDate(basicInfo.getModifyDate());
        basicInfoView.setModifyBy(basicInfo.getModifyBy());

        List<BasicInfoAccountView> basicInfoAccountViewList = basicInfoAccountTransform.transformToViewList(basicInfo.getOpenAccountList());

        basicInfoView.setBasicInfoAccountViews(basicInfoAccountViewList);

        return basicInfoView;
    }
}
