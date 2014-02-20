package com.clevel.selos.businesscontrol;

import com.clevel.selos.controller.Decision;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.DecisionTransform;
import com.rits.cloning.Cloner;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DecisionControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    //DAO

    //Transform
    @Inject
    private DecisionTransform decisionTransform;

    //Other Business Control
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

        NewCreditFacilityView newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
        if (newCreditFacilityView != null) {
            decisionView.setProposeCreditList(newCreditFacilityView.getNewCreditDetailViewList());
            decisionView.setProposeCollateralList(newCreditFacilityView.getNewCollateralViewList());
            decisionView.setProposeFeeInfoList(newCreditFacilityView.getNewFeeDetailViewList());
            decisionView.setProposeGuarantorList(newCreditFacilityView.getNewGuarantorDetailViewList());
            decisionView.setProposeTotalCreditLimit(newCreditFacilityView.getTotalPropose());
            decisionView.setProposeTotalGuaranteeAmt(newCreditFacilityView.getTotalGuaranteeAmount());

            // clone Credit info
            decisionView.setApproveCreditList(cloner.deepClone(newCreditFacilityView.getNewCreditDetailViewList()));
            decisionView.setApproveCollateralList(cloner.deepClone(newCreditFacilityView.getNewCollateralViewList()));
            decisionView.setApproveGuarantorList(cloner.deepClone(newCreditFacilityView.getNewGuarantorDetailViewList()));
        }
        return decisionView;
    }

    public ApprovalHistoryView getCurrentUserApprove() {
        User user = getCurrentUser();
        UserView actionBy = new UserView();
        if (user != null) {
            actionBy.setId(user.getId());
            actionBy.setUserName(user.getUserName());
            actionBy.setRoleDescription(user.getRole() != null ? user.getRole().getDescription() : "");
            actionBy.setTitleName(user.getTitle() != null ? user.getTitle().getName() : "");
        }

        ApprovalHistoryView approvalHistoryView = new ApprovalHistoryView();
        approvalHistoryView.setAction("Approve CA");
        approvalHistoryView.setActionBy(actionBy);
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
