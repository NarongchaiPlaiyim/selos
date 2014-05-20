package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CreditTypeDAO;
import com.clevel.selos.dao.master.ProductFormulaDAO;
import com.clevel.selos.dao.master.ProductProgramDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class DecisionControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    //DAO
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    private DecisionFollowConditionDAO decisionFollowConditionDAO;
    @Inject
    private ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private DecisionDAO decisionDAO;
    @Inject
    private NewGuarantorRelationDAO newGuarantorRelationDAO;
    @Inject
    private NewCollateralSubMortgageDAO newSubCollMortgageDAO;
    @Inject
    private NewCollateralSubOwnerDAO newCollateralSubOwnerDAO;
    @Inject
    private NewCollateralCreditDAO newCollateralRelationDAO;
    @Inject
    private NewCollateralSubRelatedDAO newCollateralSubRelatedDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;

    //Transform
    @Inject
    private NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;
    @Inject
    private NewGuarantorDetailTransform newGuarantorDetailTransform;
    @Inject
    private DecisionFollowConditionTransform decisionFollowConditionTransform;
    @Inject
    private ApprovalHistoryTransform approvalHistoryTransform;
    @Inject
    private CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    private CountryTransform countryTransform;
    @Inject
    private StepTransform stepTransform;
    @Inject
    private RoleTransform roleTransform;
    @Inject
    private UserTransform userTransform;

    //Other Business Control
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private CreditFacProposeControl creditFacProposeControl;

    @Inject
    public DecisionControl() {
    }

    public DecisionView saveApproveAndConditionData(DecisionView decisionView, WorkCase workCase) {
        log.debug("saveApproveAndConditionData() workCase: {}", workCase);
        if (workCase != null) {
            User currentUser = getCurrentUser();

            // Decision Follow up Condition
            if (decisionView.getDecisionFollowConditionViewList() != null && decisionView.getDecisionFollowConditionViewList().size() > 0) {
                List<DecisionFollowCondition> decFollowConList = decisionFollowConditionTransform.transformToModel(decisionView.getDecisionFollowConditionViewList(), workCase);
                decisionFollowConditionDAO.persist(decFollowConList);
                if(decFollowConList != null && decFollowConList.size() > 0){
                    decisionView.setDecisionFollowConditionViewList(decisionFollowConditionTransform.transformToView(decFollowConList));
                }
            }

            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCase.getId());
            log.debug("find newCreditFacility: {}", newCreditFacility);

            // Approve Credit Detail
            if (decisionView.getApproveCreditList() != null && decisionView.getApproveCreditList().size() > 0) {
                List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(decisionView.getApproveCreditList(), newCreditFacility, currentUser, workCase, ProposeType.A);
                newCreditDetailDAO.persist(newCreditDetailList);
                if(newCreditDetailList != null && newCreditDetailList.size() > 0){
                    decisionView.setApproveCreditList(newCreditDetailTransform.transformToView(newCreditDetailList));
                }
            }

            // Approve Guarantor Detail
            if (decisionView.getApproveGuarantorList() != null && decisionView.getApproveGuarantorList().size() > 0) {
                List<NewGuarantorCredit> relationDeleteList = newGuarantorRelationDAO.getListByNewCreditFacility(newCreditFacility, ProposeType.A);
                if(relationDeleteList != null && relationDeleteList.size() > 0) {
                    log.info("Guarantor Relation - deleteList size: {}",relationDeleteList.size());
                    newGuarantorRelationDAO.delete(relationDeleteList);
                }
                List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailTransform.transformToModel(decisionView.getApproveGuarantorList(), newCreditFacility, currentUser, ProposeType.A);
                newGuarantorDetailDAO.persist(newGuarantorDetailList);
                if(newGuarantorDetailList != null && newGuarantorDetailList.size() > 0){
                    decisionView.setApproveGuarantorList(newGuarantorDetailTransform.transformToView(newGuarantorDetailList));
                }
            }

            // Approve Collateral
            if (decisionView.getApproveCollateralList() != null && decisionView.getApproveCollateralList().size() > 0) {
                List<NewCollateral> tmpNewCollateralList = newCollateralDAO.findNewCollateralByTypeA(newCreditFacility);
                if (tmpNewCollateralList != null && tmpNewCollateralList.size() > 0) {
                    for (NewCollateral newCollateral : tmpNewCollateralList) {
                        // delete old collateral relation
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
                }
                List<NewCollateral> newCollateralList = newCollateralTransform.transformsCollateralToModel(decisionView.getApproveCollateralList(), newCreditFacility, currentUser, workCase, ProposeType.A);
                newCollateralDAO.persist(newCollateralList);
                if(newCollateralList != null && newCollateralList.size() > 0){
                    decisionView.setApproveCollateralList(newCollateralTransform.transformToView(newCollateralList));
                }
            }
        }
        return decisionView;
    }

    public void saveRelatedSubColl(WorkCase workCase, List<NewCollateralView> approveCollateralList) {

        if (workCase != null && approveCollateralList != null && approveCollateralList.size() > 0) {
            //remove all relate in this work case
            List<NewCollateralSubRelated> newCollateralSubRelatedList = newCollateralSubRelatedDAO.getListByWorkCase(workCase, ProposeType.A);
            if (newCollateralSubRelatedList != null) {
                newCollateralSubRelatedDAO.delete(newCollateralSubRelatedList);
            }

            //save related sub coll
            for (NewCollateralView newCollateralView : approveCollateralList) {
                if (newCollateralView.getNewCollateralHeadViewList() != null && newCollateralView.getNewCollateralHeadViewList().size() > 0) {

                    for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                        if (newCollateralHeadView.getNewCollateralSubViewList() != null && newCollateralHeadView.getNewCollateralSubViewList().size() > 0) {

                            for (NewCollateralSubView newCollateralSubView : newCollateralHeadView.getNewCollateralSubViewList()) {
                                NewCollateralSub mainNewCollSub = newCollateralSubDAO.findBySubId(newCollateralSubView.getSubId());
                                if (newCollateralSubView.getRelatedWithList() != null && newCollateralSubView.getRelatedWithList().size() > 0) {

                                    for (NewCollateralSubView related : newCollateralSubView.getRelatedWithList()) {
                                        NewCollateralSub relatedNewCollSub = newCollateralSubDAO.findBySubId(related.getSubId());
                                        NewCollateralSubRelated newCollateralSubRelated = new NewCollateralSubRelated();
                                        newCollateralSubRelated.setWorkCase(workCase);
                                        newCollateralSubRelated.setNewCollateralSub(mainNewCollSub);
                                        newCollateralSubRelated.setNewCollateralSubRelated(relatedNewCollSub);
                                        newCollateralSubRelated.setProposeType(ProposeType.A);
                                        newCollateralSubRelatedDAO.persist(newCollateralSubRelated);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ApprovalHistoryView saveApprovalHistory(ApprovalHistoryView approvalHistoryView, WorkCase workCase) {
        log.debug("saveApprovalHistory() workCase: {}", workCase);
        // Set current time for submit
        //approvalHistoryView.setSubmitDate(DateTime.now().toDate());
        approvalHistoryView.setApprovalType(ApprovalType.CA_APPROVAL.value());
        ApprovalHistory returnApprovalHistory = approvalHistoryDAO.persist(approvalHistoryTransform.transformToModel(approvalHistoryView, workCase));
        return approvalHistoryTransform.transformToView(returnApprovalHistory);
    }

    public ApprovalHistoryView saveApprovalHistoryPricing(ApprovalHistoryView approvalPricingHistoryView, WorkCase workCase) {
        log.debug("saveApprovalHistoryPricing() workCase: {}", workCase);
        // Set current time for submit
        //approvalHistoryView.setSubmitDate(DateTime.now().toDate());
        approvalPricingHistoryView.setApprovalType(ApprovalType.PRICING_APPROVAL.value());
        ApprovalHistory returnApprovalHistory = approvalHistoryDAO.persist(approvalHistoryTransform.transformToModel(approvalPricingHistoryView, workCase));
        return approvalHistoryTransform.transformToView(returnApprovalHistory);
    }

    public DecisionView getDecisionView(long workCaseId) {
        log.debug("getDecisionView() workCaseId: {}", workCaseId);
        DecisionView decisionView = new DecisionView();
        // Credit Facility Existing
        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
        if (existingCreditFacilityView != null && existingCreditFacilityView.getId() != 0) {
            // Existing Condition
            decisionView.setExtConditionComCreditList(existingCreditFacilityView.getExistingConditionDetailViewList());
            // Borrower - List
            decisionView.setExtBorrowerComCreditList(existingCreditFacilityView.getBorrowerComExistingCredit());
            decisionView.setExtBorrowerRetailCreditList(existingCreditFacilityView.getBorrowerRetailExistingCredit());
            decisionView.setExtBorrowerAppInRLOSList(existingCreditFacilityView.getBorrowerAppInRLOSCredit());
            // Borrower - Total
            decisionView.setExtBorrowerTotalComLimit(existingCreditFacilityView.getTotalBorrowerComLimit());
            decisionView.setExtBorrowerTotalRetailLimit(existingCreditFacilityView.getTotalBorrowerRetailLimit());
            decisionView.setExtBorrowerTotalAppInRLOSLimit(existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit());
            decisionView.setExtBorrowerTotalCommercial(existingCreditFacilityView.getTotalBorrowerCom());
            decisionView.setExtBorrowerTotalComAndOBOD(existingCreditFacilityView.getTotalBorrowerComOBOD());
            decisionView.setExtBorrowerTotalExposure(existingCreditFacilityView.getTotalBorrowerExposure());
            // Related - List
            decisionView.setExtRelatedComCreditList(existingCreditFacilityView.getRelatedComExistingCredit());
            decisionView.setExtRelatedRetailCreditList(existingCreditFacilityView.getRelatedRetailExistingCredit());
            decisionView.setExtRelatedAppInRLOSList(existingCreditFacilityView.getRelatedAppInRLOSCredit());
            // Related - Total
            decisionView.setExtRelatedTotalComLimit(existingCreditFacilityView.getTotalRelatedComLimit());
            decisionView.setExtRelatedTotalRetailLimit(existingCreditFacilityView.getTotalRelatedRetailLimit());
            decisionView.setExtRelatedTotalAppInRLOSLimit(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit());
            decisionView.setExtRelatedTotalCommercial(existingCreditFacilityView.getTotalRelatedCom());
            decisionView.setExtRelatedTotalComAndOBOD(existingCreditFacilityView.getTotalRelatedComOBOD());
            decisionView.setExtRelatedTotalExposure(existingCreditFacilityView.getTotalRelatedExposure());
            // Group Total
            decisionView.setExtGroupTotalCommercial(existingCreditFacilityView.getTotalGroupCom());
            decisionView.setExtGroupTotalComAndOBOD(existingCreditFacilityView.getTotalGroupComOBOD());
            decisionView.setExtGroupTotalExposure(existingCreditFacilityView.getTotalGroupExposure());
            // Collateral - List
            decisionView.setExtBorrowerCollateralList(existingCreditFacilityView.getBorrowerCollateralList());
            decisionView.setExtRelatedCollateralList(existingCreditFacilityView.getRelatedCollateralList());
            // Collateral - Total
            decisionView.setExtBorrowerTotalAppraisalValue(existingCreditFacilityView.getTotalBorrowerAppraisalValue());
            decisionView.setExtBorrowerTotalMortgageValue(existingCreditFacilityView.getTotalBorrowerMortgageValue());
            decisionView.setExtRelatedTotalAppraisalValue(existingCreditFacilityView.getTotalRelatedAppraisalValue());
            decisionView.setExtRelatedTotalMortgageValue(existingCreditFacilityView.getTotalRelatedMortgageValue());
            // Guarantor
            decisionView.setExtGuarantorList(existingCreditFacilityView.getBorrowerGuarantorList());
            decisionView.setExtTotalGuaranteeAmount(existingCreditFacilityView.getTotalGuaranteeAmount());
        }

        // Credit Facility Propose
        List<NewCreditDetailView> approveCreditDetailViews = new ArrayList<NewCreditDetailView>();
        List<NewCollateralView> approveCollViews = new ArrayList<NewCollateralView>();
        List<NewGuarantorDetailView> approveGuarantorViews = new ArrayList<NewGuarantorDetailView>();
        BigDecimal approveTotalCreditLimit = BigDecimal.ZERO;
        BigDecimal approveTotalCommercial = BigDecimal.ZERO;
        BigDecimal approveTotalComAndOBOD = BigDecimal.ZERO;
        BigDecimal approveTotalExposure = BigDecimal.ZERO;
        BigDecimal approveTotalGuaranteeAmt = BigDecimal.ZERO;

        NewCreditFacilityView newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
        if (newCreditFacilityView != null && newCreditFacilityView.getId() != 0) {
            decisionView.setNewCreditFacilityViewId(newCreditFacilityView.getId());
            decisionView.setCreditCustomerType(
                      newCreditFacilityView.getCreditCustomerType() == 2 ? CreditCustomerType.PRIME
                    : newCreditFacilityView.getCreditCustomerType() == 1 ? CreditCustomerType.NORMAL
                    : CreditCustomerType.NOT_SELECTED);
            decisionView.setLoanRequestType(newCreditFacilityView.getLoanRequestType());
            decisionView.setInvestedCountry(newCreditFacilityView.getInvestedCountry());
            decisionView.setExistingSMELimit(newCreditFacilityView.getExistingSMELimit());
            decisionView.setMaximumSMELimit(newCreditFacilityView.getMaximumSMELimit());
            // Propose Credit Info.
            decisionView.setProposeCreditList(newCreditFacilityView.getNewCreditDetailViewList());
            decisionView.setProposeTotalCreditLimit(newCreditFacilityView.getTotalPropose());
            // Propose Collateral
            decisionView.setProposeCollateralList(newCreditFacilityView.getNewCollateralViewList());
            // Propose Fee Info.
            decisionView.setProposeFeeInfoList(newCreditFacilityView.getNewFeeDetailViewList());
            // Propose Guarantor
            decisionView.setProposeGuarantorList(newCreditFacilityView.getNewGuarantorDetailViewList());
            decisionView.setProposeTotalGuaranteeAmt(newCreditFacilityView.getTotalGuaranteeAmount());
            // Propose Price Fee Reduction Info.
            decisionView.setIntFeeDOA(newCreditFacilityView.getIntFeeDOA());
            decisionView.setFrontendFeeDOA(newCreditFacilityView.getFrontendFeeDOA());
            decisionView.setGuarantorBA(newCreditFacilityView.getGuarantorBA());
            decisionView.setReasonForReduction(newCreditFacilityView.getReasonForReduction());

            // Approve Credit
            approveCreditDetailViews = newCreditFacilityView.getNewCreditDetailViewList();
            approveTotalCreditLimit = newCreditFacilityView.getTotalPropose();
            approveTotalCommercial = newCreditFacilityView.getTotalCommercial();
            approveTotalComAndOBOD = newCreditFacilityView.getTotalCommercialAndOBOD();
            approveTotalExposure = newCreditFacilityView.getTotalExposure();
            // Approve Collateral
            approveCollViews = newCreditFacilityView.getNewCollateralViewList();
            // Approve Guarantor
            approveGuarantorViews = newCreditFacilityView.getNewGuarantorDetailViewList();
            approveTotalGuaranteeAmt = newCreditFacilityView.getTotalGuaranteeAmount();

        } else {
            //if credit facility propose is not found
            decisionView.setCreditCustomerType(CreditCustomerType.NOT_SELECTED);
            decisionView.setLoanRequestType(new CreditRequestTypeView());
            decisionView.setInvestedCountry(new CountryView());
        }

        if (RoleValue.UW.id() == getUserRoleId()) {
            Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
            if (decision != null && decision.getId() != 0 && decision.getSaveFlag() == 1) {
                decisionView.setId(decision.getId());

                // Approve data already been recorded
                List<NewCreditDetail> approveCreditList = newCreditDetailDAO.findNewCreditDetail(workCaseId, ProposeType.A);
                decisionView.setApproveCreditList(newCreditDetailTransform.transformToView(approveCreditList));

                List<NewCollateral> approveCollateralList = newCollateralDAO.findNewCollateral(workCaseId, ProposeType.A);
                decisionView.setApproveCollateralList(newCollateralTransform.transformsCollateralToView(approveCollateralList));

                List<NewGuarantorDetail> approveGuarantorList;
                if (newCreditFacilityView != null && newCreditFacilityView.getId() != 0) {
                    approveGuarantorList = newGuarantorDetailDAO.findNewGuarantorByNewCreditFacId(newCreditFacilityView.getId(), ProposeType.A);
                } else {
                    approveGuarantorList = new ArrayList<NewGuarantorDetail>();
                }
                decisionView.setApproveGuarantorList(newGuarantorDetailTransform.transformToView(approveGuarantorList));

                decisionView.setApproveTotalCreditLimit(decision.getTotalApproveCredit());
                decisionView.setApproveBrwTotalCommercial(decision.getTotalApproveCommercial());
                decisionView.setApproveBrwTotalComAndOBOD(decision.getTotalApproveComAndOBOD());
                decisionView.setApproveTotalExposure(decision.getTotalApproveExposure());
                decisionView.setApproveTotalGuaranteeAmt(decision.getTotalApproveGuaranteeAmt());

                // Hidden field
                decisionView.setApproveTotalODLimit(decision.getTotalApprovedODLimit());
                decisionView.setApproveTotalNumOfNewOD(decision.getTotalApproveNumOfNewOD());
                decisionView.setApproveTotalNumProposeCreditFac(decision.getTotalApproveNumProposeCreditFac());
                decisionView.setApproveTotalNumContingentPropose(decision.getTotalApproveNumContingentPropose());
                decisionView.setGrandTotalNumOfCoreAsset(decision.getTotalApproveNumOfCoreAsset());
                decisionView.setGrandTotalNumOfNonCoreAsset(decision.getTotalApproveNumOfNonCoreAsset());
                decisionView.setApproveTotalTCGGuaranteeAmt(decision.getTotalApproveTCGGuaranteeAmt());
                decisionView.setApproveTotalIndvGuaranteeAmt(decision.getTotalApproveIndiGuaranteeAmt());
                decisionView.setApproveTotalJurisGuaranteeAmt(decision.getTotalApproveJuriGuaranteeAmt());
            } else {
                // Approve data is not recorded
                // Duplicate from propose, set all id = 0 and all type is "Approve"
                decisionView.setApproveCreditList(newCreditDetailTransform.copyToNewViews(approveCreditDetailViews, ProposeType.A, true));
                decisionView.setApproveCollateralList(newCollateralTransform.copyToNewViews(approveCollViews, ProposeType.A, true));
                decisionView.setApproveGuarantorList(newGuarantorDetailTransform.copyToNewViews(approveGuarantorViews, ProposeType.A, true));

                decisionView.setApproveTotalCreditLimit(approveTotalCreditLimit);
                decisionView.setApproveBrwTotalCommercial(approveTotalCommercial);
                decisionView.setApproveBrwTotalComAndOBOD(approveTotalComAndOBOD);
                decisionView.setApproveTotalExposure(approveTotalExposure);
                decisionView.setApproveTotalGuaranteeAmt(approveTotalGuaranteeAmt);
            }
        } else {
            // BDM, ABDM (Show duplicate data from propose only)
            decisionView.setApproveCreditList(approveCreditDetailViews);
            decisionView.setApproveCollateralList(approveCollViews);
            decisionView.setApproveGuarantorList(approveGuarantorViews);

            decisionView.setApproveTotalCreditLimit(approveTotalCreditLimit);
            decisionView.setApproveBrwTotalCommercial(approveTotalCommercial);
            decisionView.setApproveBrwTotalComAndOBOD(approveTotalComAndOBOD);
            decisionView.setApproveTotalExposure(approveTotalExposure);
            decisionView.setApproveTotalGuaranteeAmt(approveTotalGuaranteeAmt);
        }

        // Decision FollowUp Condition
        List<DecisionFollowCondition> decisionFollowConditionList = decisionFollowConditionDAO.findByWorkCase(workCaseId);
        decisionView.setDecisionFollowConditionViewList(decisionFollowConditionTransform.transformToView(decisionFollowConditionList));

        // Approval History
        List<ApprovalHistory> submittedApprovalHistories = approvalHistoryDAO.findByWorkCase(workCaseId, true);
        decisionView.setApprovalHistoryList(approvalHistoryTransform.transformToView(submittedApprovalHistories));

        return decisionView;
    }

    public ApprovalHistoryView getCurrentApprovalHistory(long workCaseId, int approvalType, long stepId){
        ApprovalHistoryView approvalHistoryView = new ApprovalHistoryView();
        ApprovalHistory approvalHistory = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), approvalType);
        if(!Util.isNull(approvalHistory)){
            approvalHistoryView = approvalHistoryTransform.transformToView(approvalHistory);
        }else{
            User user = getCurrentUser();
            Step step = stepDAO.findById(stepId);
            Role role = user.getRole();
            approvalHistoryView.setUserView(userTransform.transformToView(user));
            approvalHistoryView.setStepView(stepTransform.transformToView(step));
            approvalHistoryView.setRoleView(roleTransform.transformRoleToView(role));
            approvalHistoryView.setSubmitDate(null);
        }

        return approvalHistoryView;
    }

    public void deleteAllApproveByIdList(List<Long> deleteCreditIdList, List<Long> deleteCollIdList, List<Long> deleteGuarantorIdList, List<Long> deleteConditionIdList) {
        log.debug("deleteAllApproveByIdList()");
        log.debug("deleteCreditIdList: {}", deleteCreditIdList);
        log.debug("deleteCollIdList: {}", deleteCollIdList);
        log.debug("deleteGuarantorIdList: {}", deleteGuarantorIdList);
        log.debug("deleteConditionIdList: {}", deleteConditionIdList);

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
                deleteGuarantorList.add(newGuarantorDetailDAO.findById(id));
            }
            newGuarantorDetailDAO.delete(deleteGuarantorList);
        }

        if (deleteConditionIdList != null && deleteConditionIdList.size() > 0) {
            List<DecisionFollowCondition> deleteConditionList = new ArrayList<DecisionFollowCondition>();
            for (Long id : deleteConditionIdList) {
                deleteConditionList.add(decisionFollowConditionDAO.findById(id));
            }
            decisionFollowConditionDAO.delete(deleteConditionList);
        }
    }

    public ApprovalHistoryView getApprovalHistoryView(long stepId) {
        ApprovalHistoryView approvalHistoryView = new ApprovalHistoryView();
        StepView stepView;
        if (stepId != 0) {
            stepView = stepTransform.transformToView(stepDAO.findById(stepId));
        } else {
            stepView = new StepView();
        }

        User user = getCurrentUser();
        UserView userView = new UserView();
        RoleView roleView = new RoleView();
        if (user != null) {
            userView.setId(user.getId());
            userView.setUserName(user.getUserName());
            userView.setTitleName(user.getTitle() != null ? user.getTitle().getName() : "");
            userView.setRoleDescription(user.getRole() != null ? user.getRole().getDescription() : "");

            if (user.getRole() != null) {
                roleView = roleTransform.transformRoleToView(user.getRole());
            }
        }

        approvalHistoryView.setStepView(stepView);
        approvalHistoryView.setUserView(userView);
        approvalHistoryView.setRoleView(roleView);
        return approvalHistoryView;
    }

    public int getUserRoleId() {
        User user = getCurrentUser();
        if (user != null && user.getRole() != null) {
            return user.getRole().getId();
        }
        return 0;
    }

    public void saveDecision(DecisionView decisionView, WorkCase workCase) {
        log.debug("saveDecision() workCase: {}", workCase);
        Decision decision = decisionDAO.findByWorkCase(workCase);
        log.debug("decision: {}", decision);
        User currentUser = getCurrentUser();
        if (decision == null) {
            decision = new Decision();
            decision.setCreateBy(currentUser);
            decision.setCreateDate(new Date());
            decision.setWorkCase(workCase);
            decision.setSaveFlag(1);
        }
        decision.setModifyBy(currentUser);
        decision.setModifyDate(new Date());

        decision.setTotalApproveCredit(decisionView.getApproveTotalCreditLimit());
        decision.setTotalApproveCommercial(decisionView.getApproveBrwTotalCommercial());
        decision.setTotalApproveComAndOBOD(decisionView.getApproveBrwTotalComAndOBOD());
        decision.setTotalApproveExposure(decisionView.getApproveTotalExposure());
        decision.setTotalApprovedODLimit(decisionView.getApproveTotalODLimit());
        decision.setTotalApproveNumOfNewOD(decisionView.getApproveTotalNumOfNewOD());
        decision.setTotalApproveNumProposeCreditFac(decisionView.getApproveTotalNumProposeCreditFac());
        decision.setTotalApproveNumContingentPropose(decisionView.getApproveTotalNumContingentPropose());
        decision.setTotalApproveNumOfCoreAsset(decisionView.getGrandTotalNumOfCoreAsset());
        decision.setTotalApproveNumOfNonCoreAsset(decisionView.getGrandTotalNumOfNonCoreAsset());
        decision.setTotalApproveGuaranteeAmt(decisionView.getApproveTotalGuaranteeAmt());
        decision.setTotalApproveTCGGuaranteeAmt(decisionView.getApproveTotalTCGGuaranteeAmt());
        decision.setTotalApproveIndiGuaranteeAmt(decisionView.getApproveTotalIndvGuaranteeAmt());
        decision.setTotalApproveJuriGuaranteeAmt(decisionView.getApproveTotalJurisGuaranteeAmt());

        decisionDAO.persist(decision);
    }

    public void calculateTotalApprove(DecisionView decisionView) {
        log.debug("calculateTotalApprove()");
        if (decisionView != null) {

            BigDecimal totalApproveCredit = BigDecimal.ZERO;
            BigDecimal totalODLimit = BigDecimal.ZERO;
            BigDecimal totalNumOfNewOD = BigDecimal.ZERO;
            BigDecimal totalNumProposeCreditFac = BigDecimal.ZERO;
            BigDecimal totalNumContingentPropose = BigDecimal.ZERO;
            BigDecimal totalNumOfCoreAsset = BigDecimal.ZERO;
            BigDecimal totalNumOfNonCoreAsset = BigDecimal.ZERO;
            BigDecimal totalMortgageValue = BigDecimal.ZERO;
            BigDecimal totalApproveGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalTCGGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalIndiGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalJuriGuaranteeAmt = BigDecimal.ZERO;

            // Credit Detail
            List<NewCreditDetailView> approveCreditList = decisionView.getApproveCreditList();
            if (approveCreditList != null && approveCreditList.size() > 0) {
                for (NewCreditDetailView approveCredit : approveCreditList) {
                    // Sum total approve credit limit amount
                    totalApproveCredit = Util.add(totalApproveCredit, approveCredit.getLimit());
                    // Count All 'New' propose credit
                    totalNumProposeCreditFac = Util.add(totalNumProposeCreditFac, BigDecimal.ONE);

                    CreditTypeView creditTypeView = approveCredit.getCreditTypeView();
                    if (creditTypeView != null) {
                        // Count propose credit which credit facility = 'OD'
                        if (CreditTypeGroup.OD.value() == creditTypeView.getCreditGroup()) {
                            totalNumOfNewOD = Util.add(totalNumOfNewOD, BigDecimal.ONE);
                            totalODLimit = Util.add(totalODLimit, approveCredit.getLimit());
                        }
                        // Count the 'New' propose credit which has Contingent Flag 'Y'
                        if (creditTypeView.isContingentFlag()) {
                            totalNumContingentPropose = Util.add(totalNumContingentPropose, BigDecimal.ONE);
                        }
                    }
                }
            }

            BigDecimal totalApproveCommercial = Util.add(decisionView.getExtBorrowerTotalCommercial(), totalApproveCredit);
            BigDecimal totalApproveComAndOBOD = Util.add(decisionView.getExtBorrowerTotalComAndOBOD(), totalApproveCredit);
            BigDecimal totalApproveExposure = Util.add(decisionView.getExtBorrowerTotalExposure(), totalApproveCredit);

            List<NewCollateralView> approveCollateralList = decisionView.getApproveCollateralList();
            if (approveCollateralList != null && approveCollateralList.size() > 0) {
                for (NewCollateralView collateralView : approveCollateralList) {
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

                            // Sum total mortgage value
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

            // Guarantor Detail
            List<NewGuarantorDetailView> approveGuarantorList = decisionView.getApproveGuarantorList();
            if (approveGuarantorList != null && approveGuarantorList.size() > 0) {
                for (NewGuarantorDetailView approveGuarantor : approveGuarantorList) {
                    // Sum total approve guarantee amount
                    totalApproveGuaranteeAmt = Util.add(totalApproveGuaranteeAmt, approveGuarantor.getTotalLimitGuaranteeAmount());
                    // Sum total guarantee amount (TCG, Individual, Juristic)
                    CustomerInfoView customerInfoView = approveGuarantor.getGuarantorName();
                    if (customerInfoView != null) {
                        CustomerEntity customerEntity = customerInfoView.getCustomerEntity();
                        if (customerEntity != null) {
                            if (GuarantorCategory.INDIVIDUAL.value() == customerEntity.getId()) {
                                totalIndiGuaranteeAmt = Util.add(totalIndiGuaranteeAmt, approveGuarantor.getTotalLimitGuaranteeAmount());
                            }
                            else if (GuarantorCategory.JURISTIC.value() == customerEntity.getId()) {
                                totalJuriGuaranteeAmt = Util.add(totalJuriGuaranteeAmt, approveGuarantor.getTotalLimitGuaranteeAmount());
                            }
                            else if (GuarantorCategory.TCG.value() == customerEntity.getId()) {
                                totalTCGGuaranteeAmt = Util.add(totalTCGGuaranteeAmt, approveGuarantor.getTotalLimitGuaranteeAmount());
                            }
                        }
                    }
                }
            }

            decisionView.setApproveTotalCreditLimit(totalApproveCredit);
            decisionView.setApproveBrwTotalCommercial(totalApproveCommercial);
            decisionView.setApproveBrwTotalComAndOBOD(totalApproveComAndOBOD);
            decisionView.setApproveTotalExposure(totalApproveExposure);
            decisionView.setApproveTotalODLimit(totalODLimit);
            decisionView.setApproveTotalNumOfNewOD(totalNumOfNewOD);
            decisionView.setApproveTotalNumProposeCreditFac(totalNumProposeCreditFac);
            decisionView.setApproveTotalNumContingentPropose(totalNumContingentPropose);
            decisionView.setGrandTotalNumOfCoreAsset(totalNumOfCoreAsset);
            decisionView.setGrandTotalNumOfNonCoreAsset(totalNumOfNonCoreAsset);
            decisionView.setApproveTotalGuaranteeAmt(totalApproveGuaranteeAmt);
            decisionView.setApproveTotalTCGGuaranteeAmt(totalTCGGuaranteeAmt);
            decisionView.setApproveTotalIndvGuaranteeAmt(totalIndiGuaranteeAmt);
            decisionView.setApproveTotalJurisGuaranteeAmt(totalJuriGuaranteeAmt);
        }
    }

    //Get value pass to PDFOfferLetter by Bird
    public BigDecimal getMRRValue(){
        return Util.convertNullToZERO(super.getMRRValue());
    }

    public BigDecimal getMLRValue(){
        return Util.convertNullToZERO(super.getMLRValue());
    }

    public BigDecimal getMORValue(){
        return Util.convertNullToZERO(super.getMORValue());
    }

    public String getCurrentUserID(){
        return Util.checkNullString(super.getCurrentUserID());
    }
}
