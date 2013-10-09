package com.clevel.selos.integration.rlos.csi.model;

import com.clevel.selos.integration.RLOSInterface;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class IdModel implements Serializable {
    private RLOSInterface.DocumentType documentType;
    private String idNumber;

    public RLOSInterface.DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(RLOSInterface.DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("documentType", documentType)
                .append("idNumber", idNumber)
                .toString();
    }
}
