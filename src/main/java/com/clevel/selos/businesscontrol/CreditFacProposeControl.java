package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Stateless
public class CreditFacProposeControl extends BusinessControl {
    @SELOS
    @Inject
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewCreditFacilityTransform newCreditFacilityTransform;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    CreditTypeDetailTransform creditTypeDetailTransform;
    @Inject
    NewCollateralTransform newCollateralTransform;
    @Inject
    NewGuarantorDetailTransform newGuarantorDetailTransform;
    @Inject
    NewConditionDetailTransform newConditionDetailTransform;
    @Inject
    NewCreditTierTransform newCreditTierTransform;
    @Inject
    ProposeCreditDetailTransform proposeCreditDetailTransform;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    NewCollateralCreditDAO newCollateralCreditDAO;
    @Inject
    NewFeeCreditDAO newFeeCreditDAO;
    @Inject
    NewConditionDetailDAO newConditionDetailDAO;
    @Inject
    NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    CreditTypeDetailDAO creditTypeDetailDAO;
    @Inject
    NewCollateralDAO newCollateralDetailDAO;
    @Inject
    NewCollateralSubDAO newCollateralSubDetailDAO;
    @Inject
    NewCollateralHeadDAO newCollateralHeadDetailDAO;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    CreditFacExistingControl creditFacExistingControl;
    @Inject
    ProductFormulaDAO productFormulaDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    NewGuarantorRelationDAO newGuarantorRelationDAO;
    @Inject
    NewCollateralCreditDAO newCollateralRelationDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    NewCollateralSubOwnerDAO newSubCollCustomerDAO;
    @Inject
    NewCollateralSubMortgageDAO newSubCollMortgageDAO;
    @Inject
    NewCollateralSubRelatedDAO newSubCollRelateDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    NewCollateralSubOwnerDAO newCollateralSubOwnerDAO;
    @Inject
    NewCollateralCreditTransform newCollateralCreditTransform;
    @Inject
    NewGuarantorCreditTransform newGuarantorCreditTransform;
    @Inject
    DBRControl dbrControl;
    @Inject
    BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    NCBInfoControl ncbInfoControl;
    @Inject
    ExistingCollateralDetailDAO existingCollateralDetailDAO;
    @Inject
    COMSInterface comsInterface;
    @Inject
    BRMSControl brmsControl;
    @Inject
    NewCollateralDAO newCollateralDAO;
    @Inject
    NewCollateralSubRelatedDAO newCollateralSubRelatedDAO;
    @Inject
    NewFeeDetailTransform newFeeDetailTransform;
    @Inject
    FeeTransform feeTransform;
    @Inject
    ProductTransform productTransform;
    @Inject
    FeeDetailDAO feeDetailDAO;
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;
    @Inject
    TCGCollateralTypeDAO tcgCollateralTypeDAO;
    @Inject
    PotentialColToTCGColDAO potentialColToTCGColDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;

    @Inject
    public CreditFacProposeControl() {
    }

    public NewCreditFacilityView findNewCreditFacilityByWorkCase(long workCaseId) {
        NewCreditFacilityView newCreditFacilityView = null;
        log.debug("findNewCreditFacilityByWorkCase start ::::");
        try {
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            log.debug("find new creditFacility: {}", newCreditFacility);
            if (newCreditFacility != null) {
                newCreditFacilityView = newCreditFacilityTransform.transformToView(newCreditFacility);

                List<FeeDetail> feeDetailList = feeDetailDAO.findAllByWorkCaseId(workCaseId);
                if (feeDetailList.size() > 0) {
                    log.debug("feeDetailList size:: {}", feeDetailList.size());
//                    List<FeeDetailView> feeDetailViewList = feeTransform.transformToView(feeDetailList);
//                    log.debug("feeDetailViewList : {}", feeDetailViewList);
//                    List<NewFeeDetailView> newFeeDetailViewList = transFormNewFeeDetailViewList(feeDetailViewList);
//                    newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
                }

                List<NewCreditDetail> newCreditList = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);
                if (newCreditList.size() > 0) {
                    log.debug("newCreditFacility.getNewCreditDetailList() :: {}", newCreditFacility.getNewCreditDetailList().size());
                    List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditList);
                    log.debug("newCreditDetailViewList : {}", newCreditDetailViewList);
                    newCreditFacilityView.setNewCreditDetailViewList(newCreditDetailViewList);
                }

                List<NewCollateral> newCollateralDetailList = newCollateralDetailDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
                if (newCollateralDetailList.size() > 0) {
                    log.debug("newCreditFacility.getNewCollateralDetailList() :: {}", newCreditFacility.getNewCollateralDetailList().size());
                    List<NewCollateralView> newCollateralViewList = newCollateralTransform.transformsCollateralToView(newCollateralDetailList);
                    log.debug("newCollateralViewList : {}", newCollateralViewList);
                    newCreditFacilityView.setNewCollateralViewList(newCollateralViewList);
                }

                List<NewGuarantorDetail> newGuarantorDetails = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
                if (newGuarantorDetails.size() > 0) {
                    log.debug("newGuarantorDetails:: {}", newGuarantorDetails.size());
                    List<NewGuarantorDetailView> newGuarantorDetailViewList = newGuarantorDetailTransform.transformToView(newGuarantorDetails);
                    log.debug("newGuarantorDetailViewList : {}", newGuarantorDetailViewList);
                    newCreditFacilityView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);
                }

                List<NewConditionDetail> newConditionDetailList = newConditionDetailDAO.findByNewCreditFacility(newCreditFacility);
                if (newConditionDetailList.size() > 0) {
                    log.debug("newConditionDetailList() :: {}", newConditionDetailList.size());
                    List<NewConditionDetailView> newConditionDetailViewList = newConditionDetailTransform.transformToView(newConditionDetailList);
                    log.debug("newConditionDetailViewList : {}", newConditionDetailViewList);
                    newCreditFacilityView.setNewConditionDetailViewList(newConditionDetailViewList);
                }
            }
        } catch (Exception ex) {
            log.error("exception while load data in page {}", ex);
        } finally {
            log.debug("findNewCreditFacilityByWorkCase end");
        }
        return newCreditFacilityView;
    }

    public List<NewFeeDetailView> transFormNewFeeDetailViewList(List<FeeDetailView> feeDetailViewList) {
        List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        Map<Long, NewFeeDetailView> newFeeDetailViewMap = new HashMap<Long, NewFeeDetailView>();
        NewFeeDetailView newFeeDetailView;
        for (FeeDetailView feeDetailView : feeDetailViewList) {
            if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                if (newFeeDetailViewMap.containsKey(feeDetailView.getCreditDetailViewId())) {
                    newFeeDetailView = newFeeDetailViewMap.get(feeDetailView.getCreditDetailViewId());
                } else {
                    newFeeDetailView = new NewFeeDetailView();
                    newFeeDetailViewMap.put(feeDetailView.getCreditDetailViewId(), newFeeDetailView);
                }

                log.debug("-- transformToView :: feeDetailView ::: {}", feeDetailView.toString());
                // find productProgram

                log.debug("feeDetailView.getFeeLevel() :::: {}", feeDetailView.getFeeLevel());
                log.debug("feeDetailView.getCreditDetailViewId() :::: {}", feeDetailView.getCreditDetailViewId());
                NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(feeDetailView.getCreditDetailViewId());
                if (newCreditDetail != null) {
                    NewCreditDetailView newCreditView = newCreditDetailTransform.transformToView(newCreditDetail);
                    log.debug("newCreditView.getProductProgramView().getId() :::: {}", newCreditView.getProductProgramView().getId());
                    ProductProgram productProgram = productProgramDAO.findById(newCreditView.getProductProgramView().getId());
                    if (productProgram != null) {
                        log.debug("productProgram :: {}", productProgram.toString());
                        newFeeDetailView.setProductProgram(productProgram.getName());
                    }
                    if ("9".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=9,(Front-End-Fee)
                        newFeeDetailView.setStandardFrontEndFee(feeDetailView);
                    } else if ("15".equals(feeDetailView.getFeeTypeView().getBrmsCode())) { //type=15,(Prepayment Fee)
                        newFeeDetailView.setPrepaymentFee(feeDetailView);
                    } else if ("20".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=20,(CancellationFee)
                        newFeeDetailView.setCancellationFee(feeDetailView);
                    } else if ("21".equals(feeDetailView.getFeeTypeView().getBrmsCode())) { //type=21,(ExtensionFee)
                        newFeeDetailView.setExtensionFee(feeDetailView);
                    } else if ("22".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=22,(CommitmentFee)
                        newFeeDetailView.setCommitmentFee(feeDetailView);
                    }

                    log.debug("FeePaymentMethodView():::: {}", feeDetailView.getFeePaymentMethodView().getBrmsCode());
                }
            }
        }

        if (newFeeDetailViewMap != null && newFeeDetailViewMap.size() > 0) {
            Iterator it = newFeeDetailViewMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                newFeeDetailViewList.add((NewFeeDetailView) pairs.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        return newFeeDetailViewList;
    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        log.debug("calTotalGuaranteeAmount start :: ");

        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            log.debug("calTotalGuaranteeAmount end :: (guarantorDetailViewList is null! or size == 0) return 0");
            sumTotalGuaranteeAmount = BigDecimal.ZERO;
        } else {
            for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
                sumTotalGuaranteeAmount = Util.add(sumTotalGuaranteeAmount, guarantorDetailView.getTotalLimitGuaranteeAmount());
            }
            log.debug("calTotalGuaranteeAmount end :: sumTotalGuaranteeAmount: {}", sumTotalGuaranteeAmount);
        }
        return sumTotalGuaranteeAmount;
    }

    public NewCreditFacilityView calculateTotalProposeAmount(NewCreditFacilityView newCreditFacilityView, BasicInfoView basicInfoView, TCGView tcgView, long workCaseId) {
        log.debug("start :: calculateTotalProposeAmount :::");
        if (newCreditFacilityView != null) {
            BigDecimal sumTotalOBOD = BigDecimal.ZERO;         // OBOD of Propose
            BigDecimal sumTotalPropose = BigDecimal.ZERO;      // All Propose
            BigDecimal sumTotalBorrowerCommercial = BigDecimal.ZERO;   // Without : OBOD  Propose and Existing
            BigDecimal sumTotalBorrowerCommercialAndOBOD = BigDecimal.ZERO;  //All Propose and Existing
            BigDecimal sumTotalGroupExposure = BigDecimal.ZERO;
            BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
            BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;
            BigDecimal borrowerComOBOD = BigDecimal.ZERO;
            BigDecimal borrowerCom = BigDecimal.ZERO;
            BigDecimal groupExposure = BigDecimal.ZERO;

            ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

            if (existingCreditFacilityView != null) {

                log.debug("existingCreditFacilityView.getTotalBorrowerCom() ::; {}", existingCreditFacilityView.getTotalBorrowerCom());
                log.debug("existingCreditFacilityView.getTotalBorrowerComOBOD() ::; {}", existingCreditFacilityView.getTotalBorrowerComOBOD());
                log.debug("existingCreditFacilityView.getTotalBorrowerExposure() ::; {}", existingCreditFacilityView.getTotalBorrowerExposure());

                borrowerComOBOD = existingCreditFacilityView.getTotalBorrowerComOBOD();
                borrowerCom = existingCreditFacilityView.getTotalBorrowerCom();
                groupExposure = existingCreditFacilityView.getTotalBorrowerExposure();
            }

            if (basicInfoView != null && basicInfoView.getSpecialProgram() != null && tcgView != null) {
                log.debug("basicInfoView.getSpecialProgram()::{}", basicInfoView.getSpecialProgram().getId());
                log.debug("tcgView ::; {}", tcgView.getId());

                List<NewCreditDetailView> newCreditDetailViewList = newCreditFacilityView.getNewCreditDetailViewList();

                if (newCreditDetailViewList != null && newCreditDetailViewList.size() != 0) {
                    ProductProgram productProgram;
                    CreditType creditType;
                    PrdProgramToCreditType prdProgramToCreditType;
                    ProductFormula productFormula;

                    for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
                        log.debug("newCreditDetailView.id: {}", newCreditDetailView.getId());

                        if (newCreditDetailView.getProductProgramView().getId() != 0 && newCreditDetailView.getCreditTypeView().getId() != 0) {
                            productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgramView().getId());
                            creditType = creditTypeDAO.findById(newCreditDetailView.getCreditTypeView().getId());

                            if (productProgram != null && creditType != null) {
                                prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);
                                productFormula = productFormulaDAO.findProductFormulaPropose(
                                        prdProgramToCreditType, newCreditFacilityView.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());
                                if (productFormula != null) {
                                    log.debug("productFormula id :: {}", productFormula.getId());
                                    log.debug("productFormula.getProgramToCreditType().getCreditType().getCreditGroup():::{}", productFormula.getProgramToCreditType().getCreditType().getCreditGroup());
                                    //OBOD or CASH_IN
                                    if (CreditTypeGroup.CASH_IN.equals(productFormula.getProgramToCreditType().getCreditType().getCreditGroup())) {
                                        //ExposureMethod for check to use limit or limit*PCE%
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            log.info("NOT_CALCULATE :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalOBOD = sumTotalOBOD.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            log.info("LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalOBOD = sumTotalOBOD.add(newCreditDetailView.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) { //limit * %PCE
                                            log.info("PCE_LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalOBOD = sumTotalOBOD.add(Util.multiply(newCreditDetailView.getLimit(), newCreditDetailView.getPCEPercent()));
                                        }
                                        log.debug("sumTotalOBOD :: {}", sumTotalOBOD);
                                    } else {//All Credit
                                        //ExposureMethod for check to use limit or limit*PCE%
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            log.info("NOT_CALCULATE :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            log.info("LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(newCreditDetailView.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //limit * %PCE
                                            log.info("PCE_LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetailView.getLimit(), newCreditDetailView.getPCEPercent()));
                                        }
                                        log.debug("sumTotalPropose :: {}", sumTotalPropose);  // Commercial + OBOD
                                    }

                                    sumTotalBorrowerCommercial = Util.subtract(sumTotalPropose, sumTotalOBOD);  // Commercial - OBOD
                                    log.debug("sumTotalCommercial :: {}", sumTotalBorrowerCommercial);

                                    //For DBR  sumTotalLoanDbr and sumTotalNonLoanDbr
                                    if (productFormula.getDbrCalculate() == 1) {// No
                                        log.info("DbrCalculate NO :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                        sumTotalNonLoanDbr = BigDecimal.ZERO;
                                    } else if (productFormula.getDbrCalculate() == 2) {// Yes
                                        log.info("DbrCalculate YES :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                        if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                            log.info("NOT_CALCULATE :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                            sumTotalLoanDbr = BigDecimal.ZERO;
                                        } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                            log.info("INSTALLMENT :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                            log.info("INSTALLMENT :: newCreditDetailView.getInstallment() :: {}", newCreditDetailView.getInstallment());
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(newCreditDetailView.getInstallment());
                                        } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                            log.info("INT_YEAR :: productFormula.getDbrMethod() :: {}, productFormula.getDbrSpread() :::{}", productFormula.getDbrMethod(), productFormula.getDbrSpread());
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(newCreditDetailView, productFormula.getDbrSpread()));
                                        }
                                    }
                                    log.debug("sumTotalLoanDbr :: {}", sumTotalLoanDbr);
                                }
                            }
                        }
                    }

                    sumTotalBorrowerCommercialAndOBOD = Util.add(borrowerComOBOD, sumTotalPropose); // Total Commercial&OBOD  ของ Borrower (จาก Existing credit) +Total Propose Credit
                    sumTotalBorrowerCommercial = Util.add(borrowerCom, sumTotalBorrowerCommercial); //Total Commercial  ของ Borrower (จาก Existing credit) + *Commercial ของ propose
                    sumTotalGroupExposure = Util.add(groupExposure, sumTotalBorrowerCommercialAndOBOD); //ได้มาจาก  Total Exposure ของ Group  (จาก Existing credit) +  Total Borrower Commercial&OBOD (Propose credit)

                    log.debug("sumTotalCommercial after include Existing:: {}", sumTotalBorrowerCommercial);
                    log.debug("sumTotalExposure :: {}", sumTotalGroupExposure);
                }

            }

            newCreditFacilityView.setTotalPropose(sumTotalPropose);                 //sumTotalPropose All Credit in this case
            newCreditFacilityView.setTotalProposeLoanDBR(sumTotalLoanDbr);          //sumTotalLoanDbr
            newCreditFacilityView.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr);    //sumTotalNonLoanDbr
            newCreditFacilityView.setTotalCommercial(sumTotalBorrowerCommercial);               //sum Commercial of Existing and Propose
            newCreditFacilityView.setTotalCommercialAndOBOD(sumTotalBorrowerCommercialAndOBOD); //sum Commercial and OBOD of Existing and Propose
            newCreditFacilityView.setTotalExposure(sumTotalGroupExposure);
        }

        return newCreditFacilityView;
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(NewCreditDetailView newCreditDetailView, BigDecimal dbrSpread) {
        log.info("calTotalProposeLoanDBRForIntYear start :: newCreditDetailView and  dbrSpread ::{}", newCreditDetailView, dbrSpread);

        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        if (newCreditDetailView != null &&
                newCreditDetailView.getNewCreditTierDetailViewList() != null &&
                newCreditDetailView.getNewCreditTierDetailViewList().size() > 0) {

            log.info("limit :: {}", newCreditDetailView.getLimit());
            log.info("newCreditTierDetailViews.size :: {}", newCreditDetailView.getNewCreditTierDetailViewList().size());

            BigDecimal oneHundred = new BigDecimal("100");
            BigDecimal twelve = new BigDecimal("12");
            BigDecimal sum = BigDecimal.ZERO;

            for (NewCreditTierDetailView newCreditTierDetailView : newCreditDetailView.getNewCreditTierDetailViewList()) //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
            {
                if (newCreditTierDetailView != null) {
                    log.info("newCreditTierDetail.getFinalBasePrice().getValue() :: {}", newCreditTierDetailView.getFinalBasePrice().getValue());
                    log.info("newCreditTierDetail.getFinalInterest() :: {}", newCreditTierDetailView.getFinalInterest());
                    log.info("dbrSpread :: {}", dbrSpread);
                    log.info("newCreditDetail.getLimit() :: {}", newCreditDetailView.getLimit());
                    sum = Util.divide(Util.multiply(Util.divide(Util.add(Util.add(newCreditTierDetailView.getFinalBasePrice().getValue(), newCreditTierDetailView.getFinalInterest()), dbrSpread), oneHundred), newCreditDetailView.getLimit()), twelve);
                    sumTotalLoanDbr = Util.add(sumTotalLoanDbr, sum);
                }
            }
        }
        log.info("calTotalProposeLoanDBRForIntYear end ::: sumTotalLoanDbr ::: {}", sumTotalLoanDbr);
        return sumTotalLoanDbr;
    }

    public NewCreditFacilityView calculateTotalForBRMS(NewCreditFacilityView newCreditFacilityView) {
        log.debug("calculateTotalForBRMS()");
        if (newCreditFacilityView != null) {
            // ***** Credit Detail ***** //
            BigDecimal totalNumOfNewOD = BigDecimal.ZERO;
            BigDecimal totalNumProposeCreditFac = BigDecimal.ZERO;
            BigDecimal totalNumContingentPropose = BigDecimal.ZERO;

            List<NewCreditDetailView> newCreditDetailViewList = newCreditFacilityView.getNewCreditDetailViewList();
            if (newCreditDetailViewList != null && newCreditDetailViewList.size() > 0) {
                for (NewCreditDetailView creditDetailView : newCreditDetailViewList) {
                    // Count All 'New' propose credit
                    totalNumProposeCreditFac = Util.add(totalNumProposeCreditFac, BigDecimal.ONE);

                    CreditTypeView creditTypeView = creditDetailView.getCreditTypeView();
                    if (creditTypeView != null) {
                        // Count propose credit which credit facility = 'OD'
                        if (CreditTypeGroup.OD.value() == creditTypeView.getCreditGroup()) {
                            totalNumOfNewOD = Util.add(totalNumOfNewOD, BigDecimal.ONE);
                        }
                        // Count the 'New' propose credit which has Contingent Flag 'Y'
                        if (creditTypeView.isContingentFlag()) {
                            totalNumContingentPropose = Util.add(totalNumContingentPropose, BigDecimal.ONE);
                        }
                    }
                }
            }

            // ***** Collateral ***** //
            BigDecimal totalNumOfCoreAsset = BigDecimal.ZERO;
            BigDecimal totalNumOfNonCoreAsset = BigDecimal.ZERO;
            BigDecimal totalMortgageValue = BigDecimal.ZERO;

            List<NewCollateralView> newCollateralViewList = newCreditFacilityView.getNewCollateralViewList();
            if (newCollateralViewList != null && newCollateralViewList.size() > 0) {
                for (NewCollateralView collateralView : newCollateralViewList) {
                    List<NewCollateralHeadView> collHeadViewList = collateralView.getNewCollateralHeadViewList();
                    if (collHeadViewList != null && collHeadViewList.size() > 0) {
                        for (NewCollateralHeadView collHeadView : collHeadViewList) {
                            PotentialCollateral potentialCollateral = collHeadView.getPotentialCollateral();
                            // Count core asset and none core asset
                            if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                totalNumOfCoreAsset = Util.add(totalNumOfCoreAsset, BigDecimal.ONE);
                            } else if (PotentialCollateralValue.NONE_CORE_ASSET.id() == potentialCollateral.getId()) {
                                totalNumOfNonCoreAsset = Util.add(totalNumOfNonCoreAsset, BigDecimal.ONE);
                            }

                            List<NewCollateralSubView> collSubViewList = collHeadView.getNewCollateralSubViewList();
                            if (collSubViewList != null && collSubViewList.size() > 0) {
                                for (NewCollateralSubView collSubView : collSubViewList) {
                                    totalMortgageValue = Util.add(totalMortgageValue, collSubView.getMortgageValue());
                                }
                            }
                        }
                    }
                }
            }

            // ***** Guarantor Detail ***** //
            BigDecimal totalTCGGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalIndiGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalJuriGuaranteeAmt = BigDecimal.ZERO;

            List<NewGuarantorDetailView> newGuarantorDetailViewList = newCreditFacilityView.getNewGuarantorDetailViewList();
            if (newGuarantorDetailViewList != null && newGuarantorDetailViewList.size() > 0) {
                for (NewGuarantorDetailView guarantorDetailView : newGuarantorDetailViewList) {
                    CustomerInfoView customerInfoView = guarantorDetailView.getGuarantorName();
                    if (customerInfoView != null) {
                        CustomerEntity customerEntity = customerInfoView.getCustomerEntity();
                        if (customerEntity != null) {
                            if (GuarantorCategory.INDIVIDUAL.value() == customerEntity.getId()) {
                                totalIndiGuaranteeAmt = Util.add(totalIndiGuaranteeAmt, guarantorDetailView.getTotalLimitGuaranteeAmount());
                            } else if (GuarantorCategory.JURISTIC.value() == customerEntity.getId()) {
                                totalJuriGuaranteeAmt = Util.add(totalJuriGuaranteeAmt, guarantorDetailView.getTotalLimitGuaranteeAmount());
                            } else if (GuarantorCategory.TCG.value() == customerEntity.getId()) {
                                totalTCGGuaranteeAmt = Util.add(totalTCGGuaranteeAmt, guarantorDetailView.getTotalLimitGuaranteeAmount());
                            }
                        }
                    }
                }
            }

            // set to credit facility propose
            newCreditFacilityView.setTotalNumberOfNewOD(totalNumOfNewOD);
            newCreditFacilityView.setTotalNumberProposeCreditFac(totalNumProposeCreditFac);
            newCreditFacilityView.setTotalNumberContingenPropose(totalNumContingentPropose);
            newCreditFacilityView.setTotalNumberOfCoreAsset(totalNumOfCoreAsset);
            newCreditFacilityView.setTotalNumberOfNonCoreAsset(totalNumOfNonCoreAsset);
            newCreditFacilityView.setTotalMortgageValue(totalMortgageValue);
            newCreditFacilityView.setTotalTCGGuaranteeAmount(totalTCGGuaranteeAmt);
            newCreditFacilityView.setTotalIndvGuaranteeAmount(totalIndiGuaranteeAmt);
            newCreditFacilityView.setTotalJurisGuaranteeAmount(totalJuriGuaranteeAmt);

        }
        return newCreditFacilityView;
    }


    public void calculatePCEAmount(NewCreditDetailView creditDetailView) {
        log.info("creditDetailView : {}", creditDetailView);
        BigDecimal sumOfPCE = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;

        if (!Util.isNull(creditDetailView)) {
            sumOfPCE = Util.multiply(creditDetailView.getLimit(), creditDetailView.getPCEPercent());
            sum = Util.divide(sumOfPCE,BigDecimal.valueOf(100));

            if (sum != null) {
                sum.setScale(2, RoundingMode.HALF_UP);
            }

            log.info("creditDetailAdd :sum: {}", sum);
            creditDetailView.setPCEAmount(sum);
        }

    }

    public void calculateInstallment(NewCreditDetailView creditDetailView) {
        log.info("creditDetailView : {}", creditDetailView);
        BigDecimal sumOfInstallment = BigDecimal.ZERO;
        if (creditDetailView != null && creditDetailView.getNewCreditTierDetailViewList() != null && creditDetailView.getNewCreditTierDetailViewList().size() > 0) {

            for (NewCreditTierDetailView newCreditTierDetailView : creditDetailView.getNewCreditTierDetailViewList()) {
                // Installment = (อัตราดอกเบี้ยต่อเดือน * Limit * (1 + อัตราดอกเบี้ยต่อเดือน)ยกกำลัง tenors(month)) / ((1 + อัตราดอกเบี้ยต่อเดือน) ยกกำลัง tenors(month) - 1)
                // อัตราดอกเบี้ยต่อเดือน = baseRate.value +  interest + 1% / 12
                BigDecimal twelve = new BigDecimal(12);
                BigDecimal baseRate = BigDecimal.ZERO;
                BigDecimal interest = BigDecimal.ZERO;

                if (newCreditTierDetailView.getFinalBasePrice() != null) {
                    baseRate = newCreditTierDetailView.getFinalBasePrice().getValue();
                }
                if (newCreditTierDetailView.getFinalInterest() != null) {
                    interest = newCreditTierDetailView.getFinalInterest();
                }

                BigDecimal interestPerMonth = Util.divide(Util.add(baseRate, Util.add(interest, BigDecimal.ONE)), twelve);
                log.info("baseRate :: {}", baseRate);
                log.info("interest :: {}", interest);
                log.info("interestPerMonth :: {}", interestPerMonth);

                BigDecimal limit = BigDecimal.ZERO;
                int tenor = newCreditTierDetailView.getTenor();
                BigDecimal installment = BigDecimal.ZERO;

                if (creditDetailView.getLimit() != null) {
                    limit = creditDetailView.getLimit();
                }

                log.info("limit :: {}", limit);
                log.info("tenor :: {}", tenor);

                installment = Util.divide(Util.multiply(Util.multiply(interestPerMonth, limit), (Util.add(BigDecimal.ONE, interestPerMonth)).pow(tenor)),
                        Util.subtract(Util.add(BigDecimal.ONE, interestPerMonth).pow(tenor), BigDecimal.ONE));
                log.info("installment : {}", installment);

                if (installment != null) {
                    installment.setScale(2, RoundingMode.HALF_UP);
                }

                newCreditTierDetailView.setInstallment(installment);
                sumOfInstallment = Util.add(sumOfInstallment, installment);
                log.info("creditDetailAdd :sumOfInstallment: {}", sumOfInstallment);
                creditDetailView.setInstallment(sumOfInstallment);
            }
        }
    }

    public List<ProposeCreditDetailView> findAndGenerateSeqProposeCredits(List<NewCreditDetailView> newCreditDetailViewList, List<ExistingCreditDetailView> borrowerExistingCreditDetailViewList, long workCaseId) {
        log.debug("findAndGenerateSeqProposeCredits() workCaseId: {}", workCaseId);
        // Generate Sequence Number [1 - N] from "Propose Credit" and "Existing Credit" for the first time
        int sequenceNumber = 1;
        log.debug("Start sequence number = {}", sequenceNumber);
        List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();

        if (newCreditDetailViewList != null && newCreditDetailViewList.size() > 0) {
            ProposeCreditDetailView proposeCreditFromNew;
            for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
                // set seq to NewCreditDetail
                newCreditDetailView.setSeq(sequenceNumber);
                // create and set seq to new ProposeCredit
                proposeCreditFromNew = proposeCreditDetailTransform.convertNewCreditToProposeCredit(newCreditDetailView, sequenceNumber);
                proposeCreditDetailViewList.add(proposeCreditFromNew);
                sequenceNumber++;
            }
        }
        log.debug("End of 'NewCreditDetailList' sequence number = {}", sequenceNumber);

        List<ExistingCreditDetailView> _existingCreditDetailViewList;
        if (borrowerExistingCreditDetailViewList != null && borrowerExistingCreditDetailViewList.size() > 0) {
            _existingCreditDetailViewList = borrowerExistingCreditDetailViewList;
        } else {
            // find Borrower Existing Credit
            _existingCreditDetailViewList = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
        }

        if (_existingCreditDetailViewList != null && _existingCreditDetailViewList.size() > 0) {
            ProposeCreditDetailView proposeCreditFromExisting;
            for (ExistingCreditDetailView existingCreditDetailView : _existingCreditDetailViewList) {
                existingCreditDetailView.setSeq(sequenceNumber);
                proposeCreditFromExisting = proposeCreditDetailTransform.convertExistingCreditToProposeCredit(existingCreditDetailView, sequenceNumber);
                proposeCreditDetailViewList.add(proposeCreditFromExisting);
                sequenceNumber++;
            }
        }
        log.debug("End of 'ExistingCreditDetailList' sequence number = {}", sequenceNumber);
        return proposeCreditDetailViewList;
    }

    public int getLastSeqNumberFromProposeCredit(List<ProposeCreditDetailView> proposeCreditDetailViewList) {
        int lastSeqNumber = 1;
        if (proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0) {
            int size = proposeCreditDetailViewList.size();
            for (int i = 0; i < size; i++) {
                ProposeCreditDetailView proposeCreditDetailView = proposeCreditDetailViewList.get(i);
                if (proposeCreditDetailView.getSeq() > lastSeqNumber) {
                    lastSeqNumber = proposeCreditDetailView.getSeq();
                }
            }
        }
        return lastSeqNumber;
    }

    public void groupTypeOfStepAndOrderBySeq(List<ProposeCreditDetailView> proposeCreditDetailViewList) {
        log.debug("groupTypeOfStepAndOrderBySeq()");
        if (proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0) {
            log.debug("Start Grouping by Type of Step (N -> E) and Sort seq (ASC)");

            Comparator<ProposeCreditDetailView> comparator = new Comparator<ProposeCreditDetailView>() {
                @Override
                public int compare(ProposeCreditDetailView o1, ProposeCreditDetailView o2) {
                    int flag = o2.getTypeOfStep().compareTo(o1.getTypeOfStep());
                    if (flag == 0)
                        flag = o1.getSeq() - o2.getSeq();
                    return flag;
                }
            };

            Collections.sort(proposeCreditDetailViewList, comparator);

            log.debug("Result : ", proposeCreditDetailViewList);
        }
    }

    public List<NewCollateralSubView> findNewCollateralSubView(List<NewCollateralView> newCollateralViewList) {
        List<NewCollateralSubView> relatedWithAllList = new ArrayList<NewCollateralSubView>();
        int countNo = 1;
        for (NewCollateralView newCollateralView : Util.safetyList(newCollateralViewList)) {
            for (NewCollateralHeadView newCollateralHeadDetail : newCollateralView.getNewCollateralHeadViewList()) {
                if (newCollateralHeadDetail.getNewCollateralSubViewList().size() > 0) {
                    log.debug("newCollateralHeadDetail . getId:: {}", newCollateralHeadDetail.getId());
                    for (NewCollateralSubView newCollateralSubView : newCollateralHeadDetail.getNewCollateralSubViewList()) {
                        newCollateralSubView.setNo(countNo);
                        relatedWithAllList.add(newCollateralSubView);
                        log.debug("relatedWithAllList > size :: {}", relatedWithAllList.size());
                        countNo++;
                    }
                }
            }
        }

        return relatedWithAllList;
    }

    public void calWC(long workCaseId) { // todo: ncb && dbr && bizInfoSummary pls call me !!!!!!!!
        log.debug("calWC ::: workCaseId : {}", workCaseId);
        BigDecimal dayOfYear = BigDecimal.valueOf(365);
        BigDecimal monthOfYear = BigDecimal.valueOf(12);
        BigDecimal onePointTwoFive = BigDecimal.valueOf(1.25);
        BigDecimal onePointFive = BigDecimal.valueOf(1.50);
        BigDecimal thirtyFive = BigDecimal.valueOf(35);
        BigDecimal fifty = BigDecimal.valueOf(50);
        BigDecimal oneHundred = BigDecimal.valueOf(100);

        // ยอดขาย/รายได้
        BigDecimal adjustDBR = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนจากหน้า NCB
        BigDecimal revolvingCreditNCB = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนส่วนผู้เกี่ยวข้องในหน้า DBR
        BigDecimal revolvingCreditDBR = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes
        BigDecimal loanBurdenWCFlag = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB
        BigDecimal revolvingCreditNCBTMBFlag = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        BigDecimal loanBurdenTMBFlag = BigDecimal.ZERO;

        //how to check role and get ap ar inv !?
        BigDecimal weightAP = BigDecimal.ZERO;
        BigDecimal weightAR = BigDecimal.ZERO;
        BigDecimal weightINV = BigDecimal.ZERO;
        // Sum(weight cost of goods sold * businessProportion)
        // cost of goods = business desc ( column COG )
        // business proportion = สัดส่วนธุรกิจ ในแต่ละ business < %Income >
        BigDecimal aaaValue = BigDecimal.ZERO;

        //table 1
        BigDecimal wcNeed = BigDecimal.ZERO;
        BigDecimal totalWcDebit = BigDecimal.ZERO;
        BigDecimal totalWcTmb = BigDecimal.ZERO;
        BigDecimal wcNeedDiffer = BigDecimal.ZERO;

        //table 2
        BigDecimal case1WcLimit = BigDecimal.ZERO;
        BigDecimal case1WcMinLimit = BigDecimal.ZERO;
        BigDecimal case1Wc50CoreWc = BigDecimal.ZERO;
        BigDecimal case1WcDebitCoreWc = BigDecimal.ZERO;

        //table 3
        BigDecimal case2WcLimit = BigDecimal.ZERO;
        BigDecimal case2WcMinLimit = BigDecimal.ZERO;
        BigDecimal case2Wc50CoreWc = BigDecimal.ZERO;
        BigDecimal case2WcDebitCoreWc = BigDecimal.ZERO;

        //table 4
        BigDecimal case3WcLimit = BigDecimal.ZERO;
        BigDecimal case3WcMinLimit = BigDecimal.ZERO;
        BigDecimal case3Wc50CoreWc = BigDecimal.ZERO;
        BigDecimal case3WcDebitCoreWc = BigDecimal.ZERO;

        ////////////////////////////////////////////////////

        DBRView dbrView = dbrControl.getDBRByWorkCase(workCaseId);
        if (dbrView != null) {
            log.debug("getDBRByWorkCase :: dbrView : {}", dbrView);
            adjustDBR = dbrView.getMonthlyIncomeAdjust();
            revolvingCreditDBR = dbrView.getTotalMonthDebtRelatedWc();
        }

        List<NCB> ncbList = ncbInfoControl.getNCBByWorkCaseId(workCaseId);
        if (ncbList != null && ncbList.size() > 0) {
            log.debug("getNCBByWorkCaseId :: ncbList : {}", ncbList);
            for (NCB ncb : ncbList) {
                log.debug("getNCBByWorkCaseId :: ncb : {}", ncb);
                revolvingCreditNCB = Util.add(revolvingCreditNCB, ncb.getLoanCreditNCB());
                loanBurdenWCFlag = Util.add(loanBurdenWCFlag, ncb.getLoanCreditWC());
                revolvingCreditNCBTMBFlag = Util.add(revolvingCreditNCBTMBFlag, ncb.getLoanCreditTMB());
                loanBurdenTMBFlag = Util.add(loanBurdenTMBFlag, ncb.getLoanCreditWCTMB());
            }
        }


        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        if (bizInfoSummaryView != null) {
            log.debug("onGetBizInfoSummaryByWorkCase :: bizInfoSummaryView : {}", bizInfoSummaryView);
            weightAR = bizInfoSummaryView.getSumWeightAR();
            weightAP = bizInfoSummaryView.getSumWeightAP();
            weightINV = bizInfoSummaryView.getSumWeightINV();
            // Sum(weight cost of goods sold * businessProportion)
            List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
            if (bizInfoSummaryView.getId() != 0) {
                bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryView.getId());
                if (bizInfoDetailViewList != null && bizInfoDetailViewList.size() > 0) {
                    log.debug("bizInfoDetailViewList : {}", bizInfoDetailViewList);
                    for (BizInfoDetailView bidv : bizInfoDetailViewList) {
                        BigDecimal cog = BigDecimal.ZERO;
                        if (bidv.getBizDesc() != null) {
                            cog = bidv.getBizDesc().getCog();
                        }
                        aaaValue = Util.add(aaaValue, Util.divide(Util.multiply(cog, bidv.getPercentBiz()), oneHundred));
                    }
                }
            }
        }

//        *** ยอดขาย/รายได้  = รายได้ต่อเดือน (adjusted) [DBR] * 12
        BigDecimal salesIncome = Util.multiply(adjustDBR, monthOfYear);
        //calculation
//        (ยอดขาย/รายได้ หาร 365 คูณ Weighted AR) + (AAAValue หาร 365 คูณ Weighted INV) - ((AAAValue หาร 365 คูณ Weighted AP)
        wcNeed = Util.subtract((Util.add(Util.multiply(Util.divide(salesIncome, dayOfYear), weightAR), Util.multiply(Util.divide(aaaValue, dayOfYear), weightINV))), (Util.multiply(Util.divide(aaaValue, dayOfYear), weightAP)));
//        Sum (วงเงินสินเชื่อหมุนเวียนจากหน้า NCB และ ส่วนผู้เกี่ยวข้องในหน้า DBR + ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes )
        totalWcDebit = Util.add(Util.add(revolvingCreditNCB, revolvingCreditDBR), loanBurdenWCFlag);
//        วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB + ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        totalWcTmb = Util.add(revolvingCreditNCBTMBFlag, loanBurdenTMBFlag);
//        wcNeed - totalWcDebit
        wcNeedDiffer = Util.subtract(wcNeed, totalWcDebit);

        log.debug("Value ::: wcNeed : {}, totalWcDebit : {}, totalWcTmb : {}, wcNeedDiffer : {}", wcNeed, totalWcDebit, totalWcTmb, wcNeedDiffer);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        1.25 x wcNeed
        case1WcLimit = Util.multiply(wcNeed, onePointTwoFive);
//        case1WcLimit - totalWcDebit
        case1WcMinLimit = Util.subtract(case1WcLimit, totalWcDebit);
//        ไม่เกิน 50% ของ case1WcLimit และไม่เกิน case1WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case1Wc50CoreWc = Util.compareToFindLower(Util.subtract(case1WcLimit, fifty), case1WcMinLimit);
//        case1WcMinLimit - case1Wc50CoreWc
        case1WcDebitCoreWc = Util.subtract(case1WcMinLimit, case1Wc50CoreWc);

        log.debug("Value ::: case1WcLimit : {}, case1WcMinLimit : {}, case1Wc50CoreWc : {}, case1WcDebitCoreWc : {}", case1WcLimit, case1WcMinLimit, case1Wc50CoreWc, case1WcDebitCoreWc);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        1.5 x wcNeed
        case2WcLimit = Util.multiply(wcNeed, onePointFive);
//        case2WcLimit - totalWcDebit
        case2WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
//        ไม่เกิน 50% ของ case2WcLimit และไม่เกิน case2WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case2Wc50CoreWc = Util.compareToFindLower(Util.subtract(case2WcLimit, fifty), case2WcMinLimit);
//        case2WcMinLimit - case2Wc50CoreWc
        case2WcDebitCoreWc = Util.subtract(case2WcMinLimit, case2Wc50CoreWc);

        log.debug("Value ::: case2WcLimit : {}, case2WcMinLimit : {}, case2Wc50CoreWc : {}, case2WcDebitCoreWc : {}", case2WcLimit, case2WcMinLimit, case2Wc50CoreWc, case2WcDebitCoreWc);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        ยอดขาย/รายได้ หาร 12 คูณ 35%
        case3WcLimit = Util.divide(Util.multiply(Util.divide(salesIncome, dayOfYear), thirtyFive), oneHundred);
//        case3WcLimit - totalWcDebit
        case3WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
//        ไม่เกิน 50% ของ case3WcLimit และไม่เกิน case3WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case3Wc50CoreWc = Util.compareToFindLower(Util.subtract(case3WcLimit, fifty), case3WcMinLimit);
//        case3WcMinLimit - case3Wc50CoreWc
        case3WcDebitCoreWc = Util.subtract(case3WcMinLimit, case3Wc50CoreWc);

        log.debug("Value ::: case3WcLimit : {}, case3WcMinLimit : {}, case3Wc50CoreWc : {}, case3WcDebitCoreWc : {}", case3WcLimit, case3WcMinLimit, case3Wc50CoreWc, case3WcDebitCoreWc);

        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        if (!Util.isNull(newCreditFacility)) {
            log.debug("find new creditFacility id is ::: {}", newCreditFacility.getId());
            if ((!Util.isNull(newCreditFacility)) && (newCreditFacility.getId() != 0)) {
                newCreditFacility.setWcNeed(wcNeed);
                newCreditFacility.setTotalWcDebit(totalWcDebit);
                newCreditFacility.setTotalWcTmb(totalWcTmb);
                newCreditFacility.setWCNeedDiffer(wcNeedDiffer);
                newCreditFacility.setCase1WcLimit(case1WcLimit);
                newCreditFacility.setCase1WcMinLimit(case1WcMinLimit);
                newCreditFacility.setCase1Wc50CoreWc(case1Wc50CoreWc);
                newCreditFacility.setCase1WcDebitCoreWc(case1WcDebitCoreWc);
                newCreditFacility.setCase2WcLimit(case2WcLimit);
                newCreditFacility.setCase2WcMinLimit(case2WcMinLimit);
                newCreditFacility.setCase2Wc50CoreWc(case2Wc50CoreWc);
                newCreditFacility.setCase2WcDebitCoreWc(case2WcDebitCoreWc);
                newCreditFacility.setCase3WcLimit(case3WcLimit);
                newCreditFacility.setCase3WcMinLimit(case3WcMinLimit);
                newCreditFacility.setCase3Wc50CoreWc(case3Wc50CoreWc);
                newCreditFacility.setCase3WcDebitCoreWc(case3WcDebitCoreWc);
                newCreditFacilityDAO.persist(newCreditFacility);
            }
        }

    }


    public NewCreditFacilityView saveCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId,List<PricingFee> pricingFeeList) {
        log.debug("Starting saveCreditFacility...");
        log.debug("saveCreditFacility ::: workCaseId : {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User currentUser = getCurrentUser();

        log.debug("saveCreditFacility ::: newCreditFacilityView : {}", newCreditFacilityView);
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.persist(newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, currentUser));
        log.debug("saveCreditFacility ::: persist newCreditFacility : {}", newCreditFacility);

        //--- Save to NewFeeCredit
        if ((!Util.isNull(pricingFeeList)) && (Util.safetyList(pricingFeeList).size() > 0)) {
            log.debug("saveCreditFacility ::: pricingFeeList : {}", pricingFeeList.size());
//            List<FeeDetail> feeDetailList = feeTransform.transformToDB(pricingFeeList,workCaseId);
//            feeDetailDAO.persist(feeDetailList);
//            log.debug("persist :: feeDetailList ::");

        }

        //--- Save to NewConditionCredit
        if (Util.safetyList(newCreditFacilityView.getNewConditionDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newConditionDetailViewList : {}", newCreditFacilityView.getNewConditionDetailViewList());
            List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, currentUser);
            log.debug("saveCreditFacility ::: before persist newConditionDetailList : {}", newConditionDetailList);
            newCreditFacility.setNewConditionDetailList(newConditionDetailList);
            newConditionDetailDAO.persist(newConditionDetailList);
        }

        //--- Save to NewCreditDetail
        if (Util.safetyList(newCreditFacilityView.getNewCreditDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newCreditDetailViewList : {}", newCreditFacilityView.getNewCreditDetailViewList());

//            List<NewCreditDetail> tmpNewCreditList = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);
//            for (NewCreditDetail newCreditDetail : tmpNewCreditList) {
//                if (newCreditDetail.getProposeCreditTierDetailList()!= null) {
//                    newCreditTierDetailDAO.delete(newCreditDetail.getProposeCreditTierDetailList());
//                }
//                newCreditDetail.setProposeCreditTierDetailList(Collections.<NewCreditTierDetail>emptyList());
//            }

            List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(newCreditFacilityView.getNewCreditDetailViewList(), newCreditFacility, currentUser, workCase, ProposeType.P);
            newCreditFacility.setNewCreditDetailList(newCreditDetailList);
            newCreditDetailDAO.persist(newCreditDetailList);
        }

        //--- Save to NewGuarantor
        if (Util.safetyList(newCreditFacilityView.getNewGuarantorDetailViewList()).size() > 0) {

            List<NewGuarantorDetail> tmpNewGuarantorList = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
            for (NewGuarantorDetail newGuarantorDetail : tmpNewGuarantorList) {
                if (newGuarantorDetail.getNewGuarantorCreditList() != null) {
                    newGuarantorRelationDAO.delete(newGuarantorDetail.getNewGuarantorCreditList());
                }
                newGuarantorDetail.setNewGuarantorCreditList(Collections.<NewGuarantorCredit>emptyList());
                newGuarantorDetailDAO.persist(newGuarantorDetail);
            }

            log.debug("saveCreditFacility ::: newGuarantorDetailViewList : {}", newCreditFacilityView.getNewGuarantorDetailViewList());
            List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, currentUser, ProposeType.P);
            newCreditFacility.setNewGuarantorDetailList(newGuarantorDetailList);
            newGuarantorDetailDAO.persist(newGuarantorDetailList);

        }

        if (Util.safetyList(newCreditFacilityView.getNewCollateralViewList()).size() > 0) {

            List<NewCollateral> tmpNewCollateralList = newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            for (NewCollateral newCollateral : tmpNewCollateralList) {
                if (newCollateral.getNewCollateralCreditList() != null) {
                    newCollateralRelationDAO.delete(newCollateral.getNewCollateralCreditList());
                    newCollateral.setNewCollateralCreditList(Collections.<NewCollateralCredit>emptyList());
                }

                List<NewCollateralHead> newCollateralHeadList = newCollateral.getNewCollateralHeadList();
                for (NewCollateralHead newCollateralHead : newCollateralHeadList) {
                    List<NewCollateralSub> newCollateralSubList = newCollateralHead.getNewCollateralSubList();
                    for (NewCollateralSub newCollateralSub : newCollateralSubList) {
                        newSubCollMortgageDAO.delete(newCollateralSub.getNewCollateralSubMortgageList());
                        newCollateralSubOwnerDAO.delete(newCollateralSub.getNewCollateralSubOwnerList());
                        newCollateralSub.setNewCollateralSubMortgageList(Collections.<NewCollateralSubMortgage>emptyList());
                        newCollateralSub.setNewCollateralSubOwnerList(Collections.<NewCollateralSubOwner>emptyList());
                    }
                }
                newCollateralDAO.persist(newCollateral);
            }


            if (Util.safetyList(newCreditFacilityView.getNewCollateralViewDelList()).size() > 0) {
                log.debug("newCreditFacilityView.getNewCollateralViewDelList() :: {}", newCreditFacilityView.getNewCollateralViewDelList().size());
                List<NewCollateral> deleteList = newCollateralTransform.transformsCollateralToModel(newCreditFacilityView.getNewCollateralViewDelList(), newCreditFacility, currentUser, workCase, ProposeType.P);
                newCollateralDetailDAO.delete(deleteList);
            }

            log.debug("saveCreditFacility ::: newCollateralViewList : {}", newCreditFacilityView.getNewCollateralViewList());
            List<NewCollateral> newCollateralList = newCollateralTransform.transformsCollateralToModel(newCreditFacilityView.getNewCollateralViewList(), newCreditFacility, currentUser, workCase, ProposeType.P);
            newCreditFacility.setNewCollateralDetailList(newCollateralList);
            newCollateralDetailDAO.persist(newCollateralList);
            log.debug("saveCreditFacility ::: persist newCollateralList : {}", newCollateralList);
        }

        return newCreditFacilityTransform.transformToView(newCreditFacility);
    }


    public void deleteAllNewCreditFacilityByIdList(List<Long> deleteCreditIdList, List<Long> deleteCollIdList, List<Long> deleteGuarantorIdList, List<Long> deleteConditionIdList) {
        log.info("deleteAllApproveByIdList()");
        log.info("deleteCreditIdList: {}", deleteCreditIdList);
        log.info("deleteCollIdList: {}", deleteCollIdList);
        log.info("deleteGuarantorIdList: {}", deleteGuarantorIdList);
        log.info("deleteConditionIdList: {}", deleteConditionIdList);

        if (deleteCreditIdList != null && deleteCreditIdList.size() > 0) {
            List<NewCreditDetail> deleteCreditDetailList = new ArrayList<NewCreditDetail>();
            for (Long id : deleteCreditIdList) {
                deleteCreditDetailList.add(newCreditDetailDAO.findById(id));
            }
            newCreditDetailDAO.delete(deleteCreditDetailList);
        }

        if (deleteCollIdList != null && deleteCollIdList.size() > 0) {
            List<NewCollateral> deleteCollateralList = new ArrayList<NewCollateral>();
            for (Long id : deleteCollIdList) {
                deleteCollateralList.add(newCollateralDAO.findById(id));
            }
            newCollateralDAO.delete(deleteCollateralList);
        }

        if (deleteGuarantorIdList != null && deleteGuarantorIdList.size() > 0) {
            List<NewGuarantorDetail> deleteGuarantorList = new ArrayList<NewGuarantorDetail>();
            for (Long id : deleteGuarantorIdList) {
                deleteGuarantorList.add(newGuarantorDetailDAO.findGuarantorById(id, ProposeType.P));
            }
            newGuarantorDetailDAO.delete(deleteGuarantorList);
        }

        if (deleteConditionIdList != null && deleteConditionIdList.size() > 0) {
            List<NewConditionDetail> deleteConditionList = new ArrayList<NewConditionDetail>();
            for (Long id : deleteConditionIdList) {
                deleteConditionList.add(newConditionDetailDAO.findById(id));
            }
            newConditionDetailDAO.delete(deleteConditionList);
        }
    }


}