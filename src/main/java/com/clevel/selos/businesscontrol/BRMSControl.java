package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.MandateDocumentDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.*;
import com.clevel.selos.integration.corebanking.model.CustomerInfo;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.MandateDocument;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.MandateDocResponseView;
import com.clevel.selos.model.view.MandateDocView;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.util.DateTimeUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

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
    private BAPAInfoDAO bapaInfoDAO;

    @Inject
    private ExSummaryDAO exSummaryDAO;

    @Inject
    private DecisionDAO decisionDAO;

    @Inject
    private MandateDocumentDAO mandateDocumentDAO;

    @Inject
    private CustomerTransform customerTransform;

    @Inject
    public BRMSControl(){}

    public StandardPricingResponse getPriceFeeInterest(long workCaseId){
        BRMSApplicationInfo applicationInfo = getApplicationInfoForPricing(workCaseId);
        StandardPricingResponse _returnPricingResponse = new StandardPricingResponse();

        StandardPricingResponse _tmpPricingIntResponse = brmsInterface.checkStandardPricingIntRule(applicationInfo);
        StandardPricingResponse _tmpPricingFeeResponse = brmsInterface.checkStandardPricingFeeRule(applicationInfo);
        logger.debug("-- _tmpPricingIntResponse.getActionResult() {}", _tmpPricingIntResponse.getActionResult());
        logger.debug("-- _tmpPricingFeeResponse.getActionResult() {}", _tmpPricingFeeResponse.getActionResult());
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

        List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailDAO.findGuarantorByProposeType(workCaseId, workCase.getStep().getProposeType());
        for(NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList){
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

        List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, _proposeType);
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
        NewCreditFacility newCreditFacility = creditFacilityDAO.findByWorkCaseId(workCaseId);
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
        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, _proposeType);
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));

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

        if(workCasePrescreen.getStep() != null)
            applicationInfo.setStepCode(workCasePrescreen.getStep().getCode());
        if(prescreen.getBusinessLocation() != null)
            applicationInfo.setBizLocation(String.valueOf(prescreen.getBusinessLocation().getCode()));

        applicationInfo.setYearInBusinessMonth(new BigDecimal(DateTimeUtil.monthBetween2Dates(prescreen.getRegisterDate(), now)));
        if(prescreen.getCountryOfRegister() != null)
            applicationInfo.setCountryOfRegistration(prescreen.getCountryOfRegister().getCode2());

        BigDecimal borrowerGroupIncome = BigDecimal.ZERO;

        boolean appExistingSMECustomer = Boolean.TRUE;
        List<Customer> customerList = customerDAO.findByWorkCasePreScreenId(workcasePrescreenId);
        List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
        for(Customer customer : customerList) {
            if(customer.getRelation().getId() == RelationValue.BORROWER.value()){
                borrowerGroupIncome = borrowerGroupIncome.add(customer.getApproxIncome());
            }

            customerInfoList.add(getBRMSCustomerInfo(customer, checkDate));
        }
        applicationInfo.setCustomerInfoList(customerInfoList);

        /** Setup Bank Statement Account **/
        //2. Set BankStatement Info
        List<BRMSAccountStmtInfo> accountStmtInfoList = new ArrayList<BRMSAccountStmtInfo>();
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkcasePrescreenId(workcasePrescreenId);
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
        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityDAO.findByPreScreenId(workcasePrescreenId);
        List<BRMSAccountRequested> accountRequestedList = new ArrayList<BRMSAccountRequested>();
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
        List<PrescreenBusiness> businessList = prescreenBusinessDAO.findByPreScreenId(workcasePrescreenId);
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
            BRMSCustomerInfo brmsCustomerInfo = getBRMSCustomerInfo(customer, checkDate);
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
            accountStmtInfoList.add(getBRMSAccountStmtInfo(bankStatement));
        }
        applicationInfo.setAccountStmtInfoList(accountStmtInfoList);

        //3. Set Biz Info
        List<BRMSBizInfo> brmsBizInfoList = new ArrayList<BRMSBizInfo>();
        BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);
        List<BizInfoDetail> bizInfoDetailList = bizInfoSummary.getBizInfoDetailList();
        for(BizInfoDetail bizInfoDetail : bizInfoDetailList){
            brmsBizInfoList.add(getBRMSBizInfo(bizInfoDetail));
        }
        applicationInfo.setBizInfoList(brmsBizInfoList);

        //4. Set TMB Account Request
        NewCreditFacility newCreditFacility = creditFacilityDAO.findByWorkCaseId(workCaseId);
        BigDecimal discountFrontEndFeeRate = newCreditFacility.getFrontendFeeDOA();
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);

        ProposeType _proposeType = ProposeType.P;
        if(workCase.getStep() != null)
            _proposeType = workCase.getStep().getProposeType();

        if( _proposeType.equals(ProposeType.P)){
            if(newCreditFacility.getLoanRequestType() != null)
                applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
        }
        else if(_proposeType.equals(ProposeType.A)){
            if(newCreditFacility.getLoanRequestType() != null)
                applicationInfo.setLoanRequestType(newCreditFacility.getLoanRequestType().getBrmsCode());
            applicationInfo.setFinalGroupExposure(decision.getTotalApproveExposure());
        }

        BigDecimal totalApprovedCredit = BigDecimal.ZERO;
        List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, _proposeType);
        List<BRMSAccountRequested> accountRequestedList = new ArrayList();
        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, discountFrontEndFeeRate));

                if(!newCreditDetail.getProductProgram().isBa())
                    totalApprovedCredit = totalApprovedCredit.add(newCreditDetail.getLimit());
            }
        }
        applicationInfo.setAccountRequestedList(accountRequestedList);

        //5. Set TMB Coll Level
        List<NewCollateral> newCollateralList = newCollateralDAO.findNewCollateral(workCaseId, _proposeType);
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
                    if(newCollateralHead.getHeadCollType() != null)
                        brmsCollateralInfo.setCollateralType(newCollateralHead.getHeadCollType().getCode());
                    if(newCollateralHead.getSubCollateralType() != null)
                        brmsCollateralInfo.setSubCollateralType(newCollateralHead.getSubCollateralType().getCode());
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

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);
        DBR dbr = dbrdao.findByWorkCaseId(workCaseId);
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setProcessDate(checkDate);
        applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
        applicationInfo.setExpectedSubmitDate(bankStatementSummary.getExpectedSubmitDate());
        if(basicInfo.getBorrowerType() != null)
            applicationInfo.setBorrowerType(basicInfo.getBorrowerType().getBrmsCode());
        applicationInfo.setRequestLoanWithSameName(getRadioBoolean(basicInfo.getRequestLoanWithSameName()));
        applicationInfo.setRefinanceIN(getRadioBoolean(basicInfo.getRefinanceIN()));
        applicationInfo.setRefinanceOUT(getRadioBoolean(basicInfo.getRefinanceOUT()));
        applicationInfo.setRequestTCG(getRadioBoolean(tcg.getTcgFlag()));

        WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByWorkcaseId(workCaseId);
        AppraisalStatus appraisalStatus = AppraisalStatus.lookup(workCaseAppraisal.getAppraisalResult());
        applicationInfo.setPassAppraisalProcess(appraisalStatus.booleanValue());
        if(workCase.getStep() != null)
            applicationInfo.setStepCode(workCase.getStep().getCode());

        applicationInfo.setNumberOfYearFinancialStmt(new BigDecimal(DateTimeUtil.getYearOfDate(checkDate) - latestFinancialStmtYear));
        applicationInfo.setShareHolderRatio(shareHolderRatio);
        applicationInfo.setFinalDBR(dbr.getFinalDBR());
        applicationInfo.setNetMonthlyIncome(dbr.getNetMonthlyIncome());

        ExSummary exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);

        if(ProposeType.P.equals(_proposeType)){
            applicationInfo.setBorrowerGroupIncome(bankStatementSummary.getGrdTotalIncomeNetBDM());
            applicationInfo.setTotalGroupIncome(exSummary.getGroupSaleBDM());
        } else if(ProposeType.A.equals(_proposeType)){
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

        applicationInfo.setTotalApprovedCredit(totalApprovedCredit);
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

        if(bizInfoSummary.getProvince() != null)
            applicationInfo.setBizLocation(String.valueOf(bizInfoSummary.getProvince().getCode()));

        if(bizInfoSummary.getCountry() != null)
            applicationInfo.setCountryOfRegistration(bizInfoSummary.getCountry().getCode2());

        applicationInfo.setTradeChequeReturnPercent(bankStatementSummary.getGrdTotalTDChqRetPercent());

        if(workCase.getProductGroup() != null)
            applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());

        applicationInfo.setMaximumSMELimit(newCreditFacility.getMaximumSMELimit());

        if(bizInfoSummary.getReferredExperience() != null)
            applicationInfo.setReferredDocType(bizInfoSummary.getReferredExperience().getBrmsCode());

        applicationInfo.setNetFixAsset(bizInfoSummary.getNetFixAsset());

        UWRulesResponse uwRulesResponse = brmsInterface.checkPreScreenRule(applicationInfo);
        logger.debug("-- end getFullApplicationResult return {}", uwRulesResponse);

        return uwRulesResponse;
    }

    public MandateDocResponseView getDocCustomer(long workCaseId){
        logger.debug("getDocCustomer from workCaseId {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        List<MandateDocument> mandateDocumentList = null;

        if(workCase.getStep() != null) {
            mandateDocumentList = mandateDocumentDAO.findByStep(workCase.getStep().getId());
        }

        MandateDocResponseView mandateDocResponseView = new MandateDocResponseView();
        if(mandateDocumentList != null && mandateDocumentList.size() > 0){
            logger.debug("Get Mandate Document from mst_mandate_document {}", mandateDocumentList);
            mandateDocResponseView.setActionResult(ActionResult.SUCCESS);
            List<Customer> customerInfoList = customerDAO.findCustomerByWorkCaseId(workCaseId);
            mandateDocResponseView.setMandateDocViewMap(getMandateDocViewMap(mandateDocumentList, customerInfoList));
            logger.debug("-- Get Mandate Document from mandate_master {}", mandateDocResponseView);
        } else {
            Date checkDate = Calendar.getInstance().getTime();
            logger.debug("check at date {}", checkDate);
            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
            //1. Set Customer Info List
            List<BRMSCustomerInfo> customerInfoList = new ArrayList<BRMSCustomerInfo>();
            List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
            for(Customer customer : customerList){
                BRMSCustomerInfo brmsCustomerInfo = getCustomerInfoWithoutCreditAccount(customer, checkDate);
                customerInfoList.add(brmsCustomerInfo);
            }
            applicationInfo.setCustomerInfoList(customerInfoList);

            //2. Set Account Requested List
            ProposeType _proposeType = ProposeType.P;
            if(workCase.getStep() != null)
                _proposeType = workCase.getStep().getProposeType();

            List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCaseId, _proposeType);
            List<BRMSAccountRequested> accountRequestedList = new ArrayList();
            for(NewCreditDetail newCreditDetail : newCreditDetailList){
                if(newCreditDetail.getRequestType() == RequestTypes.NEW.value()){
                    accountRequestedList.add(getBRMSAccountRequested(newCreditDetail, null));
                }
            }
            applicationInfo.setAccountRequestedList(accountRequestedList);

            //3. Set Application Information.
            applicationInfo.setApplicationNo(workCase.getAppNumber());
            applicationInfo.setProcessDate(checkDate);
            applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
            if(basicInfo.getBorrowerType() != null)
                applicationInfo.setBorrowerType(basicInfo.getBorrowerType().getBrmsCode());
            applicationInfo.setExistingSMECustomer(getRadioBoolean(basicInfo.getExistingSMECustomer()));
            applicationInfo.setRequestLoanWithSameName(getRadioBoolean(basicInfo.getRequestLoanWithSameName()));
            applicationInfo.setRefinanceIN(getRadioBoolean(basicInfo.getRefinanceIN()));
            applicationInfo.setRefinanceOUT(getRadioBoolean(basicInfo.getRefinanceOUT()));

            BAPAInfo bapaInfo = bapaInfoDAO.findByWorkCase(workCaseId);

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

            TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);
            applicationInfo.setRequestTCG(getRadioBoolean(tcg.getTcgFlag()));
            if(workCase.getStep() != null)
                applicationInfo.setStepCode(workCase.getStep().getCode());
            if(workCase.getProductGroup() != null)
                applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());

            BizInfoSummary bizInfoSummary = bizInfoSummaryDAO.findByWorkCaseId(workCaseId);

            if(bizInfoSummary.getReferredExperience() != null)
                applicationInfo.setReferredDocType(bizInfoSummary.getReferredExperience().getBrmsCode());

            DocCustomerResponse docCustomerResponse = brmsInterface.checkDocCustomerRule(applicationInfo);
            logger.debug("docCustomerResponse return {}", docCustomerResponse);

            if(ActionResult.SUCCESS.equals(docCustomerResponse.getActionResult())){
                Map<String, MandateDocView> mandateDocViewMap = getMandateDocViewMap(docCustomerResponse.getDocumentDetailList(), customerList, workCase.getStep());
                mandateDocResponseView.setActionResult(docCustomerResponse.getActionResult());
                mandateDocResponseView.setMandateDocViewMap(mandateDocViewMap);
            } else {
                mandateDocResponseView.setActionResult(docCustomerResponse.getActionResult());
                mandateDocResponseView.setReason(docCustomerResponse.getReason());
            }

            logger.debug("-- end getDocCustomer return {}", mandateDocResponseView);
        }

        return mandateDocResponseView;
    }

    public MandateDocResponseView getDocAppraisal(long workCaseId){
        logger.debug("getDocAppraisal from workCaseId {}", workCaseId);
        Date checkDate = Calendar.getInstance().getTime();
        logger.debug("check at date {}", checkDate);

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        BRMSApplicationInfo applicationInfo = new BRMSApplicationInfo();
        applicationInfo.setApplicationNo(workCase.getAppNumber());
        applicationInfo.setProcessDate(checkDate);
        applicationInfo.setBdmSubmitDate(basicInfo.getBdmSubmitDate());
        if(workCase.getProductGroup() != null)
            applicationInfo.setProductGroup(workCase.getProductGroup().getBrmsCode());

        DocAppraisalResponse docAppraisalResponse = brmsInterface.checkDocAppraisalRule(applicationInfo);
        logger.debug("-- end getDocAppraisal ", docAppraisalResponse);

        MandateDocResponseView mandateDocResponseView = new MandateDocResponseView();
        if(ActionResult.SUCCESS.equals(docAppraisalResponse.getActionResult())){
            Map<String, MandateDocView> mandateDocViewMap = getMandateDocViewMap(docAppraisalResponse.getDocumentDetailList(), null, workCase.getStep());
            mandateDocResponseView.setActionResult(docAppraisalResponse.getActionResult());
            mandateDocResponseView.setMandateDocViewMap(mandateDocViewMap);
        } else {
            mandateDocResponseView.setActionResult(docAppraisalResponse.getActionResult());
            mandateDocResponseView.setReason(docAppraisalResponse.getReason());
        }

        logger.debug("-- end getDocCustomer return {}", mandateDocResponseView);
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
        accountStmtInfo.setTotalTransaction(bankStatement.getTotalTransaction());
        accountStmtInfo.setMainAccount(getRadioBoolean(bankStatement.getMainAccount()));
        accountStmtInfo.setHighestInflow(toBoolean(bankStatement.getHighestInflow()));
        accountStmtInfo.setTmb(toBoolean(bankStatement.getTMB()));
        accountStmtInfo.setNotCountIncome(isActive(bankStatement.getNotCountIncome()));
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
        ncbAccountInfo.setTmbFlag(isActive(ncbDetail.getAccountTMBFlag()));
        ncbAccountInfo.setNplFlag(isActive(ncbDetail.getNplFlag()));
        ncbAccountInfo.setCreditAmtAtNPLDate(ncbDetail.getNplCreditAmount());
        ncbAccountInfo.setTdrFlag(isActive(ncbDetail.getTdrFlag()));
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
                ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(DateTimeUtil.monthBetween2Dates(ncbDetail.getAccountCloseDate(), checkDate)));
        else
            ncbAccountInfo.setAccountCloseDateMonths(String.valueOf(0));
        return ncbAccountInfo;
    }

    private BRMSTMBAccountInfo getBRMSTMBAccountInfo(CustomerOblAccountInfo customerOblAccountInfo){
        logger.debug("getBRMSTMBAccountInfo CustomerOblAccountInfo {}", customerOblAccountInfo);
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
            customerInfo.setRatingFinal(String.valueOf(customerOblInfo.getRatingFinal() == null? "" : customerOblInfo.getRatingFinal().getScore()));
            if(customerOblInfo.getUnpaidFeeInsurance() != null)
                customerInfo.setUnpaidFeeInsurance(customerOblInfo.getUnpaidFeeInsurance().compareTo(BigDecimal.ZERO) != 0);
            if(customerOblInfo.getPendingClaimLG() != null)
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
            if(individual.getNationality() != null)
                customerInfo.setNationality(individual.getNationality().getCode());
            if(individual.getMaritalStatus() != null)
                customerInfo.setMarriageStatus(individual.getMaritalStatus().getCode());

            if(isActive(customer.getSpouse())){
                Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                Individual spouseIndv = spouse.getIndividual();
                customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                if(spouse.getRelation() != null)
                    customerInfo.setRelation(spouse.getRelation().getBrmsCode());
            } else {
                if(isActive(individual.getMaritalStatus().getSpouseFlag())) {
                    //Customer spouse = customerDAO.findMainCustomerBySpouseId(customer.getId());
                    Customer spouse = customerDAO.findById(customer.getSpouseId());
                    Individual spouseIndv = spouse.getIndividual();
                    customerInfo.setSpousePersonalID(spouseIndv.getCitizenId());
                    if(spouse.getRelation() != null)
                        customerInfo.setRelation(spouse.getRelation().getBrmsCode());
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
        customerInfo.setQualitativeClass("P");

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

    private BRMSAccountRequested getBRMSAccountRequested(NewCreditDetail newCreditDetail, BigDecimal discountFrontEndFeeRate){
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

    private Map<String, MandateDocView> getMandateDocViewMap(List<DocumentDetail> documentDetailList, List<Customer> customerList, Step step){
        logger.debug("-- begin getMandateDocResponseView with documentDetailList {}, customerList {}", documentDetailList, customerList);

        //Transform Result from Document Customer Response//
        Map<String, MandateDocView> mandateDocViewMap = new HashMap<String, MandateDocView>();
        if(documentDetailList != null){
            logger.debug("- documentDetailList is NOT null");
            for(DocumentDetail documentDetail : documentDetailList){
                MandateDocView mandateDocView = mandateDocViewMap.get(documentDetail.getId());
                if(mandateDocView == null){
                    mandateDocView = new MandateDocView();
                    mandateDocView.setNumberOfDoc(0);
                }

                //1. Set ECM Document Type ID
                mandateDocView.setEcmDocTypeId(documentDetail.getId());
                //2. Set Doc Level (Application or Customer)
                mandateDocView.setDocLevel(documentDetail.getDocLevel());

                //3. Set BRMS Document Description
                List<String> brmsList = mandateDocView.getBrmsDescList();
                if(brmsList == null)
                    brmsList = new ArrayList<String>();
                brmsList.add(documentDetail.getDescription());
                mandateDocView.setBrmsDescList(brmsList);

                //4. Set Document Owner - CustomerInfoSimpleView if it is Customer Level
                if(DocLevel.CUS_LEVEL.equals(documentDetail.getDocLevel())) {
                    long _customerId = getLong(documentDetail.getDocOwner());
                    List<CustomerInfoSimpleView> customerInfoSimpleViewList = mandateDocView.getCustomerInfoSimpleViewList();
                    if(customerInfoSimpleViewList == null)
                        customerInfoSimpleViewList = new ArrayList<CustomerInfoSimpleView>();

                    CustomerInfoSimpleView customerInfoSimpleView = null;
                    for(CustomerInfoSimpleView _customerInfoSimpleView : customerInfoSimpleViewList){
                        if(_customerInfoSimpleView.getId() == _customerId){
                            customerInfoSimpleView = _customerInfoSimpleView;
                            logger.debug("Already Found Customer in MandateDocCustomerList {}", _customerInfoSimpleView);
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

                //5. set number of document
                mandateDocView.setNumberOfDoc(mandateDocView.getNumberOfDoc() + 1);

                //6. set mandate doc/optional doc type
                long stepId = getLong(documentDetail.getStep());
                if(documentDetail.isMandateFlag()){
                    if(stepId != 0 && stepId == step.getId()){
                        logger.debug("Document is Mandate for this step {}.", step);
                        mandateDocView.setDocMandateType(DocMandateType.MANDATE);
                    } else {
                        logger.debug("Document is Mandate for this ");
                        mandateDocView.setDocMandateType(DocMandateType.OPTIONAL);
                    }
                    mandateDocView.setDisplay(BRMSYesNo.lookup(documentDetail.getShowFlag()).boolValue());
                } else {
                    logger.debug("Document is NOT mandate for any steps.");
                    mandateDocView.setDocMandateType(DocMandateType.OPTIONAL);
                    mandateDocView.setDisplay(BRMSYesNo.lookup(documentDetail.getShowFlag()).boolValue());
                }

                stepId = getLong(documentDetail.getOperStep());
                if(documentDetail.isOperMandateFlag()){
                    if(stepId != 0 && stepId == step.getId()){
                        logger.debug("Document is Mandate for Oper step {}.", step);
                        mandateDocView.setDocMandateType(DocMandateType.MANDATE);
                    } else {
                        mandateDocView.setDocMandateType(DocMandateType.OPTIONAL);
                    }
                    mandateDocView.setDisplay(BRMSYesNo.lookup(documentDetail.getOperShowFlag()).boolValue());
                } else {
                    logger.debug("Document is NOT mandate for any steps.");
                    mandateDocView.setDocMandateType(DocMandateType.OPTIONAL);
                    mandateDocView.setDisplay(BRMSYesNo.lookup(documentDetail.getOperShowFlag()).boolValue());
                }

                mandateDocViewMap.put(documentDetail.getId(), mandateDocView);
            }
        }
        logger.debug("-- end getMandateDocViewMap return {}", mandateDocViewMap);
        return mandateDocViewMap;
    }

    private Map<String, MandateDocView> getMandateDocViewMap(List<MandateDocument> mandateDocumentList, List<Customer> customerList){
        //Transform Result from Document Customer Response//
        Map<String, MandateDocView> mandateDocViewMap = new HashMap<String, MandateDocView>();
        for(MandateDocument mandateDocument : mandateDocumentList){

            MandateDocView mandateDocView = mandateDocViewMap.get(mandateDocument.getId());
            if(mandateDocView == null){
                mandateDocView = new MandateDocView();
                mandateDocView.setNumberOfDoc(0);
            }

            mandateDocView.setEcmDocTypeId(mandateDocument.getEcmDocId());
            mandateDocView.setDocLevel(mandateDocument.getDocLevel());

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
                    if(customer.getCustomerEntity().equals(mandateDocument.getCustomerEntity())){
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
            mandateDocViewMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);
        }
        return mandateDocViewMap;
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
