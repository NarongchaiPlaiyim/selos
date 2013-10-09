package com.clevel.selos.model;

import java.io.Serializable;

public class ManageButton implements Serializable {
    private boolean checkNCBButton;
    private boolean assignToCheckerButton;
    private boolean returnToMakerButton;

    public boolean isCheckNCBButton() {
        return checkNCBButton;
    }

    public void setCheckNCBButton(boolean checkNCBButton) {
        this.checkNCBButton = checkNCBButton;
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
}
