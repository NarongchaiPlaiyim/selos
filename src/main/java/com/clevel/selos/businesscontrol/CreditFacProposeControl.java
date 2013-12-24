package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DBRMethod;
import com.clevel.selos.model.ExposureMethod;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    NewFeeDetailTransform newFeeDetailTransform;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    CreditTypeDetailTransform creditTypeDetailTransform;
    @Inject
    NewCollateralInfoTransform newCollateralInfoTransform;
    @Inject
    NewCollHeadDetailTransform newCollHeadDetailTransform;
    @Inject
    NewSubCollDetailTransform newSubCollDetailTransform;
    @Inject
    NewGuarantorDetailTransform newGuarantorDetailTransform;
    @Inject
    NewConditionDetailTransform newConditionDetailTransform;
    @Inject
    NewCreditTierTransform newCreditTierTransform;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
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
    NewCollateralDetailDAO newCollateralDetailDAO;
    @Inject
    NewCollateralSubDetailDAO newCollateralSubDetailDAO;
    @Inject
    NewCollateralHeadDetailDAO newCollateralHeadDetailDAO;
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
    NewCollateralRelationDAO newCollateralRelationDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    NewSubCollCustomerDAO newSubCollCustomerDAO;
    @Inject
    NewSubCollMortgageDAO newSubCollMortgageDAO;
    @Inject
    NewSubCollRelateDAO newSubCollRelateDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;


    public CreditFacProposeControl() {
    }

    public NewCreditFacility getNewCreditFacilityViewByWorkCaseId(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return newCreditFacilityDAO.findByWorkCaseId(workCaseId);

    }

    public NewCreditFacilityView findNewCreditFacilityByWorkCase(long workCaseId) {
        NewCreditFacilityView newCreditFacilityView = null;

        try {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if (workCase != null) {
                NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
                if (newCreditFacility != null) {
                    newCreditFacilityView = newCreditFacilityTransform.transformToView(newCreditFacility);

                    if (newCreditFacility.getNewFeeDetailList() != null) {
                        List<NewFeeDetailView> newFeeDetailViewList = newFeeDetailTransform.transformToView(newCreditFacility.getNewFeeDetailList());
                        newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
                    }

                    if (newCreditFacility.getNewCreditDetailList() != null) {
                        List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditFacility.getNewCreditDetailList());

                        for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {

                            for (NewCreditDetail newCreditDetail : newCreditFacility.getNewCreditDetailList()) {
                                if (newCreditDetail.getProposeCreditTierDetailList() != null) {
                                    List<NewCreditTierDetailView> newCreditTierDetailViewList = newCreditTierTransform.transformToView(newCreditDetail.getProposeCreditTierDetailList());
                                    newCreditDetailView.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
                                }
                            }

                        }

                        newCreditFacilityView.setNewCreditDetailViewList(newCreditDetailViewList);
                    }

                    if (newCreditFacility.getNewCollateralDetailList() != null) {
                        List<NewCollateralInfoView> newCollateralInfoViewList = newCollateralInfoTransform.transformsToView(newCreditFacility.getNewCollateralDetailList());
                        for (NewCollateralInfoView newCollateralInfoView : newCollateralInfoViewList) {
                            for (NewCollateralDetail newCollateralDetail : newCreditFacility.getNewCollateralDetailList()) {
                                if (newCollateralDetail.getNewCollateralHeadDetailList() != null) {
                                    List<NewCollateralHeadDetailView> newCollateralHeadDetailViews = newCollHeadDetailTransform.transformToView(newCollateralDetail.getNewCollateralHeadDetailList());

                                    for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViews) {
                                        for (NewCollateralHeadDetail newCollateralHeadDetail : newCollateralDetail.getNewCollateralHeadDetailList()) {
                                            if (newCollateralHeadDetail.getNewCollateralSubDetailList() != null) {
                                                List<NewSubCollateralDetailView> newSubCollateralDetailViews = newSubCollDetailTransform.transformToView(newCollateralHeadDetail.getNewCollateralSubDetailList());
                                                newCollateralHeadDetailView.setNewSubCollateralDetailViewList(newSubCollateralDetailViews);
                                            }
                                        }
                                    }

                                    newCollateralInfoView.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViews);
                                }

                            }
                        }

                        newCreditFacilityView.setNewCollateralInfoViewList(newCollateralInfoViewList);
                    }

                    if (newCreditFacility.getNewGuarantorDetailList() != null) {
                        List<NewGuarantorDetailView> newGuarantorDetailViewList = newGuarantorDetailTransform.transformToView(newCreditFacility.getNewGuarantorDetailList());
                        newCreditFacilityView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);
                    }


                    if (newCreditFacility.getNewConditionDetailList() != null) {
                        List<NewConditionDetailView> newConditionDetailViewList = newConditionDetailTransform.transformToView(newCreditFacility.getNewConditionDetailList());
                        newCreditFacilityView.setNewConditionDetailViewList(newConditionDetailViewList);
                    }
                }
            }
        } catch (
                Exception e
                )

        {
            log.error("findNewCreditFacilityByWorkCase  error ::: {}", e.getMessage());
        } finally

        {
            log.info("findNewCreditFacilityByWorkCase end");
        }

        return newCreditFacilityView;
    }

    public void onSaveNewCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        log.info("onSaveNewCreditFacility begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();
        Date createDate = DateTime.now().toDate();
        Date modifyDate = DateTime.now().toDate();
        newCreditFacilityView = calculateTotalProposeAmount(newCreditFacilityView, workCaseId);

        NewCreditFacility newCreditFacility = newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, user);
        newCreditFacilityDAO.persist(newCreditFacility);
        log.info("persist :: creditFacilityPropose...");

        List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, user);
        newFeeCreditDAO.persist(newFeeDetailList);
        log.info("persist :: newFeeDetailList...");

        List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, user);
        newConditionDetailDAO.persist(newConditionDetailList);
        log.info("persist :: newConditionDetail ...");

        List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(newCreditFacilityView.getNewCreditDetailViewList(), newCreditFacility, user);
        newCreditDetailDAO.persist(newCreditDetailList);
        log.info("persist newCreditDetailList...");

        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
            for (NewCreditDetailView newCreditDetailView : newCreditFacilityView.getNewCreditDetailViewList()) {
                List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList(), newCreditDetail, user);
                newCreditTierDetailDAO.persist(newCreditTierDetailList);
                log.info("persist newCreditTierDetailList...");
            }
        }

        List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, user);
        newGuarantorDetailDAO.persist(newGuarantorDetailList);
        log.info("persist newGuarantorDetailList...");
        List<NewGuarantorRelCredit> newGuarantorRelCreditList = new ArrayList<NewGuarantorRelCredit>();
        NewGuarantorRelCredit newGuarantorRelCredit;
        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            for (NewGuarantorDetailView newGuarantorDetailView : newCreditFacilityView.getNewGuarantorDetailViewList()) {
//                if (newGuarantorDetailView.getNewCreditDetailViewList().size() > 0) {
//                    List<NewCreditDetail> newCreditGuarantors = newCreditDetailTransform.getNewCreditDetailForGuarantor(newGuarantorDetailView.getNewCreditDetailViewList(), newCreditFacility);
//                    newCreditDetailDAO.persist(newCreditGuarantors);
//                    for (NewCreditDetail newCreditDetail : newCreditGuarantors){
//                        newGuarantorRelCredit = new NewGuarantorRelCredit();
//                        newGuarantorRelCredit.setNewCreditDetail(newCreditDetail);
//                        newGuarantorRelCredit.setNewGuarantorDetail(newGuarantorDetail);
//                        newGuarantorRelCreditList.add(newGuarantorRelCredit);
//                    }
//                    newGuarantorRelationDAO.persist(newGuarantorRelCreditList);
//                    log.info("persist newGuarantorRelCredit...");
//
//                }
            }
        }

        List<NewCollateralDetail> newCollateralDetailList = newCollateralInfoTransform.transformsToModel(newCreditFacilityView.getNewCollateralInfoViewList(), newCreditFacility, user);
        newCollateralDetailDAO.persist(newCollateralDetailList);
        log.info("persist newCollateralDetailList...");

        List<NewCollateralRelCredit> newCollateralRelCreditList = new ArrayList<NewCollateralRelCredit>();
        NewCollateralRelCredit newCollateralRelCredit;
        for (NewCollateralDetail newCollateralDetail : newCollateralDetailList) {
            for (NewCollateralInfoView newCollateralInfoView : newCreditFacilityView.getNewCollateralInfoViewList()) {
                log.info("newCollateralInfoView.getNewCreditDetailViewList().size() :: {}",newCollateralInfoView.getNewCreditDetailViewList().size());
                if (newCollateralInfoView.getNewCreditDetailViewList().size() > 0)
                {
//                    List<NewCreditDetail> newCreditCollateralList = newCreditDetailTransform.transformToModel(newCollateralInfoView.getNewCreditDetailViewList(), newCreditFacility, user);
//                    newCreditDetailDAO.persist(newCreditCollateralList);
//                    for (NewCreditDetail newCreditDetail : newCreditCollateralList) {
//                        newCollateralRelCredit = new NewCollateralRelCredit();
//                        newCollateralRelCredit.setNewCreditDetail(newCreditDetail);
//                        newCollateralRelCredit.setNewCollateralDetail(newCollateralDetail);
//                        newCollateralRelCreditList.add(newCollateralRelCredit);
//                    }
//                    newCollateralRelationDAO.persist(newCollateralRelCreditList);
//                    log.info("persist newCollateralRelCredit...");
                }
            }
        }


        for (NewCollateralDetail newCollateralDetail : newCollateralDetailList) {
            for (NewCollateralInfoView newCollateralInfoView : newCreditFacilityView.getNewCollateralInfoViewList()) {
                List<NewCollateralHeadDetail> newCollateralHeadDetailList = newCollHeadDetailTransform.transformToModel(newCollateralInfoView.getNewCollateralHeadDetailViewList(), newCollateralDetail, user);
                newCollateralHeadDetailDAO.persist(newCollateralHeadDetailList);
                log.info("persist newCollateralHeadDetailList...");

                for (NewCollateralHeadDetail newCollateralHeadDetail : newCollateralHeadDetailList) {
                    for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralInfoView.getNewCollateralHeadDetailViewList()) {
                        List<NewCollateralSubDetail> newCollateralSubDetails = newSubCollDetailTransform.transformToModel(newCollateralHeadDetailView.getNewSubCollateralDetailViewList(), newCollateralHeadDetail, user);
                        newCollateralSubDetailDAO.persist(newCollateralSubDetails);
                        log.info("persist newCollateralSubDetailList...");
                        List<NewCollateralSubCustomer> newCollateralSubCustomerList = new ArrayList<NewCollateralSubCustomer>();
                        NewCollateralSubCustomer newCollateralSubCustomer;
                        List<NewCollateralSubMortgage> newCollateralSubMortgageList = new ArrayList<NewCollateralSubMortgage>();
                        NewCollateralSubMortgage newCollateralSubMortgage;
                        List<NewCollateralSubRelate> newCollateralSubRelateList = new ArrayList<NewCollateralSubRelate>();
                        NewCollateralSubRelate newCollateralSubRelate;
                        for (NewCollateralSubDetail newSubCollateralDetail : newCollateralSubDetails) {
                            for (NewSubCollateralDetailView newSubCollateral : newCollateralHeadDetailView.getNewSubCollateralDetailViewList()) {
                                if(newSubCollateral.getCollateralOwnerUWList()!=null){
                                    for (CustomerInfoView customerInfoView : newSubCollateral.getCollateralOwnerUWList()) {
                                        Customer customer = customerDAO.findById(customerInfoView.getId());
                                        newCollateralSubCustomer = new NewCollateralSubCustomer();
                                        newCollateralSubCustomer.setCustomer(customer);
                                        newCollateralSubCustomer.setNewCollateralSubDetail(newSubCollateralDetail);
                                        newCollateralSubCustomerList.add(newCollateralSubCustomer);
                                    }
                                    newSubCollCustomerDAO.persist(newCollateralSubCustomerList);
                                    log.info("persist newCollateralSubCustomerList...");
                                }

                                if(newSubCollateral.getMortgageList()!=null){
                                    for (MortgageType mortgageType : newSubCollateral.getMortgageList()) {
                                        MortgageType mortgage = mortgageTypeDAO.findById(mortgageType.getId());
                                        newCollateralSubMortgage = new NewCollateralSubMortgage();
                                        newCollateralSubMortgage.setMortgageType(mortgage);
                                        newCollateralSubMortgage.setNewCollateralSubDetail(newSubCollateralDetail);
                                        newCollateralSubMortgageList.add(newCollateralSubMortgage);
                                    }

                                    newSubCollMortgageDAO.persist(newCollateralSubMortgageList);
                                    log.info("persist newCollateralSubMortgageList...");
                                }

                                if(newSubCollateral.getRelatedWithList()!=null){
//                                    List<NewCollateralSubDetail> newCollateralSubRelates = newSubCollDetailTransform.transformToModel(newSubCollateral.getRelatedWithList(), newCollateralHeadDetail, user);
//                                    newCollateralSubDetailDAO.persist(newCollateralSubRelates);
//
//                                    for (NewCollateralSubDetail relate : newCollateralSubRelates) {
//                                        newCollateralSubRelate = new NewCollateralSubRelate();
//                                        newCollateralSubRelate.setNewCollateralSubDetailRel(relate);
//                                        newCollateralSubRelate.setNewCollateralSubDetail(newSubCollateralDetail);
//                                        newCollateralSubRelateList.add(newCollateralSubRelate);
//                                    }
//
//                                    newSubCollRelateDAO.persist(newCollateralSubRelateList);
//                                    log.info("persist newCollateralSubRelateList....");
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public List<NewCreditDetailView> findCreditFacility(List<NewCreditDetailView> newCreditDetailViewList, long workCaseId) {
        // todo: find credit existing and propose in this workCase

       /* int seq = 0;
        seq = newCreditDetailViewList.size() > 0 ? newCreditDetailViewList.size() + 1 : seq;

        // find existingCreditType >>> Borrower Commercial in this workCase
        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId); //call business control  to find Existing  and transform to view

        if (existingCreditFacilityView != null) {
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getBorrowerComExistingCredit()) {
                creditTypeDetailView = new CreditTypeDetailView();
                creditTypeDetailView.setAccountName(existingCreditDetailView.getAccountName());
                creditTypeDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
                creditTypeDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
                creditTypeDetailView.setProductProgram(existingCreditDetailView.getProductProgram());
                creditTypeDetailView.setCreditFacility(existingCreditDetailView.getCreditType());
                creditTypeDetailView.setLimit(existingCreditDetailView.getLimit());
                creditTypeDetailView.setSeq(seq);
                creditTypeDetailList.add(creditTypeDetailView);
                seq++;
            }
        }*/

        return newCreditDetailViewList;
    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            return sumTotalGuaranteeAmount;
        }

        for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
//            sumTotalGuaranteeAmount = sumTotalGuaranteeAmount.add(guarantorDetailView.getTotalLimitGuaranteeAmount());
            sumTotalGuaranteeAmount = Util.add(sumTotalGuaranteeAmount, guarantorDetailView.getTotalLimitGuaranteeAmount());
        }
        return sumTotalGuaranteeAmount;
    }

    public NewCreditFacilityView calculateTotalProposeAmount(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        TCGView tcgView = tcgInfoControl.getTcgView(workCaseId);

        BigDecimal sumTotalPropose = BigDecimal.ZERO;
        BigDecimal sumTotalCommercial = BigDecimal.ZERO;
        BigDecimal sumTotalCommercialAndOBOD = BigDecimal.ZERO; // wait confirm
        BigDecimal sumTotalExposure = BigDecimal.ZERO;
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;

        if ((newCreditFacilityView != null) && (basicInfoView.getSpecialProgram() != null) && (tcgView != null)) {
            List<NewCreditDetailView> newCreditDetailViewList = newCreditFacilityView.getNewCreditDetailViewList();

            if (newCreditDetailViewList != null && newCreditDetailViewList.size() != 0) {

                for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
                    if ((newCreditDetailView.getProductProgram().getId() != 0) && (newCreditDetailView.getCreditType().getId() != 0)) {
                        ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
                        CreditType creditType = creditTypeDAO.findById(newCreditDetailView.getCreditType().getId());

                        if (productProgram != null && creditType != null) {
                            PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);

                            ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType,
                                    newCreditFacilityView.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());

                            if (productFormula != null) {
                                //ExposureMethod
                                if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                    sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                    sumTotalPropose = sumTotalPropose.add(newCreditDetailView.getLimit());
                                } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //limit * %PCE
                                    sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetailView.getLimit(), newCreditDetailView.getPCEPercent()));
                                }

                                //For DBR
                                if (productFormula.getDbrCalculate() == 1)//N
                                {
                                    sumTotalNonLoanDbr = BigDecimal.ZERO;
                                } else if (productFormula.getDbrCalculate() == 2)//Y
                                {
                                    if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                        sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                    } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                        sumTotalLoanDbr = sumTotalLoanDbr.add(newCreditDetailView.getInstallment());
                                    } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                        sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(newCreditDetailView, productFormula.getDbrSpread()));
                                    }
                                }

                                //WC
                                if (productFormula.getWcCalculate() == 0) {

                                } else if (productFormula.getWcCalculate() == 1) {

                                } else if (productFormula.getWcCalculate() == 2) {

                                }
                            }
                        }
                    }
                }

                newCreditFacilityView.setTotalPropose(sumTotalPropose); //sumTotalPropose
                newCreditFacilityView.setTotalProposeLoanDBR(sumTotalLoanDbr);//sumTotalLoanDbr
                newCreditFacilityView.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr); //sumTotalNonLoanDbr

                ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

                if (existingCreditFacilityView != null) {
                    sumTotalCommercial = sumTotalCommercial.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(sumTotalPropose));
                    newCreditFacilityView.setTotalCommercial(sumTotalCommercial); //sumTotalCommercial

                    sumTotalExposure = sumTotalExposure.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(sumTotalPropose).add(existingCreditFacilityView.getTotalBorrowerRetailLimit()));
                    newCreditFacilityView.setTotalExposure(sumTotalExposure); //sumTotalExposure
                }
            }
        }

        return newCreditFacilityView;
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(NewCreditDetailView newCreditDetailView, BigDecimal dbrSpread) {
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        BigDecimal sum;
        log.info("limit :: {}", newCreditDetailView.getLimit());
        log.info("newCreditTierDetailViews.size :: {}", newCreditDetailView.getNewCreditTierDetailViewList().size());
        if (newCreditDetailView.getNewCreditTierDetailViewList() != null) {
            for (NewCreditTierDetailView newCreditTierDetailView : newCreditDetailView.getNewCreditTierDetailViewList()) //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
            {
                sum = BigDecimal.ZERO;
                if (newCreditTierDetailView != null) {
                    sum = Util.divide((Util.multiply(newCreditTierDetailView.getFinalBasePrice().getValue(), newCreditTierDetailView.getFinalInterest()).add(dbrSpread)), 100);
                    sum = Util.multiply(newCreditDetailView.getLimit(), sum);
                    sum = Util.divide(sum, 12);
                    sumTotalLoanDbr = sumTotalLoanDbr.add(sum);
                }
            }
        }

        return sumTotalLoanDbr;
    }


    public void wcCalculation(long workCaseId) {

    }

    /**
     * Formula: <br/>
     * if(standard > suggest) -> final = standard <br/>
     * else if(standard < suggest) -> final = suggest <br/>
     * else *(standard == suggest) -> final = (standard | suggest)
     *
     * @param standardBaseRateId
     * @param standardInterest
     * @param suggestBaseRateId
     * @param suggestInterest
     * @return Object[0]: (BaseRate) finalBaseRate, Object[1]: (BigDecimal) finalInterest, Object[2]: (String) finalPriceLabel,<br/>
     *         Object[3]: (BaseRate) standardBaseRate, Object[4]: (String) standardPriceLabel,<br/>
     *         Object[5]: (BaseRate) suggestBaseRate, Object[6]: (String) suggestPriceLabel
     */
    public Object[] findFinalPriceRate(int standardBaseRateId, BigDecimal standardInterest, int suggestBaseRateId, BigDecimal suggestInterest) {
        Object[] returnValues = new Object[7];

        BigDecimal standardPrice = BigDecimal.ZERO;
        StringBuilder standardPriceLabel = new StringBuilder("");

        BigDecimal suggestPrice = BigDecimal.ZERO;
        StringBuilder suggestPriceLabel = new StringBuilder("");

        BaseRate finalBaseRate = new BaseRate();
        BigDecimal finalInterest = BigDecimal.ZERO;
        String finalPriceLabel = "";

        // Standard Price Rate
        BaseRate standardBaseRate = baseRateDAO.findById(standardBaseRateId);
        if (standardBaseRate != null && standardInterest != null) {
            standardPrice = standardBaseRate.getValue().add(standardInterest);

            if (ValidationUtil.isValueLessThanZero(standardInterest)) {
                standardPriceLabel.append(standardBaseRate.getName()).append(" ").append(standardInterest);
            } else {
                standardPriceLabel.append(standardBaseRate.getName()).append(" + ").append(standardInterest);
            }
        }

        // Suggest Price Rate
        BaseRate suggestBaseRate = baseRateDAO.findById(suggestBaseRateId);
        if (suggestBaseRate != null && suggestInterest != null) {
            suggestPrice = suggestBaseRate.getValue().add(suggestInterest);

            if (ValidationUtil.isValueLessThanZero(suggestInterest)) {
                suggestPriceLabel.append(suggestBaseRate.getName()).append(" ").append(suggestInterest);
            } else {
                suggestPriceLabel.append(suggestBaseRate.getName()).append(" + ").append(suggestInterest);
            }
        }

        // Compare for Final Price
        if (ValidationUtil.isGreaterThan(standardPrice, suggestPrice) || ValidationUtil.isValueEqual(standardPrice, suggestPrice)) {
            finalBaseRate = standardBaseRate;
            finalInterest = standardInterest;
            finalPriceLabel = standardPriceLabel.toString();
        } else {
            finalBaseRate = suggestBaseRate;
            finalInterest = suggestInterest;
            finalPriceLabel = suggestPriceLabel.toString();
        }

        returnValues[0] = finalBaseRate;
        returnValues[1] = finalInterest;
        returnValues[2] = finalPriceLabel;
        returnValues[3] = standardBaseRate != null ? standardBaseRate : new BaseRate();
        returnValues[4] = standardPriceLabel.toString();
        returnValues[5] = suggestBaseRate != null ? suggestBaseRate : new BaseRate();
        returnValues[6] = suggestPriceLabel.toString();
        return returnValues;
    }
}