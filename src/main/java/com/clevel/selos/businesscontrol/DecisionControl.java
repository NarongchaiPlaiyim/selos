package com.clevel.selos.businesscontrol;

import com.clevel.selos.controller.Decision;
import com.clevel.selos.dao.working.ApprovalHistoryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ApprovalHistory;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ApprovalHistoryTransform;
import com.clevel.selos.transform.DecisionTransform;
import com.rits.cloning.Cloner;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DecisionControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    //DAO
    private ApprovalHistoryDAO approvalHistoryDAO;

    //Transform
    @Inject
    private DecisionTransform decisionTransform;
    @Inject
    private ApprovalHistoryTransform approvalHistoryTransform;

    //Other Business Control
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private CreditFacProposeControl creditFacProposeControl;

    @Inject
    public DecisionControl() {
    }

    public void saveDecision(long workCaseId) {
        // todo: saveDecision()
    }

    public DecisionView getDecision(long workCaseId) {
        Cloner cloner = new Cloner();
        DecisionView decisionView = new DecisionView();

        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.getExistingCreditFacility(workCaseId);
        if (existingCreditFacilityView != null) {
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
        NewCreditFacilityView newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
        if (newCreditFacilityView != null) {
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
            decisionView.setApproveCreditList(cloner.deepClone(newCreditFacilityView.getNewCreditDetailViewList()));
            decisionView.setApproveTotalCreditLimit(newCreditFacilityView.getTotalPropose());
            decisionView.setApproveBrwTotalCommercial(newCreditFacilityView.getTotalCommercial());
            decisionView.setApproveBrwTotalComAndOBOD(newCreditFacilityView.getTotalCommercialAndOBOD());
            decisionView.setApproveTotalExposure(newCreditFacilityView.getTotalExposure());
            // Approve Collateral
            decisionView.setApproveCollateralList(cloner.deepClone(newCreditFacilityView.getNewCollateralViewList()));
            // Approve Guarantor
            decisionView.setApproveGuarantorList(cloner.deepClone(newCreditFacilityView.getNewGuarantorDetailViewList()));
            decisionView.setApproveTotalGuaranteeAmt(newCreditFacilityView.getTotalGuaranteeAmount());

            // Hidden Fields
            decisionView.setApproveTotalNumOfNewOD(newCreditFacilityView.getTotalNumberOfNewOD());
            decisionView.setApproveTotalNumProposeCreditFac(newCreditFacilityView.getTotalNumberProposeCreditFac());
            decisionView.setApproveTotalNumContingentPropose(newCreditFacilityView.getTotalNumberContingenPropose());
            decisionView.setGrandTotalNumOfCoreAsset(BigDecimal.valueOf(newCreditFacilityView.getTotalNumberOfCoreAsset()));
            decisionView.setGrandTotalNumOfNonCoreAsset(BigDecimal.valueOf(newCreditFacilityView.getTotalNumberOfNonCoreAsset()));
            decisionView.setApproveTotalTCGGuaranteeAmt(newCreditFacilityView.getTotalTCGGuaranteeAmount());
            decisionView.setApproveTotalIndvGuaranteeAmt(newCreditFacilityView.getTotalIndvGuaranteeAmount());
            decisionView.setApproveTotalJurisGuaranteeAmt(newCreditFacilityView.getTotalJurisGuaranteeAmount());

        }

        // Approval History
        List<ApprovalHistory> submittedApprovalHistory = approvalHistoryDAO.findByWorkCase(workCaseId, true);
        decisionView.setApprovalHistoryList(approvalHistoryTransform.transformToView(submittedApprovalHistory));
        return decisionView;
    }

    public int getUserRoleId() {
        User user = getCurrentUser();
        if (user != null && user.getRole() != null) {
            return user.getRole().getId();
        }
        return 0;
    }
}
