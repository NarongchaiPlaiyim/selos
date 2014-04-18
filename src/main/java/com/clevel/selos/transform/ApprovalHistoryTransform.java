package com.clevel.selos.transform;

import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.ApprovalHistoryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApprovalType;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.db.working.ApprovalHistory;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ApprovalHistoryView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ApprovalHistoryTransform extends Transform {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private RoleDAO roleDAO;

    @Inject
    private StepTransform stepTransform;
    @Inject
    private UserTransform userTransform;
    @Inject
    private RoleTransform roleTransform;

    @Inject
    public ApprovalHistoryTransform() {
    }

    public ApprovalHistory transformToModel(ApprovalHistoryView approvalHistoryView, WorkCase workCase) {
        if (approvalHistoryView == null) {
            return null;
        }

        ApprovalHistory approvalHistory;
        if (approvalHistoryView.getId() != 0) {
            approvalHistory = approvalHistoryDAO.findById(approvalHistoryView.getId());
        } else {
            approvalHistory = new ApprovalHistory();
        }

        if (approvalHistoryView.getStepView() != null && approvalHistoryView.getStepView().getId() != 0) {
            approvalHistory.setStep(stepDAO.findById(approvalHistoryView.getStepView().getId()));
        } else {
            approvalHistory.setStep(null);
        }

        if (approvalHistoryView.getUserView() != null && !Util.isEmpty(approvalHistoryView.getUserView().getId())) {
            approvalHistory.setUser(userDAO.findById(approvalHistoryView.getUserView().getId()));
        } else {
            approvalHistory.setUser(null);
        }

        if (approvalHistoryView.getRoleView() != null && approvalHistoryView.getRoleView().getId() != 0) {
            approvalHistory.setRole(roleDAO.findById(approvalHistoryView.getRoleView().getId()));
        } else {
            approvalHistory.setRole(null);
        }

        approvalHistory.setWorkCase(workCase);
        approvalHistory.setSubmitDate(approvalHistoryView.getSubmitDate());
        approvalHistory.setComments(approvalHistoryView.getComments());
        approvalHistory.setApproveType(approvalHistoryView.getApprovalType());
        approvalHistory.setApproveDecision(DecisionType.APPROVED == approvalHistoryView.getUwDecision() ? 1 : 0);
        approvalHistory.setSubmit(approvalHistoryView.getSubmit());
        return approvalHistory;
    }

    public List<ApprovalHistory> transformToModel(List<ApprovalHistoryView> approvalHistoryViewList, WorkCase workCase) {
        List<ApprovalHistory> approvalHistoryList = new ArrayList<ApprovalHistory>();
        if (approvalHistoryViewList == null) {
            return approvalHistoryList;
        }

        for (ApprovalHistoryView approvalHistoryView : approvalHistoryViewList) {
            approvalHistoryList.add(transformToModel(approvalHistoryView, workCase));
        }
        return approvalHistoryList;
    }

    public ApprovalHistoryView transformToView(ApprovalHistory approvalHistory) {
        ApprovalHistoryView approvalHistoryView = new ApprovalHistoryView();
        if(approvalHistory == null) {
            return approvalHistoryView;
        }
        approvalHistoryView.setId(approvalHistory.getId());
        approvalHistoryView.setStepView(stepTransform.transformToView(approvalHistory.getStep()));
        approvalHistoryView.setUserView(userTransform.transformToView(approvalHistory.getUser()));
        approvalHistoryView.setRoleView(roleTransform.transformRoleToView(approvalHistory.getRole()));
        approvalHistoryView.setSubmitDate(approvalHistory.getSubmitDate());
        approvalHistoryView.setComments(approvalHistory.getComments());
        approvalHistoryView.setUwDecision(approvalHistory.getApproveDecision() == 1 ? DecisionType.APPROVED : DecisionType.REJECTED);
        approvalHistoryView.setApprovalType(approvalHistory.getApproveType());
        approvalHistoryView.setSubmit(approvalHistory.getSubmit());
        return approvalHistoryView;
    }

    public List<ApprovalHistoryView> transformToView(List<ApprovalHistory> approvalHistoryList) {
        List<ApprovalHistoryView> approvalHistoryViewList = new ArrayList<ApprovalHistoryView>();
        if (approvalHistoryList == null) {
            return approvalHistoryViewList;
        }
        for (ApprovalHistory approvalHistory : approvalHistoryList) {
            approvalHistoryViewList.add(transformToView(approvalHistory));
        }
        return approvalHistoryViewList;
    }
}
