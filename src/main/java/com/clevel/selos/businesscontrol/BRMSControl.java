package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.transform.BRMSTransform;
import com.clevel.selos.util.DateTimeUtil;
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
    @Inject
    private CustomerOblAccountInfoDAO customerOblAccountInfoDAO;
    @Inject
    private BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    private ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    private BizInfoSummaryDAO bizInfoSummaryDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private TCGDAO tcgDAO;
    @Inject
    private WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    private DBRDAO dbrdao;

    @Inject
    private ExSummaryDAO exSummaryDAO;
    @Inject
    private ExSummaryControl exSummaryControl;

    @Inject
    BRMSTransform brmsTransform;

    @Inject
    public BRMSControl(){}

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
                        if(newCollateralSubMortgage.getMortgageType() != null && (MortgageCategory.REDEEM.equals(newCollateralSubMortgage.getMortgageType().getMortgageCategory()))){
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

            //TODO: To set Loan Request Type when Decision is complete.
        }

        BigDecimal totalApprovedCredit = BigDecimal.ZERO;
        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, workCase.getStep().getProposeType());
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                accountRequestedList.add(brmsTransform.getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));

                if(!newCreditDetail.getProductProgram().isBa())
                    totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
            }
        }

        applicationInfo.setTotalApprovedCredit(totalApprovedCredit);
        applicationInfo.setAccountRequestedList(accountRequestedList);
        return applicationInfo;
    }

    public UWRulesResponse getPrescreenResult(long workcasePrescreenId){
        logger.debug("getPrescreenReult from workcasePrescreenId {}", workcasePrescreenId);
        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("check at date {}", checkDate);

        Date now = Calendar.getInstance().getTime();
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workcasePrescreenId);
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workcasePrescreenId);

        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        applicationInfo.setApplicationNo(workCasePrescreen.getAppNumber());
        applicationInfo.setProcessDate(Calendar.getInstance().getTime());
        applicationInfo.setBdmSubmitDate(workCasePrescreen.getReceivedDate());
        applicationInfo.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        if(workCasePrescreen.getBorrowerType() != null){
            applicationInfo.setBorrowerType(workCasePrescreen.getBorrowerType().getBrmsCode());
        }
        applicationInfo.setExistingSMECustomer(getRadioBoolean(prescreen.getExistingSMECustomer()));
        applicationInfo.setRefinanceIN(getRadioBoolean(prescreen.getRefinanceIN()));
        applicationInfo.setRefinanceOUT(getRadioBoolean(prescreen.getRefinanceOUT()));
        applicationInfo.setStepCode(workCasePrescreen.getStep().getCode());

        applicationInfo.setBizLocation(String.valueOf(prescreen.getBusinessLocation().getCode()));
        applicationInfo.setYearInBusinessMonth(new BigDecimal(DateTimeUtil.monthBetween2Dates(prescreen.getRegisterDate(), now)));
        applicationInfo.setCountryOfRegistration(prescreen.getCountryOfRegister().getCode2());

        BigDecimal borrowerGroupIncome = BigDecimal.ZERO;

        boolean appExistingSMECustomer = Boolean.TRUE;
        List<Customer> customerList = customerDAO.findByWorkCasePreScreenId(workcasePrescreenId);
        List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
        for(Customer customer : customerList) {
            if(customer.getRelation().getId() == RelationValue.BORROWER.value()){
                borrowerGroupIncome = borrowerGroupIncome.add(customer.getApproxIncome());
            }

            customerInfoList.add(brmsTransform.getBRMSCustomerInfo(customer, checkDate));
        }
        applicationInfo.setCustomerInfoList(customerInfoList);

        /** Setup Bank Statement Account **/
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkcasePrescreenId(workcasePrescreenId);
        if(bankStatementSummary != null){
            List<BankStatement> bankStatementList = bankStatementSummary.getBankStmtList();
            List<BRMSAccountStmtInfo> accountStmtInfoList = new ArrayList<BRMSAccountStmtInfo>();
            for(BankStatement bankStatement : bankStatementList){
                accountStmtInfoList.add(brmsTransform.getBRMSAccountStmtInfo(bankStatement));
            }
            applicationInfo.setAccountStmtInfoList(accountStmtInfoList);
        }

        /*Start Set Account Request - Propose Credit Facility*/
        BigDecimal proposedCreditAmount = BigDecimal.ZERO;
        BigDecimal totalNumberOfProposedCredit = BigDecimal.ZERO;
        BigDecimal totalNumberOfContingenCredit = BigDecimal.ZERO;
        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityDAO.findByPreScreenId(workcasePrescreenId);
        List<BRMSAccountRequested> accountRequestedList = new ArrayList<BRMSAccountRequested>();
        for(PrescreenFacility prescreenFacility : prescreenFacilityList){
            BRMSAccountRequested accountRequested = new BRMSAccountRequested();
            accountRequested.setCreditDetailId(String.valueOf(prescreenFacility.getId()));
            accountRequested.setCreditType(prescreenFacility.getCreditType().getBrmsCode());
            accountRequested.setProductProgram(prescreenFacility.getProductProgram().getBrmsCode());
            accountRequestedList.add(accountRequested);

            if(prescreenFacility.getCreditType().isContingentFlag())
                totalNumberOfContingenCredit = totalNumberOfContingenCredit.add(BigDecimal.ONE);
            else
                totalNumberOfProposedCredit = totalNumberOfProposedCredit.add(BigDecimal.ONE);
            proposedCreditAmount.add(prescreenFacility.getRequestAmount());
        }
        applicationInfo.setTotalNumberProposeCredit(totalNumberOfProposedCredit);
        applicationInfo.setTotalNumberContingenPropose(totalNumberOfContingenCredit);
        applicationInfo.setAccountRequestedList(accountRequestedList);
        /*End Set Account Request*/

        /*Start Set Business Info List*/
        List<PrescreenBusiness> businessList = prescreenBusinessDAO.findByPreScreenId(workcasePrescreenId);
        List<BRMSBizInfo> bizInfoList = new ArrayList<BRMSBizInfo>();
        for(PrescreenBusiness prescreenBusiness : businessList){
            bizInfoList.add(brmsTransform.getBRMSBizInfo(prescreenBusiness));
        }
        applicationInfo.setBizInfoList(bizInfoList);
        /*Start Set Business Info List*/

        applicationInfo.setProductGroup(prescreen.getProductGroup().getBrmsCode());
        applicationInfo.setBorrowerGroupIncome(borrowerGroupIncome);
        applicationInfo.setTotalGroupIncome(prescreen.getGroupIncome());
        applicationInfo.setReferredDocType(prescreen.getReferredExperience().getBrmsCode());

        UWRulesResponse uwRulesResponse = brmsInterface.checkPreScreenRule(applicationInfo);

        return uwRulesResponse;
    }

    public UWRulesResponse getFullApplicationResult(long workCaseId){
        logger.debug("getFullApplicationResult from workcaseId {}", workCaseId);
        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("check at date {}", checkDate);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        //1. Set Customer Information, NCB Account, TMB Account Info, Customer CSI (Warning List)
        int latestFinancialStmtYear = 0;
        BigDecimal shareHolderRatio = BigDecimal.ZERO;
        List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        for(Customer customer : customerList){
            BRMSCustomerInfo brmsCustomerInfo = brmsTransform.getBRMSCustomerInfo(customer, checkDate);
            if(customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value() &&
                    customer.getRelation().getId() == RelationValue.BORROWER.value()){
                Juristic juristic = customer.getJuristic();
                if(juristic.getFinancialYear() > latestFinancialStmtYear){
                    latestFinancialStmtYear = juristic.getFinancialYear();
                }
                shareHolderRatio = shareHolderRatio.add(juristic.getShareHolderRatio());
            }
            customerInfoList.add(brmsCustomerInfo);

        }
        applicationInfo.setCustomerInfoList(customerInfoList);

        //2. Set BankStatement Info
        List<BRMSAccountStmtInfo> accountStmtInfoList = new ArrayList<BRMSAccountStmtInfo>();
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        List<BankStatement> bankStatementList = bankStatementSummary.getBankStmtList();
        for(BankStatement bankStatement : bankStatementList){
            accountStmtInfoList.add(brmsTransform.getBRMSAccountStmtInfo(bankStatement));
        }
        applicationInfo.setAccountStmtInfoList(accountStmtInfoList);

        //3. Set Biz Info
        List<BRMSBizInfo> brmsBizInfoList = new ArrayList<BRMSBizInfo>();
        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);
        List<BizInfoDetail> bizInfoDetailList = bizInfoSummary.getBizInfoDetailList();
        for(BizInfoDetail bizInfoDetail : bizInfoDetailList){
            brmsBizInfoList.add(brmsTransform.getBRMSBizInfo(bizInfoDetail));
        }
        applicationInfo.setBizInfoList(brmsBizInfoList);

        //4. Set TMB Account Request
        NewCreditFacility newCreditFacility = creditFacilityDAO.findByWorkCaseId(workCaseId);
        BigDecimal discountFrontEndFeeRate = newCreditFacility.getFrontendFeeDOA();
        if(workCase.getStep().getProposeType() == ProposeType.P){
            applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
        }
        else if(workCase.getStep().getProposeType().equals(ProposeType.A)){

            applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
            applicationInfo.setFinalGroupExposure(newCreditFacility.getTotalApproveExposure());
        }

        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, workCase.getStep().getProposeType());
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                accountRequestedList.add(brmsTransform.getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));
            }
        }
        applicationInfo.setAccountRequestedList(accountRequestedList);

        //5. Set TMB Coll Level
        List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, workCase.getStep().getProposeType());
        List<BRMSCollateralInfo> collateralInfoList = new ArrayList<BRMSCollateralInfo>();

        for(NewCollateral newCollateral : newCollateralList){
            List<NewCollateralHead> newCollateralHeadList = newCollateral.getNewCollateralHeadList();
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                boolean isInboundMortgage = Boolean.FALSE;
                List<NewCollateralSub> newCollateralSubList = newCollateralHead.getNewCollateralSubList();
                for(NewCollateralSub newCollateralSub : newCollateralSubList){
                    List<NewCollateralSubMortgage> mortgageList = newCollateralSub.getNewCollateralSubMortgageList();
                    for(NewCollateralSubMortgage mortgage : mortgageList){
                        if(mortgage.getMortgageType() != null && MortgageCategory.INBOUND.equals(mortgage.getMortgageType().getMortgageCategory())){
                            isInboundMortgage = Boolean.TRUE;
                            break;
                        }
                    }
                    if(isInboundMortgage)
                        break;
                }
                if(isInboundMortgage){
                    BRMSCollateralInfo brmsCollateralInfo = new BRMSCollateralInfo();
                    brmsCollateralInfo.setCollateralType(newCollateralHead.getHeadCollType().getCode());
                    brmsCollateralInfo.setSubCollateralType(newCollateralHead.getSubCollateralType().getCode());
                    brmsCollateralInfo.setAadDecision(newCollateral.getAadDecision());
                    if(newCollateral.getAppraisalDate() != null){
                        brmsCollateralInfo.setAppraisalFlag(Boolean.TRUE);
                        brmsCollateralInfo.setNumberOfMonthsApprDate(new BigDecimal(DateTimeUtil.monthBetween2Dates(newCollateral.getAppraisalDate(), checkDate)));
                    }
                    brmsCollateralInfo.setCollId(String.valueOf(newCollateralHead.getId()));
                    collateralInfoList.add(brmsCollateralInfo);
                }
            }
        }

        applicationInfo.setCollateralInfoList(collateralInfoList);

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);
        DBR dbr = dbrdao.findByWorkCaseId(workCaseId);
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setProcessDate(checkDate);
        applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
        applicationInfo.setExpectedSubmitDate(bankStatementSummary.getExpectedSubmitDate());
        applicationInfo.setBorrowerType(basicInfo.getBorrowerType().getBrmsCode());
        applicationInfo.setRequestLoanWithSameName(getRadioBoolean(basicInfo.getRequestLoanWithSameName()));
        applicationInfo.setRefinanceIN(getRadioBoolean(basicInfo.getRefinanceIN()));
        applicationInfo.setRefinanceOUT(getRadioBoolean(basicInfo.getRefinanceOUT()));
        applicationInfo.setRequestTCG(getRadioBoolean(tcg.getTcgFlag()));

        WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByWorkcaseId(workCaseId);
        AppraisalStatus appraisalStatus = AppraisalStatus.lookup(workCaseAppraisal.getAppraisalResult());
        applicationInfo.setPassAppraisalProcess(appraisalStatus.booleanValue());
        applicationInfo.setStepCode(workCase.getStep().getCode());

        applicationInfo.setNumberOfYearFinancialStmt(new BigDecimal(DateTimeUtil.getYearOfDate(checkDate) - latestFinancialStmtYear));
        applicationInfo.setShareHolderRatio(shareHolderRatio);
        applicationInfo.setFinalDBR(dbr.getFinalDBR());
        applicationInfo.setNetMonthlyIncome(dbr.getNetMonthlyIncome());

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        if(exSummary == null){
            exSummaryControl.calGroupExposureBorrowerCharacteristic(workCaseId);
            exSummaryControl.calGroupSaleBorrowerCharacteristic(workCaseId);
            exSummaryControl.calYearInBusinessBorrowerCharacteristic(workCaseId);
        }
        if(ProposeType.P.equals(workCase.getStep().getProposeType())){
            applicationInfo.setBorrowerGroupIncome(bankStatementSummary.getGrdTotalIncomeNetBDM());
            applicationInfo.setTotalGroupIncome(exSummary.getGroupSaleBDM());
        } else if(ProposeType.A.equals(workCase.getStep().getProposeType())){
            applicationInfo.setBorrowerGroupIncome(bankStatementSummary.getGrdTotalBorrowerIncomeNetUW());
            applicationInfo.setTotalGroupIncome(exSummary.getGroupSaleUW());
        } else {
            applicationInfo.setBorrowerGroupIncome(BigDecimal.ZERO);
            applicationInfo.setTotalGroupIncome(BigDecimal.ZERO);
        }
        applicationInfo.setYearInBusinessMonth(new BigDecimal(exSummary.getYearInBusinessMonth()));

        ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
        applicationInfo.setExistingGroupExposure(existingCreditFacility.getTotalGroupExposure());
        applicationInfo.setTotalExistingODLimit(existingCreditFacility.getTotalBorrowerODLimit());
        applicationInfo.setTotalNumberOfExistingOD(existingCreditFacility.getTotalBorrowerNumberOfExistingOD());

        applicationInfo.setTotalApprovedCredit(newCreditFacility.getTotalApproveCredit());
        applicationInfo.setTotalNumberContingenPropose(newCreditFacility.getTotalNumberContingenPropose());
        applicationInfo.setTotalNumberProposeCredit(newCreditFacility.getTotalNumberProposeCreditFac());
        applicationInfo.setTotalNumberOfRequestedOD(newCreditFacility.getTotalNumberOfNewOD());

        applicationInfo.setTotalNumberOfCoreAsset(newCreditFacility.getTotalNumberOfCoreAsset());
        applicationInfo.setTotalNumberOfNonCoreAsset(newCreditFacility.getTotalNumberOfNonCoreAsset());

        applicationInfo.setAbleToGettingGuarantorJob(getRadioBoolean(basicInfo.getAbleToGettingGuarantorJob()));
        applicationInfo.setNoClaimLGHistory(getRadioBoolean(basicInfo.getNoClaimLGHistory()));
        applicationInfo.setNoRevokedLicense(getRadioBoolean(basicInfo.getNoRevokedLicense()));
        applicationInfo.setNoLateWorkDelivery(getRadioBoolean(basicInfo.getNoLateWorkDelivery()));
        applicationInfo.setAdequateOfCapital(getRadioBoolean(basicInfo.getAdequateOfCapitalResource()));

        applicationInfo.setCollateralPercent(tcg.getCollateralRuleResult());
        applicationInfo.setWcNeed(newCreditFacility.getWcNeed());
        applicationInfo.setCase1WCminLimit(newCreditFacility.getCase1WcMinLimit());
        applicationInfo.setCase2WCminLimit(newCreditFacility.getCase2WcMinLimit());
        applicationInfo.setCase3WCminLimit(newCreditFacility.getCase3WcLimit());
        applicationInfo.setTotalWCTMB(newCreditFacility.getTotalWcTmb());
        applicationInfo.setTotalLoanWCTMB(newCreditFacility.getTotalLoanWCTMB());

        applicationInfo.setBizLocation(String.valueOf(bizInfoSummary.getProvince().getCode()));
        applicationInfo.setCountryOfRegistration(bizInfoSummary.getCountry().getCode2());
        applicationInfo.setTradeChequeReturnPercent(bankStatementSummary.getGrdTotalTDChqRetPercent());
        applicationInfo.setProductGroup(basicInfo.getProductGroup().getBrmsCode());
        applicationInfo.setMaximumSMELimit(newCreditFacility.getMaximumSMELimit());
        applicationInfo.setTotalApprovedCredit(newCreditFacility.getTotalApproveCredit());
        applicationInfo.setReferredDocType(bizInfoSummary.getReferredExperience().getBrmsCode());

        applicationInfo.setNetFixAsset(bizInfoSummary.getNetFixAsset());

        UWRulesResponse uwRulesResponse = brmsInterface.checkPreScreenRule(applicationInfo);

        return uwRulesResponse;
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
