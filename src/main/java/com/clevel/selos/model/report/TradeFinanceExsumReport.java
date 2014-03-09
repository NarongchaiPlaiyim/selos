package com.clevel.selos.model.report;

public class TradeFinanceExsumReport {

    private String contactName;
    private String contactPhoneNo;
    private String interService;
    private String currentAddress;
    private String importMail;
    private String exportMail;
    private String depositBranchCode;
    private String ownerBranchCode;


    public TradeFinanceExsumReport() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getInterService() {
        return interService;
    }

    public void setInterService(String interService) {
        this.interService = interService;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getImportMail() {
        return importMail;
    }

    public void setImportMail(String importMail) {
        this.importMail = importMail;
    }

    public String getExportMail() {
        return exportMail;
    }

    public void setExportMail(String exportMail) {
        this.exportMail = exportMail;
    }

    public String getDepositBranchCode() {
        return depositBranchCode;
    }

    public void setDepositBranchCode(String depositBranchCode) {
        this.depositBranchCode = depositBranchCode;
    }

    public String getOwnerBranchCode() {
        return ownerBranchCode;
    }

    public void setOwnerBranchCode(String ownerBranchCode) {
        this.ownerBranchCode = ownerBranchCode;
    }

    @Override
    public String toString() {
        return "TradeFinanceExsumReport{" +
                "contactName='" + contactName + '\'' +
                ", contactPhoneNo='" + contactPhoneNo + '\'' +
                ", interService='" + interService + '\'' +
                ", currentAddress='" + currentAddress + '\'' +
                ", importMail='" + importMail + '\'' +
                ", exportMail='" + exportMail + '\'' +
                ", depositBranchCode='" + depositBranchCode + '\'' +
                ", ownerBranchCode='" + ownerBranchCode + '\'' +
                '}';
    }
}
