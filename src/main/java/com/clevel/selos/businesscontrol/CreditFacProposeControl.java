package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
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
    private COMSInterface comsInterface;

    private ExistingCreditFacilityView existingCreditFacilityView;


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

                    List<NewFeeDetail> newFeeDetailList = newFeeCreditDAO.findByNewCreditFacility(newCreditFacility);
                    if (newFeeDetailList.size() > 0) {
                        log.debug("newCreditFacility.getNewFeeDetailList() :: {}", newCreditFacility.getNewFeeDetailList());
                        List<NewFeeDetailView> newFeeDetailViewList = newFeeDetailTransform.transformToView(newFeeDetailList);
                        log.debug("newFeeDetailViewList : {}", newFeeDetailViewList);
                        newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
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
            }
        } catch (Exception ex) {
            log.error("exception while load data in page {}", ex);
        } finally {
            log.debug("findNewCreditFacilityByWorkCase end");
        }

        return newCreditFacilityView;
    }

    public void onSaveNewCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        log.debug("onSaveNewCreditFacility begin");
        log.debug("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User user = getCurrentUser();
        Date createDate = DateTime.now().toDate();
        Date modifyDate = DateTime.now().toDate();

        NewCreditFacility newCreditFacility = newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, user);
        newCreditFacilityDAO.persist(newCreditFacility);
        log.debug("persist :: creditFacilityPropose...");

        onDeleteDetailOfNewCreditFacility(newCreditFacility);

        if (newCreditFacilityView.getNewFeeDetailViewList().size() > 0) {
            log.debug("newCreditFacilityView.getNewFeeDetailViewList().size() ::  {}", newCreditFacilityView.getNewFeeDetailViewList().size());
            List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, user);
            newFeeCreditDAO.persist(newFeeDetailList);
            log.debug("persist :: newFeeDetailList...");
        }

        if (newCreditFacilityView.getNewConditionDetailViewList().size() > 0) {
            log.debug("newCreditFacilityView.getNewConditionDetailViewList().size() :: {}", newCreditFacilityView.getNewConditionDetailViewList().size());
            List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, user);
            newConditionDetailDAO.persist(newConditionDetailList);
            log.debug("persist :: newConditionDetail ...");
        }

        List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(newCreditFacilityView.getNewCreditDetailViewList(), newCreditFacility, user, workCase);
        newCreditDetailDAO.persist(newCreditDetailList);
        log.debug("persist newCreditDetailList...");
        onDeleteDetailOfNewCreditDetail(newCreditDetailList);

        for (int i = 0; i < newCreditDetailList.size(); i++) {
            log.debug(" newCreditDetailList  is " + i);
            NewCreditDetail newCreditDetail = newCreditDetailList.get(i);
            NewCreditDetailView newCreditDetailView = newCreditFacilityView.getNewCreditDetailViewList().get(i);
            List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList(), newCreditDetail, user);
            newCreditTierDetailDAO.persist(newCreditTierDetailList);
            log.debug("persist newCreditTierDetailList...{}", newCreditTierDetailList.size());
        }

        if (newCreditFacilityView.getNewCollateralViewList() != null) {
            List<NewCollateral> newCollateralList = newCollateralTransform.transformsCollateralToModel(newCreditFacilityView.getNewCollateralViewList(), newCreditFacility, user, workCase);
            newCollateralDetailDAO.persist(newCollateralList);
            log.debug("persist newCollateralDetailList...");

            onDeleteDetailOfNewCollateral(newCollateralList);

            for (int i = 0; i < newCollateralList.size(); i++) {
                log.debug(" newCollateralDetailList  is " + i);
                NewCollateral newCollateralDetail = newCollateralList.get(i);
                NewCollateralView newCollateralView = newCreditFacilityView.getNewCollateralViewList().get(i);

                if (newCollateralView.getProposeCreditDetailViewList().size() > 0) {
                    List<NewCollateralCredit> newCollateralCreditList = newCollateralCreditTransform.transformsToModelForCollateral(newCollateralView.getProposeCreditDetailViewList(), newCreditDetailList, newCollateralDetail, user);
                    newCollateralRelationDAO.persist(newCollateralCreditList);
                    log.debug("persist newCollateralCreditList...");
                }

                for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                    NewCollateralHead newCollateralHeadDetail = newCollateralTransform.transformCollateralHeadToModel(newCollateralHeadView, newCollateralDetail, user);
                    newCollateralHeadDetailDAO.persist(newCollateralHeadDetail);
                    log.debug("persist newCollateralHeadDetail...{}", newCollateralHeadDetail.getId());

                    for (NewCollateralSubView newCollateralSubView : newCollateralHeadView.getNewCollateralSubViewList()) {
                        NewCollateralSub newCollateralSubDetail = newCollateralTransform.transformCollateralSubToModel(newCollateralSubView, newCollateralHeadDetail, user);
                        newCollateralSubDetailDAO.persist(newCollateralSubDetail);
                        log.debug("persist newCollateralSubDetail...{}", newCollateralSubDetail.getId());
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
                                log.debug("persist newCollateralSubCustomerList...{}", newCollateralSubCustomerList.size());

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
                                log.debug("persist newCollateralSubMortgageList...{}", newCollateralSubMortgageList.size());

                            }

//                            if (newSubCollateralView.getRelatedWithList() != null) {
//                                NewCollateralSubRelated newCollateralSubRelate;
//                                for (NewCollateralSubView relatedView : newSubCollateralView.getRelatedWithList()) {
//                                    log.debug("relatedView.getId() ::: {} ", relatedView.getId());
//                                    NewCollateralSub relatedDetail = newCollateralSubDetailDAO.findById(relatedView.getId());
//                                    log.debug("relatedDetail.getId() ::: {} ", relatedDetail.getId());
//                                    newCollateralSubRelate = new NewCollateralSubRelated();
//                                    newCollateralSubRelate.setNewCollateralSubRelated(relatedDetail);
//                                    newCollateralSubRelate.setNewCollateralSub(newCollateralSubDetail);
//                                    newSubCollRelateDAO.persist(newCollateralSubRelate);
//                                    log.debug("persist newCollateralSubRelate. id...{}", newCollateralSubRelate.getId());
//                                }
//                            }
                        }
                    }
                }
            }
        }


        if (newCreditFacilityView.getNewGuarantorDetailViewList() != null) {
            List<NewGuarantorDetail> newGuarantorDetailListPersist = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, user);
            newGuarantorDetailDAO.persist(newGuarantorDetailListPersist);
            log.debug("persist newGuarantorDetailList :: {}", newGuarantorDetailListPersist.size());
            onDeleteDetailOfNewGuarantor(newGuarantorDetailListPersist);

            for (int i = 0; i < newGuarantorDetailListPersist.size(); i++) {
                log.debug("  newGuarantorDetailList  is " + i);
                NewGuarantorDetail newGuarantorDetail = newGuarantorDetailListPersist.get(i);
                NewGuarantorDetailView newGuarantorDetailView = newCreditFacilityView.getNewGuarantorDetailViewList().get(i);
                List<NewGuarantorCredit> newGuarantorCreditList = newGuarantorCreditTransform.transformsToModelForGuarantor(newGuarantorDetailView.getProposeCreditDetailViewList(), newCreditDetailList, newGuarantorDetail, user);
                newGuarantorRelationDAO.persist(newGuarantorCreditList);
                log.debug("persist newGuarantorCreditList...");
            }
        }

        log.debug("onSaveNewCreditFacility  end :: {}", workCaseId);
    }

    public void onDeleteDetailOfNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.debug("start delete all list under newCreditFacility id is :: {}", newCreditFacility);
        if (newCreditFacility != null) {
            List<NewFeeDetail> newFeeDetailList = newFeeCreditDAO.findByNewCreditFacility(newCreditFacility);
            if (newFeeDetailList.size() > 0) {
                log.debug(" newFeeDetailList size ::{}", newFeeDetailList.size());
                newFeeCreditDAO.delete(newFeeDetailList);
                log.debug("delete newFeeDetailList::");
            }

            List<NewConditionDetail> newConditionDetailList = newConditionDetailDAO.findByNewCreditFacility(newCreditFacility);
            if (newConditionDetailList.size() > 0) {
                log.debug("newConditionList.size :: {}", newConditionDetailList.size());
                newConditionDetailDAO.delete(newConditionDetailList);
                log.debug("delete newConditionDetailList");
            }

            List<NewCreditDetail> newCreditList = newCreditDetailDAO.findNewCreditDetailByNewCreditFacility(newCreditFacility);
            if (newCreditList.size() > 0) {
                newCreditDetailDAO.delete(newCreditList);
                log.debug("delete newCreditList :: ");
            }

            List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacility(newCreditFacility);
            if (newGuarantorDetailList.size() > 0) {
                newGuarantorDetailDAO.delete(newGuarantorDetailList);
                log.debug("delete newGuarantorDetailList :::");
            }

            List<NewCollateral> newCollateralList = newCollateralDetailDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            if (newCollateralList.size() > 0) {
                newCollateralDetailDAO.delete(newCollateralList);
                log.debug("delete newCollateralList :::");
            }

        }

        log.debug("END onDeleteAllDetailOfNewCreditFacility ::: ");

    }

    public void onDeleteDetailOfNewCreditDetail(List<NewCreditDetail> newCreditDetailList) {
        log.debug("newCreditList .size :: {}", newCreditDetailList.size());
        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
            List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierDetailDAO.findByNewCreditDetail(newCreditDetail);
            if (newCreditTierDetailList != null) {
                log.debug("newCreditTierDetailList.size ::{}", newCreditTierDetailList.size());
                newCreditTierDetailDAO.delete(newCreditTierDetailList);
                log.debug("delete newCreditTierDetailList ::");
            }
        }
    }

    public void onDeleteDetailOfNewCollateral(List<NewCollateral> newCollateralList) {
        log.debug("START onDeleteDetailOfNewCollateral newCollateralList.size :: {} ", newCollateralList.size());
        for (NewCollateral newCollateral : newCollateralList) {
            log.debug("newCollateral.id::{}", newCollateral.getId());
            List<NewCollateralCredit> newCollateralRelCreditList = newCollateralRelationDAO.getListCollRelationByNewCollateral(newCollateral);
            if (newCollateralRelCreditList.size() > 0) {
                log.debug("newCollateralRelCreditList::: {}", newCollateralRelCreditList.size());
                newCollateralRelationDAO.delete(newCollateralRelCreditList);
                log.debug("delete newCollateralRelCredits");
            }
        }
    }

    public void onDeleteDetailOfNewGuarantor(List<NewGuarantorDetail> newGuarantorDetailList) {
        log.debug("START onDeleteDetailOfNewGuarantor newGuarantorDetailList.size :: {} ", newGuarantorDetailList.size());
        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            log.debug("newGuarantorDetail.id::{}", newGuarantorDetail.getId());
            List<NewGuarantorCredit> newGuarantorCreditListDelete = newGuarantorRelationDAO.getListGuarantorRelationByNewGuarantor(newGuarantorDetail);
            if (newGuarantorCreditListDelete.size() > 0) {
                log.debug("newGuarantorRelCreditList.size :: {}", newGuarantorCreditListDelete.size());
                newGuarantorRelationDAO.delete(newGuarantorCreditListDelete);
                log.debug("delete newGuarantorCreditListDelete");
            }
        }
    }

    public void onDeleteDetailOfNewCollateralSub(NewCollateralSub newCollateralSubDetail) {
        log.debug("START onDeleteDetailOfNewCollateralSub newCollateralSubDetail.size :: {} ", newCollateralSubDetail.getId());

        List<NewCollateralSubOwner> newCollateralSubCustomerListDel = newSubCollCustomerDAO.getListNewCollateralSubCustomer(newCollateralSubDetail);
        if (newCollateralSubCustomerListDel != null) {
            log.debug("newCollateralSubCustomerListDel.size ::{}", newCollateralSubCustomerListDel.size());
            newSubCollCustomerDAO.delete(newCollateralSubCustomerListDel);
            log.debug("delete newCollateralSubCustomerListDel");
        }
        List<NewCollateralSubMortgage> newCollateralSubMortgages = newSubCollMortgageDAO.getListNewCollateralSubMortgage(newCollateralSubDetail);
        if (newCollateralSubMortgages != null) {
            log.debug("newCollateralSubMortgages .size :: {}", newCollateralSubMortgages.size());
            newSubCollMortgageDAO.delete(newCollateralSubMortgages);
            log.debug("delete newCollateralSubMortgages");
        }
        List<NewCollateralSubRelated> newCollateralSubRelates = newSubCollRelateDAO.getListNewCollateralSubRelate(newCollateralSubDetail);
        if (newCollateralSubRelates != null) {
            log.debug("newCollateralSubRelates.size ::{}", newCollateralSubRelates.size());
            newSubCollRelateDAO.delete(newCollateralSubRelates);
            log.debug("delete newCollateralSubRelates");
        }

    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        log.debug("calTotalGuaranteeAmount start :: ");
        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            return sumTotalGuaranteeAmount;
        }

        for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
            sumTotalGuaranteeAmount = Util.add(sumTotalGuaranteeAmount, guarantorDetailView.getTotalLimitGuaranteeAmount());
        }

        log.debug("calTotalGuaranteeAmount end :: ");
        return sumTotalGuaranteeAmount;
    }

    public void calculateTotalProposeAmount(long workCaseId) {
        log.debug("calculateTotalProposeAmount start ::: ");
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if (workCase != null) {
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if (newCreditFacility != null) {
                log.debug("newCreditFacility .id::: {}", newCreditFacility.getId());
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
                            log.debug("newCreditDetail id :: {}", newCreditDetail.getId());
                            if ((newCreditDetail.getProductProgram().getId() != 0) && (newCreditDetail.getCreditType().getId() != 0)) {
                                ProductProgram productProgram = productProgramDAO.findById(newCreditDetail.getProductProgram().getId());
                                CreditType creditType = creditTypeDAO.findById(newCreditDetail.getCreditType().getId());

                                if (productProgram != null && creditType != null) {
                                    PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);

                                    ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType,
                                            newCreditFacility.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());

                                    if (productFormula != null) {
                                        log.debug("productFormula id :: {}", productFormula.getId());
                                        //ExposureMethod
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            log.debug("NOT_CALCULATE :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            log.debug("LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(newCreditDetail.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //limit * %PCE
                                            log.debug("PCE_LIMIT :: productFormula.getExposureMethod() :: {}", productFormula.getExposureMethod());
                                            sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetail.getLimit(), newCreditDetail.getPcePercent()));
                                        }

                                        log.debug("sumTotalPropose :: {}", sumTotalPropose);
                                        //For DBR
                                        if (productFormula.getDbrCalculate() == 1)//No
                                        {
                                            log.info("NO calculate :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                            sumTotalNonLoanDbr = BigDecimal.ZERO;
                                        } else if (productFormula.getDbrCalculate() == 2)//Yes
                                        {
                                            log.debug("YES :: productFormula.getDbrCalculate() :: {}", productFormula.getDbrCalculate());
                                            if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                                log.debug("NOT_CALCULATE :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                            } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                                log.debug("INSTALLMENT :: productFormula.getDbrMethod() :: {}", productFormula.getDbrMethod());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(newCreditDetail.getInstallment());
                                            } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                                log.info("INT_YEAR :: productFormula.getDbrMethod() :: {}, productFormula.getDbrSpread() :::{}", productFormula.getDbrMethod(), productFormula.getDbrSpread());
                                                sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(newCreditDetail, productFormula.getDbrSpread()));
                                            }
                                        }
                                        log.info("sumTotalLoanDbr :: {}", sumTotalLoanDbr);

                                    }
                                }
                            }
                        }

                        newCreditFacility.setTotalPropose(sumTotalPropose); //sumTotalPropose
                        newCreditFacility.setTotalProposeLoanDBR(sumTotalLoanDbr);//sumTotalLoanDbr
                        newCreditFacility.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr); //sumTotalNonLoanDbr

                        existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
                        log.info("existingCreditFacilityView.getTotalBorrowerComLimit() ::; {}", existingCreditFacilityView.getTotalBorrowerComLimit() != null ? existingCreditFacilityView.getTotalBorrowerComLimit() : "null");
                        log.info("existingCreditFacilityView.getTotalBorrowerRetailLimit() ::; {}", existingCreditFacilityView.getTotalBorrowerRetailLimit() != null ? existingCreditFacilityView.getTotalBorrowerRetailLimit() : "null");

                        if (existingCreditFacilityView != null) {
                            sumTotalCommercial = Util.add(sumTotalCommercial, (Util.add(existingCreditFacilityView.getTotalBorrowerComLimit(), sumTotalPropose)));
                            newCreditFacility.setTotalCommercial(sumTotalCommercial); //sumTotalCommercial

                            sumTotalExposure = Util.add(sumTotalExposure, Util.add(existingCreditFacilityView.getTotalBorrowerComLimit(), (Util.add(sumTotalPropose, existingCreditFacilityView.getTotalBorrowerRetailLimit()))));
                            newCreditFacility.setTotalExposure(sumTotalExposure); //sumTotalExposure
                        } else {
                            newCreditFacility.setTotalCommercial(sumTotalCommercial); //sumTotalCommercial
                            newCreditFacility.setTotalExposure(sumTotalExposure); //sumTotalExposure
                        }

                        log.debug("sumTotalCommercial :: {}", sumTotalCommercial);
                        log.debug("sumTotalExposure :: {}", sumTotalExposure);
                    }
                }

                newCreditFacilityDAO.persist(newCreditFacility);
            }
        }
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(NewCreditDetail newCreditDetail, BigDecimal dbrSpread) {
//        log.debug("calTotalProposeLoanDBRForIntYear start :: newCreditDetail and  dbrSpread ::{}", newCreditDetail, dbrSpread);
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
//        BigDecimal sum;
//        log.info("limit :: {}", newCreditDetail.getLimit());
//        log.info("newCreditTierDetailViews.size :: {}", newCreditDetail.getProposeCreditTierDetailList().size());
//        if (newCreditDetail.getProposeCreditTierDetailList() != null) {
//            sum = BigDecimal.ZERO;
//            for (NewCreditTierDetail newCreditTierDetail : newCreditDetail.getProposeCreditTierDetailList()) //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
//            {
//                if (newCreditTierDetail != null) {
//                    log.info("newCreditTierDetail.getFinalBasePrice().getValue() :: {}", newCreditTierDetail.getFinalBasePrice().getValue());
//                    log.info("newCreditTierDetail.getFinalInterest() :: {}", newCreditTierDetail.getFinalInterest());
//                    log.info("dbrSpread :: {}", dbrSpread);
//                    log.info("newCreditDetail.getLimit() :: {}", newCreditDetail.getLimit());
//                    sum = Util.divide(Util.multiply(Util.divide(Util.add(Util.add(newCreditTierDetail.getFinalBasePrice().getValue(), newCreditTierDetail.getFinalInterest()), dbrSpread), BigDecimal.valueOf(100)), newCreditDetail.getLimit()), BigDecimal.valueOf(12));
//                    sumTotalLoanDbr = Util.add(sumTotalLoanDbr, sum);
//                }
//            }
//        }

        log.info("calTotalProposeLoanDBRForIntYear end ::: sumTotalLoanDbr ::: {}", sumTotalLoanDbr);
        return sumTotalLoanDbr;
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


    public List<ProposeCreditDetailView> findProposeCreditDetail(List<NewCreditDetailView> newCreditDetailViewList, long workCaseId) {
        log.debug("findProposeCreditDetail :: ", workCaseId);
        // todo: find credit existing and propose in this workCase
        List<ProposeCreditDetailView> proposeCreditDetailViewList = null;
        ProposeCreditDetailView proposeCreditDetailView;
        int rowCount = 1;       // seq of Model

        if ((!Util.isNull(newCreditDetailViewList)) && newCreditDetailViewList.size() > 0) {
            proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
            for (NewCreditDetailView tmp : newCreditDetailViewList) {
                proposeCreditDetailView = new ProposeCreditDetailView();
                proposeCreditDetailView.setSeq(tmp.getSeq());
                proposeCreditDetailView.setId(rowCount);
                proposeCreditDetailView.setTypeOfStep("N");
                proposeCreditDetailView.setAccountName(tmp.getAccountName());
                proposeCreditDetailView.setAccountNumber(tmp.getAccountNumber());
                proposeCreditDetailView.setAccountSuf(tmp.getAccountSuf());
                proposeCreditDetailView.setRequestType(tmp.getRequestType());
                proposeCreditDetailView.setProductProgram(tmp.getProductProgram());
                proposeCreditDetailView.setCreditFacility(tmp.getCreditType());
                proposeCreditDetailView.setLimit(tmp.getLimit());
                proposeCreditDetailView.setGuaranteeAmount(tmp.getGuaranteeAmount());
                proposeCreditDetailView.setUseCount(tmp.getUseCount());
                proposeCreditDetailViewList.add(proposeCreditDetailView);
                rowCount++;
            }
        }

        rowCount = newCreditDetailViewList.size() > 0 ? newCreditDetailViewList.size() + 1 : rowCount;

        // find existingCreditType >>> Borrower Commercial in this workCase
        existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId); //call business control  to find Existing  and transform to view

        if ((!Util.isNull(existingCreditFacilityView)) && existingCreditFacilityView.getBorrowerComExistingCredit().size() > 0) {
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getBorrowerComExistingCredit()) {
                proposeCreditDetailView = new ProposeCreditDetailView();
                proposeCreditDetailView.setSeq((int) existingCreditDetailView.getId());  // id form DB
                proposeCreditDetailView.setId(rowCount);
                proposeCreditDetailView.setTypeOfStep("E");
                proposeCreditDetailView.setAccountName(existingCreditDetailView.getAccountName());
                proposeCreditDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
                proposeCreditDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
                proposeCreditDetailView.setProductProgram(existingCreditDetailView.getExistProductProgram());
                proposeCreditDetailView.setCreditFacility(existingCreditDetailView.getExistCreditType());
                proposeCreditDetailView.setLimit(existingCreditDetailView.getLimit());
                proposeCreditDetailViewList.add(proposeCreditDetailView);
                rowCount++;
            }
        }

        return proposeCreditDetailViewList;
    }

    public List<NewCollateralSubView> findNewCollateralSubView(List<NewCollateralView> newCollateralViewList) {
        List<NewCollateralSubView> relatedWithAllList = new ArrayList<NewCollateralSubView>();
        int countNo = 1;
        for (NewCollateralView newCollateralView : newCollateralViewList) {
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
//        Sum(weight cost of goods sold * businessProportion)
            if (bizInfoSummaryView.getBizInfoDetailViewList() != null && bizInfoSummaryView.getBizInfoDetailViewList().size() > 0) {
                log.debug("onGetBizInfoSummaryByWorkCase :: bizInfoSummaryView.getBizInfoDetailViewList() : {}", bizInfoSummaryView.getBizInfoDetailViewList());
                for (BizInfoDetailView bidv : bizInfoSummaryView.getBizInfoDetailViewList()) {
                    BigDecimal cog = BigDecimal.ZERO;
                    if (bidv.getBizDesc() != null) {
                        cog = bidv.getBizDesc().getCog();
                    }
                    aaaValue = Util.add(aaaValue, Util.multiply(cog, bidv.getPercentBiz()));
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
        case1Wc50CoreWc = compareToFindLower(Util.subtract(case1WcLimit, fifty), case1WcMinLimit);
//        case1WcMinLimit - case1Wc50CoreWc
        case1WcDebitCoreWc = Util.subtract(case1WcMinLimit, case1Wc50CoreWc);

        log.debug("Value ::: case1WcLimit : {}, case1WcMinLimit : {}, case1Wc50CoreWc : {}, case1WcDebitCoreWc : {}", case1WcLimit, case1WcMinLimit, case1Wc50CoreWc, case1WcDebitCoreWc);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        1.5 x wcNeed
        case2WcLimit = Util.multiply(wcNeed, onePointFive);
//        case2WcLimit - totalWcDebit
        case2WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
//        ไม่เกิน 50% ของ case2WcLimit และไม่เกิน case2WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case2Wc50CoreWc = compareToFindLower(Util.subtract(case2WcLimit, fifty), case2WcMinLimit);
//        case2WcMinLimit - case2Wc50CoreWc
        case2WcDebitCoreWc = Util.subtract(case2WcMinLimit, case2Wc50CoreWc);

        log.debug("Value ::: case2WcLimit : {}, case2WcMinLimit : {}, case2Wc50CoreWc : {}, case2WcDebitCoreWc : {}", case2WcLimit, case2WcMinLimit, case2Wc50CoreWc, case2WcDebitCoreWc);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        ยอดขาย/รายได้ หาร 12 คูณ 35%
        case3WcLimit = Util.divide(Util.multiply(Util.divide(salesIncome, dayOfYear), thirtyFive), oneHundred);
//        case3WcLimit - totalWcDebit
        case3WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
//        ไม่เกิน 50% ของ case3WcLimit และไม่เกิน case3WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case3Wc50CoreWc = compareToFindLower(Util.subtract(case3WcLimit, fifty), case3WcMinLimit);
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

    public BigDecimal compareToFindLower(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
        }

        if (b1.compareTo(b2) > 0) {
            return b2;
        } else {
            return b1;
        }
    }

    public void saveCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId) {
        log.debug("Starting saveCreditFacility...");
        log.debug("saveCreditFacility ::: workCaseId : {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        User currentUser = getCurrentUser();

        log.debug("saveCreditFacility ::: newCreditFacilityView : {}", newCreditFacilityView);
        NewCreditFacility newCreditFacility = newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, currentUser);
        newCreditFacilityDAO.persist(newCreditFacility);
        log.debug("saveCreditFacility ::: persist newCreditFacility : {}", newCreditFacility);

        //--- Save to NewFeeCredit
        if (Util.safetyList(newCreditFacilityView.getNewFeeDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newFeeDetailViewList : {}", newCreditFacilityView.getNewFeeDetailViewList());
            List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, currentUser);
            newFeeCreditDAO.persist(newFeeDetailList);
            log.debug("saveCreditFacility ::: persist newFeeDetailList : {}", newFeeDetailList);
        }

        //--- Save to NewConditionCredit
        if (Util.safetyList(newCreditFacilityView.getNewConditionDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newConditionDetailViewList : {}", newCreditFacilityView.getNewConditionDetailViewList());
            List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, currentUser);
            log.debug("saveCreditFacility ::: before persist newConditionDetailList : {}", newConditionDetailList);
            newConditionDetailDAO.persist(newConditionDetailList);
            log.debug("saveCreditFacility ::: after persist newConditionDetailList : {}", newConditionDetailList);
        }

        //--- Save to NewCreditDetail
        if (Util.safetyList(newCreditFacilityView.getNewCreditDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newCreditDetailViewList : {}", newCreditFacilityView.getNewCreditDetailViewList());
            List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(newCreditFacilityView.getNewCreditDetailViewList(), newCreditFacility, currentUser, workCase);
            newCreditDetailDAO.persist(newCreditDetailList);
            log.debug("saveCreditFacility ::: persist newCreditDetailList : {}", newCreditDetailList);
        }

        //--- Save to NewGuarantor
        if (Util.safetyList(newCreditFacilityView.getNewGuarantorDetailViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newGuarantorDetailViewList : {}", newCreditFacilityView.getNewGuarantorDetailViewList());
            List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, currentUser);
            newGuarantorDetailDAO.persist(newGuarantorDetailList);
            log.debug("saveCreditFacility ::: persist newGuarantorDetailList : {}", newGuarantorDetailList);
        }

        //--- Save to NewCollateral
        //--- Need to Delete SubOwner from CollateralSubOwner before Insert new
        List<NewCollateralSubOwner> newCollateralSubOwnerList = newCollateralSubOwnerDAO.getListByWorkCase(workCase);
        newCollateralSubOwnerDAO.delete(newCollateralSubOwnerList);
        //--- Need to delete Collateral Credit from CollateralRelCredit before Insert new
        List<NewCollateralCredit> newCollateralCreditList = newCollateralCreditDAO.getListByWorkCase(workCase);
        newCollateralCreditDAO.delete(newCollateralCreditList);

        if (Util.safetyList(newCreditFacilityView.getNewCollateralViewList()).size() > 0) {
            log.debug("saveCreditFacility ::: newCollateralViewList : {}", newCreditFacilityView.getNewCollateralViewList());
            List<NewCollateral> newCollateralList = newCollateralTransform.transformsCollateralToModel(newCreditFacilityView.getNewCollateralViewList(), newCreditFacility, currentUser, workCase);
            newCollateralDetailDAO.persist(newCollateralList);
            log.debug("saveCreditFacility ::: persist newCollateralList : {}", newCollateralList);
        }
    }

    // Call COMSInterface
    public AppraisalDataResult toCallComsInterface(final String jobId) {
        log.debug("onCallRetrieveAppraisalReportInfo begin jobId is  :: {}", jobId);
        AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(getCurrentUserID(), jobId);
        if (appraisalDataResult != null) {
            log.debug("-- appraisalDataResult.getActionResult() ::: {}", appraisalDataResult.getActionResult());
        }
        return appraisalDataResult;
    }

}