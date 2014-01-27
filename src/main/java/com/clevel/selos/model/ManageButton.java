package com.clevel.selos.model;

import java.io.Serializable;

public class ManageButton implements Serializable {
    private boolean assignToCheckerButton;
    private boolean assignToABDMButton;
    private boolean cancelCAButton;
    private boolean checkNCBButton;
    private boolean checkBRMSButton;
    private boolean checkMandateDocButton;
    private boolean checkCriteriaButton;
    private boolean closeSaleButton;
    private boolean returnToMakerButton;
    private boolean requestAppraisalButton;
    private boolean submitCAButton;
    private boolean viewRelatedCA;

    public boolean isCheckNCBButton() {
        return checkNCBButton;
    }

    public void setCheckNCBButton(boolean checkNCBButton) {
        this.checkNCBButton = checkNCBButton;
    }

    public boolean isCheckBRMSButton() {
        return checkBRMSButton;
    }

    public void setCheckBRMSButton(boolean checkBRMSButton) {
        this.checkBRMSButton = checkBRMSButton;
    }

    public boolean isCheckMandateDocButton() {
        return checkMandateDocButton;
    }

    public void setCheckMandateDocButton(boolean checkMandateDocButton) {
        this.checkMandateDocButton = checkMandateDocButton;
    }

    public boolean isCancelCAButton() {
        return cancelCAButton;
    }

    public void setCancelCAButton(boolean cancelCAButton) {
        this.cancelCAButton = cancelCAButton;
    }

    public boolean isAssignToCheckerButton() {
        return assignToCheckerButton;
    }

    public void setAssignToCheckerButton(boolean assignToCheckerButton) {
        this.assignToCheckerButton = assignToCheckerButton;
    }

    public boolean isReturnToMakerButton() {
        return returnToMakerButton;
    }

    public void setReturnToMakerButton(boolean returnToMakerButton) {
        this.returnToMakerButton = returnToMakerButton;
    }

    public boolean isCloseSaleButton() {
        return closeSaleButton;
    }

    public void setCloseSaleButton(boolean closeSaleButton) {
        this.closeSaleButton = closeSaleButton;
    }

    public boolean isRequestAppraisalButton() {
        return requestAppraisalButton;
    }

    public void setRequestAppraisalButton(boolean requestAppraisalButton) {
        this.requestAppraisalButton = requestAppraisalButton;
    }

    public boolean isAssignToABDMButton() {
        return assignToABDMButton;
    }

    public void setAssignToABDMButton(boolean assignToABDMButton) {
        this.assignToABDMButton = assignToABDMButton;
    }

    public boolean isCheckCriteriaButton() {
        return checkCriteriaButton;
    }

    public void setCheckCriteriaButton(boolean checkCriteriaButton) {
        this.checkCriteriaButton = checkCriteriaButton;
    }

    public boolean isViewRelatedCA() {
        return viewRelatedCA;
    }

    public void setViewRelatedCA(boolean viewRelatedCA) {
        this.viewRelatedCA = viewRelatedCA;
    }

    public boolean isSubmitCAButton() {
        return submitCAButton;
    }

    public void setSubmitCAButton(boolean submitCAButton) {
        this.submitCAButton = submitCAButton;
    }
}
