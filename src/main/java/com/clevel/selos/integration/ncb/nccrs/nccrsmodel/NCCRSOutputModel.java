package com.clevel.selos.integration.ncb.nccrs.nccrsmodel;

import com.clevel.selos.integration.ncb.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.model.ActionResult;

import java.io.Serializable;

public class NCCRSOutputModel implements Serializable {
    private String appRefNumber;
    private ActionResult actionResult;
    private String reason;
    private String idNumber;
    private NCCRSResponseModel responseModel;

    public NCCRSOutputModel() {
    }

    public NCCRSOutputModel(String appRefNumber, ActionResult actionResult, String reason, String idNumber, NCCRSResponseModel responseModel) {
        this.appRefNumber = appRefNumber;
        this.actionResult = actionResult;
        this.reason = reason;
        this.idNumber = idNumber;
        this.responseModel = responseModel;
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
}
