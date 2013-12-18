package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
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

                                if (newCollateralDetail.getCreditTypeDetailList() != null) {
                                    List<CreditTypeDetailView> creditTypeDetailViews = creditTypeDetailTransform.transformToView(newCollateralDetail.getCreditTypeDetailList());
                                    newCollateralInfoView.setCreditTypeDetailViewList(creditTypeDetailViews);
                                }
                            }
                        }

                        newCreditFacilityView.setNewCollateralInfoViewList(newCollateralInfoViewList);
                    }

                    if (newCreditFacility.getNewGuarantorDetailList() != null) {
                        List<NewGuarantorDetailView> newGuarantorDetailViewList = newGuarantorDetailTransform.transformToView(newCreditFacility.getNewGuarantorDetailList());

                        for (NewGuarantorDetailView newGuarantorDetailView : newGuarantorDetailViewList) {
                            for (NewGuarantorDetail newGuarantorDetail : newCreditFacility.getNewGuarantorDetailList()) {
                                if (newGuarantorDetail.getCreditTypeDetailList() != null) {
                                    List<CreditTypeDetailView> creditTypeDetailViewList = creditTypeDetailTransform.transformToView(newGuarantorDetail.getCreditTypeDetailList());
                                    newGuarantorDetailView.setCreditTypeDetailViewList(creditTypeDetailViewList);

                                }
                            }

                        }

                        newCreditFacilityView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);
                    }


                    if (newCreditFacility.getNewConditionDetailList() != null) {
                        List<NewConditionDetailView> newConditionDetailViewList = newConditionDetailTransform.transformToView(newCreditFacility.getNewConditionDetailList());
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

    public void onSaveNewCreditFacility(NewCreditFacilityView newCreditFacilityView, long workCaseId, User user) {
        log.info("onSaveNewCreditFacility begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
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

        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            for (NewGuarantorDetailView newGuarantorDetailView : newCreditFacilityView.getNewGuarantorDetailViewList()) {
                List<CreditTypeDetail> creditTypeDetailList = creditTypeDetailTransform.transformToModelForGuarantor(newGuarantorDetailView.getCreditTypeDetailViewList(), newGuarantorDetail, user);
                creditTypeDetailDAO.persist(creditTypeDetailList);
                log.info("persist creditTypeDetailList...");
            }

        }

        List<NewCollateralDetail> newCollateralDetailList = newCollateralInfoTransform.transformsToModel(newCreditFacilityView.getNewCollateralInfoViewList(), newCreditFacility, user);
        newCollateralDetailDAO.persist(newCollateralDetailList);
        log.info("persist newCollateralDetailList...");

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
                    }
                }

                List<CreditTypeDetail> creditTypeDetailList = creditTypeDetailTransform.transformToModelForCollateral(newCollateralInfoView.getCreditTypeDetailViewList(), newCollateralDetail, user);
                creditTypeDetailDAO.persist(creditTypeDetailList);
                log.info("persist creditTypeDetailList...");

            }
        }

    }

    public List<CreditTypeDetailView> findCreditFacility(List<NewCreditDetailView> newCreditDetailViewList, long workCaseId) {
        // todo: find credit existing and propose in this workCase
        List<CreditTypeDetailView> creditTypeDetailList = new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView;

        if (newCreditDetailViewList != null && newCreditDetailViewList.size() > 0) {
            for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
                creditTypeDetailView = new CreditTypeDetailView();
                creditTypeDetailView.setSeq(newCreditDetailView.getSeq());
                creditTypeDetailView.setAccountName("-");
                creditTypeDetailView.setAccountNumber("-");
                creditTypeDetailView.setAccountSuf("-");
                creditTypeDetailView.setRequestType(newCreditDetailView.getRequestType());
                creditTypeDetailView.setProductProgram(newCreditDetailView.getProductProgram().getName());
                creditTypeDetailView.setCreditFacility(newCreditDetailView.getCreditType().getName());
                creditTypeDetailView.setLimit(newCreditDetailView.getLimit());
                creditTypeDetailView.setPCEPercent(newCreditDetailView.getPCEPercent());
                creditTypeDetailView.setPCEAmount(newCreditDetailView.getPCEAmount());
                creditTypeDetailList.add(creditTypeDetailView);
            }
        }

        int seq = 0;
        seq = newCreditDetailViewList.size()>0? newCreditDetailViewList.size()+1 : seq;

        // find existingCreditType >>> Borrower Commercial in this workCase
        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId); //call business control  to find Existing  and transform to view

//        ExistingCreditFacilityView existingCreditFacilityView = new ExistingCreditFacilityView();
//        List<ExistingCreditDetailView> borrowerComExistingCredits = new ArrayList<ExistingCreditDetailView>();
//        ExistingCreditDetailView existingCreditDetailTest = new ExistingCreditDetailView();
//        existingCreditDetailTest.setAccountName("test existing");
//        existingCreditDetailTest.setAccountRef("test existing");
//        existingCreditDetailTest.setAccountNumber("12345");
//        existingCreditDetailTest.setCreditType("Loan");
//        existingCreditDetailTest.setProductProgram("Existing");
//        existingCreditDetailTest.setLimit(BigDecimal.valueOf(2000000));
//        borrowerComExistingCredits.add(existingCreditDetailTest);
//        existingCreditFacilityView.setBorrowerComExistingCredit(borrowerComExistingCredits);

        if (existingCreditFacilityView != null) {
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getBorrowerComExistingCredit()) {
                creditTypeDetailView = new CreditTypeDetailView();
                creditTypeDetailView.setAccountName(existingCreditDetailView.getAccountName());
                creditTypeDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
                creditTypeDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
                creditTypeDetailView.setProductProgram(existingCreditDetailView.getProductProgram());
                creditTypeDetailView.setCreditFacility(existingCreditDetailView.getCreditType());
                creditTypeDetailView.setLimit(existingCreditDetailView.getLimit());
                creditTypeDetailView.setType("BorrowerExistingCredit");
                creditTypeDetailView.setSeq(seq);
                creditTypeDetailList.add(creditTypeDetailView);
                seq++;
            }
        }

        return creditTypeDetailList;
    }

    public BigDecimal calTotalGuaranteeAmount(List<NewGuarantorDetailView> guarantorDetailViewList) {
        BigDecimal sumTotalGuaranteeAmount = BigDecimal.ZERO;
        if (guarantorDetailViewList == null || guarantorDetailViewList.size() == 0) {
            return sumTotalGuaranteeAmount;
        }

        for (NewGuarantorDetailView guarantorDetailView : guarantorDetailViewList) {
            sumTotalGuaranteeAmount = sumTotalGuaranteeAmount.add(guarantorDetailView.getTotalLimitGuaranteeAmount());
        }
        return sumTotalGuaranteeAmount;
    }

    public BigDecimal calTotalProposeAmount(long workCaseId) {
        NewCreditFacilityView newCreditFacilityView = findNewCreditFacilityByWorkCase(workCaseId);
        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        TCGView tcgView = tcgInfoControl.getTcgView(workCaseId);
        BigDecimal sumTotalPropose = BigDecimal.ZERO;

        if ((newCreditFacilityView != null) && (basicInfoView.getSpecialProgram() != null) && (tcgView != null)) {
            List<NewCreditDetailView> newCreditDetailViewList = newCreditFacilityView.getNewCreditDetailViewList();

            if (newCreditDetailViewList != null && newCreditDetailViewList.size() != 0) {

                for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {
                    if ((newCreditDetailView.getProductProgram().getId() != 0)&&(newCreditDetailView.getCreditType().getId() != 0))
                    {
                        ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
                        CreditType creditType = creditTypeDAO.findById(newCreditDetailView.getCreditType().getId());

                        if(productProgram!=null && creditType!=null)
                        {
                            PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType,productProgram);

                            ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType,
                                    newCreditFacilityView.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());

                            if (productFormula != null) {
                                if (productFormula.getExposureMethod() == 1) {  //limit
                                    sumTotalPropose = sumTotalPropose.add(newCreditDetailView.getLimit());
                                } else if (productFormula.getExposureMethod() == 2) {  //limit * %PCE
                                    sumTotalPropose = sumTotalPropose.add(Util.multiply(newCreditDetailView.getLimit(), newCreditDetailView.getPCEPercent()));
                                } else if (productFormula.getExposureMethod() == 3) { //ไม่คำนวณ
                                    sumTotalPropose = sumTotalPropose.add(BigDecimal.ZERO);
                                }
                            }
                        }
                    }
                }
            }
        }

        return sumTotalPropose;
    }

    public BigDecimal calTotalCommercialAmount(long workCaseId) {
        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
        BigDecimal sumTotalCommercial = BigDecimal.ZERO;

        if (existingCreditFacilityView != null) {
            sumTotalCommercial = sumTotalCommercial.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(calTotalProposeAmount(workCaseId)));
        }

        return sumTotalCommercial;
    }

    public BigDecimal calTotalCommercialAndOBODAmount(long workCaseId) { //wait Formula
        NewCreditFacilityView newCreditFacilityView = findNewCreditFacilityByWorkCase(workCaseId);
        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
        BigDecimal sumTotalCommercialAndOBOD = BigDecimal.ZERO;
        return sumTotalCommercialAndOBOD;
    }

    public BigDecimal calTotalExposureAmount(long workCaseId) {
        BigDecimal sumTotalExposure = BigDecimal.ZERO;
        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

        if (existingCreditFacilityView != null) {
            sumTotalExposure = sumTotalExposure.add(existingCreditFacilityView.getTotalBorrowerComLimit().add(calTotalProposeAmount(workCaseId)).add(existingCreditFacilityView.getTotalBorrowerRetailLimit()));
        }

        return sumTotalExposure;
    }

    public void wcCalculation(long workCaseId) {

    }

    /**
     * Formula: <br/>
     * if(standard > suggest) -> final = standard <br/>
     * else if(standard < suggest) -> final = suggest <br/>
     * else *(standard == suggest) -> final = (standard | suggest)
     * @param standardBaseRateId
     * @param standardInterest
     * @param suggestBaseRateId
     * @param suggestInterest
     * @return Object[0]: (BaseRate) finalBaseRate, Object[1]: (BigDecimal) finalInterest, Object[2]: (String) finalPriceLabel,<br/>
     * Object[3]: (BaseRate) standardBaseRate, Object[4]: (String) standardPriceLabel,<br/>
     * Object[5]: (BaseRate) suggestBaseRate, Object[6]: (String) suggestPriceLabel
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
        }
        else {
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