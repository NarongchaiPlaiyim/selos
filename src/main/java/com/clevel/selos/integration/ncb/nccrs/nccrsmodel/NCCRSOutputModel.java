package com.clevel.selos.integration.ncb.nccrs.nccrsmodel;

import com.clevel.selos.integration.ncb.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NCCRSOutputModel implements Serializable {
    private String appRefNumber;
    private ActionResult actionResult;
    private String reason;
    private String idNumber;
    private NCCRSResponseModel responseModel;
    private NCCRSModel nccrsModel; // use for csi

    public NCCRSOutputModel() {
    }

    public NCCRSOutputModel(String appRefNumber, ActionResult actionResult, String reason, String idNumber, NCCRSResponseModel responseModel, NCCRSModel nccrsModel) {
        this.appRefNumber = appRefNumber;
        this.actionResult = actionResult;
        this.reason = reason;
        this.idNumber = idNumber;
        this.responseModel = responseModel;
        this.nccrsModel = nccrsModel;
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

    public NCCRSResponseModel getResponseModel() {
        return responseModel;
    }

    public NCCRSModel getNccrsModel() {
        return nccrsModel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appRefNumber", appRefNumber)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("idNumber", idNumber)
                .append("nccrsModel", nccrsModel)
                .toString();
    }
}
