package com.clevel.selos.integration.ncb.ncrs.ncrsmodel;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;

public class NCRSInputModel implements Serializable {
    private String userId;
    private String appRefNumber;
    private ArrayList<NCRSModel> ncrsModelArrayList;

    public NCRSInputModel(String userId, String appRefNumber, ArrayList<NCRSModel> ncrsModelArrayList) {
        this.userId = userId;
        this.appRefNumber = appRefNumber;
        this.ncrsModelArrayList = ncrsModelArrayList;
    }

    public String getUserId() {
        return userId;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public ArrayList<NCRSModel> getNcrsModelArrayList() {
        return ncrsModelArrayList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("userId", userId)
                .append("appRefNumber", appRefNumber)
                .append("ncrsModelArrayList", ncrsModelArrayList)
                .toString();
    }
}
