package com.clevel.selos.integration.rlos.csi.model;

import com.clevel.selos.integration.RLOSInterface;

public class IdModel {
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
}
