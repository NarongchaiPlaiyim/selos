package com.clevel.selos.model.view;

import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProposeLineView implements Serializable {
    private long id;

    private BigDecimal wcNeed;
    private BigDecimal totalWCTmb;
    private BigDecimal wcNeedDiffer;
    private BigDecimal totalWCDebit;
    private BigDecimal totalLoanWCTMB;  //Existing Core W/Loan Credit limit with TMB //Do not use In Page

    private BigDecimal case1WCLimit;
    private BigDecimal case1WCMinLimit;
    private BigDecimal case1WC50CoreWC;
    private BigDecimal case1WCDebitCoreWC;

    private BigDecimal case2WCLimit;
    private BigDecimal case2WCMinLimit;
    private BigDecimal case2WC50CoreWC;
    private BigDecimal case2WCDebitCoreWC;

    private BigDecimal case3WCLimit;
    private BigDecimal case3WCMinLimit;
    private BigDecimal case3WC50CoreWC;
    private BigDecimal case3WCDebitCoreWC;

    private CreditCustomerType creditCustomerType;
    private CreditRequestTypeView loanRequestType;
    private CountryView investedCountry;
    private BigDecimal existingSMELimit;
    private BigDecimal maximumSMELimit;

    private String contactName;
    private String contactPhoneNo;
    private String interService;
    private String currentAddress;
    private String registeredAddress;
    private String importMail;
    private String exportMail;
    private String depositBranchCode;
    private String ownerBranchCode;

    private BigDecimal intFeeDOA;
    private BigDecimal frontendFeeDOA;
    private BigDecimal guarantorBA;
    private String reasonForReduction;

    private int relatedTMBLending;
    private int twentyFivePercentShareRelatedTMBLending;
    private int singleLendingLimit;

    private BigDecimal totalPropose;
    private BigDecimal totalCommercial;
    private BigDecimal totalCommercialAndOBOD;
    private BigDecimal totalExposure;

    private BigDecimal totalGuaranteeAmount;

    private BigDecimal totalProposeLoanDBR;
    private BigDecimal totalProposeNonLoanDBR;
    private BigDecimal totalNumberOfNewOD;
    private BigDecimal totalNumberProposeCreditFac;
    private BigDecimal totalNumberContingenPropose;
    private BigDecimal totalNumberOfCoreAsset;
    private BigDecimal totalNumberOfNonCoreAsset;
    private BigDecimal totalMortgageValue;
    private BigDecimal totalTCGGuaranteeAmount;
    private BigDecimal totalIndvGuaranteeAmount;
    private BigDecimal totalJurisGuaranteeAmount;

    private List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList;
    private List<ProposeConditionInfoView> proposeConditionInfoViewList;
    private List<ProposeGuarantorInfoView> proposeGuarantorInfoViewList;
    private List<ProposeCollateralInfoView> proposeCollateralInfoViewList;
    private List<ProposeFeeDetailView> proposeFeeDetailViewList; // for only show in screen
    private List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList; // have all proposeFeeDetail
    private List<FeeDetailView> proposeAppFeeDetailViewList; // appFeeDetail

    private List<Long> deleteCreditIdList;
    private List<Long> deleteConditionIdList;
    private List<Long> deleteGuarantorIdList;
    private List<Long> deleteCollateralIdList;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public ProposeLineView() {
        reset();
    }

    public void reset() {
        this.wcNeed = BigDecimal.ZERO;
        this.totalWCTmb = BigDecimal.ZERO;
        this.wcNeedDiffer = BigDecimal.ZERO;
        this.totalWCDebit = BigDecimal.ZERO;
        this.totalLoanWCTMB = BigDecimal.ZERO;
        this.case1WCLimit = BigDecimal.ZERO;
        this.case1WCMinLimit = BigDecimal.ZERO;
        this.case1WC50CoreWC = BigDecimal.ZERO;
        this.case1WCDebitCoreWC = BigDecimal.ZERO;
        this.case2WCLimit = BigDecimal.ZERO;
        this.case2WCMinLimit = BigDecimal.ZERO;
        this.case2WC50CoreWC = BigDecimal.ZERO;
        this.case2WCDebitCoreWC = BigDecimal.ZERO;
        this.case3WCLimit = BigDecimal.ZERO;
        this.case3WCMinLimit = BigDecimal.ZERO;
        this.case3WC50CoreWC = BigDecimal.ZERO;
        this.case3WCDebitCoreWC = BigDecimal.ZERO;
        this.loanRequestType = new CreditRequestTypeView();
        this.investedCountry = new CountryView();
        this.creditCustomerType = CreditCustomerType.NOT_SELECTED;
        this.existingSMELimit = BigDecimal.ZERO;
        this.maximumSMELimit = BigDecimal.ZERO;
        this.contactName = "";
        this.contactPhoneNo = "";
        this.interService = "";
        this.currentAddress = "";
        this.registeredAddress = "";
        this.importMail = "";
        this.exportMail = "";
        this.depositBranchCode = "";
        this.ownerBranchCode = "";
        this.intFeeDOA = BigDecimal.ZERO;
        this.frontendFeeDOA = BigDecimal.ZERO;
        this.guarantorBA = BigDecimal.ZERO;
        this.reasonForReduction = "";
        this.relatedTMBLending = 0;
        this.twentyFivePercentShareRelatedTMBLending = 0;
        this.singleLendingLimit = 0;
        this.totalPropose = BigDecimal.ZERO;
        this.totalCommercial = BigDecimal.ZERO;
        this.totalCommercialAndOBOD = BigDecimal.ZERO;
        this.totalExposure = BigDecimal.ZERO;
        this.totalGuaranteeAmount = BigDecimal.ZERO;

        this.proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
        this.proposeConditionInfoViewList = new ArrayList<ProposeConditionInfoView>();
        this.proposeGuarantorInfoViewList = new ArrayList<ProposeGuarantorInfoView>();
        this.proposeCollateralInfoViewList = new ArrayList<ProposeCollateralInfoView>();

        this.proposeFeeDetailViewList = new ArrayList<ProposeFeeDetailView>();
        this.proposeFeeDetailViewOriginalList = new ArrayList<ProposeFeeDetailView>();
        this.proposeAppFeeDetailViewList = new ArrayList<FeeDetailView>();

        this.deleteCreditIdList = new ArrayList<Long>();
        this.deleteConditionIdList = new ArrayList<Long>();
        this.deleteGuarantorIdList = new ArrayList<Long>();
        this.deleteCollateralIdList = new ArrayList<Long>();

        this.totalProposeLoanDBR = BigDecimal.ZERO;
        this.totalProposeNonLoanDBR = BigDecimal.ZERO;

        this.totalNumberOfNewOD = BigDecimal.ZERO;
        this.totalNumberProposeCreditFac = BigDecimal.ZERO;
        this.totalNumberContingenPropose = BigDecimal.ZERO;
        this.totalNumberOfCoreAsset = BigDecimal.ZERO;
        this.totalNumberOfNonCoreAsset = BigDecimal.ZERO;
        this.totalMortgageValue = BigDecimal.ZERO;
        this.totalTCGGuaranteeAmount = BigDecimal.ZERO;
        this.totalIndvGuaranteeAmount = BigDecimal.ZERO;
        this.totalJurisGuaranteeAmount = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getWcNeed() {
        return wcNeed;
    }

    public void setWcNeed(BigDecimal wcNeed) {
        this.wcNeed = wcNeed;
    }

    public BigDecimal getTotalWCTmb() {
        return totalWCTmb;
    }

    public void setTotalWCTmb(BigDecimal totalWCTmb) {
        this.totalWCTmb = totalWCTmb;
    }

    public BigDecimal getWcNeedDiffer() {
        return wcNeedDiffer;
    }

    public void setWcNeedDiffer(BigDecimal wcNeedDiffer) {
        this.wcNeedDiffer = wcNeedDiffer;
    }

    public BigDecimal getTotalWCDebit() {
        return totalWCDebit;
    }

    public void setTotalWCDebit(BigDecimal totalWCDebit) {
        this.totalWCDebit = totalWCDebit;
    }

    public BigDecimal getTotalLoanWCTMB() {
        return totalLoanWCTMB;
    }

    public void setTotalLoanWCTMB(BigDecimal totalLoanWCTMB) {
        this.totalLoanWCTMB = totalLoanWCTMB;
    }

    public BigDecimal getCase1WCLimit() {
        return case1WCLimit;
    }

    public void setCase1WCLimit(BigDecimal case1WCLimit) {
        this.case1WCLimit = case1WCLimit;
    }

    public BigDecimal getCase1WCMinLimit() {
        return case1WCMinLimit;
    }

    public void setCase1WCMinLimit(BigDecimal case1WCMinLimit) {
        this.case1WCMinLimit = case1WCMinLimit;
    }

    public BigDecimal getCase1WC50CoreWC() {
        return case1WC50CoreWC;
    }

    public void setCase1WC50CoreWC(BigDecimal case1WC50CoreWC) {
        this.case1WC50CoreWC = case1WC50CoreWC;
    }

    public BigDecimal getCase1WCDebitCoreWC() {
        return case1WCDebitCoreWC;
    }

    public void setCase1WCDebitCoreWC(BigDecimal case1WCDebitCoreWC) {
        this.case1WCDebitCoreWC = case1WCDebitCoreWC;
    }

    public BigDecimal getCase2WCLimit() {
        return case2WCLimit;
    }

    public void setCase2WCLimit(BigDecimal case2WCLimit) {
        this.case2WCLimit = case2WCLimit;
    }

    public BigDecimal getCase2WCMinLimit() {
        return case2WCMinLimit;
    }

    public void setCase2WCMinLimit(BigDecimal case2WCMinLimit) {
        this.case2WCMinLimit = case2WCMinLimit;
    }

    public BigDecimal getCase2WC50CoreWC() {
        return case2WC50CoreWC;
    }

    public void setCase2WC50CoreWC(BigDecimal case2WC50CoreWC) {
        this.case2WC50CoreWC = case2WC50CoreWC;
    }

    public BigDecimal getCase2WCDebitCoreWC() {
        return case2WCDebitCoreWC;
    }

    public void setCase2WCDebitCoreWC(BigDecimal case2WCDebitCoreWC) {
        this.case2WCDebitCoreWC = case2WCDebitCoreWC;
    }

    public BigDecimal getCase3WCLimit() {
        return case3WCLimit;
    }

    public void setCase3WCLimit(BigDecimal case3WCLimit) {
        this.case3WCLimit = case3WCLimit;
    }

    public BigDecimal getCase3WCMinLimit() {
        return case3WCMinLimit;
    }

    public void setCase3WCMinLimit(BigDecimal case3WCMinLimit) {
        this.case3WCMinLimit = case3WCMinLimit;
    }

    public BigDecimal getCase3WC50CoreWC() {
        return case3WC50CoreWC;
    }

    public void setCase3WC50CoreWC(BigDecimal case3WC50CoreWC) {
        this.case3WC50CoreWC = case3WC50CoreWC;
    }

    public BigDecimal getCase3WCDebitCoreWC() {
        return case3WCDebitCoreWC;
    }

    public void setCase3WCDebitCoreWC(BigDecimal case3WCDebitCoreWC) {
        this.case3WCDebitCoreWC = case3WCDebitCoreWC;
    }

    public CreditCustomerType getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(CreditCustomerType creditCustomerType) {
        this.creditCustomerType = creditCustomerType;
    }

    public CreditRequestTypeView getLoanRequestType() {
        return loanRequestType;
    }

    public void setLoanRequestType(CreditRequestTypeView loanRequestType) {
        this.loanRequestType = loanRequestType;
    }

    public CountryView getInvestedCountry() {
        return investedCountry;
    }

    public void setInvestedCountry(CountryView investedCountry) {
        this.investedCountry = investedCountry;
    }

    public BigDecimal getExistingSMELimit() {
        return existingSMELimit;
    }

    public void setExistingSMELimit(BigDecimal existingSMELimit) {
        this.existingSMELimit = existingSMELimit;
    }

    public BigDecimal getMaximumSMELimit() {
        return maximumSMELimit;
    }

    public void setMaximumSMELimit(BigDecimal maximumSMELimit) {
        this.maximumSMELimit = maximumSMELimit;
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

    public int getSingleLendingLimit() {
        return singleLendingLimit;
    }

    public void setSingleLendingLimit(int singleLendingLimit) {
        this.singleLendingLimit = singleLendingLimit;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditInfoDetailViewList() {
        return proposeCreditInfoDetailViewList;
    }

    public void setProposeCreditInfoDetailViewList(List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList) {
        this.proposeCreditInfoDetailViewList = proposeCreditInfoDetailViewList;
    }

    public List<Long> getDeleteCreditIdList() {
        return deleteCreditIdList;
    }

    public void setDeleteCreditIdList(List<Long> deleteCreditIdList) {
        this.deleteCreditIdList = deleteCreditIdList;
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

    public List<ProposeConditionInfoView> getProposeConditionInfoViewList() {
        return proposeConditionInfoViewList;
    }

    public void setProposeConditionInfoViewList(List<ProposeConditionInfoView> proposeConditionInfoViewList) {
        this.proposeConditionInfoViewList = proposeConditionInfoViewList;
    }

    public List<Long> getDeleteConditionIdList() {
        return deleteConditionIdList;
    }

    public void setDeleteConditionIdList(List<Long> deleteConditionIdList) {
        this.deleteConditionIdList = deleteConditionIdList;
    }

    public List<ProposeGuarantorInfoView> getProposeGuarantorInfoViewList() {
        return proposeGuarantorInfoViewList;
    }

    public void setProposeGuarantorInfoViewList(List<ProposeGuarantorInfoView> proposeGuarantorInfoViewList) {
        this.proposeGuarantorInfoViewList = proposeGuarantorInfoViewList;
    }

    public List<Long> getDeleteGuarantorIdList() {
        return deleteGuarantorIdList;
    }

    public void setDeleteGuarantorIdList(List<Long> deleteGuarantorIdList) {
        this.deleteGuarantorIdList = deleteGuarantorIdList;
    }

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
    }

    public List<ProposeFeeDetailView> getProposeFeeDetailViewList() {
        return proposeFeeDetailViewList;
    }

    public void setProposeFeeDetailViewList(List<ProposeFeeDetailView> proposeFeeDetailViewList) {
        this.proposeFeeDetailViewList = proposeFeeDetailViewList;
    }

    public List<ProposeFeeDetailView> getProposeFeeDetailViewOriginalList() {
        return proposeFeeDetailViewOriginalList;
    }

    public void setProposeFeeDetailViewOriginalList(List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList) {
        this.proposeFeeDetailViewOriginalList = proposeFeeDetailViewOriginalList;
    }

    public List<ProposeCollateralInfoView> getProposeCollateralInfoViewList() {
        return proposeCollateralInfoViewList;
    }

    public void setProposeCollateralInfoViewList(List<ProposeCollateralInfoView> proposeCollateralInfoViewList) {
        this.proposeCollateralInfoViewList = proposeCollateralInfoViewList;
    }

    public List<Long> getDeleteCollateralIdList() {
        return deleteCollateralIdList;
    }

    public void setDeleteCollateralIdList(List<Long> deleteCollateralIdList) {
        this.deleteCollateralIdList = deleteCollateralIdList;
    }

    public BigDecimal getTotalProposeLoanDBR() {
        return totalProposeLoanDBR;
    }

    public void setTotalProposeLoanDBR(BigDecimal totalProposeLoanDBR) {
        this.totalProposeLoanDBR = totalProposeLoanDBR;
    }

    public BigDecimal getTotalProposeNonLoanDBR() {
        return totalProposeNonLoanDBR;
    }

    public void setTotalProposeNonLoanDBR(BigDecimal totalProposeNonLoanDBR) {
        this.totalProposeNonLoanDBR = totalProposeNonLoanDBR;
    }

    public BigDecimal getTotalJurisGuaranteeAmount() {
        return totalJurisGuaranteeAmount;
    }

    public void setTotalJurisGuaranteeAmount(BigDecimal totalJurisGuaranteeAmount) {
        this.totalJurisGuaranteeAmount = totalJurisGuaranteeAmount;
    }

    public BigDecimal getTotalIndvGuaranteeAmount() {
        return totalIndvGuaranteeAmount;
    }

    public void setTotalIndvGuaranteeAmount(BigDecimal totalIndvGuaranteeAmount) {
        this.totalIndvGuaranteeAmount = totalIndvGuaranteeAmount;
    }

    public BigDecimal getTotalNumberOfNewOD() {
        return totalNumberOfNewOD;
    }

    public void setTotalNumberOfNewOD(BigDecimal totalNumberOfNewOD) {
        this.totalNumberOfNewOD = totalNumberOfNewOD;
    }

    public BigDecimal getTotalNumberProposeCreditFac() {
        return totalNumberProposeCreditFac;
    }

    public void setTotalNumberProposeCreditFac(BigDecimal totalNumberProposeCreditFac) {
        this.totalNumberProposeCreditFac = totalNumberProposeCreditFac;
    }

    public BigDecimal getTotalNumberContingenPropose() {
        return totalNumberContingenPropose;
    }

    public void setTotalNumberContingenPropose(BigDecimal totalNumberContingenPropose) {
        this.totalNumberContingenPropose = totalNumberContingenPropose;
    }

    public BigDecimal getTotalNumberOfCoreAsset() {
        return totalNumberOfCoreAsset;
    }

    public void setTotalNumberOfCoreAsset(BigDecimal totalNumberOfCoreAsset) {
        this.totalNumberOfCoreAsset = totalNumberOfCoreAsset;
    }

    public BigDecimal getTotalNumberOfNonCoreAsset() {
        return totalNumberOfNonCoreAsset;
    }

    public void setTotalNumberOfNonCoreAsset(BigDecimal totalNumberOfNonCoreAsset) {
        this.totalNumberOfNonCoreAsset = totalNumberOfNonCoreAsset;
    }

    public BigDecimal getTotalMortgageValue() {
        return totalMortgageValue;
    }

    public void setTotalMortgageValue(BigDecimal totalMortgageValue) {
        this.totalMortgageValue = totalMortgageValue;
    }

    public BigDecimal getTotalTCGGuaranteeAmount() {
        return totalTCGGuaranteeAmount;
    }

    public void setTotalTCGGuaranteeAmount(BigDecimal totalTCGGuaranteeAmount) {
        this.totalTCGGuaranteeAmount = totalTCGGuaranteeAmount;
    }

    public List<FeeDetailView> getProposeAppFeeDetailViewList() {
        return proposeAppFeeDetailViewList;
    }

    public void setProposeAppFeeDetailViewList(List<FeeDetailView> proposeAppFeeDetailViewList) {
        this.proposeAppFeeDetailViewList = proposeAppFeeDetailViewList;
    }
}
