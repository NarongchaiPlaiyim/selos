package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class ActionValidationResult {
    private ActionResult actionResult;
    private List<MandateFieldMessageView> mandateFieldMessageViewList;

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public List<MandateFieldMessageView> getMandateFieldMessageViewList() {
        return mandateFieldMessageViewList;
    }

    public void setMandateFieldMessageViewList(List<MandateFieldMessageView> mandateFieldMessageViewList) {
        this.mandateFieldMessageViewList = mandateFieldMessageViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionResult", actionResult)
                .append("mandateFieldMessageViewList", mandateFieldMessageViewList)
                .toString();
    }
}
