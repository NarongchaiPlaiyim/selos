package com.clevel.selos.model.view;

import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreditFacProposeView implements Serializable {
    private BigDecimal WCNeed;
    private BigDecimal totalCreditTurnover;
    private BigDecimal WCNeedDiffer;
    private BigDecimal totalWcDebit;

    private BigDecimal case1WcLimit;
    private BigDecimal case1WcMinLimit;
    private BigDecimal case1Wc50CoreWc;
    private BigDecimal case1WcDebitCoreWc;

    private BigDecimal case2WcLimit;
    private BigDecimal case2WcMinLimit;
    private BigDecimal case2Wc50CoreWc;
    private BigDecimal case2WcDebitCoreWc;

    private BigDecimal case3WcLimit;
    private BigDecimal case3WcMinLimit;
    private BigDecimal case3Wc50CoreWc;
    private BigDecimal case3WcDebitCoreWc;

    private BigDecimal existingSMELimit;
    private BigDecimal maximumExistingSMELimit;

    private BigDecimal totalPropose;
    private BigDecimal totalProposeLoanDBR;
    private BigDecimal totalProposeNonLoanDBR;
    private BigDecimal totalCommercial;
    private BigDecimal totalCommercialAndOBOD;
    private BigDecimal totalExposure;

    private String contactName;
    private String contactPhoneNo;
    private String interService;
    private String currentAddress;
    private String registeredAddress;
    private String emailAddress;
    private String importMail;
    private String exportMail;
    private String depositBranchCode;
    private String ownerBranchCode;

    private BigDecimal intFeeDOA;
    private BigDecimal frontendFeeDOA;
    private BigDecimal guarantorBA;
    private String reasonForReduction;

    private CreditCustomerType creditCustomerType;
    private CreditRequestType  creditRequestType;
    private Country country;

    private List<ProposeFeeDetailView> proposeFeeDetailViewList;
    private List<ProposeCreditDetailView> proposeCreditDetailViewList;
    private List<ProposeCollateralInfoView> proposeCollateralInfoViewList;
    private List<ProposeGuarantorDetailView> proposeGuarantorDetailViewList;
    private BigDecimal totalGuaranteeAmount;
    private List<ProposeConditionDetailView> proposeConditionDetailViewList;

    private int relatedTMBLending;
    private int twentyFivePercentShareRelatedTMBLending;
    private int singleLendingLimit;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public CreditFacProposeView() {
        reset();
    }

    public void reset() {
        this.WCNeed = BigDecimal.ZERO;
        this.totalCreditTurnover = BigDecimal.ZERO;
        this.WCNeedDiffer = BigDecimal.ZERO;
        this.totalWcDebit = BigDecimal.ZERO;
        this.case1WcLimit = BigDecimal.ZERO;
        this.case1WcMinLimit = BigDecimal.ZERO;
        this.case1Wc50CoreWc = BigDecimal.ZERO;
        this.case1WcDebitCoreWc = BigDecimal.ZERO;
        this.case2WcLimit = BigDecimal.ZERO;
        this.case2WcMinLimit = BigDecimal.ZERO;
        this.case2Wc50CoreWc = BigDecimal.ZERO;
        this.case2WcDebitCoreWc = BigDecimal.ZERO;
        this.case3WcLimit = BigDecimal.ZERO;
        this.case3WcMinLimit = BigDecimal.ZERO;
        this.case3Wc50CoreWc = BigDecimal.ZERO;
        this.case3WcDebitCoreWc = BigDecimal.ZERO;
        this.existingSMELimit = BigDecimal.ZERO;
        this.maximumExistingSMELimit = BigDecimal.ZERO;

        this.totalPropose = BigDecimal.ZERO;
        this.totalProposeLoanDBR = BigDecimal.ZERO;
        this.totalProposeNonLoanDBR = BigDecimal.ZERO;
        this.totalCommercial = BigDecimal.ZERO;
        this.totalCommercialAndOBOD = BigDecimal.ZERO;
        this.totalExposure = BigDecimal.ZERO;

        this.contactName = "";
        this.contactPhoneNo = "";
        this.interService = "";
        this.currentAddress = "";
        this.registeredAddress = "";
        this.emailAddress = "";
        this.importMail = "";
        this.exportMail = "";
        this.depositBranchCode = "";
        this.ownerBranchCode = "";

        this.intFeeDOA = BigDecimal.ZERO;
        this.frontendFeeDOA = BigDecimal.ZERO;
        this.guarantorBA = BigDecimal.ZERO;
        this.reasonForReduction = "";

        this.proposeFeeDetailViewList = new ArrayList<ProposeFeeDetailView>();
        this.proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
        this.proposeCollateralInfoViewList = new ArrayList<ProposeCollateralInfoView>();
        this.proposeGuarantorDetailViewList = new ArrayList<ProposeGuarantorDetailView>();
        this.totalGuaranteeAmount = BigDecimal.ZERO;
        this.proposeConditionDetailViewList = new ArrayList<ProposeConditionDetailView>();

        this.relatedTMBLending = 0;
        this.twentyFivePercentShareRelatedTMBLending = 0;
        this.singleLendingLimit = 0;

        this.creditRequestType = new CreditRequestType();
        this.country = new Country();

    }

    public BigDecimal getTotalProposeNonLoanDBR() {
        return totalProposeNonLoanDBR;
    }

    public void setTotalProposeNonLoanDBR(BigDecimal totalProposeNonLoanDBR) {
        this.totalProposeNonLoanDBR = totalProposeNonLoanDBR;
    }

    public BigDecimal getTotalProposeLoanDBR() {
        return totalProposeLoanDBR;
    }

    public void setTotalProposeLoanDBR(BigDecimal totalProposeLoanDBR) {
        this.totalProposeLoanDBR = totalProposeLoanDBR;
    }

    public BigDecimal getWCNeed() {
        return WCNeed;
    }

    public void setWCNeed(BigDecimal WCNeed) {
        this.WCNeed = WCNeed;
    }

    public BigDecimal getTotalCreditTurnover() {
        return totalCreditTurnover;
    }

    public void setTotalCreditTurnover(BigDecimal totalCreditTurnover) {
        this.totalCreditTurnover = totalCreditTurnover;
    }

    public BigDecimal getWCNeedDiffer() {
        return WCNeedDiffer;
    }

    public void setWCNeedDiffer(BigDecimal WCNeedDiffer) {
        this.WCNeedDiffer = WCNeedDiffer;
    }

    public BigDecimal getTotalWcDebit() {
        return totalWcDebit;
    }

    public void setTotalWcDebit(BigDecimal totalWcDebit) {
        this.totalWcDebit = totalWcDebit;
    }

    public BigDecimal getCase1WcLimit() {
        return case1WcLimit;
    }

    public void setCase1WcLimit(BigDecimal case1WcLimit) {
        this.case1WcLimit = case1WcLimit;
    }

    public BigDecimal getCase1WcMinLimit() {
        return case1WcMinLimit;
    }

    public void setCase1WcMinLimit(BigDecimal case1WcMinLimit) {
        this.case1WcMinLimit = case1WcMinLimit;
    }

    public BigDecimal getCase1Wc50CoreWc() {
        return case1Wc50CoreWc;
    }

    public void setCase1Wc50CoreWc(BigDecimal case1Wc50CoreWc) {
        this.case1Wc50CoreWc = case1Wc50CoreWc;
    }

    public BigDecimal getCase1WcDebitCoreWc() {
        return case1WcDebitCoreWc;
    }

    public void setCase1WcDebitCoreWc(BigDecimal case1WcDebitCoreWc) {
        this.case1WcDebitCoreWc = case1WcDebitCoreWc;
    }

    public BigDecimal getCase2WcLimit() {
        return case2WcLimit;
    }

    public void setCase2WcLimit(BigDecimal case2WcLimit) {
        this.case2WcLimit = case2WcLimit;
    }

    public BigDecimal getCase2WcMinLimit() {
        return case2WcMinLimit;
    }

    public void setCase2WcMinLimit(BigDecimal case2WcMinLimit) {
        this.case2WcMinLimit = case2WcMinLimit;
    }

    public BigDecimal getCase2Wc50CoreWc() {
        return case2Wc50CoreWc;
    }

    public void setCase2Wc50CoreWc(BigDecimal case2Wc50CoreWc) {
        this.case2Wc50CoreWc = case2Wc50CoreWc;
    }

    public BigDecimal getCase2WcDebitCoreWc() {
        return case2WcDebitCoreWc;
    }

    public void setCase2WcDebitCoreWc(BigDecimal case2WcDebitCoreWc) {
        this.case2WcDebitCoreWc = case2WcDebitCoreWc;
    }

    public BigDecimal getCase3WcLimit() {
        return case3WcLimit;
    }

    public void setCase3WcLimit(BigDecimal case3WcLimit) {
        this.case3WcLimit = case3WcLimit;
    }

    public BigDecimal getCase3WcMinLimit() {
        return case3WcMinLimit;
    }

    public void setCase3WcMinLimit(BigDecimal case3WcMinLimit) {
        this.case3WcMinLimit = case3WcMinLimit;
    }

    public BigDecimal getCase3Wc50CoreWc() {
        return case3Wc50CoreWc;
    }

    public void setCase3Wc50CoreWc(BigDecimal case3Wc50CoreWc) {
        this.case3Wc50CoreWc = case3Wc50CoreWc;
    }

    public BigDecimal getCase3WcDebitCoreWc() {
        return case3WcDebitCoreWc;
    }

    public void setCase3WcDebitCoreWc(BigDecimal case3WcDebitCoreWc) {
        this.case3WcDebitCoreWc = case3WcDebitCoreWc;
    }

    public List<ProposeCreditDetailView> getProposeCreditDetailViewList() {
        return proposeCreditDetailViewList;
    }

    public void setProposeCreditDetailViewList(List<ProposeCreditDetailView> proposeCreditDetailViewList) {
        this.proposeCreditDetailViewList = proposeCreditDetailViewList;
    }

    public List<ProposeCollateralInfoView> getProposeCollateralInfoViewList() {
        return proposeCollateralInfoViewList;
    }

    public void setProposeCollateralInfoViewList(List<ProposeCollateralInfoView> proposeCollateralInfoViewList) {
        this.proposeCollateralInfoViewList = proposeCollateralInfoViewList;
    }

    public BigDecimal getExistingSMELimit() {
        return existingSMELimit;
    }

    public void setExistingSMELimit(BigDecimal existingSMELimit) {
        this.existingSMELimit = existingSMELimit;
    }

    public BigDecimal getMaximumExistingSMELimit() {
        return maximumExistingSMELimit;
    }

    public void setMaximumExistingSMELimit(BigDecimal maximumExistingSMELimit) {
        this.maximumExistingSMELimit = maximumExistingSMELimit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public BigDecimal getTotalPropose() {
        return totalPropose;
    }

    public void setTotalPropose(BigDecimal totalPropose) {
        this.totalPropose = totalPropose;
    }

    public BigDecimal getTotalCommercial() {
        return totalCommercial;
    }

    public void setTotalCommercial(BigDecimal totalCommercial) {
        this.totalCommercial = totalCommercial;
    }

    public BigDecimal getTotalCommercialAndOBOD() {
        return totalCommercialAndOBOD;
    }

    public void setTotalCommercialAndOBOD(BigDecimal totalCommercialAndOBOD) {
        this.totalCommercialAndOBOD = totalCommercialAndOBOD;
    }

    public BigDecimal getTotalExposure() {
        return totalExposure;
    }

    public void setTotalExposure(BigDecimal totalExposure) {
        this.totalExposure = totalExposure;
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

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public BigDecimal getIntFeeDOA() {
        return intFeeDOA;
    }

    public void setIntFeeDOA(BigDecimal intFeeDOA) {
        this.intFeeDOA = intFeeDOA;
    }

    public BigDecimal getFrontendFeeDOA() {
        return frontendFeeDOA;
    }

    public void setFrontendFeeDOA(BigDecimal frontendFeeDOA) {
        this.frontendFeeDOA = frontendFeeDOA;
    }

    public BigDecimal getGuarantorBA() {
        return guarantorBA;
    }

    public void setGuarantorBA(BigDecimal guarantorBA) {
        this.guarantorBA = guarantorBA;
    }

    public String getReasonForReduction() {
        return reasonForReduction;
    }

    public void setReasonForReduction(String reasonForReduction) {
        this.reasonForReduction = reasonForReduction;
    }

    public List<ProposeFeeDetailView> getProposeFeeDetailViewList() {
        return proposeFeeDetailViewList;
    }

    public void setProposeFeeDetailViewList(List<ProposeFeeDetailView> proposeFeeDetailViewList) {
        this.proposeFeeDetailViewList = proposeFeeDetailViewList;
    }

    public List<ProposeGuarantorDetailView> getProposeGuarantorDetailViewList() {
        return proposeGuarantorDetailViewList;
    }

    public void setProposeGuarantorDetailViewList(List<ProposeGuarantorDetailView> proposeGuarantorDetailViewList) {
        this.proposeGuarantorDetailViewList = proposeGuarantorDetailViewList;
    }

    public List<ProposeConditionDetailView> getProposeConditionDetailViewList() {
        return proposeConditionDetailViewList;
    }

    public void setProposeConditionDetailViewList(List<ProposeConditionDetailView> proposeConditionDetailViewList) {
        this.proposeConditionDetailViewList = proposeConditionDetailViewList;
    }

    public int getSingleLendingLimit() {
        return singleLendingLimit;
    }

    public void setSingleLendingLimit(int singleLendingLimit) {
        this.singleLendingLimit = singleLendingLimit;
    }

    public int getRelatedTMBLending() {
        return relatedTMBLending;
    }

    public void setRelatedTMBLending(int relatedTMBLending) {
        this.relatedTMBLending = relatedTMBLending;
    }

    public int getTwentyFivePercentShareRelatedTMBLending() {
        return twentyFivePercentShareRelatedTMBLending;
    }

    public void setTwentyFivePercentShareRelatedTMBLending(int twentyFivePercentShareRelatedTMBLending) {
        this.twentyFivePercentShareRelatedTMBLending = twentyFivePercentShareRelatedTMBLending;
    }

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
    }

    public CreditCustomerType getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(CreditCustomerType creditCustomerType) {
        this.creditCustomerType = creditCustomerType;
    }

    public CreditRequestType getCreditRequestType() {
        return creditRequestType;
    }

    public void setCreditRequestType(CreditRequestType creditRequestType) {
        this.creditRequestType = creditRequestType;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
