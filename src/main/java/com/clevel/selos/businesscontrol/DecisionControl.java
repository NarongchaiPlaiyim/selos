package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.rits.cloning.Cloner;
import org.joda.time.DateTime;
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

    //Other Business Control
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private CreditFacProposeControl creditFacProposeControl;

    @Inject
    public DecisionControl() {
    }

    public DecisionView saveDecision(DecisionView decisionView, long workCaseId) {
        log.debug("saveDecision() workCaseId: {}", workCaseId);
        if (workCaseId != 0) {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            User user = getCurrentUser();

            // Approve Credit Info.
            if (decisionView.getApproveCreditList() != null && decisionView.getApproveCreditList().size() > 0) {
                log.debug("Before persist - ApproveCreditViews: {}", decisionView.getApproveCreditList());
                List<NewCreditDetail> returnCreditList = newCreditDetailDAO.persistAndReturn(newCreditDetailTransform.transformToModel(decisionView.getApproveCreditList(), newCreditFacility, user, workCase, ProposeType.A));
                log.debug("After persist - ApproveCredits: {}", returnCreditList);
                decisionView.setApproveCreditList(newCreditDetailTransform.transformToView(returnCreditList));
            }

            // Approve Collateral
            if (decisionView.getApproveCollateralList() != null && decisionView.getApproveCollateralList().size() > 0) {
                log.debug("Before persist - ApproveCollateralViews: {}", decisionView.getApproveCollateralList());
                List<NewCollateral> returnCollateralList = newCollateralDAO.persistAndReturn(newCollateralTransform.transformsCollateralToModel(decisionView.getApproveCollateralList(), newCreditFacility, user, workCase, ProposeType.A));
                log.debug("After persist - ApproveCollaterals: {}", returnCollateralList);
                decisionView.setApproveCollateralList(newCollateralTransform.transformsCollateralToView(returnCollateralList));
            }

            // Approve Guarantor
            if (decisionView.getApproveGuarantorList() != null && decisionView.getApproveGuarantorList().size() > 0) {
                log.debug("Before persist - ApproveGuarantorViews: {}", decisionView.getApproveGuarantorList());
                List<NewGuarantorDetail> returnGuarantorList = newGuarantorDetailDAO.persistAndReturn(newGuarantorDetailTransform.transformToModel(decisionView.getApproveGuarantorList(), newCreditFacility, user, ProposeType.A));
                log.debug("After persist - ApproveGuarantors: {}", returnGuarantorList);
                decisionView.setApproveGuarantorList(newGuarantorDetailTransform.transformToView(returnGuarantorList));
            }

            // Decision Follow up Condition
            if (decisionView.getDecisionFollowConditionViewList() != null && decisionView.getDecisionFollowConditionViewList().size() > 0) {
                log.debug("Before persist - DecisionFollowConditionViews: {}", decisionView.getDecisionFollowConditionViewList());
                List<DecisionFollowCondition> returnConditionList = decisionFollowConditionDAO.persistAndReturn(decisionFollowConditionTransform.transformToModel(decisionView.getDecisionFollowConditionViewList(), workCase));
                log.debug("After persist - DecisionFollowConditions: {}", returnConditionList);
                decisionView.setDecisionFollowConditionViewList(decisionFollowConditionTransform.transformToView(returnConditionList));
            }
        }
        return decisionView;
    }

    public ApprovalHistoryView saveApprovalHistory(ApprovalHistoryView approvalHistoryView, long workCaseId) {
        log.debug("");
        if (workCaseId != 0) {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            // Set current time for submit
            approvalHistoryView.setSubmitDate(DateTime.now().toDate());
            ApprovalHistory returnApprovalHistory = approvalHistoryDAO.persist(approvalHistoryTransform.transformToModel(approvalHistoryView, workCase));
            approvalHistoryView = approvalHistoryTransform.transformToView(returnApprovalHistory);
        }
        return approvalHistoryView;
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
            decisionView.setCreditCustomerType(
                      newCreditFacilityView.getCreditCustomerType() == 2 ? CreditCustomerType.PRIME
                    : newCreditFacilityView.getCreditCustomerType() == 1 ? CreditCustomerType.NORMAL
                    : CreditCustomerType.NOT_SELECTED);
            decisionView.setLoanRequestType(creditRequestTypeTransform.transformToView(newCreditFacilityView.getLoanRequestType()));
            decisionView.setInvestedCountry(countryTransform.transformToView(newCreditFacilityView.getInvestedCountry()));
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
            approveCreditDetailViews = newCreditDetailTransform.copyToNewViews(newCreditFacilityView.getNewCreditDetailViewList(), ProposeType.A, true);
            approveTotalCreditLimit = newCreditFacilityView.getTotalPropose();
            approveTotalCommercial = newCreditFacilityView.getTotalCommercial();
            approveTotalComAndOBOD = newCreditFacilityView.getTotalCommercialAndOBOD();
            approveTotalExposure = newCreditFacilityView.getTotalExposure();
            // Approve Collateral
            approveCollViews = newCollateralTransform.copyToNewViews(newCreditFacilityView.getNewCollateralViewList(), ProposeType.A, true);
            // Approve Guarantor
            approveGuarantorViews = newGuarantorDetailTransform.copyToNewViews(newCreditFacilityView.getNewGuarantorDetailViewList(), ProposeType.A, true);
            approveTotalGuaranteeAmt = newCreditFacilityView.getTotalGuaranteeAmount();

            // Hidden Fields
//            decisionView.setApproveTotalNumOfNewOD(newCreditFacilityView.getTotalApproveNumOfNewOD());
//            decisionView.setApproveTotalNumProposeCreditFac(newCreditFacilityView.getTotalApproveNumProposeCreditFac());
//            decisionView.setApproveTotalNumContingentPropose(newCreditFacilityView.getTotalApproveNumContingenPropose());
//            decisionView.setGrandTotalNumOfCoreAsset(newCreditFacilityView.getTotalApproveNumOfCoreAsset());
//            decisionView.setGrandTotalNumOfNonCoreAsset(newCreditFacilityView.getTotalApproveNumOfNonCoreAsset());
//            decisionView.setApproveTotalTCGGuaranteeAmt(newCreditFacilityView.getTotalApproveTCGGuaranteeAmt());
//            decisionView.setApproveTotalIndvGuaranteeAmt(newCreditFacilityView.getTotalApproveIndiGuaranteeAmt());
//            decisionView.setApproveTotalJurisGuaranteeAmt(newCreditFacilityView.getTotalApproveJurisGuaranteeAmt());
        }
        else {
            //if credit facility propose is not found
            decisionView.setCreditCustomerType(CreditCustomerType.NOT_SELECTED);
            decisionView.setLoanRequestType(new CreditRequestTypeView());
            decisionView.setInvestedCountry(new CountryView());
        }

        if (RoleValue.UW.id() == getUserRoleId()) {

        }
        else {
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
        if (user != null) {
            userView.setId(user.getId());
            userView.setUserName(user.getUserName());
            userView.setTitleName(user.getTitle() != null ? user.getTitle().getName() : "");
            userView.setRoleDescription(user.getRole() != null ? user.getRole().getDescription() : "");
        }

        approvalHistoryView.setStepView(stepView);
        approvalHistoryView.setUserView(userView);
        return approvalHistoryView;
    }

    public int getUserRoleId() {
        User user = getCurrentUser();
        if (user != null && user.getRole() != null) {
            return user.getRole().getId();
        }
        return 0;
    }
}
