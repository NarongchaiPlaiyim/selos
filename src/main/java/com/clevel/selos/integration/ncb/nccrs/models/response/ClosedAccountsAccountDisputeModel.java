package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ClosedAccountsAccountDisputeModel implements Serializable {
    private String date;
    private String status;
    private String topic;
    private String detail;

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTopic() {
        return topic;
    }

    public String getDetail() {
        return detail;
    }
}
