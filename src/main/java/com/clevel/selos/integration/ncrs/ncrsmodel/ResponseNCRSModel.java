package com.clevel.selos.integration.ncrs.ncrsmodel;

import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ResponseNCRSModel implements Serializable {
    private String appRefNumber;
    private String result;
    private String reason;
    private String idNumber;
    private NCRSResponseModel responseModel;

    public ResponseNCRSModel() {
    }

    public ResponseNCRSModel(String appRefNumber, String result, String reason, String idNumber, NCRSResponseModel responseModel) {
        this.appRefNumber = appRefNumber;
        this.result = result;
        this.reason = reason;
        this.idNumber = idNumber;
        this.responseModel = responseModel;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public String getResult() {
        return result;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appRefNumber", appRefNumber)
                .append("result", result)
                .append("reason", reason)
                .append("idNumber", idNumber)
                .append("responseModel", responseModel)
                .toString();
    }
}
