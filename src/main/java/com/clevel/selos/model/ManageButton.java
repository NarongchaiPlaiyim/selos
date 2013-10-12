package com.clevel.selos.model;

import java.io.Serializable;

public class ManageButton implements Serializable {
    private boolean checkNCBButton;
    private boolean checkBRMSButton;
    private boolean checkMandateDocButton;
    private boolean cancelCAButton;
    private boolean assignToCheckerButton;
    private boolean returnToMakerButton;
    private boolean closeSaleButton;
    private boolean requestAppraisalButton;

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
}
