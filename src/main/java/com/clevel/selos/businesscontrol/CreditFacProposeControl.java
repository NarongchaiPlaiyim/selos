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
    NewCollateralTransform newCollateralTransform;
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
    NewCollateralCreditTransform newCollateralCreditTransform;
    @Inject
    NewGuarantorCreditTransform newGuarantorCreditTransform;

    public CreditFacProposeControl() {
    }

    public NewCreditFacilityView findNewCreditFacilityByWorkCase(Long workCaseId) {
        NewCreditFacilityView newCreditFacilityView = null;
        log.info("findNewCreditFacilityByWorkCase start ::::");
        try {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if (workCase != null) {
                NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
                log.info("find new creditFacility{}", newCreditFacility.getId());
                if (newCreditFacility != null) {
                    newCreditFacilityView = newCreditFacilityTransform.transformToView(newCreditFacility);

                    List<NewFeeDetail> newFeeDetailList = newFeeCreditDAO.findByNewCreditFacility(newCreditFacility);
                    if (newFeeDetailList.size() > 0) {
                        log.info("newCreditFacility.getNewFeeDetailList() :: {}", newCreditFacility.getNewFeeDetailList());
                        List<NewFeeDetailView> newFeeDetailViewList = newFeeDetailTransform.transformToView(newFeeDetailList);
                        newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
                    }

                    List<NewCreditDetail> newCreditList = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);
                    if (newCreditList.size() > 0) {
                        log.info("newCreditFacility.getNewCreditDetailList() :; {}", newCreditFacility.getNewCreditDetailList().size());
                        List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditList);
                        newCreditFacilityView.setNewCreditDetailViewList(newCreditDetailViewList);
                    }

                    List<NewCollateral> newCollateralDetailList = newCollateralDetailDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
                    if (newCollateralDetailList.size() > 0) {
                        log.info("newCreditFacility.getNewCollateralDetailList() :: {}", newCreditFacility.getNewCollateralDetailList().size());
                        List<NewCollateralView> newCollateralViewList = newCollateralTransform.transformsCollateralToView(newCollateralDetailList);
                        newCreditFacilityView.setNewCollateralViewList(newCollateralViewList);
                    }

                    List<NewGuarantorDetail> newGuarantorDetails = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
                    if (newGuarantorDetails.size() > 0) {
                        log.info("newGuarantorDetails:: {}", newGuarantorDetails.size());
                        List<NewGuarantorDetailView> newGuarantorDetailViewList = newGuarantorDetailTransform.transformToView(newGuarantorDetails);
                        newCreditFacilityView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);
                    }

                    List<NewConditionDetail> newConditionDetailList = newConditionDetailDAO.findByNewCreditFacility(newCreditFacility);
                    if (newConditionDetailList.size() > 0) {
                        log.info("newConditionDetailList() :: {}", newConditionDetailList.size());
                        List<NewConditionDetailView> newConditionDetailViewList = newConditionDetailTransform.transformToView(newConditionDetailList);
                        newCreditFacilityView.setNewConditionDetailViewList(newConditionDetailViewList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("findNewCreditFacilityByWorkCase  error ::: {}", e.getMessage());
        } finally {
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

        NewCreditFacility newCreditFacility = newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, user);
        newCreditFacilityDAO.persist(newCreditFacility);
        log.info("persist :: creditFacilityPropose...");

        onDeleteDetailOfNewCreditFacility(newCreditFacility);

        if (newCreditFacilityView.getNewFeeDetailViewList().size() > 0) {
            log.info("newCreditFacilityView.getNewFeeDetailViewList().size() ::  {}", newCreditFacilityView.getNewFeeDetailViewList().size());
            List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, user);
            newFeeCreditDAO.persist(newFeeDetailList);
            log.info("persist :: newFeeDetailList...");
        }

        if (newCreditFacilityView.getNewConditionDetailViewList().size() > 0) {
            log.info("newCreditFacilityView.getNewConditionDetailViewList().size() :: {}", newCreditFacilityView.getNewConditionDetailViewList().size());
            List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, user);
            newConditionDetailDAO.persist(newConditionDetailList);
            log.info("persist :: newConditionDetail ...");
        }

        List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(newCreditFacilityView.getNewCreditDetailViewList(), newCreditFacility, user);
        newCreditDetailDAO.persist(newCreditDetailList);
        log.info("persist newCreditDetailList...");
        onDeleteDetailOfNewCreditDetail(newCreditDetailList);

        for (int i = 0; i < newCreditDetailList.size(); i++) {
            log.info(" newCreditDetailList  is " + i);
            NewCreditDetail newCreditDetail = newCreditDetailList.get(i);
            NewCreditDetailView newCreditDetailView = newCreditFacilityView.getNewCreditDetailViewList().get(i);
            List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList(), newCreditDetail, user);
            newCreditTierDetailDAO.persist(newCreditTierDetailList);
            log.info("persist newCreditTierDetailList...{}", newCreditTierDetailList.size());
        }

        if (newCreditFacilityView.getNewCollateralViewList() != null) {
            List<NewCollateral> newCollateralList = newCollateralTransform.transformsCollateralToModel(newCreditFacilityView.getNewCollateralViewList(), newCreditFacility, user);
            newCollateralDetailDAO.persist(newCollateralList);
            log.info("persist newCollateralDetailList...");
            onDeleteDetailOfNewCollateral(newCollateralList);

            for (int i = 0; i < newCollateralList.size(); i++) {
                log.info(" newCollateralDetailList  is " + i);
                NewCollateral newCollateralDetail = newCollateralList.get(i);
                NewCollateralView newCollateralView = newCreditFacilityView.getNewCollateralViewList().get(i);

                List<NewCollateralCredit> newCollateralCreditList = newCollateralCreditTransform.transformsToModelForCollateral(newCollateralView.getNewCreditDetailViewList(), newCreditDetailList, newCollateralDetail, user);
                newCollateralRelationDAO.persist(newCollateralCreditList);
                log.info("persist newCollateralCreditList...");

                for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                    NewCollateralHead newCollateralHeadDetail = newCollateralTransform.transformCollateralHeadToModel(newCollateralHeadView, newCollateralDetail, user);
                    newCollateralHeadDetailDAO.persist(newCollateralHeadDetail);
                    log.info("persist newCollateralHeadDetail...{}", newCollateralHeadDetail.getId());

                    for (NewCollateralSubView newCollateralSubView : newCollateralHeadView.getNewCollateralSubViewList()) {
                        NewCollateralSub newCollateralSubDetail = newCollateralTransform.transformCollateralSubToModel(newCollateralSubView, newCollateralHeadDetail, user);
                        newCollateralSubDetailDAO.persist(newCollateralSubDetail);
                        log.info("persist newCollateralSubDetail...{}", newCollateralSubDetail.getId());
                        onDeleteDetailOfNewCollateralSub(newCollateralSubDetail);

                        for (NewCollateralSubView newSubCollateralView : newCollateralHeadView.getNewCollateralSubViewList()) {
                            if (newSubCollateralView.getCollateralOwnerUWList() != null) {
                                List<NewCollateralSubOwner> newCollateralSubCustomerList = new ArrayList<NewCollateralSubOwner>();
                                NewCollateralSubOwner newCollateralSubCustomer;
                                for (CustomerInfoView customerInfoView : newSubCollateralView.getCollateralOwnerUWList()) {
                                    Customer customer = customerDAO.findById(customerInfoView.getId());
                                    newCollateralSubCustomer = new NewCollateralSubOwner();
                                    newCollateralSubCustomer.setCustomer(customer);
                                    newCollateralSubCustomer.setNewCollateralSub(newCollateralSubDetail);
                                    newCollateralSubCustomerList.add(newCollateralSubCustomer);
                                }
                                newSubCollCustomerDAO.persist(newCollateralSubCustomerList);
                                log.info("persist newCollateralSubCustomerList...{}", newCollateralSubCustomerList.size());

                            }

                            if (newSubCollateralView.getMortgageList() != null) {
                                List<NewCollateralSubMortgage> newCollateralSubMortgageList = new ArrayList<NewCollateralSubMortgage>();
                                NewCollateralSubMortgage newCollateralSubMortgage;
                                for (MortgageType mortgageType : newSubCollateralView.getMortgageList()) {
                                    MortgageType mortgage = mortgageTypeDAO.findById(mortgageType.getId());
                                    newCollateralSubMortgage = new NewCollateralSubMortgage();
                                    newCollateralSubMortgage.setMortgageType(mortgage);
                                    newCollateralSubMortgage.setNewCollateralSub(newCollateralSubDetail);
                                    newCollateralSubMortgageList.add(newCollateralSubMortgage);
                                }
                                newSubCollMortgageDAO.persist(newCollateralSubMortgageList);
                                log.info("persist newCollateralSubMortgageList...{}", newCollateralSubMortgageList.size());

                            }

                            if (newSubCollateralView.getRelatedWithList() != null) {
                                NewCollateralSubRelated newCollateralSubRelate;
                                for (NewCollateralSubView relatedView : newSubCollateralView.getRelatedWithList()) {
                                    log.info("relatedView.getId() ::: {} ", relatedView.getId());
                                    NewCollateralSub relatedDetail = newCollateralSubDetailDAO.findById(relatedView.getId());
                                    log.info("relatedDetail.getId() ::: {} ", relatedDetail.getId());
                                    newCollateralSubRelate = new NewCollateralSubRelated();
                                    newCollateralSubRelate.setNewCollateralSubRelated(relatedDetail);
                                    newCollateralSubRelate.setNewCollateralSub(newCollateralSubDetail);
                                    newSubCollRelateDAO.persist(newCollateralSubRelate);
                                    log.info("persist newCollateralSubRelate. id...{}", newCollateralSubRelate.getId());
                                }
                            }
                        }
                    }
                }
            }
        }


        if (newCreditFacilityView.getNewGuarantorDetailViewList() != null) {
            List<NewGuarantorDetail> newGuarantorDetailListPersist = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, user);
            newGuarantorDetailDAO.persist(newGuarantorDetailListPersist);
            log.info("persist newGuarantorDetailList :: {}", newGuarantorDetailListPersist.size());

            for (int i = 0; i < newGuarantorDetailListPersist.size(); i++) {
                log.info("  newGuarantorDetailList  is " + i);
                NewGuarantorDetail newGuarantorDetail = newGuarantorDetailListPersist.get(i);
                NewGuarantorDetailView newGuarantorDetailView = newCreditFacilityView.getNewGuarantorDetailViewList().get(i);
                List<NewGuarantorRelCredit> newGuarantorRelCreditList = newGuarantorCreditTransform.transformsToModelForGuarantor(newGuarantorDetailView.getNewCreditDetailViewList(), newCreditDetailList, newGuarantorDetail, user);
                newGuarantorRelationDAO.persist(newGuarantorRelCreditList);
                log.info("persist newGuarantorRelCreditList...");
            }
        }


//        calculateTotalProposeAmount(workCaseId);
        log.info("onSaveNewCreditFacility  end :: {}", workCaseId);
    }

    public void onDeleteDetailOfNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("start delete all list under newCreditFacility id is :: {}", newCreditFacility);
        if (newCreditFacility != null) {
            List<NewFeeDetail> newFeeDetailList = newFeeCreditDAO.findByNewCreditFacility(newCreditFacility);
            if (newFeeDetailList.size() > 0) {
                log.info(" newFeeDetailList size ::{}", newFeeDetailList.size());
                newFeeCreditDAO.delete(newFeeDetailList);
                log.info("delete newFeeDetailList::");
            }

            List<NewConditionDetail> newConditionDetailList = newConditionDetailDAO.findByNewCreditFacility(newCreditFacility);
            if (newConditionDetailList.size() > 0) {
                log.info("newConditionList.size :: {}", newConditionDetailList.size());
                newConditionDetailDAO.delete(newConditionDetailList);
                log.info("delete newConditionDetailList");
            }

            List<NewCreditDetail> newCreditList = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);
            if (newCreditList.size() > 0) {
                newCreditDetailDAO.delete(newCreditList);
                log.info("delete newCreditList :: ");
            }

            List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
            if (newGuarantorDetailList.size() > 0) {
                newGuarantorDetailDAO.delete(newGuarantorDetailList);
                log.info("delete newGuarantorDetailList :::");
            }

            List<NewCollateral> newCollateralList = newCollateralDetailDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            if (newCollateralList.size() > 0) {
                newCollateralDetailDAO.delete(newCollateralList);
                log.info("delete newCollateralList :::");
            }

            List<NewGuarantorRelCredit> newGuarantorRelCreditListDelete = newGuarantorRelationDAO.getListGuarantorCreditByNewCreditFacility(newCreditFacility);
            if (newGuarantorRelCreditListDelete.size() > 0) {
                log.info("newGuarantorRelCreditList.size :: {}", newGuarantorRelCreditListDelete.size());
                newGuarantorRelationDAO.delete(newGuarantorRelCreditListDelete);
                log.info("delete newGuarantorRelCreditListDelete");
            }

            List<NewCollateralCredit> newCollateralRelCreditList = newCollateralRelationDAO.getListCollRelationByNewCreditFacility(newCreditFacility);
            if (newCollateralRelCreditList.size() > 0) {
                log.info("newCollateralRelCreditList::: {}", newCollateralRelCreditList.size());
                newCollateralRelationDAO.delete(newCollateralRelCreditList);
                log.info("delete newCollateralRelCredits");
            }

        }

        log.info("END onDeleteAllDetailOfNewCreditFacility ::: ");

    }

    public void onDeleteDetailOfNewCreditDetail(List<NewCreditDetail> newCreditDetailList) {
        log.info("newCreditList .size :: {}", newCreditDetailList.size());
        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
            List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierDetailDAO.findByNewCreditDetail(newCreditDetail);
            if (newCreditTierDetailList != null) {
                log.info("newCreditTierDetailList.size ::{}", newCreditTierDetailList.size());
                newCreditTierDetailDAO.delete(newCreditTierDetailList);
                log.info("delete newCreditTierDetailList ::");
            }
        }
    }

    public void onDeleteDetailOfNewCollateral(List<NewCollateral> newCollateralList) {
        log.info("START onDeleteDetailOfNewCollateral newCollateralList.size :: {} ", newCollateralList.size());
        for (NewCollateral newCollateral : newCollateralList) {
            log.info("newCollateral.id::{}", newCollateral.getId());
            List<NewCollateralCredit> newCollateralRelCreditList = newCollateralRelationDAO.getListCollRelationByNewCollateral(newCollateral);
            if (newCollateralRelCreditList.size() > 0) {
                log.info("newCollateralRelCreditList::: {}", newCollateralRelCreditList.size());
                newCollateralRelationDAO.delete(newCollateralRelCreditList);
                log.info("delete newCollateralRelCredits");
            }
        }
    }

    public void onDeleteDetailOfNewCollateralSub(NewCollateralSub newCollateralSubDetail) {
        log.info("START onDeleteDetailOfNewCollateralSub newCollateralSubDetail.size :: {} ", newCollateralSubDetail.getId());

        List<NewCollateralSubOwner> newCollateralSubCustomerListDel = newSubCollCustomerDAO.getListNewCollateralSubCustomer(newCollateralSubDetail);
        if (newCollateralSubCustomerListDel != null) {
            log.info("newCollateralSubCustomerListDel.size ::{}", newCollateralSubCustomerListDel.size());
            newSubCollCustomerDAO.delete(newCollateralSubCustomerListDel);
            log.info("delete newCollateralSubCustomerListDel");
        }
        List<NewCollateralSubMortgage> newCollateralSubMortgages = newSubCollMortgageDAO.getListNewCollateralSubMortgage(newCollateralSubDetail);
        if (newCollateralSubMortgages != null) {
            log.info("newCollateralSubMortgages .size :: {}", newCollateralSubMortgages.size());
            newSubCollMortgageDAO.delete(newCollateralSubMortgages);
            log.info("delete newCollateralSubMortgages");
        }
        List<NewCollateralSubRelated> newCollateralSubRelates = newSubCollRelateDAO.getListNewCollateralSubRelate(newCollateralSubDetail);
        if (newCollateralSubRelates != null) {
            log.info("newCollateralSubRelates.size ::{}", newCollateralSubRelates.size());
            newSubCollRelateDAO.delete(newCollateralSubRelates);
            log.info("delete newCollateralSubRelates");
        }

    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        log.info("calTotalGuaranteeAmount start :: ");
        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            return sumTotalGuaranteeAmount;
        }

        for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
            sumTotalGuaranteeAmount = Util.add(sumTotalGuaranteeAmount, guarantorDetailView.getTotalLimitGuaranteeAmount());
        }

        log.info("calTotalGuaranteeAmount end :: ");
        return sumTotalGuaranteeAmount;
    }

    public void calculateTotalProposeAmount(long workCaseId) {
        log.info("calculateTotalProposeAmount start ::: ");
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if (workCase != null) {
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if (newCreditFacility != null) {
                log.info("newCreditFacility .id::: {}", newCreditFacility.getId());
                BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
                TCGView tcgView = tcgInfoControl.getTcgView(workCaseId);

                BigDecimal sumTotalPropose = BigDecimal.ZERO;
                BigDecimal sumTotalCommercial = BigDecimal.ZERO;
                BigDecimal sumTotalCommercialAndOBOD = BigDecimal.ZERO; // wait confirm
                BigDecimal sumTotalExposure = BigDecimal.ZERO;
                BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
                BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;

                if ((newCreditFacility != null) && (basicInfoView.getSpecialProgram() != null) && (tcgView != null)) {
                    List<NewCreditDetail> newCreditDetailList = newCreditFacility.getNewCreditDetailList();

                    if (newCreditDetailList != null && newCreditDetailList.size() != 0) {

                        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
                            log.info("newCreditDetail id :: {}", newCreditDetail.getId());
                            if ((newCreditDetail.getProductProgram().getId() != 0) && (newCreditDetail.getCreditType().getId() != 0)) {
                                ProductProgram productProgram = productProgramDAO.findById(newCreditDetail.getProductProgram().getId());
                                CreditType creditType = creditTypeDAO.findById(newCreditDetail.getCreditType().getId());

                                if (productProgram != null && creditType != null) {
                                    PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);

                                    ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType,
                                            newCreditFacility.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());

                                    if (productFormula != null) {
                                        log.info("productFormula id :: {}", productFormula.getId());
                                        //ExposureMethod
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            log.info("NOT_CALCULATE :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            log.info("LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(newCreditDetail.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //limit * %PCE
                                            log.info("PCE_LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetail.getLimit(), newCreditDetail.getPcePercent()));
                                        }

                                        log.info("sumTotalPropose :: {}", sumTotalPropose);
                                        //For DBR
                                        if (productFormula.getDbrCalculate() == 1)//N
                                        {
                                            log.info("NO :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                            sumTotalNonLoanDbr = BigDecimal.ZERO;
                                        } else if (productFormula.getDbrCalculate() == 2)//Y
                                        {
                                            log.info("YES :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                            if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                                log.info("NOT_CALCULATE :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                            } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                                log.info("INSTALLMENT :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(newCreditDetail.getInstallment());
                                            } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                                log.info("INT_YEAR :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(newCreditDetail, productFormula.getDbrSpread()));
                                            }
                                        }
                                        log.info("sumTotalLoanDbr :: {}", sumTotalLoanDbr);
                                        //WC
                                        if (productFormula.getWcCalculate() == 0) {

                                        } else if (productFormula.getWcCalculate() == 1) {

                                        } else if (productFormula.getWcCalculate() == 2) {

                                        }
                                    }
                                }
                            }
                        }

                        newCreditFacility.setTotalPropose(sumTotalPropose); //sumTotalPropose
                        newCreditFacility.setTotalProposeLoanDBR(sumTotalLoanDbr);//sumTotalLoanDbr
                        newCreditFacility.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr); //sumTotalNonLoanDbr

                        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

                        if (existingCreditFacilityView != null) {
                            sumTotalCommercial = sumTotalCommercial.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(sumTotalPropose));
                            newCreditFacility.setTotalCommercial(sumTotalCommercial); //sumTotalCommercial

                            sumTotalExposure = sumTotalExposure.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(sumTotalPropose).add(existingCreditFacilityView.getTotalBorrowerRetailLimit()));
                            newCreditFacility.setTotalExposure(sumTotalExposure); //sumTotalExposure
                        }

                        log.info("sumTotalCommercial :: {}", sumTotalCommercial);
                        log.info("sumTotalExposure :: {}", sumTotalExposure);
                    }
                }

                newCreditFacilityDAO.persist(newCreditFacility);
            }
        }
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(NewCreditDetail newCreditDetail, BigDecimal dbrSpread) {
        log.info("calTotalProposeLoanDBRForIntYear start :: newCreditDetail and  dbrSpread ::{}", newCreditDetail, dbrSpread);
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        BigDecimal sum;
        log.info("limit :: {}", newCreditDetail.getLimit());
        log.info("newCreditTierDetailViews.size :: {}", newCreditDetail.getProposeCreditTierDetailList().size());
        if (newCreditDetail.getProposeCreditTierDetailList() != null) {
            for (NewCreditTierDetail newCreditTierDetail : newCreditDetail.getProposeCreditTierDetailList()) //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
            {
                sum = BigDecimal.ZERO;
                if (newCreditTierDetail != null) {
                    log.info("newCreditTierDetail.getFinalBasePrice().getValue() :: {}", newCreditTierDetail.getFinalBasePrice().getValue());
                    log.info("newCreditTierDetail.getFinalInterest() :: {}", newCreditTierDetail.getFinalInterest());
                    log.info("dbrSpread :: {}", dbrSpread);
                    sum = Util.divide((Util.multiply(newCreditTierDetail.getFinalBasePrice().getValue(), newCreditTierDetail.getFinalInterest()).add(dbrSpread)), 100);
                    log.info("newCreditTierDetail.getFinalBasePrice().getValue() :: {}", newCreditTierDetail.getFinalBasePrice().getValue());
                    sum = Util.multiply(newCreditDetail.getLimit(), sum);
                    sum = Util.divide(sum, 12);
                    sumTotalLoanDbr = sumTotalLoanDbr.add(sum);
                }
            }
        }

        log.info("calTotalProposeLoanDBRForIntYear end");
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
    public Object[] findFinalPriceRate(int standardBaseRateId, BigDecimal standardInterest,
                                       int suggestBaseRateId, BigDecimal suggestInterest) {
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