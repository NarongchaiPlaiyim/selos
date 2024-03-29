package com.clevel.selos.integration.ncb.ncrs.ncrsmodel;

import com.clevel.selos.integration.ncb.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NCRSOutputModel implements Serializable {
    private String appRefNumber;
    private ActionResult actionResult;
    private String reason;
    private String idNumber;
    private NCRSResponseModel responseModel;
    private NCRSModel ncrsModel; //user for csi

    public NCRSOutputModel() {
    }

    public NCRSOutputModel(String appRefNumber, ActionResult actionResult, String reason, String idNumber, NCRSResponseModel responseModel, NCRSModel ncrsModel) {
        this.appRefNumber = appRefNumber;
        this.actionResult = actionResult;
        this.reason = reason;
        this.idNumber = idNumber;
        this.responseModel = responseModel;
        this.ncrsModel = ncrsModel;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public String getReason() {
        return reason;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public NCRSResponseModel getResponseModel() {
        return responseModel;
    }

    public NCRSModel getNcrsModel() {
        return ncrsModel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appRefNumber", appRefNumber)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("idNumber", idNumber)
                .append("ncrsModel", ncrsModel)
                .toString();
    }
}
