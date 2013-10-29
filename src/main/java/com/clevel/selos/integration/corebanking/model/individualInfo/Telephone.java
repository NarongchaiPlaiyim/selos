package com.clevel.selos.integration.corebanking.model.individualInfo;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Telephone implements Serializable {

    private String telephoneNumber;
    private String extension;
    private String telephoneType;

    public Telephone() {

    }

    public Telephone(String telephoneNumber, String extension, String telephoneType) {
        this.telephoneNumber = telephoneNumber;
        this.extension = extension;
        this.telephoneType = telephoneType;
    }


    public String getTelephoneType() {
        return telephoneType;
    }

    public void setTelephoneType(String telephoneType) {
        this.telephoneType = telephoneType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("telephoneNumber", telephoneNumber)
                .append("extension", extension)
                .append("telephoneType", telephoneType)
                .toString();
    }
}
