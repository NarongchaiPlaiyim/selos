package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.BRMSInterfaceException;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    NewCreditTierDetailDAO newCreditTierDetailDAO;
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
    public CreditFacProposeControl() {
    }

    public NewCreditFacilityView findNewCreditFacilityByWorkCase(Long workCaseId) {
        NewCreditFacilityView newCreditFacilityView = null;
        log.debug("findNewCreditFacilityByWorkCase start ::::");
        try {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if (workCase != null) {
                NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
                log.debug("find new creditFacility: {}", newCreditFacility);
                if (newCreditFacility != null) {
                    newCreditFacilityView = newCreditFacilityTransform.transformToView(newCreditFacility);

//                    List<NewFeeDetail> newFeeDetailList = newFeeCreditDAO.findByNewCreditFacility(newCreditFacility);
//                    if (newFeeDetailList.size() > 0) {
//                        log.debug("newCreditFacility.getNewFeeDetailList() :: {}", newCreditFacility.getNewFeeDetailList());
//                        List<NewFeeDetailView> newFeeDetailViewList = newFeeDetailTransform.transformToView(newFeeDetailList);
//                        log.debug("newFeeDetailViewList : {}", newFeeDetailViewList);
//                        newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
//                    }

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

//                    error when saved and find data from table by newCreditFacility
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
            }
        } catch (Exception ex) {
            log.error("exception while load data in page {}", ex);
        } finally {
            log.debug("findNewCreditFacilityByWorkCase end");
        }

        return newCreditFacilityView;
    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        log.debug("calTotalGuaranteeAmount start :: ");
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            log.debug("calTotalGuaranteeAmount end :: (guarantorDetailViewList is null! or size == 0) return 0");
            return BigDecimal.ZERO;
        }

        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
            sumTotalGuaranteeAmount = Util.add(sumTotalGuaranteeAmount, guarantorDetailView.getTotalLimitGuaranteeAmount());
        }
        log.debug("calTotalGuaranteeAmount end :: sumTotalGuaranteeAmount: {}", sumTotalGuaranteeAmount);
        return sumTotalGuaranteeAmount;
    }

    public void calculateTotalProposeAmount(NewCreditFacilityView newCreditFacilityView, BasicInfoView basicInfoView, TCGView tcgView, long workCaseId) {
        log.debug("calculateTotalProposeAmount()");
        if (newCreditFacilityView != null) {

            BigDecimal sumTotalPropose = BigDecimal.ZERO;
            BigDecimal sumTotalCommercial = BigDecimal.ZERO;
            BigDecimal sumTotalCommercialAndOBOD = BigDecimal.ZERO; // wait confirm
            BigDecimal sumTotalExposure = BigDecimal.ZERO;
            BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
            BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;

            if (basicInfoView != null && basicInfoView.getSpecialProgram() != null && tcgView != null) {
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
                                    //ExposureMethod
                                    if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                        log.debug("NOT_CALCULATE :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                        sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                    }
                                    else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                        log.debug("LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                        sumTotalPropose = sumTotalPropose.add(newCreditDetailView.getLimit());
                                    }
                                    else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //limit * %PCE
                                        log.debug("PCE_LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                        sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetailView.getLimit(), newCreditDetailView.getPCEPercent()));
                                    }
                                    log.debug("sumTotalPropose :: {}", sumTotalPropose);

                                    //For DBR
                                    if (productFormula.getDbrCalculate() == 1) {// No
                                        log.info("NO calculate :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                        sumTotalNonLoanDbr = BigDecimal.ZERO;
                                    }
                                    else if (productFormula.getDbrCalculate() == 2) {// Yes
                                        log.debug("YES :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                        if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                            log.debug("NOT_CALCULATE :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                        }
                                        else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                            log.debug("INSTALLMENT :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(newCreditDetailView.getInstallment());
                                        }
                                        else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                            log.debug("INT_YEAR :: productFormula.getDbrMethod() :: {}, productFormula.getDbrSpread() :::{}", productFormula.getDbrMethod(), productFormula.getDbrSpread());
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(newCreditDetailView, productFormula.getDbrSpread()));
                                        }
                                    }
                                    log.debug("sumTotalLoanDbr :: {}", sumTotalLoanDbr);
                                }
                            }
                        }
                    }

                    newCreditFacilityView.setTotalPropose(sumTotalPropose); //sumTotalPropose
                    newCreditFacilityView.setTotalProposeLoanDBR(sumTotalLoanDbr); //sumTotalLoanDbr
                    newCreditFacilityView.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr); //sumTotalNonLoanDbr

                    ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
                    if (existingCreditFacilityView != null) {
                        log.info("existingCreditFacilityView.getTotalBorrowerComLimit() ::; {}", existingCreditFacilityView.getTotalBorrowerComLimit());
                        log.info("existingCreditFacilityView.getTotalBorrowerRetailLimit() ::; {}", existingCreditFacilityView.getTotalBorrowerRetailLimit());

                        sumTotalCommercial = Util.add(sumTotalCommercial, (Util.add(existingCreditFacilityView.getTotalBorrowerComLimit(), sumTotalPropose)));
                        sumTotalExposure = Util.add(sumTotalExposure, Util.add(existingCreditFacilityView.getTotalBorrowerComLimit(), (Util.add(sumTotalPropose, existingCreditFacilityView.getTotalBorrowerRetailLimit()))));
                    }
                    log.debug("sumTotalCommercial :: {}", sumTotalCommercial);
                    log.debug("sumTotalExposure :: {}", sumTotalExposure);

                    newCreditFacilityView.setTotalCommercial(sumTotalCommercial);
                    newCreditFacilityView.setTotalExposure(sumTotalExposure);
                }
            }

        }
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(NewCreditDetailView newCreditDetailView, BigDecimal dbrSpread) {
        log.debug("calTotalProposeLoanDBRForIntYear start :: newCreditDetailView and  dbrSpread ::{}", newCreditDetailView, dbrSpread);

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

    public void calculateTotalForBRMS(NewCreditFacilityView newCreditFacilityView) {
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
                            }
                            else if (PotentialCollateralValue.NONE_CORE_ASSET.id() == potentialCollateral.getId()) {
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
                            }
                            else if (GuarantorCategory.JURISTIC.value() == customerEntity.getId()) {
                                totalJuriGuaranteeAmt = Util.add(totalJuriGuaranteeAmt, guarantorDetailView.getTotalLimitGuaranteeAmount());
                            }
                            else if (GuarantorCategory.TCG.value() == customerEntity.getId()) {
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
                BigDecimal installment;

                if (creditDetailView.getLimit() != null) {
                    limit = creditDetailView.getLimit();
                }

                log.info("limit :: {}", limit);
                log.info("tenor :: {}", tenor);

                installment = Util.divide(Util.multiply(Util.multiply(interestPerMonth, limit), (Util.add(BigDecimal.ONE, interestPerMonth)).pow(tenor)),
                        Util.subtract(Util.add(BigDecimal.ONE, interestPerMonth).pow(tenor), BigDecimal.ONE));
                log.info("installment : {}", installment);

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
        BigDecimal wcNeed;
        BigDecimal totalWcDebit;
        BigDecimal totalWcTmb;
        BigDecimal wcNeedDiffer;

        //table 2
        BigDecimal case1WcLimit;
        BigDecimal case1WcMinLimit;
        BigDecimal case1Wc50CoreWc;
        BigDecimal case1WcDebitCoreWc;

        //table 3
        BigDecimal case2WcLimit;
        BigDecimal case2WcMinLimit;
        BigDecimal case2Wc50CoreWc;
        BigDecimal case2WcDebitCoreWc;

        //table 4
        BigDecimal case3WcLimit;
        BigDecimal case3WcMinLimit;
        BigDecimal case3Wc50CoreWc;
        BigDecimal case3WcDebitCoreWc;

        ////////////////////////////////////////////////////

        DBRView dbrView = dbrControl.getDBRByWorkCase(workCaseId);
        log.debug("getDBRByWorkCase :: dbrView : {}", dbrView);
        if (dbrView != null) {
            adjustDBR = dbrView.getMonthlyIncomeAdjust();
            revolvingCreditDBR = dbrView.getTotalMonthDebtRelatedWc();
        }

        List<NCB> ncbList = ncbInfoControl.getNCBByWorkCaseId(workCaseId);
        log.debug("getNCBByWorkCaseId :: ncbList : {}", ncbList);
        if (ncbList != null && ncbList.size() > 0) {
            for (NCB ncb : ncbList) {
                log.debug("getNCBByWorkCaseId :: ncb : {}", ncb);
                revolvingCreditNCB = Util.add(revolvingCreditNCB, ncb.getLoanCreditNCB());
                loanBurdenWCFlag = Util.add(loanBurdenWCFlag, ncb.getLoanCreditWC());
                revolvingCreditNCBTMBFlag = Util.add(revolvingCreditNCBTMBFlag, ncb.getLoanCreditTMB());
                loanBurdenTMBFlag = Util.add(loanBurdenTMBFlag, ncb.getLoanCreditWCTMB());
            }
        }


        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        log.debug("onGetBizInfoSummaryByWorkCase :: bizInfoSummaryView : {}", bizInfoSummaryView);
        if (bizInfoSummaryView != null) {
            weightAR = bizInfoSummaryView.getSumWeightAR();
            weightAP = bizInfoSummaryView.getSumWeightAP();
            weightINV = bizInfoSummaryView.getSumWeightINV();
            //      Sum(weight cost of goods sold * businessProportion)
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
        log.debug("findByWorkCaseId :: newCreditFacility : {}", newCreditFacility);
        if (newCreditFacility == null) {
            newCreditFacility = new NewCreditFacility();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            newCreditFacility.setWorkCase(workCase);
        }
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

        log.debug("newCreditFacility : {}", newCreditFacility);
        newCreditFacilityDAO.persist(newCreditFacility);
        log.debug("after persist newCreditFacility : {}", newCreditFacility);
    }

    public NewCreditFacilityView saveCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        log.debug("Starting saveCreditFacility...");
        log.debug("saveCreditFacility ::: workCaseId : {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User currentUser = getCurrentUser();

        log.debug("saveCreditFacility ::: newCreditFacilityView : {}", newCreditFacilityView);
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.persist(newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, currentUser));
        log.debug("saveCreditFacility ::: persist newCreditFacility : {}", newCreditFacility);

        //--- Save to NewFeeCredit
        if (Util.safetyList(newCreditFacilityView.getNewFeeDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newFeeDetailViewList : {}", newCreditFacilityView.getNewFeeDetailViewList());
            List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, currentUser);
            newCreditFacility.setNewFeeDetailList(newFeeDetailList);
            newFeeCreditDAO.persist(newFeeDetailList);
            log.debug("saveCreditFacility ::: persist newFeeDetailList : {}", newFeeDetailList);

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
                for(NewCollateralHead newCollateralHead:newCollateralHeadList)
                {
                    List<NewCollateralSub> newCollateralSubList = newCollateralHead.getNewCollateralSubList();
                    for(NewCollateralSub newCollateralSub:newCollateralSubList)
                    {
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

    // Call COMSInterface
    public AppraisalDataResult toCallComsInterface(final String jobId) {
        log.debug("onCallRetrieveAppraisalReportInfo begin jobId is  :: {}", jobId);
        AppraisalDataResult appraisalDataResult = null;
        try {
            appraisalDataResult = comsInterface.getAppraisalData(getCurrentUserID(), jobId);

            if (appraisalDataResult != null) {
                log.debug("-- appraisalDataResult.getActionResult() ::: {}", appraisalDataResult.getActionResult());
            }

        } catch (COMSInterfaceException e) {
            log.error("Exception while get COMS Appraisal data!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get CSI data!", e);
        }
        return appraisalDataResult;
    }

    //call BRMS
    public List<NewFeeDetailView> getPriceFeeInterest(final long workCaseId) {
        log.debug("getPriceFeeInterest begin workCaseId is  :: {}", workCaseId);
        StandardPricingResponse standardPricingResponse = null;
        List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        NewFeeDetailView newFeeDetailView = new NewFeeDetailView();
        try {
            standardPricingResponse = brmsControl.getPriceFeeInterest(workCaseId);
            log.debug("getPriceFeeInterest ::::workCase :: {}",workCaseId);

            if (standardPricingResponse != null) {
                log.debug("-- standardPricingResponse.getActionResult() ::: {}", standardPricingResponse.getActionResult().toString());
                log.debug("-- standardPricingResponse.getReason() ::: {}", standardPricingResponse.getReason());
                log.debug("-- standardPricingResponse.getPricingFeeList ::: {}", standardPricingResponse.getPricingFeeList().size());
                log.debug("-- standardPricingResponse.getPricingInterest ::: {}", standardPricingResponse.getPricingInterest().toString());
            }

            for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()){
                FeeDetailView feeDetailView = feeTransform.transformToView(pricingFee);
                log.debug("-- transformToView :: feeDetailView ::: {}", feeDetailView.toString());
                // find productProgram
                if(feeDetailView.getFeeLevel()==FeeLevel.CREDIT_LEVEL){
                    ProductProgramView productProgramView = productTransform.transformToView(productProgramDAO.findById((int)feeDetailView.getCreditDetailViewId()));
                    newFeeDetailView.setProductProgram(productProgramView.getDescription());
                    if (feeDetailView.getFeeTypeView().getId() == 9) {//type=9,(Front-End-Fee)
                        newFeeDetailView.setStandardFrontEndFee(feeDetailView);
                    }else if (feeDetailView.getFeeTypeView().getId() == 15) { //type=15,(Prepayment Fee)
                        newFeeDetailView.setPrepaymentFee(feeDetailView);
                    }else if (feeDetailView.getFeeTypeView().getId() == 20) {//type=20,(CancellationFee)
                        newFeeDetailView.setCancellationFee(feeDetailView);
                    }else if (feeDetailView.getFeeTypeView().getId() == 21) { //type=21,(ExtensionFee)
                        newFeeDetailView.setExtensionFee(feeDetailView);
                    }else  if(feeDetailView.getFeeTypeView().getId()==22){//type=22,(CommitmentFee)
                        newFeeDetailView.setCommitmentFee(feeDetailView);
                    }

                    log.debug("FeePaymentMethodView():::: {}",feeDetailView.getFeePaymentMethodView().getBrmsCode());
                    newFeeDetailViewList.add(newFeeDetailView);
                }
            }
        } catch (BRMSInterfaceException e) {
            log.error("Exception while get getPriceFeeInterest Appraisal data!", e);
            throw e;
        } catch (Exception e) {
            log.error("Exception while get getPriceFeeInterest data!", e);
        }
        return newFeeDetailViewList;
    }

    public void deleteAllNewCreditFacilityByIdList(List<Long> deleteCreditIdList, List<Long> deleteCollIdList, List<Long> deleteGuarantorIdList, List<Long> deleteConditionIdList) {
        log.info("deleteAllApproveByIdList()");
        log.info("deleteCreditIdList: {}", deleteCreditIdList);
        log.info("deleteCollIdList: {}", deleteCollIdList);
        log.info("deleteGuarantorIdList: {}", deleteGuarantorIdList);
        log.info("deleteConditionIdList: {}", deleteConditionIdList);

//        if (deleteCreditIdList != null && deleteCreditIdList.size() > 0) {
//            List<NewCreditDetail> deleteCreditDetailList = new ArrayList<NewCreditDetail>();
//            for (Long id : deleteCreditIdList) {
//                deleteCreditDetailList.add(newCreditDetailDAO.findById(id));
//            }
//            newCreditDetailDAO.delete(deleteCreditDetailList);
//        }

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