package com.clevel.selos.model.view;

import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditFacilityView implements Serializable {
    private long id;
    private BigDecimal WCNeed;
    private BigDecimal totalWcTmb;
    private BigDecimal WCNeedDiffer;
    private BigDecimal totalWcDebit;
    private BigDecimal totalLoanWCTMB;  //Existing Core W/Loan Credit limit with TMB

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
    private BigDecimal maximumSMELimit;

    private BigDecimal totalPropose;
    private BigDecimal totalProposeLoanDBR;
    private BigDecimal totalProposeNonLoanDBR;
    private BigDecimal totalCommercial;
    private BigDecimal totalCommercialAndOBOD;
    private BigDecimal totalExposure;
    private BigDecimal totalNumberOfNewOD;
    private BigDecimal totalNumberProposeCreditFac;
    private BigDecimal totalNumberContingenPropose;
    private BigDecimal totalNumberOfCoreAsset;
    private BigDecimal totalNumberOfNonCoreAsset;
    private BigDecimal totalMortgageValue;
    private BigDecimal totalGuaranteeAmount;
    private BigDecimal totalTCGGuaranteeAmount;
    private BigDecimal totalIndvGuaranteeAmount;
    private BigDecimal totalJurisGuaranteeAmount;

    // Approve
    private BigDecimal totalApproveCredit;
    private BigDecimal totalApproveCommercial;
    private BigDecimal totalApproveComAndOBOD;
    private BigDecimal totalApproveExposure;
    private BigDecimal totalApproveNumOfNewOD;
    private BigDecimal totalApproveNumProposeCreditFac;
    private BigDecimal totalApproveNumContingenPropose;
    private BigDecimal totalApproveNumOfCoreAsset;
    private BigDecimal totalApproveNumOfNonCoreAsset;
    private BigDecimal totalApproveGuaranteeAmt;
    private BigDecimal totalApproveTCGGuaranteeAmt;
    private BigDecimal totalApproveIndiGuaranteeAmt;
    private BigDecimal totalApproveJurisGuaranteeAmt;

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
    private int creditCustomerType;
    private CreditRequestType  loanRequestType;
    private Country investedCountry;

    private int relatedTMBLending;
    private int twentyFivePercentShareRelatedTMBLending;
    private int singleLendingLimit;

    private List<NewFeeDetailView> newFeeDetailViewList;
    private List<NewCreditDetailView> newCreditDetailViewList;
    private List<NewCollateralView> newCollateralViewList;
    private List<NewGuarantorDetailView> newGuarantorDetailViewList;
    private List<NewConditionDetailView> newConditionDetailViewList;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public NewCreditFacilityView() {
        reset();
    }

    public void reset() {
        this.WCNeed = BigDecimal.ZERO;
        this.totalWcTmb = BigDecimal.ZERO;
        this.WCNeedDiffer = BigDecimal.ZERO;
        this.totalWcDebit = BigDecimal.ZERO;
        this.totalLoanWCTMB = BigDecimal.ZERO;
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
        this.maximumSMELimit = BigDecimal.ZERO;

        this.totalPropose = BigDecimal.ZERO;
        this.totalProposeLoanDBR = BigDecimal.ZERO;
        this.totalProposeNonLoanDBR = BigDecimal.ZERO;
        this.totalCommercial = BigDecimal.ZERO;
        this.totalCommercialAndOBOD = BigDecimal.ZERO;
        this.totalExposure = BigDecimal.ZERO;
        this.totalNumberOfNewOD = BigDecimal.ZERO;
        this.totalNumberProposeCreditFac = BigDecimal.ZERO;
        this.totalNumberContingenPropose=BigDecimal.ZERO;
        this.totalNumberOfCoreAsset = BigDecimal.ZERO;
        this.totalNumberOfNonCoreAsset = BigDecimal.ZERO;
        this.totalMortgageValue = BigDecimal.ZERO;
        this.totalGuaranteeAmount = BigDecimal.ZERO;
        this.totalTCGGuaranteeAmount = BigDecimal.ZERO;
        this.totalIndvGuaranteeAmount = BigDecimal.ZERO;
        this.totalJurisGuaranteeAmount = BigDecimal.ZERO;

        this.totalApproveCredit = BigDecimal.ZERO;
        this.totalApproveCommercial = BigDecimal.ZERO;
        this.totalApproveComAndOBOD = BigDecimal.ZERO;
        this.totalApproveExposure = BigDecimal.ZERO;
        this.totalApproveNumOfNewOD = BigDecimal.ZERO;
        this.totalApproveNumProposeCreditFac = BigDecimal.ZERO;
        this.totalApproveNumContingenPropose = BigDecimal.ZERO;
        this.totalApproveNumOfCoreAsset = BigDecimal.ZERO;
        this.totalApproveNumOfNonCoreAsset = BigDecimal.ZERO;
        this.totalApproveGuaranteeAmt = BigDecimal.ZERO;
        this.totalApproveTCGGuaranteeAmt = BigDecimal.ZERO;
        this.totalApproveIndiGuaranteeAmt = BigDecimal.ZERO;
        this.totalApproveJurisGuaranteeAmt = BigDecimal.ZERO;

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

        this.newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        this.newCreditDetailViewList = new ArrayList<NewCreditDetailView>();
        this.newCollateralViewList = new ArrayList<NewCollateralView>();
        this.newGuarantorDetailViewList = new ArrayList<NewGuarantorDetailView>();
        this.newConditionDetailViewList = new ArrayList<NewConditionDetailView>();

        this.relatedTMBLending = 0;
        this.twentyFivePercentShareRelatedTMBLending = 0;
        this.singleLendingLimit = 0;

        this.loanRequestType = new CreditRequestType();
        this.investedCountry = new Country();
        this.creditCustomerType = CreditCustomerType.NOT_SELECTED.value();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getWCNeed() {
        return WCNeed;
    }

    public void setWCNeed(BigDecimal WCNeed) {
        this.WCNeed = WCNeed;
    }

    public BigDecimal getTotalWcTmb() {
        return totalWcTmb;
    }

    public void setTotalWcTmb(BigDecimal totalWcTmb) {
        this.totalWcTmb = totalWcTmb;
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

    public BigDecimal getTotalLoanWCTMB() {
        return totalLoanWCTMB;
    }

    public void setTotalLoanWCTMB(BigDecimal totalLoanWCTMB) {
        this.totalLoanWCTMB = totalLoanWCTMB;
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

    public BigDecimal getTotalPropose() {
        return totalPropose;
    }

    public void setTotalPropose(BigDecimal totalPropose) {
        this.totalPropose = totalPropose;
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

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
    }

    public BigDecimal getTotalTCGGuaranteeAmount() {
        return totalTCGGuaranteeAmount;
    }

    public void setTotalTCGGuaranteeAmount(BigDecimal totalTCGGuaranteeAmount) {
        this.totalTCGGuaranteeAmount = totalTCGGuaranteeAmount;
    }

    public BigDecimal getTotalIndvGuaranteeAmount() {
        return totalIndvGuaranteeAmount;
    }

    public void setTotalIndvGuaranteeAmount(BigDecimal totalIndvGuaranteeAmount) {
        this.totalIndvGuaranteeAmount = totalIndvGuaranteeAmount;
    }

    public BigDecimal getTotalJurisGuaranteeAmount() {
        return totalJurisGuaranteeAmount;
    }

    public void setTotalJurisGuaranteeAmount(BigDecimal totalJurisGuaranteeAmount) {
        this.totalJurisGuaranteeAmount = totalJurisGuaranteeAmount;
    }

    public BigDecimal getTotalApproveCredit() {
        return totalApproveCredit;
    }

    public void setTotalApproveCredit(BigDecimal totalApproveCredit) {
        this.totalApproveCredit = totalApproveCredit;
    }

    public BigDecimal getTotalApproveCommercial() {
        return totalApproveCommercial;
    }

    public void setTotalApproveCommercial(BigDecimal totalApproveCommercial) {
        this.totalApproveCommercial = totalApproveCommercial;
    }

    public BigDecimal getTotalApproveComAndOBOD() {
        return totalApproveComAndOBOD;
    }

    public void setTotalApproveComAndOBOD(BigDecimal totalApproveComAndOBOD) {
        this.totalApproveComAndOBOD = totalApproveComAndOBOD;
    }

    public BigDecimal getTotalApproveExposure() {
        return totalApproveExposure;
    }

    public void setTotalApproveExposure(BigDecimal totalApproveExposure) {
        this.totalApproveExposure = totalApproveExposure;
    }

    public BigDecimal getTotalApproveNumOfNewOD() {
        return totalApproveNumOfNewOD;
    }

    public void setTotalApproveNumOfNewOD(BigDecimal totalApproveNumOfNewOD) {
        this.totalApproveNumOfNewOD = totalApproveNumOfNewOD;
    }

    public BigDecimal getTotalApproveNumProposeCreditFac() {
        return totalApproveNumProposeCreditFac;
    }

    public void setTotalApproveNumProposeCreditFac(BigDecimal totalApproveNumProposeCreditFac) {
        this.totalApproveNumProposeCreditFac = totalApproveNumProposeCreditFac;
    }

    public BigDecimal getTotalApproveNumContingenPropose() {
        return totalApproveNumContingenPropose;
    }

    public void setTotalApproveNumContingenPropose(BigDecimal totalApproveNumContingenPropose) {
        this.totalApproveNumContingenPropose = totalApproveNumContingenPropose;
    }

    public BigDecimal getTotalApproveNumOfCoreAsset() {
        return totalApproveNumOfCoreAsset;
    }

    public void setTotalApproveNumOfCoreAsset(BigDecimal totalApproveNumOfCoreAsset) {
        this.totalApproveNumOfCoreAsset = totalApproveNumOfCoreAsset;
    }

    public BigDecimal getTotalApproveNumOfNonCoreAsset() {
        return totalApproveNumOfNonCoreAsset;
    }

    public void setTotalApproveNumOfNonCoreAsset(BigDecimal totalApproveNumOfNonCoreAsset) {
        this.totalApproveNumOfNonCoreAsset = totalApproveNumOfNonCoreAsset;
    }

    public BigDecimal getTotalApproveGuaranteeAmt() {
        return totalApproveGuaranteeAmt;
    }

    public void setTotalApproveGuaranteeAmt(BigDecimal totalApproveGuaranteeAmt) {
        this.totalApproveGuaranteeAmt = totalApproveGuaranteeAmt;
    }

    public BigDecimal getTotalApproveTCGGuaranteeAmt() {
        return totalApproveTCGGuaranteeAmt;
    }

    public void setTotalApproveTCGGuaranteeAmt(BigDecimal totalApproveTCGGuaranteeAmt) {
        this.totalApproveTCGGuaranteeAmt = totalApproveTCGGuaranteeAmt;
    }

    public BigDecimal getTotalApproveIndiGuaranteeAmt() {
        return totalApproveIndiGuaranteeAmt;
    }

    public void setTotalApproveIndiGuaranteeAmt(BigDecimal totalApproveIndiGuaranteeAmt) {
        this.totalApproveIndiGuaranteeAmt = totalApproveIndiGuaranteeAmt;
    }

    public BigDecimal getTotalApproveJurisGuaranteeAmt() {
        return totalApproveJurisGuaranteeAmt;
    }

    public void setTotalApproveJurisGuaranteeAmt(BigDecimal totalApproveJurisGuaranteeAmt) {
        this.totalApproveJurisGuaranteeAmt = totalApproveJurisGuaranteeAmt;
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

    public int getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(int creditCustomerType) {
        this.creditCustomerType = creditCustomerType;
    }

    public CreditRequestType getLoanRequestType() {
        return loanRequestType;
    }

    public void setLoanRequestType(CreditRequestType loanRequestType) {
        this.loanRequestType = loanRequestType;
    }

    public Country getInvestedCountry() {
        return investedCountry;
    }

    public void setInvestedCountry(Country investedCountry) {
        this.investedCountry = investedCountry;
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

    public List<NewFeeDetailView> getNewFeeDetailViewList() {
        return newFeeDetailViewList;
    }

    public void setNewFeeDetailViewList(List<NewFeeDetailView> newFeeDetailViewList) {
        this.newFeeDetailViewList = newFeeDetailViewList;
    }

    public List<NewCreditDetailView> getNewCreditDetailViewList() {
        return newCreditDetailViewList;
    }

    public void setNewCreditDetailViewList(List<NewCreditDetailView> newCreditDetailViewList) {
        this.newCreditDetailViewList = newCreditDetailViewList;
    }

    public List<NewCollateralView> getNewCollateralViewList() {
        return newCollateralViewList;
    }

    public void setNewCollateralViewList(List<NewCollateralView> newCollateralViewList) {
        this.newCollateralViewList = newCollateralViewList;
    }

    public List<NewGuarantorDetailView> getNewGuarantorDetailViewList() {
        return newGuarantorDetailViewList;
    }

    public void setNewGuarantorDetailViewList(List<NewGuarantorDetailView> newGuarantorDetailViewList) {
        this.newGuarantorDetailViewList = newGuarantorDetailViewList;
    }

    public List<NewConditionDetailView> getNewConditionDetailViewList() {
        return newConditionDetailViewList;
    }

    public void setNewConditionDetailViewList(List<NewConditionDetailView> newConditionDetailViewList) {
        this.newConditionDetailViewList = newConditionDetailViewList;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("WCNeed", WCNeed)
                .append("totalWcTmb", totalWcTmb)
                .append("WCNeedDiffer", WCNeedDiffer)
                .append("totalWcDebit", totalWcDebit)
                .append("totalLoanWCTMB", totalLoanWCTMB)
                .append("case1WcLimit", case1WcLimit)
                .append("case1WcMinLimit", case1WcMinLimit)
                .append("case1Wc50CoreWc", case1Wc50CoreWc)
                .append("case1WcDebitCoreWc", case1WcDebitCoreWc)
                .append("case2WcLimit", case2WcLimit)
                .append("case2WcMinLimit", case2WcMinLimit)
                .append("case2Wc50CoreWc", case2Wc50CoreWc)
                .append("case2WcDebitCoreWc", case2WcDebitCoreWc)
                .append("case3WcLimit", case3WcLimit)
                .append("case3WcMinLimit", case3WcMinLimit)
                .append("case3Wc50CoreWc", case3Wc50CoreWc)
                .append("case3WcDebitCoreWc", case3WcDebitCoreWc)
                .append("existingSMELimit", existingSMELimit)
                .append("maximumSMELimit", maximumSMELimit)
                .append("totalPropose", totalPropose)
                .append("totalProposeLoanDBR", totalProposeLoanDBR)
                .append("totalProposeNonLoanDBR", totalProposeNonLoanDBR)
                .append("totalCommercial", totalCommercial)
                .append("totalCommercialAndOBOD", totalCommercialAndOBOD)
                .append("totalExposure", totalExposure)
                .append("totalNumberOfNewOD", totalNumberOfNewOD)
                .append("totalNumberProposeCreditFac", totalNumberProposeCreditFac)
                .append("totalNumberContingenPropose", totalNumberContingenPropose)
                .append("totalNumberOfCoreAsset", totalNumberOfCoreAsset)
                .append("totalNumberOfNonCoreAsset", totalNumberOfNonCoreAsset)
                .append("totalMortgageValue", totalMortgageValue)
                .append("totalGuaranteeAmount", totalGuaranteeAmount)
                .append("totalTCGGuaranteeAmount", totalTCGGuaranteeAmount)
                .append("totalIndvGuaranteeAmount", totalIndvGuaranteeAmount)
                .append("totalJurisGuaranteeAmount", totalJurisGuaranteeAmount)
                .append("totalApproveCredit", totalApproveCredit)
                .append("totalApproveCommercial", totalApproveCommercial)
                .append("totalApproveComAndOBOD", totalApproveComAndOBOD)
                .append("totalApproveExposure", totalApproveExposure)
                .append("totalApproveNumOfNewOD", totalApproveNumOfNewOD)
                .append("totalApproveNumProposeCreditFac", totalApproveNumProposeCreditFac)
                .append("totalApproveNumContingenPropose", totalApproveNumContingenPropose)
                .append("totalApproveNumOfCoreAsset", totalApproveNumOfCoreAsset)
                .append("totalApproveNumOfNonCoreAsset", totalApproveNumOfNonCoreAsset)
                .append("totalApproveGuaranteeAmt", totalApproveGuaranteeAmt)
                .append("totalApproveTCGGuaranteeAmt", totalApproveTCGGuaranteeAmt)
                .append("totalApproveIndiGuaranteeAmt", totalApproveIndiGuaranteeAmt)
                .append("totalApproveJurisGuaranteeAmt", totalApproveJurisGuaranteeAmt)
                .append("contactName", contactName)
                .append("contactPhoneNo", contactPhoneNo)
                .append("interService", interService)
                .append("currentAddress", currentAddress)
                .append("registeredAddress", registeredAddress)
                .append("emailAddress", emailAddress)
                .append("importMail", importMail)
                .append("exportMail", exportMail)
                .append("depositBranchCode", depositBranchCode)
                .append("ownerBranchCode", ownerBranchCode)
                .append("intFeeDOA", intFeeDOA)
                .append("frontendFeeDOA", frontendFeeDOA)
                .append("guarantorBA", guarantorBA)
                .append("reasonForReduction", reasonForReduction)
                .append("creditCustomerType", creditCustomerType)
                .append("loanRequestType", loanRequestType)
                .append("investedCountry", investedCountry)
                .append("relatedTMBLending", relatedTMBLending)
                .append("twentyFivePercentShareRelatedTMBLending", twentyFivePercentShareRelatedTMBLending)
                .append("singleLendingLimit", singleLendingLimit)
                .append("newFeeDetailViewList", newFeeDetailViewList)
                .append("newCreditDetailViewList", newCreditDetailViewList)
                .append("newCollateralViewList", newCollateralViewList)
                .append("newGuarantorDetailViewList", newGuarantorDetailViewList)
                .append("newConditionDetailViewList", newConditionDetailViewList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
