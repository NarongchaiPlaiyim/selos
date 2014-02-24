package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.StandardPricingIntResponse;
import com.clevel.selos.model.GuarantorCategory;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Stateless
public class BRMSControl extends BusinessControl {

    @Inject
    BRMSInterface brmsInterface;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    NewCollateralDAO newCollateralDAO;

    public void getPriceFee(long workCaseId){
        BRMSApplicationInfo applicationInfo = getPriceApplicationInfo(workCaseId);

        brmsInterface.checkStandardPricingFeeRule(applicationInfo);
    }


    public StandardPricingIntResponse getPricingInt(long workCaseId){
        BRMSApplicationInfo applicationInfo = getPriceApplicationInfo(workCaseId);
        StandardPricingIntResponse response = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        return response;
    }

    private BRMSApplicationInfo getPriceApplicationInfo(long workCaseId){
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);

        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        applicationInfo.setStatusCode(workCase.getStatus().getCode());
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setBdmSubmitDate(null);
        applicationInfo.setProcessDate(Calendar.getInstance().getTime());
        applicationInfo.setProductGroup(basicInfo.getProductGroup().getBrmsCode());


        BigDecimal totalTCGGuaranteeAmount = BigDecimal.ZERO;
        BigDecimal numberOfIndvGuarantor = BigDecimal.ZERO;
        BigDecimal numberOfJurisGuarantor = BigDecimal.ZERO;

        List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailDAO.findGuarantorByProposeType(workCaseId, workCase.getStep().getProposeType());
        for(NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList){
            if(newGuarantorDetail.getGuarantorCategory().equals(GuarantorCategory.TCG)){
                if(newGuarantorDetail.getTotalLimitGuaranteeAmount().compareTo(BigDecimal.ZERO) > 0){
                    totalTCGGuaranteeAmount = totalTCGGuaranteeAmount.add(newGuarantorDetail.getTotalLimitGuaranteeAmount());
                }
            } else if(newGuarantorDetail.getGuarantorCategory().equals(GuarantorCategory.INDIVIDUAL)){
                numberOfIndvGuarantor = numberOfIndvGuarantor.add(BigDecimal.ONE);

            } else if(newGuarantorDetail.getGuarantorCategory().equals(GuarantorCategory.JURISTIC)){
                numberOfJurisGuarantor = numberOfJurisGuarantor.add(BigDecimal.ONE);
            }
        }

        applicationInfo.setTotalTCGGuaranteeAmount(totalTCGGuaranteeAmount);
        applicationInfo.setNumberOfIndvGuarantor(numberOfIndvGuarantor);
        applicationInfo.setNumberOfJurisGuarantor(numberOfJurisGuarantor);

        //TODO: Update Logic of Mortgage and Approved Credit
        applicationInfo.setTotalMortgageValue(BigDecimal.ONE);
        applicationInfo.setTotalRedeemTransaction(BigDecimal.ONE);
        applicationInfo.setTotalApprovedCredit(BigDecimal.ZERO);

        //Update Credit Type info
        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, workCase.getStep().getProposeType());
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            BRMSAccountRequested accountRequested = new BRMSAccountRequested();

            accountRequested.setProposeId(String.valueOf(newCreditDetail.getId()));
            accountRequested.setProductProgram(newCreditDetail.getProductProgram().getBrmsCode());
            accountRequested.setCreditType(newCreditDetail.getCreditType().getBrmsCode());
            accountRequested.setLimit(newCreditDetail.getLimit());
            accountRequested.setLoanPurpose(newCreditDetail.getLoanPurpose().getBrmsCode());
        }

        return applicationInfo;
    }

}
