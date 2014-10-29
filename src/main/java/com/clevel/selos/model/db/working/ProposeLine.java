package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_credit_facility")
public class ProposeLine implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_NEW_WRK_CREDIT_FAC_ID", sequenceName = "SEQ_NEW_WRK_CREDIT_FAC_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NEW_WRK_CREDIT_FAC_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    //#1
    @Column(name = "wc_need")
    private BigDecimal wcNeed;

    @Column(name = "total_wc_tmb")
    private BigDecimal totalWCTmb;

    @Column(name = "wc_need_differ")
    private BigDecimal wcNeedDiffer;

    @Column(name = "total_wc_debit")
    private BigDecimal totalWCDebit;

    @Column(name = "total_loan_wc_tmb")
    private BigDecimal totalLoanWCTMB;

    //#2
    @Column(name = "case1_wc_limit")
    private BigDecimal case1WCLimit;

    @Column(name = "case1_wc_min_limit")
    private BigDecimal case1WCMinLimit;

    @Column(name = "case1_wc_50_core_wc")
    private BigDecimal case1WC50CoreWC;

    @Column(name = "case1_wc_debit_core_wc")
    private BigDecimal case1WCDebitCoreWC;

    //#3
    @Column(name = "case2_wc_limit")
    private BigDecimal case2WCLimit;

    @Column(name = "case2_wc_min_limit")
    private BigDecimal case2WCMinLimit;

    @Column(name = "case2_wc_50_core_wc")
    private BigDecimal case2WC50CoreWC;

    @Column(name = "case2_wc_debit_core_wc")
    private BigDecimal case2WCDebitCoreWC;

    //#4
    @Column(name = "case3_wc_limit")
    private BigDecimal case3WCLimit;

    @Column(name = "case3_wc_min_limit")
    private BigDecimal case3WCMinLimit;

    @Column(name = "case3_wc_50_core_wc")
    private BigDecimal case3WC50CoreWC;

    @Column(name = "case3_wc_debit_core_wc")
    private BigDecimal case3WCDebitCoreWC;

    @Column(name = "credit_customer_type", columnDefinition = "int default 0")
    private int creditCustomerType;

    @OneToOne
    @JoinColumn(name = "credit_request_type")
    private CreditRequestType loanRequestType;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country investedCountry;

    @Column(name = "existing_sme_limit")
    private BigDecimal existingSMELimit;

    @Column(name = "maximum_sme_limit")
    private BigDecimal maximumSMELimit;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_phone_no")
    private String contactPhoneNo;

    @Column(name = "inter_service")
    private String interService;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "registered_address")
    private String registeredAddress;

    @Column(name = "import_mail")
    private String importMail;

    @Column(name = "export_mail")
    private String exportMail;

    @Column(name = "deposit_branch_code")
    private String depositBranchCode;

    @Column(name = "owner_branch_code")
    private String ownerBranchCode;

    @Column(name = "int_fee_doa")
    private BigDecimal intFeeDOA;

    @Column(name = "frontend_fee_doa")
    private BigDecimal frontendFeeDOA;

    @Column(name = "guarantor_ba")
    private BigDecimal guarantorBA;

    @Column(name = "reason_for_reduction")
    private String reasonForReduction;

    @Column(name = "related_tmb_lending", columnDefinition = "int default 0")
    private int relatedTMBLending;

    @Column(name = "percent_share_related_tmb", columnDefinition = "int default 0")
    private int twentyFivePercentShareRelatedTMBLending;

    @Column(name = "single_lending_limit", columnDefinition = "int default 0")
    private int singleLendingLimit;

    @Column(name = "total_propose")
    private BigDecimal totalPropose;

    @Column(name = "total_commercial")
    private BigDecimal totalCommercial;

    @Column(name = "total_commercial_and_obod")
    private BigDecimal totalCommercialAndOBOD;

    @Column(name = "total_exposure")
    private BigDecimal totalExposure;

    @Column(name = "total_guarantee_amount")
    private BigDecimal totalGuaranteeAmount;

    @Column(name = "total_new_credit")
    private BigDecimal totalNewCredit;

    // ********** Total Section ********** //
    @Column(name = "total_propose_loan_dbr")
    private BigDecimal totalProposeLoanDBR;

    @Column(name = "total_propose_non_loan_dbr")
    private BigDecimal totalProposeNonLoanDBR;

    @Column(name = "total_num_new_od")
    private BigDecimal totalNumberOfNewOD;

    @Column(name = "total_number_propose_credit")
    private BigDecimal totalNumberProposeCreditFac;

    @Column(name = "total_num_contingen_propose")
    private BigDecimal totalNumberContingenPropose;

    // *********** add *************** //
    @Column(name = "tot_num_core_asset")
    private BigDecimal totalNumberOfCoreAsset;

    @Column(name = "tot_num__non_core_asset")
    private BigDecimal totalNumberOfNonCoreAsset;

    @Column(name = "tot_mortgage_value")
    private BigDecimal totalMortgageValue;

    @Column(name = "tot_tcg_gauarantee_amt")
    private BigDecimal totalTCGGuaranteeAmount;

    @Column(name = "tot_indv_guarantee_amt")
    private BigDecimal totalIndvGuaranteeAmount;

    @Column(name = "tot_juris_guarantee_amt")
    private BigDecimal totalJurisGuaranteeAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @OneToMany(mappedBy = "proposeLine")
    @Sort(type = SortType.NATURAL)
    private List<ProposeCreditInfo> proposeCreditInfoList;

    @OneToMany(mappedBy = "proposeLine")
    private List<ProposeConditionInfo> proposeConditionInfoList;

    @OneToMany(mappedBy = "proposeLine")
    private List<ProposeGuarantorInfo> proposeGuarantorInfoList;

    @OneToMany(mappedBy = "proposeLine")
    private List<ProposeCollateralInfo> proposeCollateralInfoList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
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

    public List<ProposeCreditInfo> getProposeCreditInfoList() {
        return proposeCreditInfoList;
    }

    public void setProposeCreditInfoList(List<ProposeCreditInfo> proposeCreditInfoList) {
        this.proposeCreditInfoList = proposeCreditInfoList;
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

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
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

    public List<ProposeConditionInfo> getProposeConditionInfoList() {
        return proposeConditionInfoList;
    }

    public void setProposeConditionInfoList(List<ProposeConditionInfo> proposeConditionInfoList) {
        this.proposeConditionInfoList = proposeConditionInfoList;
    }

    public List<ProposeGuarantorInfo> getProposeGuarantorInfoList() {
        return proposeGuarantorInfoList;
    }

    public void setProposeGuarantorInfoList(List<ProposeGuarantorInfo> proposeGuarantorInfoList) {
        this.proposeGuarantorInfoList = proposeGuarantorInfoList;
    }

    public List<ProposeCollateralInfo> getProposeCollateralInfoList() {
        return proposeCollateralInfoList;
    }

    public void setProposeCollateralInfoList(List<ProposeCollateralInfo> proposeCollateralInfoList) {
        this.proposeCollateralInfoList = proposeCollateralInfoList;
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

    public BigDecimal getTotalNewCredit() {
        return totalNewCredit;
    }

    public void setTotalNewCredit(BigDecimal totalNewCredit) {
        this.totalNewCredit = totalNewCredit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("workCase", workCase).
                append("workCasePrescreen", workCasePrescreen).
                append("wcNeed", wcNeed).
                append("totalWCTmb", totalWCTmb).
                append("wcNeedDiffer", wcNeedDiffer).
                append("totalWCDebit", totalWCDebit).
                append("totalLoanWCTMB", totalLoanWCTMB).
                append("case1WCLimit", case1WCLimit).
                append("case1WCMinLimit", case1WCMinLimit).
                append("case1WC50CoreWC", case1WC50CoreWC).
                append("case1WCDebitCoreWC", case1WCDebitCoreWC).
                append("case2WCLimit", case2WCLimit).
                append("case2WCMinLimit", case2WCMinLimit).
                append("case2WC50CoreWC", case2WC50CoreWC).
                append("case2WCDebitCoreWC", case2WCDebitCoreWC).
                append("case3WCLimit", case3WCLimit).
                append("case3WCMinLimit", case3WCMinLimit).
                append("case3WC50CoreWC", case3WC50CoreWC).
                append("case3WCDebitCoreWC", case3WCDebitCoreWC).
                append("creditCustomerType", creditCustomerType).
                append("loanRequestType", loanRequestType).
                append("investedCountry", investedCountry).
                append("existingSMELimit", existingSMELimit).
                append("maximumSMELimit", maximumSMELimit).
                append("contactName", contactName).
                append("contactPhoneNo", contactPhoneNo).
                append("interService", interService).
                append("currentAddress", currentAddress).
                append("registeredAddress", registeredAddress).
                append("importMail", importMail).
                append("exportMail", exportMail).
                append("depositBranchCode", depositBranchCode).
                append("ownerBranchCode", ownerBranchCode).
                append("intFeeDOA", intFeeDOA).
                append("frontendFeeDOA", frontendFeeDOA).
                append("guarantorBA", guarantorBA).
                append("reasonForReduction", reasonForReduction).
                append("relatedTMBLending", relatedTMBLending).
                append("twentyFivePercentShareRelatedTMBLending", twentyFivePercentShareRelatedTMBLending).
                append("singleLendingLimit", singleLendingLimit).
                append("totalPropose", totalPropose).
                append("totalCommercial", totalCommercial).
                append("totalCommercialAndOBOD", totalCommercialAndOBOD).
                append("totalExposure", totalExposure).
                append("totalGuaranteeAmount", totalGuaranteeAmount).
                append("totalNewCredit", totalNewCredit).
                append("totalProposeLoanDBR", totalProposeLoanDBR).
                append("totalProposeNonLoanDBR", totalProposeNonLoanDBR).
                append("totalNumberOfNewOD", totalNumberOfNewOD).
                append("totalNumberProposeCreditFac", totalNumberProposeCreditFac).
                append("totalNumberContingenPropose", totalNumberContingenPropose).
                append("totalNumberOfCoreAsset", totalNumberOfCoreAsset).
                append("totalNumberOfNonCoreAsset", totalNumberOfNonCoreAsset).
                append("totalMortgageValue", totalMortgageValue).
                append("totalTCGGuaranteeAmount", totalTCGGuaranteeAmount).
                append("totalIndvGuaranteeAmount", totalIndvGuaranteeAmount).
                append("totalJurisGuaranteeAmount", totalJurisGuaranteeAmount).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("proposeCreditInfoList", proposeCreditInfoList).
                append("proposeConditionInfoList", proposeConditionInfoList).
                append("proposeGuarantorInfoList", proposeGuarantorInfoList).
                append("proposeCollateralInfoList", proposeCollateralInfoList).
                toString();
    }
}