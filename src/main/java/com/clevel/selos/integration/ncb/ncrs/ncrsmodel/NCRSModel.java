package com.clevel.selos.integration.ncb.ncrs.ncrsmodel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCRSModel implements Serializable {
    private String firstName;
    private String lastName;
    private String citizenId;
    private String idType;
    private String trackingId;
    private String memberref;
    private String enqpurpose = "01";
    private String enqamount = "0";
    private String consent = "Y";

    public NCRSModel() {
    }

    public void setIdTypePassport() {
        idType = IdType.PASSPORT.value();
    }

    public void setIdTypeCitizen() {
        idType = IdType.CITIZEN.value();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getIdType() {
        return idType;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("citizenId", citizenId)
                .append("idType", idType)
                .append("trackingId", trackingId)
                .append("memberref", memberref)
                .append("enqpurpose", enqpurpose)
                .append("enqamount", enqamount)
                .append("consent", consent)
                .toString();
    }
}
