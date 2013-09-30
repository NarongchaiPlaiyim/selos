package com.clevel.selos.integration.ncb.nccrs.nccrsmodel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;

public class NCCRSInputModel implements Serializable {
    private String userId;
    private String appRefNumber;
    private ArrayList<NCCRSModel> nccrsModelArrayList;

    public NCCRSInputModel(String userId, String appRefNumber, ArrayList<NCCRSModel> nccrsModelArrayList) {
        this.userId = userId;
        this.appRefNumber = appRefNumber;
        this.nccrsModelArrayList = nccrsModelArrayList;
    }

    public String getUserId() {
        return userId;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public ArrayList<NCCRSModel> getNccrsModelArrayList() {
        return nccrsModelArrayList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("userId", userId)
                .append("appRefNumber", appRefNumber)
                .append("nccrsModelArrayList", nccrsModelArrayList)
                .toString();
    }
}
