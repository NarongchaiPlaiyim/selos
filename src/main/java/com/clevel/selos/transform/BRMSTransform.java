package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerCSIDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.CustomerOblAccountInfoDAO;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.CSIMatchedType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.util.DateTimeUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BRMSTransform extends Transform{
    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private CustomerCSIDAO customerCSIDAO;
    @Inject
    private CustomerOblAccountInfoDAO customerOblAccountInfoDAO;

    @Inject
    public BRMSTransform(){

    }

    public BRMSAccountStmtInfo getBRMSAccountStmtInfo(BankStatement bankStatement){
        log.debug("transform BankStatement {}", bankStatement);
        BRMSAccountStmtInfo accountStmtInfo = new BRMSAccountStmtInfo();
        accountStmtInfo.setAvgUtilizationPercent(bankStatement.getAvgUtilizationPercent());
        accountStmtInfo.setAvgSwingPercent(bankStatement.getAvgSwingPercent());
        accountStmtInfo.setAvgIncomeGross(bankStatement.getAvgIncomeGross());
        accountStmtInfo.setTotalTransaction(bankStatement.getTotalTransaction());
        accountStmtInfo.setMainAccount(getRadioBoolean(bankStatement.getMainAccount()));
        accountStmtInfo.setHighestInflow(toBoolean(bankStatement.getHighestInflow()));
        accountStmtInfo.setTmb(toBoolean(bankStatement.getTMB()));
        accountStmtInfo.setNotCountIncome(isActive(bankStatement.getNotCountIncome()));
        log.debug("transform Result {}", accountStmtInfo);
        return accountStmtInfo;
    }

    public BRMSNCBAccountInfo getBRMSNCBAccountInfo(NCBDetail ncbDetail, boolean isIndividual, Date checkDate){
        BRMSNCBAccountInfo ncbAccountInfo = new BRMSNCBAccountInfo();
        if(isIndividual)
            ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus() == null ? "" : ncbDetail.getAccountStatus().getNcbCodeInd());
        else
            ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus() == null ? "" : ncbDetail.getAccountStatus().getNcbCodeJur());
        ncbAccountInfo.setLoanAccountType(ncbDetail.getAccountType() == null ? "" : ncbDetail.getAccountType().getNcbCode());
        ncbAccountInfo.setTmbFlag(isActive(ncbDetail.getAccountTMBFlag()));
        ncbAccountInfo.setNplFlag(isActive(ncbDetail.getNplFlag()));
        ncbAccountInfo.setCreditAmtAtNPLDate(ncbDetail.getNplCreditAmount());
        ncbAccountInfo.setTdrFlag(isActive(ncbDetail.getTdrFlag()));
        ncbAccountInfo.setCurrentPaymentType(ncbDetail.getCurrentPayment() == null ? "" : ncbDetail.getCurrentPayment().getNcbCode());
        ncbAccountInfo.setSixMonthPaymentType(ncbDetail.getHistorySixPayment() == null ? "" : ncbDetail.getHistorySixPayment().getNcbCode());
        ncbAccountInfo.setTwelveMonthPaymentType(ncbDetail.getHistoryTwelvePayment() == null ? "" : ncbDetail.getHistoryTwelvePayment().getNcbCode());
        ncbAccountInfo.setNumberOfOverDue(ncbDetail.getOutstandingIn12Month());
        ncbAccountInfo.setNumberOfOverLimit(ncbDetail.getOverLimit());
        if(ncbDetail.getAccountCloseDate() != null)
            ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(ncbDetail.getAccountCloseDate() == null ? "" : DateTimeUtil.monthBetween2Dates(ncbDetail.getAccountCloseDate(), checkDate)));
        else
            ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(0));
        return ncbAccountInfo;
    }

    public BRMSTMBAccountInfo getBRMSTMBAccountInfo(CustomerOblAccountInfo customerOblAccountInfo){
        log.debug("");
        if(customerOblAccountInfo != null){
            BRMSTMBAccountInfo tmbAccountInfo = new BRMSTMBAccountInfo();
            tmbAccountInfo.setActiveFlag(customerOblAccountInfo.isAccountActiveFlag());
            tmbAccountInfo.setDataSource(customerOblAccountInfo.getDataSource());
            tmbAccountInfo.setAccountRef(customerOblAccountInfo.getAccountRef());
            tmbAccountInfo.setCustToAccountRelationCD(customerOblAccountInfo.getCusRelAccount());
            tmbAccountInfo.setTmbTDRFlag(customerOblAccountInfo.isTdrFlag());
            tmbAccountInfo.setNumMonthIntPastDue(customerOblAccountInfo.getNumMonthIntPastDue());
            tmbAccountInfo.setNumMonthIntPastDueTDRAcc(customerOblAccountInfo.getNumMonthIntPastDueTDRAcc());
            tmbAccountInfo.setTmbDelPriDay(customerOblAccountInfo.getTmbDelPriDay());
            tmbAccountInfo.setTmbDelIntDay(customerOblAccountInfo.getTmbDelIntDay());
            tmbAccountInfo.setTmbBlockCode(customerOblAccountInfo.getCardBlockCode());
            return tmbAccountInfo;
        }
        return null;
    }

    public BRMSCustomerInfo getBRMSCustomerInfo(Customer customer, Date checkDate){
        BRMSCustomerInfo customerInfo = new BRMSCustomerInfo();
        customerInfo.setRelation(customer.getRelation().getBrmsCode());
        customerInfo.setCustomerEntity(customer.getCustomerEntity().getBrmsCode());
        customerInfo.setReference(customer.getReference().getBrmsCode());

        CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();

        if(customerOblInfo == null) {
            customerInfo.setExistingSMECustomer(Boolean.FALSE);
            customerInfo.setNumberOfMonthLastContractDate(BigDecimal.ZERO);
        } else {
            customerInfo.setExistingSMECustomer(getRadioBoolean(customerOblInfo.getExistingSMECustomer()));
            customerInfo.setNumberOfMonthLastContractDate(new BigDecimal(DateTimeUtil.monthBetween2Dates(customerOblInfo.getLastContractDate(), checkDate)));
            customerInfo.setNextReviewDate(customerOblInfo.getNextReviewDate());
            customerInfo.setNextReviewDateFlag(customerOblInfo.getNextReviewDate() == null? Boolean.FALSE: Boolean.TRUE);
            customerInfo.setExtendedReviewDate(customerOblInfo.getExtendedReviewDate());
            customerInfo.setExtendedReviewDateFlag(customerOblInfo.getExtendedReviewDate() == null? Boolean.FALSE: Boolean.TRUE);
            customerInfo.setRatingFinal(String.valueOf(customerOblInfo.getRatingFinal() == null? "" : customerOblInfo.getRatingFinal().getScore()));
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
            customerInfo.setAgeMonths(DateTimeUtil.monthBetween2Dates(individual.getBirthDate(), checkDate));
            customerInfo.setNationality(individual.getNationality().getCode());
            customerInfo.setMarriageStatus(individual.getMaritalStatus().getCode());

            if(isActive(customer.getSpouse())){
                Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                Individual spouseIndv = spouse.getIndividual();
                customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                customerInfo.setRelation(spouse.getRelation().getBrmsCode());
            } else {
                if(isActive(individual.getMaritalStatus().getSpouseFlag())) {
                    //Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                    Customer spouse = customerDAO.findById(customer.getSpouseId());
                    Individual spouseIndv = spouse.getIndividual();
                    customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                    customerInfo.setRelation(spouse.getRelation().getBrmsCode());
                }
            }
        }

        customerInfo.setNcbFlag(Boolean.FALSE);
        if(customer.getRelation().getId() == RelationValue.BORROWER.value()){
            NCB ncb = customer.getNcb();
            customerInfo.setNumberOfNCBCheckIn6Months(ncb.getCheckIn6Month());
            customerInfo.setNumberOfDayLastNCBCheck(new BigDecimal(DateTimeUtil.daysBetween2Dates(ncb.getCheckingDate(), checkDate)));

            List<NCBDetail> ncbDetailList = ncb.getNcbDetailList();
            List<BRMSNCBAccountInfo> ncbAccountInfoList = new ArrayList<BRMSNCBAccountInfo>();
            if(ncbDetailList == null || ncbDetailList.size() == 0){
                customerInfo.setNcbFlag(Boolean.FALSE);
            } else {
                customerInfo.setNcbFlag(Boolean.TRUE);
                for(NCBDetail ncbDetail : ncbDetailList){
                    ncbAccountInfoList.add(getBRMSNCBAccountInfo(ncbDetail, customerInfo.isIndividual(), checkDate));
                }
            }
            customerInfo.setNcbAccountInfoList(ncbAccountInfoList);

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
        }

            /*Start setting TMB Account for each customer*/
        List<CustomerOblAccountInfo> oblAccountInfoList = customerOblAccountInfoDAO.findByCustomerId(customer.getId());
        if(oblAccountInfoList != null && oblAccountInfoList.size() > 0){
            List<BRMSTMBAccountInfo> tmbAccountInfoList = new ArrayList<BRMSTMBAccountInfo>();
            for(CustomerOblAccountInfo customerOblAccountInfo : oblAccountInfoList){
                tmbAccountInfoList.add(getBRMSTMBAccountInfo(customerOblAccountInfo));
            }
            customerInfo.setTmbAccountInfoList(tmbAccountInfoList);
        }
        return customerInfo;
    }

    public BRMSCustomerInfo getCustomerInfoWithoutCreditAccount(Customer customer, Date checkDate){
        BRMSCustomerInfo customerInfo = new BRMSCustomerInfo();
        customerInfo.setRelation(customer.getRelation().getBrmsCode());
        customerInfo.setCustomerEntity(customer.getCustomerEntity().getBrmsCode());
        customerInfo.setReference(customer.getReference().getBrmsCode());

        CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();

        if(customerOblInfo == null) {
            customerInfo.setExistingSMECustomer(Boolean.FALSE);
        } else {
            customerInfo.setExistingSMECustomer(getRadioBoolean(customerOblInfo.getExistingSMECustomer()));
        }

        if(customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
            Juristic juristic = customer.getJuristic();
            customerInfo.setIndividual(Boolean.FALSE);
            customerInfo.setPersonalID(juristic.getRegistrationId());
        } else if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
            Individual individual = customer.getIndividual();
            customerInfo.setIndividual(Boolean.TRUE);
            customerInfo.setPersonalID(individual.getCitizenId());
            customerInfo.setAgeMonths(DateTimeUtil.monthBetween2Dates(individual.getBirthDate(), checkDate));
            customerInfo.setNationality(individual.getNationality().getCode());
            customerInfo.setMarriageStatus(individual.getMaritalStatus().getCode());

            if(isActive(customer.getSpouse())){
                Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                Individual spouseIndv = spouse.getIndividual();
                customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                customerInfo.setRelation(spouse.getRelation().getBrmsCode());
            } else {
                if(isActive(individual.getMaritalStatus().getSpouseFlag())) {
                    Customer spouse = customerDAO.findById(customer.getSpouseId());
                    Individual spouseIndv = spouse.getIndividual();
                    customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                    customerInfo.setRelation(spouse.getRelation().getBrmsCode());
                }
            }
        }
        return customerInfo;
    }

    public BRMSAccountRequested getBRMSAccountRequested(NewCreditDetail newCreditDetail, BigDecimal discountFrontEndFeeRate){
        log.debug("-- getBRMSAccountRequested with newCreditDetail {}, discountFrontEndFeeRate {}", newCreditDetail, discountFrontEndFeeRate);
        if(newCreditDetail == null){
            log.debug("getBRMSAccountRequested return null");
            return null;
        }

        BRMSAccountRequested accountRequested = new BRMSAccountRequested();
        accountRequested.setCreditDetailId(String.valueOf(newCreditDetail.getId()));
        accountRequested.setProductProgram(newCreditDetail.getProductProgram().getBrmsCode());
        accountRequested.setCreditType(newCreditDetail.getCreditType().getBrmsCode());
        accountRequested.setLimit(newCreditDetail.getLimit());
        if(newCreditDetail.getProposeCreditTierDetailList() != null){
            List<NewCreditTierDetail> creditTierDetailList = newCreditDetail.getProposeCreditTierDetailList();
            if(creditTierDetailList.size() > 0){
                int tenors = 0;
                for(NewCreditTierDetail newCreditTierDetail : creditTierDetailList){
                    tenors = tenors + newCreditTierDetail.getTenor();
                }
                accountRequested.setTenors(tenors);
            }
        }
        accountRequested.setLoanPurpose(newCreditDetail.getLoanPurpose().getBrmsCode());

        if(discountFrontEndFeeRate != null) {
            if(getRadioBoolean(newCreditDetail.getReduceFrontEndFee()))
                accountRequested.setFontEndFeeDiscountRate(discountFrontEndFeeRate);
            else
                accountRequested.setFontEndFeeDiscountRate(BigDecimal.ZERO);
        } else {
            accountRequested.setFontEndFeeDiscountRate(BigDecimal.ZERO);
        }

        return accountRequested;
    }

    public BRMSBizInfo getBRMSBizInfo(PrescreenBusiness prescreenBusiness){
        log.debug("- getBRMSBizInfo with prescreenBusiness {}", prescreenBusiness);
        BRMSBizInfo brmsBizInfo = new BRMSBizInfo();
        brmsBizInfo.setId(String.valueOf(prescreenBusiness.getId()));
        BusinessDescription businessDescription = prescreenBusiness.getBusinessDescription();
        brmsBizInfo = updateBusinessDescription(businessDescription, brmsBizInfo);
        log.debug("return BRMSBizInfo {}", brmsBizInfo);
        return brmsBizInfo;
    }

    public BRMSBizInfo getBRMSBizInfo(BizInfoDetail bizInfoDetail){
        log.debug("- getBRMSBizInfo with bizInfoDetail {}", bizInfoDetail);
        BRMSBizInfo brmsBizInfo = new BRMSBizInfo();
        brmsBizInfo.setId(String.valueOf(bizInfoDetail.getId()));
        BusinessDescription businessDescription = bizInfoDetail.getBusinessDescription();
        brmsBizInfo = updateBusinessDescription(businessDescription, brmsBizInfo);
        log.debug("return BRMSBizInfo {}", brmsBizInfo);
        return brmsBizInfo;

    }

    private BRMSBizInfo updateBusinessDescription(BusinessDescription businessDescription, BRMSBizInfo brmsBizInfo){
        brmsBizInfo.setBizCode(businessDescription.getTmbCode());
        brmsBizInfo.setNegativeValue(businessDescription.getNegative());
        brmsBizInfo.setHighRiskValue(businessDescription.getHighRisk());
        brmsBizInfo.setEsrValue(businessDescription.getEsr());
        brmsBizInfo.setSuspendValue(businessDescription.getSuspend());
        brmsBizInfo.setDeviationValue(businessDescription.getSuspend());
        return brmsBizInfo;
    }

    private boolean getRadioBoolean(int value){
        RadioValue radioValue = RadioValue.lookup(value);
        return radioValue.getBoolValue();
    }

    private boolean isActive(int value){
        return value == 1;
    }

    private boolean toBoolean(String value){
        return "Y".equals(value)? true: false;
    }

}
