package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCRSModel implements Serializable {

    private String memberref;
    private String enqpurpose = "01";
    private String enqamount = "0";
    private String consent = "Y";
    private String disputeenquiry;
    private ArrayList<TUEFEnquiryNameModel> nameList;
    private ArrayList<TUEFEnquiryIdModel> idList;

    @Inject
    public NCRSModel() {
    }

    public String getMemberref() {
        return memberref;
    }

    public void setMemberref(String memberref) {
        this.memberref = memberref;
    }

    public String getEnqpurpose() {
        return enqpurpose;
    }

    public void setEnqpurpose(String enqpurpose) {
        this.enqpurpose = enqpurpose;
    }

    public String getEnqamount() {
        return enqamount;
    }

    public void setEnqamount(String enqamount) {
        this.enqamount = enqamount;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getDisputeenquiry() {
        return disputeenquiry;
    }

    public void setDisputeenquiry(String disputeenquiry) {
        this.disputeenquiry = disputeenquiry;
    }

    public ArrayList<TUEFEnquiryNameModel> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<TUEFEnquiryNameModel> nameList) {
        this.nameList = nameList;
    }

    public ArrayList<TUEFEnquiryIdModel> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<TUEFEnquiryIdModel> idList) {
        this.idList = idList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("memberref", memberref)
                .append("enqpurpose", enqpurpose)
                .append("enqamount", enqamount)
                .append("consent", consent)
                .append("disputeenquiry", disputeenquiry)
                .append("nameList", nameList.toString())
                .append("idList", idList.toString())
                .toString();
    }
}
