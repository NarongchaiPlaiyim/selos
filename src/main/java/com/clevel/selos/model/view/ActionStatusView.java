package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ActionStatusView {

    private ActionResult statusCode;
    private String statusDesc;

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public ActionResult getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(ActionResult statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("statusCode", statusCode)
                .append("statusDesc", statusDesc)
                .toString();
    }
}
