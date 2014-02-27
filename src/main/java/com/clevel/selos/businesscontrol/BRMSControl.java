package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.GuarantorCategory;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.working.*;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Stateless
public class BRMSControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BRMSInterface brmsInterface;

    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;
    @Inject
    private NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCreditFacilityDAO creditFacilityDAO;

    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private PrescreenDAO prescreenDAO;
    @Inject
    private PrescreenBusinessDAO prescreenBusinessDAO;
    @Inject
    private PrescreenCollateralDAO prescreenCollateralDAO;
    @Inject
    private PrescreenFacilityDAO prescreenFacilityDAO;
    @Inject
    StepDAO stepDAO;

    @Inject
    public BRMSControl() {
    }

    public StandardPricingResponse getPriceFeeInterest(long workCaseId,long stepId) {
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId,stepId);
        StandardPricingResponse _returnPricingResponse = new StandardPricingResponse();

        StandardPricingResponse _tmpPricingIntResponse = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        StandardPricingResponse _tmpPricingFeeResponse = brmsInterface.checkStandardPricingFeeRule(applicationInfo);

        if (_tmpPricingIntResponse.getActionResult().equals(ActionResult.SUCCESS) && _tmpPricingFeeResponse.getActionResult().equals(ActionResult.SUCCESS)) {
            _returnPricingResponse.setActionResult(_tmpPricingFeeResponse.getActionResult());
            _returnPricingResponse.setPricingInterest(_tmpPricingIntResponse.getPricingInterest());
            _returnPricingResponse.setPricingFeeList(_tmpPricingFeeResponse.getPricingFeeList());
        } else if (_tmpPricingFeeResponse.getActionResult().equals(ActionResult.FAILED)) {
            _returnPricingResponse.setActionResult(_tmpPricingFeeResponse.getActionResult());
            _returnPricingResponse.setReason(_tmpPricingFeeResponse.getReason());
        } else {
            _returnPricingResponse.setActionResult(_tmpPricingIntResponse.getActionResult());
            _returnPricingResponse.setReason(_tmpPricingIntResponse.getReason());
        }

        return _returnPricingResponse;
    }

    public StandardPricingResponse getPriceFee(long workCaseId,long stepId) {
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId,stepId);
        StandardPricingResponse standardPricingResponse = brmsInterface.checkStandardPricingFeeRule(applicationInfo);
        return standardPricingResponse;
    }

    public StandardPricingResponse getPricingInt(long workCaseId,long stepId) {
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId,stepId);
        StandardPricingResponse response = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        return response;
    }

    private BRMSApplicationInfo getApplicationInfoForPricing(long workCaseId,long stepId) {
        logger.debug("-- start getApplicationInfoForPricing with workCaseId {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        Step step = stepDAO.findById(stepId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        applicationInfo.setStatusCode(workCase.getStatus().getCode());
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        //TODO: Using real bdm Submit Date;
        applicationInfo.setBdmSubmitDate(null);

        applicationInfo.setProcessDate(Calendar.getInstance().getTime());
        applicationInfo.setProductGroup(basicInfo.getProductGroup().getBrmsCode());


        BigDecimal totalTCGGuaranteeAmount = BigDecimal.ZERO;
        BigDecimal numberOfIndvGuarantor = BigDecimal.ZERO;
        BigDecimal numberOfJurisGuarantor = BigDecimal.ZERO;

        if (workCase.getStep() != null) {

            logger.debug("workCaseId :: {}", workCaseId);
            logger.debug("workCase.getStep() :: {}", workCase.getStep());
            logger.debug("workCase.getStep().getProposeType() :: {}", workCase.getStep().getProposeType());
            logger.debug("step :: {}",step.getProposeType().value());

//            List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailDAO.findGuarantorByProposeType(workCaseId, workCase.getStep().getProposeType());
            List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailDAO.findGuarantorByProposeType(workCaseId, step.getProposeType());
            for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
                if (newGuarantorDetail.getGuarantorCategory().equals(GuarantorCategory.TCG)) {
                    if (newGuarantorDetail.getTotalLimitGuaranteeAmount().compareTo(BigDecimal.ZERO) > 0) {
                        totalTCGGuaranteeAmount = totalTCGGuaranteeAmount.add(newGuarantorDetail.getTotalLimitGuaranteeAmount());
                    }
                } else if (newGuarantorDetail.getGuarantorCategory().equals(GuarantorCategory.INDIVIDUAL)) {
                    numberOfIndvGuarantor = numberOfIndvGuarantor.add(BigDecimal.ONE);

                } else if (newGuarantorDetail.getGuarantorCategory().equals(GuarantorCategory.JURISTIC)) {
                    numberOfJurisGuarantor = numberOfJurisGuarantor.add(BigDecimal.ONE);
                }
            }
        }

        applicationInfo.setTotalTCGGuaranteeAmount(totalTCGGuaranteeAmount);
        applicationInfo.setNumberOfIndvGuarantor(numberOfIndvGuarantor);
        applicationInfo.setNumberOfJurisGuarantor(numberOfJurisGuarantor);


        BigDecimal totalRedeemTransaction = BigDecimal.ZERO;
        BigDecimal totalMortgageValue = BigDecimal.ZERO;
        List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, workCase.getStep().getProposeType());
        for (
                NewCollateral newCollateral
                : newCollateralList)

        {
            List<NewCollateralHead> newCollateralHeadList = newCollateral.getNewCollateralHeadList();
            for (NewCollateralHead newCollateralHead : newCollateralHeadList) {
                List<NewCollateralSub> newCollateralSubList = newCollateralHead.getNewCollateralSubList();
                for (NewCollateralSub newCollateralSub : newCollateralSubList) {
                    List<NewCollateralSubMortgage> newCollateralSubMortgageList = newCollateralSub.getNewCollateralSubMortgageList();
                    for (NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgageList) {
                        if (newCollateralSubMortgage.getMortgageType() != null && newCollateralSubMortgage.getMortgageType().isRedeem()) {
                            totalRedeemTransaction = totalRedeemTransaction.add(BigDecimal.ONE);
                            break;
                        } else if (newCollateralSubMortgage.getMortgageType().isMortgageFeeFlag()) {
                            totalMortgageValue = totalMortgageValue.add(newCollateralSub.getMortgageValue());
                            break;
                        }
                    }
                }
            }
        }

        applicationInfo.setTotalRedeemTransaction(totalRedeemTransaction);
        applicationInfo.setTotalMortgageValue(totalMortgageValue);

        //Update Credit Type info
        NewCreditFacility newCreditFacility = creditFacilityDAO.findByWorkCaseId(workCaseId);
        BigDecimal discountFrontEndFeeRate = newCreditFacility.getFrontendFeeDOA();
        if (workCase.getStep().

                getProposeType()

                == ProposeType.P)

        {
            applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
        } else if (workCase.getStep().

                getProposeType()

                .

                        equals(ProposeType.A)

                )

        {
            //TODO: To set Loan Request Type when Decision is complete.
        }

        BigDecimal totalApprovedCredit = BigDecimal.ZERO;
        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, workCase.getStep().getProposeType());
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for (
                NewCreditDetail newCreditDetail
                : newCreditDetailList)

        {
            if (newCreditDetail.getRequestType() == RequestTypes.NEW.value()) {
                BRMSAccountRequested accountRequested = new BRMSAccountRequested();

                accountRequested.setCreditDetailId(String.valueOf(newCreditDetail.getId()));
                accountRequested.setProductProgram(newCreditDetail.getProductProgram().getBrmsCode());
                accountRequested.setCreditType(newCreditDetail.getCreditType().getBrmsCode());
                accountRequested.setLimit(newCreditDetail.getLimit());
                accountRequested.setLoanPurpose(newCreditDetail.getLoanPurpose().getBrmsCode());
                accountRequested.setFontEndFeeDiscountRate(discountFrontEndFeeRate);
                accountRequestedList.add(accountRequested);

                if (!newCreditDetail.getProductProgram().isBa())
                    totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
            }
        }

        applicationInfo.setTotalApprovedCredit(totalApprovedCredit);
        applicationInfo.setAccountRequestedList(accountRequestedList);
        return applicationInfo;
    }

    public void getPrescreenResult(long workcasePrescreenId) {
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workcasePrescreenId);


    }

}
