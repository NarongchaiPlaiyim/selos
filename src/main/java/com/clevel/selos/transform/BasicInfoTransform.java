package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BasicInfoView;

import java.util.Date;

public class BasicInfoTransform extends Transform {

    public BasicInfo transformToModel(BasicInfoView basicInfoView, WorkCase workCase, User user){
        BasicInfo basicInfo = new BasicInfo();

        basicInfo.setWorkCase(workCase);

        basicInfo.setRequestType(basicInfoView.getRequestType());
        basicInfo.setProductGroup(basicInfoView.getProductGroup());

        basicInfo.setUnpaidFeeInsurance(basicInfoView.isCharUnPaid());
        basicInfo.setNoPendingClaimLG(basicInfoView.isCharNoPending());

        basicInfo.setConstructionRequestLG(basicInfoView.isCharFCLG());
        basicInfo.setAbleToGettingGuarantorJob(basicInfoView.isCharFCIns());
        basicInfo.setNoClaimLGHistory(basicInfoView.isCharFCCom());
        basicInfo.setNoRevokedLicense(basicInfoView.isCharFCAba());
        basicInfo.setNoLateWorkDelivery(basicInfoView.isCharFCLate());
        basicInfo.setAdequateOfCapitalResource(basicInfoView.isCharFCFund());

        basicInfo.setApplySpecialProgram(basicInfoView.isSpProgram());
        basicInfo.setSpecialProgram(basicInfoView.getSpecialProgram());

        basicInfo.setRefinanceIN(basicInfoView.isRefIn());
        basicInfo.setRefinanceInValue(basicInfoView.getRefinanceIn());

        basicInfo.setRefinanceOUT(basicInfoView.isRefOut());
        basicInfo.setRefinanceOutValue(basicInfoView.getRefinanceOut());

        basicInfo.setRiskCustomerType(basicInfoView.getRiskType());

        basicInfo.setQualitativeType(basicInfoView.getQualitative());
        basicInfo.setExistingSMECustomer(basicInfoView.isExistingSME());
        basicInfo.setExistingSMECustomerSince(basicInfoView.getSince());
        basicInfo.setLastReviewDate(basicInfoView.getLastReviewDate());
        basicInfo.setExtendedReviewDate(basicInfoView.getExtReviewDate());

        basicInfo.setSbfScore(basicInfoView.getSbfScore());

        basicInfo.setRequestLoanWithSameName(basicInfoView.isLoan());
        basicInfo.setHaveLoanInOneYear(basicInfoView.isMoreOneYear());
        basicInfo.setPassAnnualReview(basicInfoView.isAnnual());

        basicInfo.setLoanRequestPattern(basicInfoView.getLoanRequestPattern());

        basicInfo.setReferralName(basicInfoView.getRefName());
        basicInfo.setReferralID(basicInfoView.getRefId());

        basicInfo.setCreateDate(new Date());
        basicInfo.setCreateBy(user);

        return basicInfo;
    }

    public BasicInfoView transformToView(BasicInfo basicInfo,WorkCase workCase){
        BasicInfoView basicInfoView = new BasicInfoView();

        basicInfoView.setAppNo(workCase.getAppNumber());
        basicInfoView.setRefAppNo(workCase.getRefAppNumber());
        basicInfoView.setCaNo(workCase.getCaNumber());

        basicInfoView.setId(basicInfo.getId());


        basicInfoView.setRequestType(basicInfo.getRequestType());
        if(basicInfoView.getRequestType() == null){
            basicInfoView.setRequestType(new RequestType());
        }

        basicInfoView.setProductGroup(basicInfo.getProductGroup());
        if(basicInfoView.getProductGroup() == null){
            basicInfoView.setProductGroup(new ProductGroup());
        }

        basicInfoView.setCharUnPaid(basicInfo.isUnpaidFeeInsurance());
        basicInfoView.setCharNoPending(basicInfo.isNoPendingClaimLG());

        basicInfoView.setCharFCLG(basicInfo.isConstructionRequestLG());
        basicInfoView.setCharFCIns(basicInfo.isAbleToGettingGuarantorJob());
        basicInfoView.setCharFCCom(basicInfo.isNoClaimLGHistory());
        basicInfoView.setCharFCAba(basicInfo.isNoRevokedLicense());
        basicInfoView.setCharFCLate(basicInfo.isNoLateWorkDelivery());
        basicInfoView.setCharFCFund(basicInfo.isAdequateOfCapitalResource());

        basicInfoView.setSpProgram(basicInfo.isApplySpecialProgram());

        basicInfoView.setSpecialProgram(basicInfo.getSpecialProgram());
        if(basicInfoView.getSpecialProgram() == null){
            basicInfoView.setSpecialProgram(new SpecialProgram());
        }

        basicInfoView.setRefIn(basicInfo.isRefinanceIN());
        basicInfoView.setRefinanceIn(basicInfo.getRefinanceInValue());
        if(basicInfoView.getRefinanceIn() == null){
            basicInfoView.setRefinanceIn(new Bank());
        }

        basicInfoView.setRefOut(basicInfo.isRefinanceOUT());
        basicInfoView.setRefinanceOut(basicInfo.getRefinanceOutValue());
        if(basicInfoView.getRefinanceOut() == null){
            basicInfoView.setRefinanceOut(new Bank());
        }

        basicInfoView.setRiskType(basicInfo.getRiskCustomerType());
        if(basicInfoView.getRiskType() == null){
            basicInfoView.setRiskType(new RiskType());
        }

        basicInfoView.setQualitative(basicInfo.getQualitativeType());
        basicInfoView.setExistingSME(basicInfo.isExistingSMECustomer());
        basicInfoView.setSince(basicInfo.getExistingSMECustomerSince());
        basicInfoView.setLastReviewDate(basicInfo.getLastReviewDate());
        basicInfoView.setExtReviewDate(basicInfo.getExtendedReviewDate());

        basicInfoView.setSbfScore(basicInfo.getSbfScore());
        if(basicInfoView.getSbfScore() == null){
            basicInfoView.setSbfScore(new SBFScore());
        }

        basicInfoView.setLoan(basicInfo.isRequestLoanWithSameName());
        basicInfoView.setMoreOneYear(basicInfo.isHaveLoanInOneYear());
        basicInfoView.setAnnual(basicInfo.isPassAnnualReview());

        basicInfoView.setLoanRequestPattern(basicInfo.getLoanRequestPattern());
        if(basicInfoView.getLoanRequestPattern() == null){
            basicInfoView.setLoanRequestPattern(new BorrowingType());
        }

        basicInfoView.setRefName(basicInfo.getReferralName());
        basicInfoView.setRefId(basicInfo.getReferralID());

        basicInfoView.setCreateDate(basicInfo.getCreateDate());
        basicInfoView.setCreateBy(basicInfo.getCreateBy());
        basicInfoView.setModifyDate(basicInfo.getModifyDate());
        basicInfoView.setModifyBy(basicInfo.getModifyBy());

        return basicInfoView;
    }
}
