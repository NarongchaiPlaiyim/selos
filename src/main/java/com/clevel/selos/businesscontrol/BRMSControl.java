package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.request.BRMSCustomerInfo;
import com.clevel.selos.integration.brms.model.request.BRMSNCBAccountInfo;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private DecisionDAO decisionDAO;

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
    private CustomerDAO customerDAO;
    @Inject
    private CustomerCSIDAO customerCSIDAO;

    public StandardPricingResponse getPriceFeeInterest(long workCaseId){
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId);
        StandardPricingResponse _returnPricingResponse = new StandardPricingResponse();

        StandardPricingResponse _tmpPricingIntResponse = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        StandardPricingResponse _tmpPricingFeeResponse = brmsInterface.checkStandardPricingFeeRule(applicationInfo);

        if(_tmpPricingIntResponse.getActionResult().equals(ActionResult.SUCCESS) && _tmpPricingIntResponse.getActionResult().equals(ActionResult.SUCCESS)){
            _returnPricingResponse.setActionResult(_tmpPricingFeeResponse.getActionResult());
            _returnPricingResponse.setPricingInterest(_tmpPricingIntResponse.getPricingInterest());
            _returnPricingResponse.setPricingFeeList(_tmpPricingFeeResponse.getPricingFeeList());
        } else if(_tmpPricingFeeResponse.getActionResult().equals(ActionResult.FAILED)){
            _returnPricingResponse.setActionResult(_tmpPricingFeeResponse.getActionResult());
            _returnPricingResponse.setReason(_tmpPricingFeeResponse.getReason());
        } else {
            _returnPricingResponse.setActionResult(_tmpPricingIntResponse.getActionResult());
            _returnPricingResponse.setReason(_tmpPricingIntResponse.getReason());
        }

        return _returnPricingResponse;
    }

    public StandardPricingResponse getPriceFee(long workCaseId){
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId);
        StandardPricingResponse standardPricingResponse = brmsInterface.checkStandardPricingFeeRule(applicationInfo);
        return standardPricingResponse;
    }

    public StandardPricingResponse getPricingInt(long workCaseId){
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId);
        StandardPricingResponse response = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        return response;
    }

    private BRMSApplicationInfo getApplicationInfoForPricing(long workCaseId){
        logger.debug("-- start getApplicationInfoForPricing with workCaseId {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        applicationInfo.setStatusCode(workCase.getStatus().getCode());
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        //TODO: Using real bdm Submit Date;
        applicationInfo.setBdmSubmitDate(workCase.getReceivedDate());

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


        BigDecimal totalRedeemTransaction = BigDecimal.ZERO;
        BigDecimal totalMortgageValue = BigDecimal.ZERO;
        List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, workCase.getStep().getProposeType());
        for(NewCollateral newCollateral : newCollateralList){
            List<NewCollateralHead> newCollateralHeadList = newCollateral.getNewCollateralHeadList();
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                List<NewCollateralSub> newCollateralSubList = newCollateralHead.getNewCollateralSubList();
                for(NewCollateralSub newCollateralSub : newCollateralSubList){
                    List<NewCollateralSubMortgage> newCollateralSubMortgageList = newCollateralSub.getNewCollateralSubMortgageList();
                    for(NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgageList){
                        if(newCollateralSubMortgage.getMortgageType() != null && newCollateralSubMortgage.getMortgageType().isRedeem()){
                            totalRedeemTransaction = totalRedeemTransaction.add(BigDecimal.ONE);
                            break;
                        } else if(newCollateralSubMortgage.getMortgageType().isMortgageFeeFlag()){
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
        if(workCase.getStep().getProposeType() == ProposeType.P){
            applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
        }
        else if(workCase.getStep().getProposeType().equals(ProposeType.A)){
            Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
            //TODO: To set Loan Request Type when Decision is complete.
        }

        BigDecimal totalApprovedCredit = BigDecimal.ZERO;
        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, workCase.getStep().getProposeType());
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                BRMSAccountRequested accountRequested = new BRMSAccountRequested();

                accountRequested.setCreditDetailId(String.valueOf(newCreditDetail.getId()));
                accountRequested.setProductProgram(newCreditDetail.getProductProgram().getBrmsCode());
                accountRequested.setCreditType(newCreditDetail.getCreditType().getBrmsCode());
                accountRequested.setLimit(newCreditDetail.getLimit());
                accountRequested.setLoanPurpose(newCreditDetail.getLoanPurpose().getBrmsCode());
                accountRequested.setFontEndFeeDiscountRate(discountFrontEndFeeRate);
                accountRequestedList.add(accountRequested);

                if(!newCreditDetail.getProductProgram().isBa())
                    totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
            }
        }

        applicationInfo.setTotalApprovedCredit(totalApprovedCredit);
        applicationInfo.setAccountRequestedList(accountRequestedList);
        return applicationInfo;
    }

    public UWRulesResponse getPrescreenResult(long workcasePrescreenId){

        Date now = Calendar.getInstance().getTime();
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workcasePrescreenId);
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workcasePrescreenId);
        List<PrescreenBusiness> businessList = prescreenBusinessDAO.findByPreScreenId(workcasePrescreenId);
        List<Customer> customerList = customerDAO.findCustomerByWorkCasePreScreenId(workcasePrescreenId);


        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        applicationInfo.setApplicationNo(workCasePrescreen.getAppNumber());
        applicationInfo.setProcessDate(Calendar.getInstance().getTime());
        applicationInfo.setBdmSubmitDate(workCasePrescreen.getReceivedDate());
        applicationInfo.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        if(workCasePrescreen.getBorrowerType() != null)
            applicationInfo.setBorrowerType(workCasePrescreen.getBorrowerType().getBrmsCode());

        applicationInfo.setExistingSMECustomer(getBoolean(prescreen.getExistingSMECustomer()));
        applicationInfo.setRefinanceIN(getBoolean(prescreen.getRefinanceIN()));
        applicationInfo.setRefinanceOUT(getBoolean(prescreen.getRefinanceOUT()));
        applicationInfo.setStepCode(workCasePrescreen.getStep().getCode());


        applicationInfo.setBizLocation(String.valueOf(prescreen.getBusinessLocation().getCode()));
        applicationInfo.setYearInBusinessMonth(new BigDecimal(DateTimeUtil.monthBetween2DatesWithNoDate(prescreen.getRegisterDate(), now)));
        applicationInfo.setCountryOfRegistration(prescreen.getCountry().getCode2());

        BigDecimal borrowerGroupIncome = BigDecimal.ZERO;
        String bizCountry = new String();

        boolean appExistingSMECustomer = Boolean.TRUE;
        List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
        for(Customer customer : customerList){
            BRMSCustomerInfo customerInfo = new BRMSCustomerInfo();
            customerInfo.setRelation(customer.getRelation().getBrmsCode());
            customerInfo.setCustomerEntity(customer.getCustomerEntity().getBrmsCode());
            customerInfo.setReference(customer.getReference().getBrmsCode());

            CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();
            if(customer.getRelation().getId() == RelationValue.BORROWER.value() && customerOblInfo == null){
                appExistingSMECustomer = Boolean.FALSE;
            }

            if(customerOblInfo == null) {
                customerInfo.setExistingSMECustomer(Boolean.FALSE);
                customerInfo.setNumberOfMonthLastContractDate(BigDecimal.ZERO);
            } else {
                customerInfo.setExistingSMECustomer(getBoolean(customerOblInfo.getExistingSMECustomer()));
                customerInfo.setNumberOfMonthLastContractDate(new BigDecimal(DateTimeUtil.monthBetween2DatesWithNoDate(customerOblInfo.getLastContractDate(), now)));
                customerInfo.setNextReviewDate(customerOblInfo.getNextReviewDate());
                customerInfo.setNextReviewDateFlag(customerOblInfo.getNextReviewDate() == null? Boolean.FALSE: Boolean.TRUE);
                customerInfo.setExtendedReviewDate(customerOblInfo.getExtendedReviewDate());
                customerInfo.setExtendedReviewDateFlag(customerOblInfo.getExtendedReviewDate() == null? Boolean.FALSE: Boolean.TRUE);
                customerInfo.setRatingFinal(String.valueOf(customerOblInfo.getRatingFinal().getScore()));
                customerInfo.setUnpaidFeeInsurance(customerOblInfo.getUnpaidFeeInsurance().compareTo(BigDecimal.ZERO) != 0);
                customerInfo.setPendingClaimLG(customerOblInfo.getPendingClaimLG().compareTo(BigDecimal.ZERO) != 0);
            }


            if(customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                Juristic juristic = customer.getJuristic();
                customerInfo.setIndividual(Boolean.FALSE);
                customerInfo.setPersonalID(juristic.getRegistrationId());
            } else if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                Individual individual = customer.getIndividual();
                customerInfo.setIndividual(Boolean.TRUE);
                customerInfo.setPersonalID(individual.getCitizenId());
                customerInfo.setAgeMonths(DateTimeUtil.monthBetween2DatesWithNoDate(individual.getBirthDate(), now));
                customerInfo.setNationality(individual.getNationality().getCode());
                customerInfo.setMarriageStatus(individual.getMaritalStatus().getCode());

                if(isActive(customer.getSpouse())){
                    Customer spouse = customerDAO.findSpouseById(customer.getSpouseId());
                    Individual spouseIndv = spouse.getIndividual();
                    customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                    customerInfo.setRelation(spouse.getRelation().getBrmsCode());
                } else {
                    if(isActive(individual.getMaritalStatus().getSpouseFlag())) {
                        Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                        Individual spouseIndv = spouse.getIndividual();
                        customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                        customerInfo.setRelation(spouse.getRelation().getBrmsCode());
                    }
                }
            }

            NCB ncb = customer.getNcb();
            customerInfo.setNumberOfNCBCheckIn6Months(ncb.getCheckIn6Month());
            customerInfo.setNumberOfDayLastNCBCheck(new BigDecimal(DateTimeUtil.daysBetween2Dates(ncb.getCheckingDate(), now)));

            List<NCBDetail> ncbDetailList = ncb.getNcbDetailList();
            if(ncbDetailList == null || ncbDetailList.size() == 0){
                customerInfo.setNcbFlag(Boolean.FALSE);
            } else {
                customerInfo.setNcbFlag(Boolean.TRUE);
                List<BRMSNCBAccountInfo> ncbAccountInfoList = new ArrayList<BRMSNCBAccountInfo>();
                for(NCBDetail ncbDetail : ncbDetailList){
                    BRMSNCBAccountInfo ncbAccountInfo = new BRMSNCBAccountInfo();
                    if(customerInfo.isIndividual())
                        ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus().getNcbCodeInd());
                    else
                        ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus().getNcbCodeJur());
                    ncbAccountInfo.setLoanAccountType(ncbDetail.getAccountType().getNcbCode());
                    ncbAccountInfo.setTmbFlag(isActive(ncbDetail.getAccountTMBFlag()));
                    ncbAccountInfo.setNplFlag(isActive(ncbDetail.getNplFlag()));
                    ncbAccountInfo.setCreditAmtAtNPLDate(ncbDetail.getNplCreditAmount());
                    ncbAccountInfo.setTdrFlag(isActive(ncbDetail.getTdrFlag()));
                    ncbAccountInfo.setCurrentPaymentType(ncbDetail.getCurrentPayment().getNcbCode());
                    ncbAccountInfo.setSixMonthPaymentType(ncbDetail.getHistorySixPayment().getNcbCode());
                    ncbAccountInfo.setTwelveMonthPaymentType(ncbDetail.getHistoryTwelvePayment().getNcbCode());
                    ncbAccountInfo.setNumberOfOverDue(ncbDetail.getOutstandingIn12Month());
                    ncbAccountInfo.setNumberOfOverLimit(ncbDetail.getOverLimit());
                    if(ncbDetail.getAccountCloseDate() != null)
                        ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(DateTimeUtil.monthBetween2DatesWithNoDate(ncbDetail.getAccountCloseDate(), now)));
                    else
                        ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(0));
                    ncbAccountInfoList.add(ncbAccountInfo);
                }
                customerInfo.setNcbAccountInfoList(ncbAccountInfoList);
            }

            List<String> warningFullMatchList = new ArrayList<String>();
            List<String> warningSomeMatchList = new ArrayList<String>();
            List<CustomerCSI> customerCSIList = customerCSIDAO.findCustomerCSIByCustomerId(customer.getId());
            for(CustomerCSI customerCSI : customerCSIList){
                if(customerCSI.getMatchedType().equals(CSIMatchedType.F.name())){
                    warningFullMatchList.add(customerCSI.getWarningCode().getCode());
                } else {
                    warningSomeMatchList.add(customerCSI.getWarningCode().getCode());
                }
            }
            customerInfo.setCsiFullyMatchCode(warningFullMatchList);
            customerInfo.setCsiSomeMatchCode(warningSomeMatchList);
            customerInfo.setQualitativeClass("P");
            borrowerGroupIncome = borrowerGroupIncome.add(customer.getApproxIncome());

            customerInfoList.add(customerInfo);
        }

        applicationInfo.setBorrowerGroupIncome(borrowerGroupIncome);
        applicationInfo.setTotalGroupIncome(prescreen.getGroupIncome());
        //applicationInfo.setTotalNumberProposeCredit();
        //applicationInfo.setTotalNumberContingenPropose();
        applicationInfo.setProductGroup(prescreen.getProductGroup().getBrmsCode());
        applicationInfo.setReferredDocType(prescreen.getReferredExperience().getBrmsCode());

        UWRulesResponse uwRulesResponse = brmsInterface.checkPreScreenRule(applicationInfo);
        return uwRulesResponse;
    }

    private boolean getBoolean(int value){
        RadioValue radioValue = RadioValue.lookup(value);
        return radioValue.getBoolValue();
    }

    private boolean isActive(int value){
        return value == 1;
    }

}
