package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.models.response.Ncrsresponse;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;

public class NCRSModel implements Serializable {
    private String url;
    private String id;
    private String pass;
    private String memberref;
    private String enqpurpose;
    private String enqamount;
    private String consent;
    private String disputeenquiry;
    private ArrayList<TUEFEnquiryNameModel> nameList;
    private ArrayList<TUEFEnquiryIdModel> idList;

    public NCRSModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
                .append("id", id)
                .append("pass", pass)
                .append("memberref", memberref)
                .append("enqpurpose", enqpurpose)
                .append("enqamount", enqamount)
                .append("consent", consent)
                .append("disputeenquiry", disputeenquiry)
                .append("nameList", nameList)
                .append("idList", idList)
                .toString();
    }
}
