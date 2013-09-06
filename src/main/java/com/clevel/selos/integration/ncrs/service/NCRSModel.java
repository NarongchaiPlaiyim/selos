package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.util.ValidationUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;

public class NCRSModel implements Validation, Serializable {

    private String memberref;
    private String enqpurpose;
    private String enqamount;
    private String consent;
    private String disputeenquiry;
    private ArrayList<TUEFEnquiryNameModel> nameList;
    private ArrayList<TUEFEnquiryIdModel> idList;

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

    public void validation()throws Exception{

        if(!ValidationUtil.isNull(memberref) && ValidationUtil.isGreaterThan(25, memberref))throw new ValidationException("Length of memberref is more than 25");

        if(ValidationUtil.isNull(enqpurpose))throw new ValidationException("enqpurpose is null");
        if(ValidationUtil.isGreaterThan(2, enqpurpose))throw new ValidationException("Length of enqpurpose is more than 2");

        if(!ValidationUtil.isNull(enqamount) && ValidationUtil.isGreaterThan(9, enqamount))throw new ValidationException("Length of enqamount is more than 9");

        if(ValidationUtil.isNull(consent))throw new ValidationException("enqpurpose is null");
        if(ValidationUtil.isGreaterThan(1, consent))throw new ValidationException("Length of consent is more than 1");

        if(ValidationUtil.isLessThan(1, nameList))throw new ValidationException("Size of nameList is less than 1");
        for (TUEFEnquiryNameModel nameModel : nameList)nameModel.validation();

        if(ValidationUtil.isLessThan(1, idList))throw new ValidationException("Size of idList is less than 1");
        for (TUEFEnquiryIdModel idModel : idList) idModel.validation();

    }
}
