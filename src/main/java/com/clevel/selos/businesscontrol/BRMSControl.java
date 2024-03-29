package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.StepControl;
import com.clevel.selos.dao.master.MandateDocumentDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.*;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.MandateDocument;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.UWRuleResultTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private BasicInfoDAO basicInfoDAO;
    @Inject
    private ProposeLineDAO creditFacilityDAO;
    @Inject
    private ProposeCreditInfoDAO newCreditDetailDAO;
    @Inject
    private ProposeGuarantorInfoDAO newGuarantorDetailDAO;
    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;

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
    private ProposeLineDAO newCreditFacilityDAO;
    @Inject
    private TCGDAO tcgDAO;
    @Inject
    private WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    private WorkCaseOwnerDAO workCaseOwnerDAO;
    @Inject
    private DBRDAO dbrdao;
    @Inject
    private BAPAInfoDAO bapaInfoDAO;

    @Inject
    private ExSummaryDAO exSummaryDAO;

    @Inject
    private DecisionDAO decisionDAO;

    @Inject
    private MandateDocumentDAO mandateDocumentDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private QualitativeADAO qualitativeADAO;
    @Inject
    private QualitativeBDAO qualitativeBDAO;

    @Inject
    private CustomerTransform customerTransform;

    @Inject
    private UWRuleResultTransform uwRuleResultTransform;

    //@Inject
    //private ActionValidationControl actionValidationControl;

    @Inject
    private MandateFieldValidationControl mandateFieldValidationControl;
    @Inject
    private StepControl stepControl;

    @Inject
    public BRMSControl(){}

    public StandardPricingResponse getPriceFeeInterest(long workCaseId){
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId);
        StandardPricingResponse _returnPricingResponse = new StandardPricingResponse();

        StandardPricingResponse _tmpPricingIntResponse = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        StandardPricingResponse _tmpPricingFeeResponse = brmsInterface.checkStandardPricingFeeRule(applicationInfo);
        if(_tmpPricingIntResponse.getActionResult().equals(ActionResult.SUCCESS) && _tmpPricingFeeResponse.getActionResult().equals(ActionResult.SUCCESS)){
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
        if(workCase.getStatus() != null)
            applicationInfo.setStatusCode(workCase.getStatus().getCode());
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());

        applicationInfo.setProcessDate(Calendar.getInstance().getTime());
        if(workCase.getProductGroup() != null)
            applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());

        BigDecimal totalTCGGuaranteeAmount = BigDecimal.ZERO;
        BigDecimal numberOfIndvGuarantor = BigDecimal.ZERO;
        BigDecimal numberOfJurisGuarantor = BigDecimal.ZERO;

        List<ProposeGuarantorInfo> newGuarantorDetailList = newGuarantorDetailDAO.findGuarantorByProposeType(workCaseId, workCase.getStep().getProposeType());
        for(ProposeGuarantorInfo newGuarantorDetail : newGuarantorDetailList){
            if(GuarantorCategory.TCG.equals(newGuarantorDetail.getGuarantorCategory())){
                if(newGuarantorDetail.getTotalLimitGuaranteeAmount().compareTo(BigDecimal.ZERO) > 0){
                    totalTCGGuaranteeAmount = totalTCGGuaranteeAmount.add(newGuarantorDetail.getTotalLimitGuaranteeAmount());
                }
            } else if(GuarantorCategory.INDIVIDUAL.equals(newGuarantorDetail.getGuarantorCategory())){
                numberOfIndvGuarantor = numberOfIndvGuarantor.add(BigDecimal.ONE);

            } else if(GuarantorCategory.JURISTIC.equals(newGuarantorDetail.getGuarantorCategory())){
                numberOfJurisGuarantor = numberOfJurisGuarantor.add(BigDecimal.ONE);
            }
        }

        applicationInfo.setTotalTCGGuaranteeAmount(totalTCGGuaranteeAmount);
        applicationInfo.setNumberOfIndvGuarantor(numberOfIndvGuarantor);
        applicationInfo.setNumberOfJurisGuarantor(numberOfJurisGuarantor);

        BigDecimal totalRedeemTransaction = BigDecimal.ZERO;
        BigDecimal totalMortgageValue = BigDecimal.ZERO;

        ProposeType _proposeType = ProposeType.P;
        if(workCase.getStep() != null)
            _proposeType = workCase.getStep().getProposeType();

        List<ProposeCollateralInfo> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, _proposeType);
        for(ProposeCollateralInfo newCollateral : newCollateralList){
            List<ProposeCollateralInfoHead> newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
            for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                List<ProposeCollateralInfoSub> newCollateralSubList = newCollateralHead.getProposeCollateralInfoSubList();
                for(ProposeCollateralInfoSub newCollateralSub : newCollateralSubList){
                    List<ProposeCollateralSubMortgage> newCollateralSubMortgageList = newCollateralSub.getProposeCollateralSubMortgageList();
                    for(ProposeCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgageList){
                        if(newCollateralSubMortgage.getMortgageType() != null && (MortgageCategory.REDEEM.equals(newCollateralSubMortgage.getMortgageType().getMortgageCategory()))){
                            totalRedeemTransaction = totalRedeemTransaction.add(BigDecimal.ONE);
                            break;
                        } else if(newCollateralSubMortgage.getMortgageType() != null && newCollateralSubMortgage.getMortgageType().isMortgageFeeFlag()){
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
        ProposeLine newCreditFacility = creditFacilityDAO.findByWorkCaseId(workCaseId);
        BigDecimal discountFrontEndFeeRate = newCreditFacility.getFrontendFeeDOA();
        if(_proposeType.equals(ProposeType.P)){
            if(newCreditFacility.getLoanRequestType() != null)
            applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
        }
        else if(_proposeType.equals(ProposeType.A)){
            if(newCreditFacility.getLoanRequestType() != null)
                applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
        }

        BigDecimal totalApprovedCredit = BigDecimal.ZERO;
        List<ProposeCreditInfo> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, _proposeType);
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(ProposeCreditInfo newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));

                if(!newCreditDetail.getProductProgram().isBa()) {
                    if(_proposeType.equals(ProposeType.A)){
                        if(newCreditDetail.getUwDecision() == DecisionType.APPROVED) {
                            totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
                        }
                    }else {
                        totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
                    }

                }
            }
        }

        applicationInfo.setTotalApprovedCredit(totalApprovedCredit);
        applicationInfo.setAccountRequestedList(accountRequestedList);
        return applicationInfo;
    }

    public UWRuleResponseView getPrescreenResult(long workcasePrescreenId, long actionId) throws Exception{
        logger.debug("getPrescreenReult from workcasePrescreenId {}", workcasePrescreenId);
        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("check at date {}", checkDate);

        Date now = Calendar.getInstance().getTime();
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workcasePrescreenId);
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workcasePrescreenId);

        UWRuleResponseView uwRuleResponseView = new UWRuleResponseView();

        CustomerEntity mainBorrower = workCasePrescreen.getBorrowerType();

        mandateFieldValidationControl.loadMandateField(workCasePrescreen.getStep().getId(), actionId);
        logger.info("-- load Action Validation");
        mandateFieldValidationControl.validate(workCasePrescreen, WorkCasePrescreen.class.getName());
        mandateFieldValidationControl.validate(prescreen, Prescreen.class.getName());

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

        if(workCasePrescreen.getStep() != null)
            applicationInfo.setStepCode(workCasePrescreen.getStep().getCode());
        if(prescreen.getBusinessLocation() != null)
            applicationInfo.setBizLocation(String.valueOf(prescreen.getBusinessLocation().getCode()));

        //---- Check register date is older than reference date : use older date to send BRMS ----
        BigDecimal registerDateMonth = new BigDecimal(DateTimeUtil.monthBetween2Dates(prescreen.getRegisterDate(), now));
        BigDecimal referenceDateMonth = new BigDecimal(DateTimeUtil.monthBetween2Dates(prescreen.getReferredDate(), now));
        BigDecimal yearInBusinessMonth;
        if(registerDateMonth.compareTo(referenceDateMonth) > 0)
            yearInBusinessMonth = registerDateMonth;
        else
            yearInBusinessMonth = referenceDateMonth;

        applicationInfo.setYearInBusinessMonth(yearInBusinessMonth);
        if(prescreen.getCountryOfRegister() != null)
            applicationInfo.setCountryOfRegistration(prescreen.getCountryOfRegister().getCode2());

        BigDecimal borrowerGroupIncome = BigDecimal.ZERO;

        List<Customer> customerList = customerDAO.findByWorkCasePreScreenId(workcasePrescreenId);
        //Validate Customer List
        mandateFieldValidationControl.validate(customerList, Customer.class.getName());
        int numberOfGuarantor = 0;
        List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
        for(Customer customer : customerList) {
            if(customer.getRelation().getId() == RelationValue.BORROWER.value()){
                borrowerGroupIncome = borrowerGroupIncome.add(customer.getApproxIncome());
            }

            customerInfoList.add(getBRMSCustomerInfo(customer, checkDate));

            if(customer.getRelation().getId() == RelationValue.GUARANTOR.value()){
                logger.debug("found guarantor!");
                if(customer.getReference() != null){
                    logger.debug("customer reference : {}", customer.getReference().getBrmsCode());
                    if(customer.getReference().getBrmsCode().equalsIgnoreCase("004") || customer.getReference().getBrmsCode().equalsIgnoreCase("005")) {
                        logger.debug("found guarantor with reference 004/005");
                        numberOfGuarantor = numberOfGuarantor + 1;
                    }
                }
            }
        }
        applicationInfo.setCustomerInfoList(customerInfoList);

        logger.debug("number of guarantor ({}), mainBorrower ({})",numberOfGuarantor, mainBorrower);
        if(mainBorrower != null && mainBorrower.getId() == BorrowerType.JURISTIC.value() && numberOfGuarantor == 0){
            logger.debug("Juristic should have at least one guarantor. mainBorrower : {}, numberOfGuarantor : {}", mainBorrower, numberOfGuarantor);
            MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
            mandateFieldMessageView.setFieldName("Guarantor.");
            mandateFieldMessageView.setFieldDesc("Guarantor Info.");
            mandateFieldMessageView.setMessage("Juristic should have at least one guarantor with authorization.");
            mandateFieldMessageView.setPageName("Customer Info.");
            List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
            mandateFieldMessageViewList.add(mandateFieldMessageView);

            uwRuleResponseView.setActionResult(ActionResult.FAILED);
            uwRuleResponseView.setReason("Mandatory fields are missing!!");
            uwRuleResponseView.setMandateFieldMessageViewList(mandateFieldMessageViewList);

            return uwRuleResponseView;
        }

        /** Setup Bank Statement Account **/
        //2. Set BankStatement Info
        List<BRMSAccountStmtInfo> accountStmtInfoList = new ArrayList<BRMSAccountStmtInfo>();
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkcasePrescreenId(workcasePrescreenId);
        //validate bankStatementSummary
        mandateFieldValidationControl.validate(bankStatementSummary, BankStatement.class.getName());
        if(bankStatementSummary != null){
            List<BankStatement> bankStatementList = bankStatementSummary.getBankStmtList();
            for(BankStatement bankStatement : bankStatementList){
                accountStmtInfoList.add(getBRMSAccountStmtInfo(bankStatement));
            }
        } else {
            applicationInfo.setAccountStmtInfoList(new ArrayList<BRMSAccountStmtInfo>());
        }
        applicationInfo.setAccountStmtInfoList(accountStmtInfoList);

        /*Start Set Account Request - Propose Credit Facility*/
        BigDecimal proposedCreditAmount = BigDecimal.ZERO;
        BigDecimal totalNumberOfProposedCredit = BigDecimal.ZERO;
        BigDecimal totalNumberOfContingenCredit = BigDecimal.ZERO;
        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityDAO.findByPreScreenId(prescreen.getId());
        List<BRMSAccountRequested> accountRequestedList = new ArrayList<BRMSAccountRequested>();

        mandateFieldValidationControl.validate(prescreenFacilityList, PrescreenFacility.class.getName());

        for(PrescreenFacility prescreenFacility : prescreenFacilityList){
            BRMSAccountRequested accountRequested = new BRMSAccountRequested();
            accountRequested.setCreditDetailId(String.valueOf(prescreenFacility.getId()));
            if(prescreenFacility.getCreditType() != null)
                accountRequested.setCreditType(prescreenFacility.getCreditType().getBrmsCode());
            if(prescreenFacility.getProductProgram() != null)
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
        List<PrescreenBusiness> businessList = prescreenBusinessDAO.findByPreScreenId(prescreen.getId());
        mandateFieldValidationControl.validate(businessList, PrescreenBusiness.class.getName());

        List<BRMSBizInfo> bizInfoList = new ArrayList<BRMSBizInfo>();
        for(PrescreenBusiness prescreenBusiness : businessList){
            bizInfoList.add(getBRMSBizInfo(prescreenBusiness));
        }
        applicationInfo.setBizInfoList(bizInfoList);
        /*Start Set Business Info List*/

        if(prescreen.getProductGroup() != null)
            applicationInfo.setProductGroup(prescreen.getProductGroup().getBrmsCode());
        applicationInfo.setBorrowerGroupIncome(borrowerGroupIncome);
        applicationInfo.setTotalGroupIncome(prescreen.getGroupIncome());

        if(prescreen.getReferredExperience() != null)
            applicationInfo.setReferredDocType(prescreen.getReferredExperience().getBrmsCode());

        //TODO waiting to confirm with TMB
        applicationInfo.setBotClass("");

        MandateFieldValidationResult mandateFieldValidationResult = mandateFieldValidationControl.getMandateFieldValidationResult();
        logger.info("actionValidationResult: {}", mandateFieldValidationResult);
        if(mandateFieldValidationResult.getActionResult().equals(ActionResult.SUCCESS)){

            /** To Change to use test Data using second line**/
            UWRulesResponse uwRulesResponse = brmsInterface.checkPreScreenRule(applicationInfo);

             //Transform to View//
            uwRuleResponseView.setActionResult(uwRulesResponse.getActionResult());
            uwRuleResponseView.setReason(uwRulesResponse.getReason());
            if(uwRulesResponse.getUwRulesResultMap() != null && uwRulesResponse.getUwRulesResultMap().size() > 0){
                UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultTransform.transformToView(uwRulesResponse.getUwRulesResultMap(), customerList);
                uwRuleResponseView.setUwRuleResultSummaryView(uwRuleResultSummaryView);
            }
        } else {
            uwRuleResponseView.setActionResult(mandateFieldValidationResult.getActionResult());
            uwRuleResponseView.setReason("Mandatory fields are missing!!");
            uwRuleResponseView.setMandateFieldMessageViewList(mandateFieldValidationResult.getMandateFieldMessageViewList());
        }
        return uwRuleResponseView;
    }

    public UWRuleResponseView getFullApplicationResult(long workCaseId, long actionId){
        logger.debug("getFullApplicationResult from workcaseId {}", workCaseId);
        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("check at date {}", checkDate);

        UWRuleResponseView uwRuleResponseView = new UWRuleResponseView();

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        mandateFieldValidationControl.loadMandateField(workCase.getStep().getId(), actionId);
        mandateFieldValidationControl.validate(workCase, WorkCase.class.getName());

        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        //1. Set Customer Information, NCB Account, TMB Account Info, Customer CSI (Warning List)
        int latestFinancialStmtYear = 0;
        BigDecimal shareHolderRatio = BigDecimal.ZERO;
        List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);

        mandateFieldValidationControl.validate(customerList, Customer.class.getName());

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(basicInfo, BasicInfo.class.getName());

        CustomerEntity mainBorrower = basicInfo != null ? basicInfo.getBorrowerType() : new CustomerEntity();
        String newQualitativeClass = "";
        int qualitativeType = basicInfo != null ? basicInfo.getQualitativeType() : 0;
        if(qualitativeType == 1){
            QualitativeA qualitativeA = qualitativeADAO.findByWorkCaseId(workCaseId);
            newQualitativeClass = qualitativeA != null ? qualitativeA.getQualityResult() : "";
        } else if (qualitativeType == 2){
            QualitativeB qualitativeB = qualitativeBDAO.findByWorkCaseId(workCaseId);
            newQualitativeClass = qualitativeB != null ? qualitativeB.getQualityResult() : "";
        }

        int numberOfGuarantor = 0;
        for(Customer customer : customerList){
            BRMSCustomerInfo brmsCustomerInfo = getBRMSCustomerInfo(customer, checkDate);
            if(customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value() &&
                    customer.getRelation().getId() == RelationValue.BORROWER.value()){
                Juristic juristic = customer.getJuristic();
                if(juristic.getFinancialYear() > latestFinancialStmtYear){
                    latestFinancialStmtYear = juristic.getFinancialYear();
                }
                if(juristic.getShareHolderRatio() != null)
                    shareHolderRatio = shareHolderRatio.add(juristic.getShareHolderRatio());
            }
            //To set New_Qualitative for All Customer
            brmsCustomerInfo.setQualitativeClass(newQualitativeClass);
            customerInfoList.add(brmsCustomerInfo);

            if(customer.getRelation().getId() == RelationValue.GUARANTOR.value()){
                logger.debug("found guarantor!");
                if(customer.getReference() != null){
                    logger.debug("customer reference : {}", customer.getReference().getBrmsCode());
                    if(customer.getReference().getBrmsCode().equalsIgnoreCase("004") || customer.getReference().getBrmsCode().equalsIgnoreCase("005")) {
                        logger.debug("found guarantor with reference 004/005");
                        numberOfGuarantor = numberOfGuarantor + 1;
                    }
                }
            }
        }
        applicationInfo.setCustomerInfoList(customerInfoList);
        applicationInfo.setExistingSMECustomer(getRadioBoolean(basicInfo.getExistingSMECustomer()));

        logger.debug("number of guarantor ({})", numberOfGuarantor);
        if(mainBorrower != null && mainBorrower.getId() == BorrowerType.JURISTIC.value() && numberOfGuarantor == 0){
            MandateFieldMessageView mandateFieldMessageView = new MandateFieldMessageView();
            mandateFieldMessageView.setFieldName("Guarantor.");
            mandateFieldMessageView.setFieldDesc("Guarantor Info.");
            mandateFieldMessageView.setMessage("Juristic should have at least one guarantor with authorization.");
            mandateFieldMessageView.setPageName("Customer Info.");
            List<MandateFieldMessageView> mandateFieldMessageViewList = new ArrayList<MandateFieldMessageView>();
            mandateFieldMessageViewList.add(mandateFieldMessageView);

            uwRuleResponseView.setActionResult(ActionResult.FAILED);
            uwRuleResponseView.setReason("Mandatory fields are missing!!");
            uwRuleResponseView.setMandateFieldMessageViewList(mandateFieldMessageViewList);

            return uwRuleResponseView;
        }

        //2. Set BankStatement Info
        List<BRMSAccountStmtInfo> accountStmtInfoList = new ArrayList<BRMSAccountStmtInfo>();
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(bankStatementSummary, BankStatement.class.getName());
        if(bankStatementSummary!=null){
            List<BankStatement> bankStatementList = bankStatementSummary.getBankStmtList();
            if(bankStatementList != null) {
                for (BankStatement bankStatement : bankStatementList) {
                    accountStmtInfoList.add(getBRMSAccountStmtInfo(bankStatement));
                }
            }
            applicationInfo.setAccountStmtInfoList(accountStmtInfoList);
        }

        //3. Set Biz Info
        List<BRMSBizInfo> brmsBizInfoList = new ArrayList<BRMSBizInfo>();
        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(bizInfoSummary, BizInfoSummary.class.getName());
        if(bizInfoSummary!=null){
            List<BizInfoDetail> bizInfoDetailList = bizInfoSummary.getBizInfoDetailList();
            if(bizInfoDetailList != null) {
                for (BizInfoDetail bizInfoDetail : bizInfoDetailList) {
                    brmsBizInfoList.add(getBRMSBizInfo(bizInfoDetail));
                }
            }
            applicationInfo.setBizInfoList(brmsBizInfoList);
        }

        //4. Set TMB Account Request
        ProposeLine newCreditFacility = creditFacilityDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(newCreditFacility, ProposeLine.class.getName());
        BigDecimal discountFrontEndFeeRate = BigDecimal.ZERO;

        if(newCreditFacility!=null)
            discountFrontEndFeeRate = newCreditFacility.getFrontendFeeDOA();


        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(decision, Decision.class.getName());

        ProposeType _proposeType = ProposeType.P;
        if(workCase.getStep() != null)
            _proposeType = workCase.getStep().getProposeType();

        applicationInfo.setFinalGroupExposure(BigDecimal.ZERO);     //Set default for Aggregate
        if( _proposeType.equals(ProposeType.P)){
            if(newCreditFacility!=null){
                if(newCreditFacility.getLoanRequestType() != null)
                    applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
                applicationInfo.setFinalGroupExposure(newCreditFacility.getTotalExposure());
            }
        }
        else if(_proposeType.equals(ProposeType.A)){
            if(newCreditFacility!=null)
                if(newCreditFacility.getLoanRequestType() != null){
                    applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
                    if(decision!=null)
                        applicationInfo.setFinalGroupExposure(decision.getTotalApproveExposure());
                }

        }

        BigDecimal totalApprovedCredit = BigDecimal.ZERO;
        List<ProposeCreditInfo> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, _proposeType);
        mandateFieldValidationControl.validate(newCreditDetailList, ProposeCreditInfo.class.getName());

        logger.debug("proposeType : {}, newCreditDetailList.size() : {}", _proposeType, newCreditDetailList != null ? newCreditDetailList.size() : null);
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(ProposeCreditInfo newCreditDetail : newCreditDetailList){
            if(_proposeType.equals(ProposeType.P)) {
                if (newCreditDetail.getRequestType() == RequestTypes.NEW.value()) {
                    accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));

                    if (!newCreditDetail.getProductProgram().isBa())
                        totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
                }
            }else if(_proposeType.equals(ProposeType.A)){
                logger.debug("newCreditDetail.getRequestType() : {}", newCreditDetail.getRequestType());
                if (newCreditDetail.getRequestType() == RequestTypes.NEW.value()) {
                    accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));

                    if (!newCreditDetail.getProductProgram().isBa())
                        totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
                }
            }
        }
        applicationInfo.setAccountRequestedList(accountRequestedList);

        //5. Set TMB Coll Level
        List<ProposeCollateralInfo> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, _proposeType);
        mandateFieldValidationControl.validate(newCollateralList, ProposeCollateralInfo.class.getName());

        List<BRMSCollateralInfo> collateralInfoList = new ArrayList<BRMSCollateralInfo>();
        for(ProposeCollateralInfo newCollateral : newCollateralList){
            List<ProposeCollateralInfoHead> newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
            for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                boolean isInboundMortgage = Boolean.FALSE;
                List<ProposeCollateralInfoSub> newCollateralSubList = newCollateralHead.getProposeCollateralInfoSubList();
                for(ProposeCollateralInfoSub newCollateralSub : newCollateralSubList){
                    List<ProposeCollateralSubMortgage> mortgageList = newCollateralSub.getProposeCollateralSubMortgageList();
                    if(!Util.isNull(mortgageList)){
                        if(mortgageList.size() > 0){
                            for(ProposeCollateralSubMortgage mortgage : mortgageList){
                                if(mortgage.getMortgageType() != null && MortgageCategory.INBOUND.equals(mortgage.getMortgageType().getMortgageCategory())){
                                    isInboundMortgage = Boolean.TRUE;
                                    break;
                                }
                            }
                        }else{
                            isInboundMortgage = Boolean.TRUE;
                        }
                    }else{
                        isInboundMortgage = Boolean.TRUE;
                    }

                    if(isInboundMortgage)
                        break;
                }
                if(isInboundMortgage){
                    BRMSCollateralInfo brmsCollateralInfo = new BRMSCollateralInfo();
                    if(newCollateralHead.getHeadCollType() != null)
                        brmsCollateralInfo.setCollateralType(newCollateralHead.getHeadCollType().getCode());
                    //todo:this sub coll type is list on head
                    if(newCollateralHead.getProposeCollateralInfoSubList() != null &&
                            newCollateralHead.getProposeCollateralInfoSubList().size() > 0){
                        List<String> subCollateral = new ArrayList<String>();
                        for(ProposeCollateralInfoSub subCol : newCollateralHead.getProposeCollateralInfoSubList()){
                            String subColCode = "";
                            subColCode = subCol.getSubCollateralType() != null ? subCol.getSubCollateralType().getCode() : "";
                            subCollateral.add(subColCode);
                        }
                        brmsCollateralInfo.setSubCollateralTypeList(subCollateral);
                    }

//                    if(newCollateralHead.getSubCollateralType() != null)
//                        brmsCollateralInfo.setSubCollateralType(newCollateralHead.getSubCollateralType().getCode());
                    brmsCollateralInfo.setAadDecision(newCollateral.getAadDecision());
                    if(newCollateral.getAppraisalDate() != null){
                        brmsCollateralInfo.setAppraisalFlag(Boolean.TRUE);
                        if(newCollateral.getAppraisalDate() != null)
                            brmsCollateralInfo.setNumberOfMonthsApprDate(new BigDecimal(DateTimeUtil.monthBetween2Dates(newCollateral.getAppraisalDate(), checkDate)));
                    }
                    brmsCollateralInfo.setCollId(String.valueOf(newCollateralHead.getId()));
                    collateralInfoList.add(brmsCollateralInfo);
                }
            }
        }

        applicationInfo.setCollateralInfoList(collateralInfoList);

        TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);
        DBR dbr = dbrdao.findByWorkCaseId(workCaseId);

        mandateFieldValidationControl.validate(tcg, TCG.class.getName());
        mandateFieldValidationControl.validate(dbr, DBR.class.getName());

        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setProcessDate(checkDate);
        if(basicInfo!=null){
            applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
            if(basicInfo.getBorrowerType() != null)
                applicationInfo.setBorrowerType(basicInfo.getBorrowerType().getBrmsCode());
        }
        if(bankStatementSummary!=null)
            applicationInfo.setExpectedSubmitDate(bankStatementSummary.getExpectedSubmitDate());

        if(basicInfo!=null){
            applicationInfo.setRequestLoanWithSameName(getRadioBoolean(basicInfo.getRequestLoanWithSameName()));
            applicationInfo.setRefinanceIN(getRadioBoolean(basicInfo.getRefinanceIN()));
            applicationInfo.setRefinanceOUT(getRadioBoolean(basicInfo.getRefinanceOUT()));
        }

        if(tcg!=null)
            applicationInfo.setRequestTCG(getRadioBoolean(tcg.getTcgFlag()));

        WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByWorkcaseId(workCaseId);
        mandateFieldValidationControl.validate(workCaseAppraisal, WorkCaseAppraisal.class.getName());

        if(workCaseAppraisal != null){
            AppraisalStatus appraisalStatus = AppraisalStatus.lookup(workCaseAppraisal.getAppraisalResult());
            applicationInfo.setPassAppraisalProcess(appraisalStatus.booleanValue());
        }else{
            applicationInfo.setPassAppraisalProcess(false);
        }
        if(workCase.getStep() != null)
            applicationInfo.setStepCode(workCase.getStep().getCode());

        applicationInfo.setNumberOfYearFinancialStmt(new BigDecimal(DateTimeUtil.getYearOfDate(checkDate) - latestFinancialStmtYear));
        applicationInfo.setShareHolderRatio(shareHolderRatio);
        if(dbr!=null){
            applicationInfo.setFinalDBR(dbr.getFinalDBR());
            applicationInfo.setNetMonthlyIncome(dbr.getNetMonthlyIncome());
        }


        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(exSummary, ExSummary.class.getName());

        if(ProposeType.P.equals(_proposeType)){
            if(bankStatementSummary!=null)
                applicationInfo.setBorrowerGroupIncome(bankStatementSummary.getGrdTotalIncomeNetBDM());
            if(exSummary!=null)
                applicationInfo.setTotalGroupIncome(exSummary.getGroupSaleBDM());
        } else if(ProposeType.A.equals(_proposeType)){
            if(bankStatementSummary!=null)
                applicationInfo.setBorrowerGroupIncome(bankStatementSummary.getGrdTotalBorrowerIncomeNetUW());
            if(exSummary!=null)
                applicationInfo.setTotalGroupIncome(exSummary.getGroupSaleUW());
        } else {
            applicationInfo.setBorrowerGroupIncome(BigDecimal.ZERO);
            applicationInfo.setTotalGroupIncome(BigDecimal.ZERO);
        }
        if(exSummary!=null) {
            applicationInfo.setYearInBusinessMonth(new BigDecimal(exSummary.getYearInBusinessMonth()));
            applicationInfo.setBotClass(exSummary.getCreditRiskBOTClass());
        }

        ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
        mandateFieldValidationControl.validate(existingCreditFacility, ExistingCreditFacility.class.getName());

        if(existingCreditFacility!=null){
            applicationInfo.setExistingGroupExposure(existingCreditFacility.getTotalGroupExposure());
            applicationInfo.setTotalExistingODLimit(existingCreditFacility.getTotalBorrowerODLimit());
            applicationInfo.setTotalNumberOfExistingOD(existingCreditFacility.getTotalBorrowerNumberOfExistingOD());
        }


        applicationInfo.setTotalApprovedCredit(totalApprovedCredit);
        if(newCreditFacility!=null){
            applicationInfo.setTotalNumberContingenPropose(newCreditFacility.getTotalNumberContingenPropose());
            applicationInfo.setTotalNumberProposeCredit(newCreditFacility.getTotalNumberProposeCreditFac());
            applicationInfo.setTotalNumberOfRequestedOD(newCreditFacility.getTotalNumberOfNewOD());

            applicationInfo.setTotalNumberOfCoreAsset(newCreditFacility.getTotalNumberOfCoreAsset());
            applicationInfo.setTotalNumberOfNonCoreAsset(newCreditFacility.getTotalNumberOfNonCoreAsset());
        }

        //set characteristic for f-cash program
        //if requestConstructionFlag not select ,, application set default flag = Y
        if(basicInfo!=null){
            if(Util.isTrue(basicInfo.getConstructionRequestLG())) {
                applicationInfo.setAbleToGettingGuarantorJob(Util.isTrue(basicInfo.getAbleToGettingGuarantorJob()));
                applicationInfo.setNoClaimLGHistory(Util.isTrue(basicInfo.getNoClaimLGHistory()));
                applicationInfo.setNoRevokedLicense(Util.isTrue(basicInfo.getNoRevokedLicense()));
                applicationInfo.setNoLateWorkDelivery(Util.isTrue(basicInfo.getNoLateWorkDelivery()));
                applicationInfo.setAdequateOfCapital(Util.isTrue(basicInfo.getAdequateOfCapitalResource()));
            } else {
                applicationInfo.setAbleToGettingGuarantorJob(true);
                applicationInfo.setNoClaimLGHistory(true);
                applicationInfo.setNoRevokedLicense(true);
                applicationInfo.setNoLateWorkDelivery(true);
                applicationInfo.setAdequateOfCapital(true);
            }
        }


        if(tcg!=null)
            applicationInfo.setCollateralPercent(tcg.getCollateralRuleResult());
        if(newCreditFacility!=null){
            applicationInfo.setWcNeed(newCreditFacility.getWcNeed());
            applicationInfo.setCase1WCminLimit(newCreditFacility.getCase1WCMinLimit());
            applicationInfo.setCase2WCminLimit(newCreditFacility.getCase2WCMinLimit());
            applicationInfo.setCase3WCminLimit(newCreditFacility.getCase3WCLimit());
            applicationInfo.setTotalWCTMB(newCreditFacility.getTotalWCTmb());
            applicationInfo.setTotalLoanWCTMB(newCreditFacility.getTotalLoanWCTMB());
            applicationInfo.setCreditCusType(newCreditFacility.getCreditCustomerType()==2? "P" : "N");
        }


        if(bizInfoSummary!=null)
            if(bizInfoSummary.getProvince() != null)
                applicationInfo.setBizLocation(String.valueOf(bizInfoSummary.getProvince().getCode()));

        if(bizInfoSummary!=null)
            if(bizInfoSummary.getCountry() != null)
                applicationInfo.setCountryOfRegistration(bizInfoSummary.getCountry().getCode2());

        if(bankStatementSummary!=null)
            applicationInfo.setTradeChequeReturnPercent(bankStatementSummary.getGrdTotalTDChqRetPercent());

        if(workCase.getProductGroup() != null)
            applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());

        if(newCreditFacility!=null)
            applicationInfo.setMaximumSMELimit(newCreditFacility.getMaximumSMELimit());

        if(bizInfoSummary!=null)
            if(bizInfoSummary.getReferredExperience() != null)
                applicationInfo.setReferredDocType(bizInfoSummary.getReferredExperience().getBrmsCode());

        if(bizInfoSummary!=null)
            applicationInfo.setNetFixAsset(bizInfoSummary.getNetFixAsset());

        MandateFieldValidationResult mandateFieldValidationResult = mandateFieldValidationControl.getMandateFieldValidationResult();
        if(mandateFieldValidationResult.getActionResult().equals(ActionResult.SUCCESS)){

            UWRulesResponse uwRulesResponse = brmsInterface.checkFullApplicationRule(applicationInfo);

            logger.debug("-- end getFullApplicationResult return {}", uwRulesResponse);

            //Transform to View//
            uwRuleResponseView.setActionResult(uwRulesResponse.getActionResult());
            uwRuleResponseView.setReason(uwRulesResponse.getReason());
            if(uwRulesResponse.getUwRulesResultMap() != null && uwRulesResponse.getUwRulesResultMap().size() > 0){
                UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultTransform.transformToView(uwRulesResponse.getUwRulesResultMap(), customerList);
                uwRuleResponseView.setUwRuleResultSummaryView(uwRuleResultSummaryView);
            }
        } else {
            uwRuleResponseView.setActionResult(mandateFieldValidationResult.getActionResult());
            uwRuleResponseView.setReason("Mandatory fields are missing!!");
            uwRuleResponseView.setMandateFieldMessageViewList(mandateFieldValidationResult.getMandateFieldMessageViewList());
        }

        logger.debug("-- uwRuleResponseView : {}", uwRuleResponseView);

        return uwRuleResponseView;
    }

    public MandateDocResponseView  getDocCustomerForPrescreen(long workCasePrescreenId){
        logger.debug("-- getDocCustomerForPrescreen from workCaseId {}", workCasePrescreenId);
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
        List<MandateDocument> mandateDocumentList = null;

        if(!Util.isNull(workCasePrescreen.getStep())){
            logger.debug("StepId[{}]", workCasePrescreen.getStep().getId());
        }
        if(workCasePrescreen.getStep() != null) {
            mandateDocumentList = Util.safetyList(mandateDocumentDAO.findByStep(workCasePrescreen.getStep().getId()));
            logger.debug("MandateDocumentList.size()[{}]", mandateDocumentList.size());
        }

        MandateDocResponseView mandateDocResponseView = new MandateDocResponseView();
        if(mandateDocumentList != null && mandateDocumentList.size() > 0){
            logger.debug("-- Get Mandate Document from mst_mandate_document {}", mandateDocumentList);
            List<Customer> customerInfoList = Util.safetyList(customerDAO.findCustomerByWorkCasePreScreenId(workCasePrescreenId));

            mandateDocResponseView = getMandateDocViewMap(mandateDocumentList, customerInfoList);
            mandateDocResponseView.setActionResult(ActionResult.SUCCESS);
            logger.debug("-- Get Mandate Document from mandate_master {}", mandateDocResponseView);
        } else {
            Date checkDate = Calendar.getInstance().getTime();
            logger.debug("-- check at date {}", checkDate);
            Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePrescreenId);

            BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
            //1. Set Customer Info List
            List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
            List<Customer> customerList = Util.safetyList(customerDAO.findCustomerByWorkCasePreScreenId(workCasePrescreenId));
            logger.debug("CustomerList.size()[{}]", customerList.size());
            for(Customer customer : customerList){
                BRMSCustomerInfo brmsCustomerInfo = getCustomerInfoWithoutCreditAccount(customer, checkDate);
                customerInfoList.add(brmsCustomerInfo);
            }
            logger.debug("CustomerInfoList.size()[{}]", customerInfoList.size());
            applicationInfo.setCustomerInfoList(customerInfoList);

            //2. Set Account Requested List
            if(!Util.isNull(prescreen)){
                List<PrescreenFacility> prescreenFacilityList = Util.safetyList(prescreenFacilityDAO.findByPreScreen(prescreen));
                logger.debug("PrescreenFacilityList.size()[{}]", prescreenFacilityList.size());
                List<BRMSAccountRequested> accountRequestedList = new ArrayList();
                for(PrescreenFacility prescreenFacility : prescreenFacilityList){
                    accountRequestedList.add(getBRMSAccountRequested(prescreenFacility));
                }
                logger.debug("AccountRequestedList.size()[{}]", accountRequestedList.size());
                applicationInfo.setAccountRequestedList(accountRequestedList);

                //3. Set Application Information.
                if(!Util.isNull(workCasePrescreen.getAppNumber())){
                    applicationInfo.setApplicationNo(workCasePrescreen.getAppNumber());
                    logger.debug("WorkCasePrescreen.AppNumber[{}]", workCasePrescreen.getAppNumber());
                } else {
                    logger.debug("WorkCasePrescreen.AppNumber[{}]", workCasePrescreen.getAppNumber());
                }

                logger.debug("checkDate[{}]", checkDate);
                applicationInfo.setProcessDate(checkDate);


                if(!Util.isNull(workCasePrescreen.getBorrowerType())) {
                    applicationInfo.setBorrowerType(workCasePrescreen.getBorrowerType().getBrmsCode());
                    logger.debug("WorkCasePrescreen.BorrowerType[{}]", workCasePrescreen.getBorrowerType());
                }
                if(!Util.isNull(prescreen.getExistingSMECustomer())){
                    applicationInfo.setExistingSMECustomer(getRadioBoolean(prescreen.getExistingSMECustomer()));
                    logger.debug("Prescreen.ExistingSMECustomer[{}]", prescreen.getExistingSMECustomer());
                }
                if(!Util.isNull(prescreen.getRefinanceIN())){
                    applicationInfo.setRefinanceIN(getRadioBoolean(prescreen.getRefinanceIN()));
                    logger.debug("Prescreen.RefinanceIN[{}]", prescreen.getRefinanceIN());
                }
                if(!Util.isNull(prescreen.getRefinanceOUT())){
                    applicationInfo.setRefinanceOUT(getRadioBoolean(prescreen.getRefinanceOUT()));
                    logger.debug("Prescreen.RefinanceOUT[{}]", prescreen.getRefinanceOUT());
                }
                if(!Util.isNull(prescreen.getTcg())){
                    applicationInfo.setRequestTCG(getRadioBoolean(prescreen.getTcg()));
                    logger.debug("Prescreen.TCG[{}]", prescreen.getTcg());
                }
                if(!Util.isNull(workCasePrescreen.getStep())){
                    applicationInfo.setStepCode(workCasePrescreen.getStep().getCode());
                    logger.debug("WorkCasePrescreen.Step.Code[{}]", workCasePrescreen.getStep().getCode());
                }
                if(!Util.isNull(workCasePrescreen.getProductGroup())){
                    applicationInfo.setProductGroup(workCasePrescreen.getProductGroup().getBrmsCode());
                    logger.debug("WorkCasePrescreen.ProductGroup.BRMSCode[{}]", workCasePrescreen.getProductGroup().getBrmsCode());
                }
                if(!Util.isNull(prescreen.getReferredExperience())){
                    applicationInfo.setReferredDocType(prescreen.getReferredExperience().getBrmsCode());
                    logger.debug("Prescreen.ReferredExperience.BRMSCode()[{}]", prescreen.getReferredExperience().getBrmsCode());
                }
            } else {
                logger.debug("Prescreen is {}", prescreen);
            }

            DocCustomerResponse docCustomerResponse = null;
            if(!Util.isNull(applicationInfo)){
                docCustomerResponse = brmsInterface.checkDocCustomerRule(applicationInfo);
                if(!Util.isNull(docCustomerResponse)){
                    logger.debug("-- docCustomerResponse return {}", docCustomerResponse);
                    if(ActionResult.SUCCESS.equals(docCustomerResponse.getActionResult())){
                        mandateDocResponseView = getMandateDocViewMap(docCustomerResponse.getDocumentDetailList(), customerList, workCasePrescreen.getStep());
                        mandateDocResponseView.setActionResult(docCustomerResponse.getActionResult());
                    } else {
                        mandateDocResponseView = new MandateDocResponseView();
                        mandateDocResponseView.setActionResult(docCustomerResponse.getActionResult());
                        mandateDocResponseView.setReason(docCustomerResponse.getReason());
                    }
                    return mandateDocResponseView;
                } else {
                    logger.debug("DocCustomerResponse is {}", docCustomerResponse);
                }
            } else {
                logger.debug("ApplicationInfo is {}", applicationInfo);
            }
            logger.debug("-- end getDocCustomerForPrescreen return {}", mandateDocResponseView);
        }
        return mandateDocResponseView;
    }

    public MandateDocResponseView getDocCustomerForFullApp(long workCaseId){
        logger.debug("-- getDocCustomerForFullApp(workCaseId {})", workCaseId);
        WorkCase workCase = null;
        BasicInfo basicInfo = null;
        MandateDocResponseView mandateDocResponseView = null;
        BRMSApplicationInfo applicationInfo = null;
        List<BRMSCustomerInfo> customerInfoList = null;
        List<Customer> customerList = null;
        List<ProposeCreditInfo> newCreditDetailList = null;
        List<BRMSAccountRequested> accountRequestedList = null;
        BAPAInfo bapaInfo = null;
        TCG tcg = null;
        BizInfoSummary bizInfoSummary = null;
        DocCustomerResponse docCustomerResponse = null;

        workCase = workCaseDAO.findById(workCaseId);
        if(!Util.isNull(workCase)){
            logger.debug("WorkCase.id[{}]", workCase.getId());
            basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if(!Util.isNull(basicInfo)){
                logger.debug("BasicInfo.id[{}]", basicInfo.getId());
            } else {
                logger.debug("BasicInfo is {}", basicInfo);
            }
        } else {
            logger.debug("WorkCase is {}", workCase);
        }


        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("-- check at date {}", checkDate);

        applicationInfo = new BRMSApplicationInfo();
        logger.debug("[NEW] BRMSApplicationInfo created");

        //1. Set Customer Info List
        customerInfoList = new ArrayList<BRMSCustomerInfo>();
        logger.debug("[NEW] CustomerInfoList created");

        customerList = Util.safetyList(customerDAO.findByWorkCaseId(workCaseId));
        logger.debug("CustomerList.size()[{}]", customerList.size());
        for(Customer customer : customerList){
            BRMSCustomerInfo brmsCustomerInfo = getCustomerInfoWithoutCreditAccount(customer, checkDate);
            customerInfoList.add(brmsCustomerInfo);
        }
        logger.debug("CustomerInfoList.size()[{}]", customerInfoList.size());
        applicationInfo.setCustomerInfoList(customerInfoList);

        //2. Set Account Requested List
        ProposeType _proposeType = ProposeType.P;
        if(!Util.isNull(workCase)){
            if(!Util.isNull(workCase.getStep())){
                _proposeType = workCase.getStep().getProposeType();
                logger.debug("ProposeType is {}", _proposeType);
            } else {
                logger.debug("WorkCase.Step is ", workCase.getStep());
            }
        }


        newCreditDetailList = Util.safetyList(newCreditDetailDAO.findNewCreditDetail(workCaseId, _proposeType));
        logger.debug("NewCreditDetailList.size()[{}]", newCreditDetailList.size());

        accountRequestedList = new ArrayList<BRMSAccountRequested>();
        logger.debug("[NEW] AccountRequestedList created");
        for(ProposeCreditInfo newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, null));
            }
        }
        logger.debug("AccountRequestedList.size()[{}]", accountRequestedList.size());
        applicationInfo.setAccountRequestedList(accountRequestedList);

        //3. Set Application Information.
        if(!Util.isNull(workCase.getAppNumber())){
            applicationInfo.setApplicationNo(workCase.getAppNumber());
            logger.debug("WorkCase.AppNumber[{}]", workCase.getAppNumber());
        } else {
            logger.debug("WorkCase.AppNumber[{}]", workCase.getAppNumber());
        }

        logger.debug("checkDate[{}]", checkDate);
        applicationInfo.setProcessDate(checkDate);


        applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
        if(basicInfo.getBorrowerType() != null)
            applicationInfo.setBorrowerType(basicInfo.getBorrowerType().getBrmsCode());
        applicationInfo.setExistingSMECustomer(getRadioBoolean(basicInfo.getExistingSMECustomer()));
        applicationInfo.setRequestLoanWithSameName(getRadioBoolean(basicInfo.getRequestLoanWithSameName()));
        applicationInfo.setRefinanceIN(getRadioBoolean(basicInfo.getRefinanceIN()));
        applicationInfo.setRefinanceOUT(getRadioBoolean(basicInfo.getRefinanceOUT()));

        bapaInfo = bapaInfoDAO.findByWorkCase(workCaseId);
        if(!Util.isNull(bapaInfo)){
            if(!Util.isNull(bapaInfo.getApplyBA())){
                if(bapaInfo.getApplyBA().getBoolValue()){
                    if(BAPaymentMethodValue.DIRECT.equals(bapaInfo.getBaPaymentMethod())){
                        applicationInfo.setApplyBAwithCash(Boolean.TRUE);
                        applicationInfo.setTopupBA(Boolean.FALSE);
                    } else if(BAPaymentMethodValue.TOPUP.equals(bapaInfo.getBaPaymentMethod())){
                        applicationInfo.setApplyBAwithCash(Boolean.FALSE);
                        applicationInfo.setTopupBA(Boolean.TRUE);
                    } else {
                        applicationInfo.setApplyBAwithCash(Boolean.FALSE);
                        applicationInfo.setTopupBA(Boolean.FALSE);
                    }
                } else {
                    applicationInfo.setApplyBAwithCash(Boolean.FALSE);
                    applicationInfo.setTopupBA(Boolean.FALSE);
                }
            } else {
                logger.debug("BAPAInfo.ApplyBA[{}]", bapaInfo);
            }
        } else {
            logger.debug("BAPAInfo is {}", bapaInfo);
        }
        tcg = tcgDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(tcg)){
            applicationInfo.setRequestTCG(getRadioBoolean(tcg.getTcgFlag()));
        } else {
            logger.debug("TCG is {}", tcg);
        }
        if(!Util.isNull(workCase.getStep())){
            applicationInfo.setStepCode(workCase.getStep().getCode());
            logger.debug("WorkCase.Step.Code[{}]", workCase.getStep().getCode());
        } else {
            logger.debug("WorkCase.Step is {}", workCase.getStep());
        }
        if(!Util.isNull(workCase.getProductGroup())){
            applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());
            logger.debug("WorkCase.ProductGroup.BRMSCode[{}]", workCase.getProductGroup().getBrmsCode());
        } else {
            logger.debug("WorkCase.ProductGroup is {}", workCase.getProductGroup());
        }
        bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(bizInfoSummary)){
            if(!Util.isNull(bizInfoSummary.getReferredExperience())){
                applicationInfo.setReferredDocType(bizInfoSummary.getReferredExperience().getBrmsCode());
            } else {
                logger.debug("BizInfoSummary.ReferredExperience is {}", bizInfoSummary.getReferredExperience());
            }
        } else {
            logger.debug("BizInfoSummary is {}", bizInfoSummary);
        }
        docCustomerResponse = brmsInterface.checkDocCustomerRule(applicationInfo);

        mandateDocResponseView = new MandateDocResponseView();
        if(!Util.isNull(docCustomerResponse)){
            logger.debug("-- docCustomerResponse return {}", docCustomerResponse);
            if(ActionResult.SUCCESS.equals(docCustomerResponse.getActionResult())){
                mandateDocResponseView = getMandateDocViewMap(docCustomerResponse.getDocumentDetailList(), customerList, workCase.getStep());
                mandateDocResponseView.setActionResult(docCustomerResponse.getActionResult());
            } else {
                mandateDocResponseView.setActionResult(docCustomerResponse.getActionResult());
                mandateDocResponseView.setReason(docCustomerResponse.getReason());
            }
        } else {
            logger.debug("DocCustomerResponse is null");
        }
        logger.debug("-- end getDocCustomer return {}", mandateDocResponseView);
        return mandateDocResponseView;
    }

    public MandateDocResponseView getDocAppraisal(long stepId, long workCaseId){
        logger.debug("getDocAppraisal from workCaseId {}", workCaseId);
        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("check at date {}", checkDate);

        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();

        StepView stepView = stepControl.getStepView(stepId);
        StageView stageView = stepView.getStageView();
        AbstractWorkCase workCase = null;
        if(StageValue.PRESCREEN.value() == stageView.getId()){
            workCase = workCasePrescreenDAO.findById(workCaseId);
            applicationInfo.setBdmSubmitDate(checkDate);

        } else {
            workCase = workCaseDAO.findById(workCaseId);
            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
        }

        if(workCase.getProductGroup() != null) {
            applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());
        }

        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setProcessDate(checkDate);

        DocAppraisalResponse docAppraisalResponse = brmsInterface.checkDocAppraisalRule(applicationInfo);
        logger.debug("-- end getDocAppraisal ", docAppraisalResponse);

        MandateDocResponseView mandateDocResponseView = new MandateDocResponseView();
        if(ActionResult.SUCCESS.equals(docAppraisalResponse.getActionResult())){
            mandateDocResponseView = getMandateDocViewMap(docAppraisalResponse.getDocumentDetailList(), null, workCase.getStep());
            mandateDocResponseView.setActionResult(docAppraisalResponse.getActionResult());
        } else {
            mandateDocResponseView.setActionResult(docAppraisalResponse.getActionResult());
            mandateDocResponseView.setReason(docAppraisalResponse.getReason());
        }

        logger.debug("-- end getDocAppraisal return {}", mandateDocResponseView);
        return mandateDocResponseView;
    }

    /** The following method is for BRMSControl internal used - move from brms transform**/

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

    private BRMSAccountStmtInfo getBRMSAccountStmtInfo(BankStatement bankStatement){
        logger.debug("transform BankStatement {}", bankStatement);
        BRMSAccountStmtInfo accountStmtInfo = new BRMSAccountStmtInfo();
        accountStmtInfo.setAvgUtilizationPercent(bankStatement.getAvgUtilizationPercent());
        accountStmtInfo.setAvgSwingPercent(bankStatement.getAvgSwingPercent());
        accountStmtInfo.setAvgIncomeGross(bankStatement.getAvgIncomeGross());
        accountStmtInfo.setAvgGrossInflowPerLimit(bankStatement.getAvgGrossInflowPerLimit());
        BigDecimal minTransaction = BigDecimal.ZERO;
        if(bankStatement.getBankStatementDetailList() != null && bankStatement.getBankStatementDetailList().size() > 0) {
            int tmpMinTransaction = 9999999;
            for (BankStatementDetail bankDetail : bankStatement.getBankStatementDetailList()){
                if(bankDetail.getTotalTransaction() < tmpMinTransaction){
                    tmpMinTransaction = bankDetail.getTotalTransaction();
                }
            }
            minTransaction = new BigDecimal(tmpMinTransaction);
        }
        accountStmtInfo.setTotalTransaction(minTransaction);
        accountStmtInfo.setMainAccount(getRadioBoolean(bankStatement.getMainAccount()));
        accountStmtInfo.setHighestInflow(toBoolean(bankStatement.getHighestInflow()));
        accountStmtInfo.setTmb(toBoolean(bankStatement.getTMB()));
        accountStmtInfo.setNotCountIncome(isActive(bankStatement.getNotCountIncome()));
        accountStmtInfo.setOverLimitDays(bankStatement.getOverLimitDays());
        accountStmtInfo.setCheckReturn(bankStatement.getChequeReturn());
        logger.debug("transform Result {}", accountStmtInfo);
        return accountStmtInfo;
    }

    private BRMSNCBAccountInfo getBRMSNCBAccountInfo(NCBDetail ncbDetail, boolean isIndividual, Date checkDate){
        logger.debug("getBRMSNCBAccountInfo NCBDetail {}, isIndividual {}", ncbDetail, isIndividual);
        BRMSNCBAccountInfo ncbAccountInfo = new BRMSNCBAccountInfo();
        if(isIndividual)
            ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus() == null ? "" : ncbDetail.getAccountStatus().getNcbCodeInd());
        else
            ncbAccountInfo.setLoanAccountStatus(ncbDetail.getAccountStatus() == null ? "" : ncbDetail.getAccountStatus().getNcbCodeJur());
        ncbAccountInfo.setLoanAccountType(ncbDetail.getAccountType() == null ? "" : ncbDetail.getAccountType().getNcbCode());
        ncbAccountInfo.setTmbFlag(getRadioBoolean(ncbDetail.getAccountTMBFlag()));
        ncbAccountInfo.setNplFlag(getRadioBoolean(ncbDetail.getNplFlag()));
        ncbAccountInfo.setCreditAmtAtNPLDate(ncbDetail.getNplCreditAmount());
        ncbAccountInfo.setTdrFlag(getRadioBoolean(ncbDetail.getTdrFlag()));
        if(ncbDetail.getCurrentPayment() != null)
            ncbAccountInfo.setCurrentPaymentType(ncbDetail.getCurrentPayment().getNcbCode());
        if(ncbDetail.getHistorySixPayment() != null)
            ncbAccountInfo.setSixMonthPaymentType(ncbDetail.getHistorySixPayment().getNcbCode());
        if(ncbDetail.getHistoryTwelvePayment() != null)
            ncbAccountInfo.setTwelveMonthPaymentType(ncbDetail.getHistoryTwelvePayment().getNcbCode());
        ncbAccountInfo.setNumberOfOverDue(ncbDetail.getOutstandingIn12Month());
        ncbAccountInfo.setNumberOfOverLimit(ncbDetail.getOverLimit());
        if(ncbDetail.getAccountCloseDate() != null)
            if(ncbDetail.getAccountCloseDate() != null)
                ncbAccountInfo.setAccountCloseDateMonths(new BigDecimal(DateTimeUtil.monthBetween2Dates(ncbDetail.getAccountCloseDate(), checkDate)));
        else
            ncbAccountInfo.setAccountCloseDateMonths(BigDecimal.ZERO);
        return ncbAccountInfo;
    }

    private BRMSTMBAccountInfo getBRMSTMBAccountInfo(CustomerOblAccountInfo customerOblAccountInfo){
        logger.debug("getBRMSTMBAccountInfo CustomerOblAccountInfo {}", customerOblAccountInfo);
        if(customerOblAccountInfo != null){
            BRMSTMBAccountInfo tmbAccountInfo = new BRMSTMBAccountInfo();
            tmbAccountInfo.setActiveFlag(customerOblAccountInfo.isAccountActiveFlag());
            tmbAccountInfo.setDataSource(customerOblAccountInfo.getDataSource());
            if(customerOblAccountInfo.getAccountRef() != null && customerOblAccountInfo.getAccountRef().length() >= 2){
                tmbAccountInfo.setAccountRef(customerOblAccountInfo.getAccountRef().substring(customerOblAccountInfo.getAccountRef().length() - 2));
            }
            tmbAccountInfo.setCustToAccountRelationCD(customerOblAccountInfo.getCusRelAccount());
            tmbAccountInfo.setTmbTDRFlag(customerOblAccountInfo.getTdrFlag().value());
            tmbAccountInfo.setNumMonthIntPastDue(customerOblAccountInfo.getNumMonthIntPastDue());
            tmbAccountInfo.setNumMonthIntPastDueTDRAcc(customerOblAccountInfo.getNumMonthIntPastDueTDRAcc());
            tmbAccountInfo.setTmbDelPriDay(customerOblAccountInfo.getTmbDelPriDay());
            tmbAccountInfo.setTmbDelIntDay(customerOblAccountInfo.getTmbDelIntDay());
            tmbAccountInfo.setTmbBlockCode(customerOblAccountInfo.getCardBlockCode());
            return tmbAccountInfo;
        }
        return null;
    }

    private BRMSCustomerInfo getBRMSCustomerInfo(Customer customer, Date checkDate){
        BRMSCustomerInfo customerInfo = new BRMSCustomerInfo();

        customerInfo.setCustomerId(String.valueOf(customer.getId()));
        if(customer.getRelation() != null)
            customerInfo.setRelation(customer.getRelation().getBrmsCode());
        if(customer.getCustomerEntity() != null)
            customerInfo.setCustomerEntity(customer.getCustomerEntity().getBrmsCode());
        if(customer.getReference() != null)
            customerInfo.setReference(customer.getReference().getBrmsCode());

        CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();

        if(customerOblInfo == null) {
            customerInfo.setExistingSMECustomer(Boolean.FALSE);
            customerInfo.setNumberOfMonthLastContractDate(BigDecimal.ZERO);
        } else {
            customerInfo.setExistingSMECustomer(getRadioBoolean(customerOblInfo.getExistingSMECustomer()));
            if(customerOblInfo.getLastContractDate() != null)
                customerInfo.setNumberOfMonthLastContractDate(new BigDecimal(DateTimeUtil.monthBetween2Dates(customerOblInfo.getLastContractDate(), checkDate)));
            customerInfo.setNextReviewDate(customerOblInfo.getNextReviewDate());
            customerInfo.setNextReviewDateFlag(customerOblInfo.getNextReviewDate() == null? Boolean.FALSE: Boolean.TRUE);
            customerInfo.setExtendedReviewDate(customerOblInfo.getExtendedReviewDate());
            customerInfo.setExtendedReviewDateFlag(customerOblInfo.getExtendedReviewDate() == null? Boolean.FALSE: Boolean.TRUE);
            customerInfo.setRatingFinal(String.valueOf(customerOblInfo.getRatingFinal() == null? "" : customerOblInfo.getRatingFinal().getBrmsCode()));
            if(customerOblInfo.getUnpaidFeeInsurance() != null)
                customerInfo.setUnpaidFeeInsurance(customerOblInfo.getUnpaidFeeInsurance().compareTo(BigDecimal.ZERO) != 0);
            if(customerOblInfo.getPendingClaimLG() != null)
                customerInfo.setPendingClaimLG(customerOblInfo.getPendingClaimLG().compareTo(BigDecimal.ZERO) != 0);
            if(customerOblInfo.getAdjustClass() != null)
                customerInfo.setAdjustClass(customerOblInfo.getAdjustClass());
        }

        if(customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
            Juristic juristic = customer.getJuristic();
            customerInfo.setIndividual(Boolean.FALSE);
            customerInfo.setPersonalID(juristic.getRegistrationId());
            customerInfo.setAgeMonths(DateTimeUtil.monthBetween2Dates(juristic.getRegisterDate(), checkDate));
        } else if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
            Individual individual = customer.getIndividual();
            customerInfo.setIndividual(Boolean.TRUE);
            customerInfo.setPersonalID(individual.getCitizenId());
            customerInfo.setAgeMonths(DateTimeUtil.monthBetween2Dates(individual.getBirthDate(), checkDate));
            if(individual.getNationality() != null)
                customerInfo.setNationality(individual.getNationality().getCode());
            if(individual.getMaritalStatus() != null)
                customerInfo.setMarriageStatus(individual.getMaritalStatus().getCode());

            if(isActive(customer.getSpouse())){
                Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                Individual spouseIndv = spouse.getIndividual();
                customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                if(spouse.getRelation() != null)
                    customerInfo.setSpouseRelationType(spouse.getRelation().getBrmsCode());
                    //customerInfo.setRelation(spouse.getRelation().getBrmsCode());
            } else {
                if(isActive(individual.getMaritalStatus().getSpouseFlag())) {
                    //Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                    if(!Util.isZero(customer.getSpouseId())){
                        Customer spouse = customerDAO.findById(customer.getSpouseId());
                        Individual spouseIndv = spouse.getIndividual();
                        customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());

                        if(spouse.getRelation() != null)
                            customerInfo.setSpouseRelationType(spouse.getRelation().getBrmsCode());
                    }
                        //customerInfo.setRelation(spouse.getRelation().getBrmsCode());   //REMOVE BECAUSE IT'S OVERIDE MAIN CUSTOMER RELATION
                }
            }
        }

        //Set NCB Account
        customerInfo.setNcbFlag(Boolean.FALSE);
        NCB ncb = customer.getNcb();
        List<BRMSNCBAccountInfo> ncbAccountInfoList = new ArrayList<BRMSNCBAccountInfo>();
        if(ncb != null){
            customerInfo.setNumberOfNCBCheckIn6Months(ncb.getCheckIn6Month());
            customerInfo.setNumberOfDayLastNCBCheck(new BigDecimal(DateTimeUtil.daysBetween2Dates(ncb.getCheckingDate(), checkDate)));

            List<NCBDetail> ncbDetailList = ncb.getNcbDetailList();
            if(ncbDetailList == null || ncbDetailList.size() == 0){
                customerInfo.setNcbFlag(Boolean.FALSE);
                ncbAccountInfoList.add(getBRMSNCBAccountInfo(new NCBDetail(), customerInfo.isIndividual(), checkDate));
            } else {
                customerInfo.setNcbFlag(Boolean.TRUE);
                for(NCBDetail ncbDetail : ncbDetailList){
                    ncbAccountInfoList.add(getBRMSNCBAccountInfo(ncbDetail, customerInfo.isIndividual(), checkDate));
                }
            }
        }
        customerInfo.setNcbAccountInfoList(ncbAccountInfoList);

        //Set warning list
        List<String> warningFullMatchList = new ArrayList<String>();
        List<String> warningSomeMatchList = new ArrayList<String>();
        List<CustomerCSI> customerCSIList = customerCSIDAO.findCustomerCSIByCustomerId(customer.getId());
        for(CustomerCSI customerCSI : customerCSIList){
            if(customerCSI.getMatchedType() != null){
                if(customerCSI.getMatchedType().equals(CSIMatchedType.F.name())){
                    if(customerCSI.getWarningCode() != null)
                        warningFullMatchList.add(customerCSI.getWarningCode().getCode());
                } else {
                    if(customerCSI.getWarningCode() != null)
                        warningSomeMatchList.add(customerCSI.getWarningCode().getCode());
                }
            }
        }
        customerInfo.setCsiFullyMatchCode(warningFullMatchList);
        customerInfo.setCsiSomeMatchCode(warningSomeMatchList);
        customerInfo.setQualitativeClass("P");      //Default for PreScreen
        customerInfo.setKycLevel(customer.getKycLevel() != null ? customer.getKycLevel().getKycLevel() : 0);

            /*Start setting TMB Account for each customer*/
        List<CustomerOblAccountInfo> oblAccountInfoList = customerOblAccountInfoDAO.findByCustomerId(customer.getId());
        List<BRMSTMBAccountInfo> tmbAccountInfoList = new ArrayList<BRMSTMBAccountInfo>();
        if(oblAccountInfoList != null && oblAccountInfoList.size() > 0){
            for(CustomerOblAccountInfo customerOblAccountInfo : oblAccountInfoList){
                tmbAccountInfoList.add(getBRMSTMBAccountInfo(customerOblAccountInfo));
            }
        }
        customerInfo.setTmbAccountInfoList(tmbAccountInfoList);
        if(customer.getWorthiness() == 0){
            customerInfo.setCreditWorthiness("NA");
        }else if(customer.getWorthiness() == 3){
            customerInfo.setCreditWorthiness("P");
        }else if(customer.getWorthiness() == 4){
            customerInfo.setCreditWorthiness("F");
        }else if(customer.getWorthiness() == 5){
            customerInfo.setCreditWorthiness("NA");
        }

        return customerInfo;
    }

    public BRMSCustomerInfo getCustomerInfoWithoutCreditAccount(Customer customer, Date checkDate){
        BRMSCustomerInfo customerInfo = new BRMSCustomerInfo();
        customerInfo.setCustomerId(String.valueOf(customer.getId()));
        if(customer.getRelation() != null)
            customerInfo.setRelation(customer.getRelation().getBrmsCode());
        if(customer.getCustomerEntity() != null)
            customerInfo.setCustomerEntity(customer.getCustomerEntity().getBrmsCode());
        if(customer.getReference() != null)
            customerInfo.setReference(customer.getReference().getBrmsCode());

        CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();

        if(customerOblInfo == null) {
            customerInfo.setExistingSMECustomer(Boolean.FALSE);
        } else {
            customerInfo.setExistingSMECustomer(getRadioBoolean(customerOblInfo.getExistingSMECustomer()));
        }
        if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
            Juristic juristic = customer.getJuristic();
            customerInfo.setIndividual(Boolean.FALSE);
            customerInfo.setPersonalID(juristic.getRegistrationId());
        } else if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
            Individual individual = customer.getIndividual();
            customerInfo.setIndividual(Boolean.TRUE);
            customerInfo.setPersonalID(individual.getCitizenId());
            customerInfo.setAgeMonths(DateTimeUtil.monthBetween2Dates(individual.getBirthDate(), checkDate));
            if(individual.getNationality() != null)
                customerInfo.setNationality(individual.getNationality().getCode());
            if(individual.getMaritalStatus() != null)
                customerInfo.setMarriageStatus(individual.getMaritalStatus().getCode());

            if(isActive(customer.getSpouse())){
                Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                Individual spouseIndv = spouse.getIndividual();
                customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                if(spouse.getReference() != null)
                customerInfo.setRelation(spouse.getRelation().getBrmsCode());
            } else {
                if(isActive(individual.getMaritalStatus().getSpouseFlag())) {
                    Customer spouse = customerDAO.findById(customer.getSpouseId());
                    Individual spouseIndv = spouse.getIndividual();
                    customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                    if(spouse.getRelation() != null)
                        customerInfo.setRelation(spouse.getRelation().getBrmsCode());
                }
            }
        }
        return customerInfo;
    }

    private BRMSAccountRequested getBRMSAccountRequested(ProposeCreditInfo newCreditDetail, BigDecimal discountFrontEndFeeRate){
        logger.debug("-- getBRMSAccountRequested with newCreditDetail {}, discountFrontEndFeeRate {}", newCreditDetail, discountFrontEndFeeRate);
        if(newCreditDetail == null){
            logger.debug("getBRMSAccountRequested return null");
            return null;
        }

        BRMSAccountRequested accountRequested = new BRMSAccountRequested();
        accountRequested.setCreditDetailId(String.valueOf(newCreditDetail.getId()));

        if(newCreditDetail.getProductProgram() != null)
            accountRequested.setProductProgram(newCreditDetail.getProductProgram().getBrmsCode());

        if(newCreditDetail.getCreditType() != null)
            accountRequested.setCreditType(newCreditDetail.getCreditType().getBrmsCode());

        if(newCreditDetail.getUwDecision() == DecisionType.REJECTED){
            accountRequested.setLimit(BigDecimal.ZERO);
        }else {
            accountRequested.setLimit(newCreditDetail.getLimit());
        }

        if(newCreditDetail.getProposeCreditInfoTierDetailList() != null){
            List<ProposeCreditInfoTierDetail> creditTierDetailList = newCreditDetail.getProposeCreditInfoTierDetailList();
            if(creditTierDetailList.size() > 0){
                int tenors = 0;
                for(ProposeCreditInfoTierDetail newCreditTierDetail : creditTierDetailList){
                    tenors = tenors + newCreditTierDetail.getTenor();
                }
                accountRequested.setTenors(tenors);
            }
        }

        if(newCreditDetail.getLoanPurpose() != null)
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

    private BRMSAccountRequested getBRMSAccountRequested(PrescreenFacility prescreenFacility){
        logger.debug("-- getBRMSAccountRequested with PrescreenFacility {}", prescreenFacility);
        if(prescreenFacility == null){
            logger.debug("getBRMSAccountRequested return null");
            return null;
        }

        BRMSAccountRequested accountRequested = new BRMSAccountRequested();
        accountRequested.setCreditDetailId(String.valueOf(prescreenFacility.getId()));
        if(prescreenFacility.getProductProgram() != null)
            accountRequested.setProductProgram(prescreenFacility.getProductProgram().getBrmsCode());
        if(prescreenFacility.getCreditType() != null)
            accountRequested.setCreditType(prescreenFacility.getCreditType().getBrmsCode());
        accountRequested.setLimit(prescreenFacility.getRequestAmount());

        return accountRequested;
    }

    private BRMSBizInfo getBRMSBizInfo(PrescreenBusiness prescreenBusiness){
        logger.debug("- getBRMSBizInfo with prescreenBusiness {}", prescreenBusiness);
        BRMSBizInfo brmsBizInfo = new BRMSBizInfo();
        brmsBizInfo.setId(String.valueOf(prescreenBusiness.getId()));
        BusinessDescription businessDescription = prescreenBusiness.getBusinessDescription();
        brmsBizInfo = updateBusinessDescription(businessDescription, brmsBizInfo);
        logger.debug("return BRMSBizInfo {}", brmsBizInfo);
        return brmsBizInfo;
    }

    private BRMSBizInfo getBRMSBizInfo(BizInfoDetail bizInfoDetail){
        logger.debug("- getBRMSBizInfo with bizInfoDetail {}", bizInfoDetail);
        BRMSBizInfo brmsBizInfo = new BRMSBizInfo();
        brmsBizInfo.setId(String.valueOf(bizInfoDetail.getId()));
        BusinessDescription businessDescription = bizInfoDetail.getBusinessDescription();
        brmsBizInfo = updateBusinessDescription(businessDescription, brmsBizInfo);
        logger.debug("return BRMSBizInfo {}", brmsBizInfo);
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

    private MandateDocResponseView getMandateDocViewMap(List<DocumentDetail> documentDetailList, List<Customer> customerList, Step step){
        logger.debug("-- begin getMandateDocResponseView with documentDetailList {}, customerList {}", documentDetailList, customerList);

        //Transform Result from Document Customer Response//
        MandateDocResponseView mandateDocResponseView = new MandateDocResponseView();
        Map<String, MandateDocView> mandateDocViewMap = new ConcurrentHashMap<String, MandateDocView>();
        Map<String, MandateDocView> optionalDocViewMap = new ConcurrentHashMap<String, MandateDocView>();
        List<String> ecmDocTypeIdList = new ArrayList<String>();

        if(documentDetailList != null){
            logger.debug("- documentDetailList is NOT null");
            for(DocumentDetail documentDetail : documentDetailList){
                MandateDocView mandateDocView = null;
                //1. check mandate doc/optional doc type
                long stepId = getLong(documentDetail.getStep());

                //Check if the step is in Post Approval Process, the mandatory flag must check against Operation Flag.
                boolean isMandate = Boolean.FALSE;
                boolean showFlag = Boolean.FALSE;
                if(step.getStage().getId() == StageValue.AGREEMENT_PLEDGE_REGISTRATION.value() ||
                        step.getStage().getId() == StageValue.CUSTOMER_ACCEPTANCE_FINAL_APPROVAL.value() ||
                        step.getStage().getId() == StageValue.DISBURSEMENT.value() ||
                        step.getStage().getId() == StageValue.LIMIT_SET_UP.value()){
                    if(documentDetail.isOperMandateFlag()){
                        long operStepId = getLong(documentDetail.getOperStep());
                        if(operStepId == step.getId()){
                            isMandate = Boolean.TRUE;
                        } else {
                            isMandate = Boolean.FALSE;
                        }
                    }
                    showFlag = BRMSYesNo.lookup(documentDetail.getOperShowFlag()).boolValue();
                } else {
                    if(documentDetail.isMandateFlag()){
                        if(stepId != 0 && stepId == step.getId()){
                            logger.debug("Document is Mandate for this step {}.", step);
                            isMandate = Boolean.TRUE;
                        }
                    }
                    showFlag = BRMSYesNo.lookup(documentDetail.getShowFlag()).boolValue();
                }

                if(isMandate){
                    mandateDocView = mandateDocViewMap.get(documentDetail.getId());
                    if(mandateDocView == null){
                        mandateDocView = _intialMandateDocView();
                    }
                    mandateDocView.setDocMandateType(DocMandateType.MANDATE);
                } else {
                    mandateDocView = optionalDocViewMap.get(documentDetail.getId());
                    if(mandateDocView == null){
                        mandateDocView = _intialMandateDocView();
                    }
                    mandateDocView.setDocMandateType(DocMandateType.OPTIONAL);
                }
                mandateDocView.setDisplay(showFlag);

                //2. Set ECM Document Type ID
                mandateDocView.setEcmDocTypeId(documentDetail.getId());
                //3. Set Doc Level (Application or Customer)
                mandateDocView.setDocLevel(documentDetail.getDocLevel());

                //4. Set BRMS Document Description
                List<String> brmsList = mandateDocView.getBrmsDescList();
                if(brmsList == null)
                    brmsList = new ArrayList<String>();
                brmsList.add(documentDetail.getDescription());
                mandateDocView.setBrmsDescList(brmsList);

                //5. Set Document Owner - CustomerInfoSimpleView if it is Customer Level
                if(DocLevel.CUS_LEVEL.equals(documentDetail.getDocLevel())) {
                    long _customerId = getLong(documentDetail.getDocOwner());
                    List<CustomerInfoSimpleView> customerInfoSimpleViewList = mandateDocView.getCustomerInfoSimpleViewList();
                    if(customerInfoSimpleViewList == null)
                        customerInfoSimpleViewList = new ArrayList<CustomerInfoSimpleView>();

                    CustomerInfoSimpleView customerInfoSimpleView = null;
                    logger.debug("- customerInfoSimpleViewList[{}]", customerInfoSimpleViewList);
                    for(CustomerInfoSimpleView _customerInfoSimpleView : customerInfoSimpleViewList){
                        if(_customerInfoSimpleView.getId() == _customerId){
                            customerInfoSimpleView = _customerInfoSimpleView;
                            logger.debug("- Already Found Customer in MandateDocCustomerList {}", _customerInfoSimpleView);
                            break;
                        }
                    }

                    if(customerInfoSimpleView == null){
                        if(customerList != null) {
                            logger.debug("Add New Customer Info");
                            for(Customer customer : customerList){
                                if(customer.getId() == _customerId){
                                    customerInfoSimpleView = customerTransform.transformToSimpleView(customer);
                                    customerInfoSimpleViewList.add(customerInfoSimpleView);
                                    logger.debug("Add Customer Info Simple View into Mandate Doc Customer List customerInfoSimpleView {}", customerInfoSimpleView);
                                    break;
                                }
                            }
                        }
                    }
                    mandateDocView.setCustomerInfoSimpleViewList(customerInfoSimpleViewList);
                }

                //6. set number of document
                mandateDocView.setNumberOfDoc(mandateDocView.getNumberOfDoc() + 1);

                if(isMandate){
                    logger.debug("put into Mandate Doc List");
                    mandateDocViewMap.put(documentDetail.getId(), mandateDocView);
                } else {
                    logger.debug("put into Optional Doc List");
                    optionalDocViewMap.put(documentDetail.getId(), mandateDocView);
                }

                //7. Store ecmDocId to query doc description
                ecmDocTypeIdList.add(documentDetail.getId());
            }
        }

        logger.debug("-- ecmDocTypeId: {}", ecmDocTypeIdList);

        mandateDocResponseView.setMandateDocViewMap(mandateDocViewMap);
        mandateDocResponseView.setOptionalDocViewMap(optionalDocViewMap);
        mandateDocResponseView.setEcmDocTypeIDList(ecmDocTypeIdList);

        logger.debug("-- end getMandateDocViewMap return {}", mandateDocResponseView);
        return mandateDocResponseView;
    }

    private MandateDocView _intialMandateDocView(){
        MandateDocView mandateDocView = new MandateDocView();
        mandateDocView.setNumberOfDoc(0);
        return mandateDocView;
    }

    private MandateDocResponseView getMandateDocViewMap(List<MandateDocument> mandateDocumentList, List<Customer> customerList){
        //Transform Result from Document Customer Response//
        MandateDocResponseView mandateDocResponseView = new MandateDocResponseView();

        Map<String, MandateDocView> mandateDocViewMap = new HashMap<String, MandateDocView>();
        List<String> ecmDocTypeIdList = new ArrayList<String>();

        for(MandateDocument mandateDocument : mandateDocumentList){

            MandateDocView mandateDocView = mandateDocViewMap.get(mandateDocument.getId());
            if(mandateDocView == null){
                mandateDocView = new MandateDocView();
                mandateDocView.setNumberOfDoc(0);
            }

            mandateDocView.setEcmDocTypeId(mandateDocument.getEcmDocId());
            mandateDocView.setDocLevel(mandateDocument.getDocLevel());
            mandateDocView.setDocMandateType(DocMandateType.MANDATE);

            //2. Set BRMS Document Description
            List<String> brmsList = mandateDocView.getBrmsDescList();
            if(brmsList == null)
                brmsList = new ArrayList<String>();
            brmsList.add(mandateDocument.getDescription());
            mandateDocView.setBrmsDescList(brmsList);
            int numberOfDoc = mandateDocView.getNumberOfDoc();

            if(DocLevel.CUS_LEVEL.equals(mandateDocument.getDocLevel())){
                List<CustomerInfoSimpleView> customerInfoSimpleViewList = mandateDocView.getCustomerInfoSimpleViewList();
                if(customerInfoSimpleViewList == null)
                    customerInfoSimpleViewList = new ArrayList<CustomerInfoSimpleView>();

                logger.debug("Add New Customer Info");
                for(Customer customer : customerList){
                    if(customer.getRelation().equals(mandateDocument.getRelation())){
                        Customer _customerInfo = null;
                        for(CustomerInfoSimpleView _compare : customerInfoSimpleViewList){
                            if(_compare.getId() == customer.getId()){
                                _customerInfo = customer;
                                break;
                            }
                        }

                        if(_customerInfo == null){
                            CustomerInfoSimpleView customerInfoSimpleView = customerTransform.transformToSimpleView(customer);
                            customerInfoSimpleViewList.add(customerInfoSimpleView);
                            logger.debug("Add Customer Info Simple View into Mandate Doc Customer List customerInfoSimpleView {}", customerInfoSimpleView);
                        }
                        numberOfDoc++;
                    }
                }

                mandateDocView.setCustomerInfoSimpleViewList(customerInfoSimpleViewList);
            }else {
                numberOfDoc++;
            }

            mandateDocView.setNumberOfDoc(numberOfDoc);
            mandateDocView.setDisplay(true);
            mandateDocViewMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);

            //3. Store ecmDocId to query doc description
            ecmDocTypeIdList.add(mandateDocView.getEcmDocTypeId());
        }

        mandateDocResponseView.setMandateDocViewMap(mandateDocViewMap);
        mandateDocResponseView.setEcmDocTypeIDList(ecmDocTypeIdList);
        return mandateDocResponseView;
    }

    //For Converting Document ID
    private long getLong(String value){
        if(value == null || "".equals(value))
            return 0;
        else {
            try{
                return Long.parseLong(value);
            } catch (Exception ex){
                return 0;
            }
        }
    }
}
