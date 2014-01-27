package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CollateralDetailResultView implements Serializable {
    private ActionResult actionResult;
    private String reason;
    private NewCollateralView collateralDetailView;

    public CollateralDetailResultView() {
        //reset();
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public NewCollateralView getCollateralDetailView() {
        return collateralDetailView;
    }

    public void setCollateralDetailView(NewCollateralView collateralDetailView) {
        this.collateralDetailView = collateralDetailView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("collateralDetailView", collateralDetailView)
                .toString();
    }
}
