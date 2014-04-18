package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UWRuleResponseView implements Serializable{
    private ActionResult actionResult;
    private UWRuleResultSummaryView uwRuleResultSummaryView;
    private List<MandateFieldMessageView> mandateFieldMessageViewList;
    private String reason;

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public UWRuleResultSummaryView getUwRuleResultSummaryView() {
        return uwRuleResultSummaryView;
    }

    public void setUwRuleResultSummaryView(UWRuleResultSummaryView uwRuleResultSummaryView) {
        this.uwRuleResultSummaryView = uwRuleResultSummaryView;
    }

    public List<MandateFieldMessageView> getMandateFieldMessageViewList() {
        return mandateFieldMessageViewList;
    }

    public void setMandateFieldMessageViewList(List<MandateFieldMessageView> mandateFieldMessageViewList) {
        this.mandateFieldMessageViewList = mandateFieldMessageViewList;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionResult", actionResult)
                .append("uwRuleResultSummaryView", uwRuleResultSummaryView)
                .append("mandateFieldMessageViewList", mandateFieldMessageViewList)
                .append("reason", reason)
                .toString();
    }
}
