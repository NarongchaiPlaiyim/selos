package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_credit_facility")
public class NewCreditFacility implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_NEW_WRK_CREDIT_FAC_ID", sequenceName = "SEQ_NEW_WRK_CREDIT_FAC_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NEW_WRK_CREDIT_FAC_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "wc_need")
    private BigDecimal wcNeed;

    @Column(name = "total_wc_tmb")
    private BigDecimal totalWcTmb;

    @Column(name = "wc_need_differ")
    private BigDecimal WCNeedDiffer;

    @Column(name = "total_wc_debit")
    private BigDecimal totalWcDebit;

    @Column(name = "case1_wc_limit")
    private BigDecimal case1WcLimit;

    @Column(name = "case1_wc_min_limit")
    private BigDecimal case1WcMinLimit;

    @Column(name = "case1_wc_50_core_wc")
    private BigDecimal case1Wc50CoreWc;

    @Column(name = "case1_wc_debit_core_wc")
    private BigDecimal case1WcDebitCoreWc;

    @Column(name = "case2_wc_limit")
    private BigDecimal case2WcLimit;

    @Column(name = "case2_wc_min_limit")
    private BigDecimal case2WcMinLimit;

    @Column(name = "case2_wc_50_core_wc")
    private BigDecimal case2Wc50CoreWc;

    @Column(name = "case2_wc_debit_core_wc")
    private BigDecimal case2WcDebitCoreWc;

    @Column(name = "case3_wc_limit")
    private BigDecimal case3WcLimit;

    @Column(name = "case3_wc_min_limit")
    private BigDecimal case3WcMinLimit;

    @Column(name = "case3_wc_50_core_wc")
    private BigDecimal case3Wc50CoreWc;

    @Column(name = "case3_wc_debit_core_wc")
    private BigDecimal case3WcDebitCoreWc;

    @Column(name = "existing_sme_limit")
    private BigDecimal existingSMELimit;

    @Column(name = "maximum_existing_sme_limit")
    private BigDecimal maximumExistingSMELimit;

    @Column(name = "total_propose")
    private BigDecimal totalPropose;

    @Column(name = "total_propose_loan_dbr")
    private BigDecimal totalProposeLoanDBR;

    @Column(name = "total_propose_non_loan_dbr")
    private BigDecimal totalProposeNonLoanDBR;

    @Column(name = "total_commercial")
    private BigDecimal totalCommercial;

    @Column(name = "total_commercial_and_obod")
    private BigDecimal totalCommercialAndOBOD;

    @Column(name = "total_exposure")
    private BigDecimal totalExposure;

    @Column(name = "total_guarantee_amount")
    private BigDecimal totalGuaranteeAmount;

    @Column(name = "total_num_new_od")
    private BigDecimal totalNumberOfNewOD;

    @Column(name = "total_num_contingen_propose")
    private BigDecimal totalNumberContingenPropose;

    @Column(name = "total_number_propose_credit")
    private BigDecimal totalNumberProposeCreditFac;

    @Column(name = "related_tmb_lending")
    private int relatedTMBLending;

    @Column(name = "percent_share_related_tmb")
    private int twentyFivePercentShareRelatedTMBLending;

    @Column(name = "single_lending_limit")
    private int singleLendingLimit;

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

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "import_mail")
    private String importMail;

    @Column(name = "export_mail")
    private String exportMail;

    @Column(name = "deposit_branch_code")
    private String depositBranchCode;

    @Column(name = "owner_branch_code")
    private String ownerBranchCode;

    @Column(name = "reason_for_reduction")
    private String reasonForReduction;

    @Column(name = "int_fee_doa")
    private BigDecimal intFeeDOA;

    @Column(name = "frontend_fee_doa")
    private BigDecimal frontendFeeDOA;

    @Column(name = "guarantor_ba")
    private BigDecimal guarantorBA;

    @Column(name = "credit_customer_type")
    private int creditCustomerType;

    @OneToOne
    @JoinColumn(name = "credit_request_type")
    private  CreditRequestType creditRequestType;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

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

    @OneToMany(mappedBy = "newCreditFacility", cascade = CascadeType.ALL)
    private List<NewFeeDetail> newFeeDetailList;

    @OneToMany(mappedBy = "newCreditFacility", cascade = CascadeType.ALL)
    private List<NewCreditDetail> newCreditDetailList;

    @OneToMany(mappedBy = "newCreditFacility", cascade = CascadeType.ALL)
    private List<NewCollateralDetail> newCollateralDetailList;

    @OneToMany(mappedBy = "newCreditFacility", cascade = CascadeType.ALL)
    private List<NewGuarantorDetail> newGuarantorDetailList;

    @OneToMany(mappedBy = "newCreditFacility", cascade = CascadeType.ALL)
    private List<NewConditionDetail> newConditionDetailList;

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

    public BigDecimal getMaximumExistingSMELimit() {
        return maximumExistingSMELimit;
    }

    public void setMaximumExistingSMELimit(BigDecimal maximumExistingSMELimit) {
        this.maximumExistingSMELimit = maximumExistingSMELimit;
    }

    public BigDecimal getTotalNumberOfNewOD() {
        return totalNumberOfNewOD;
    }

    public void setTotalNumberOfNewOD(BigDecimal totalNumberOfNewOD) {
        this.totalNumberOfNewOD = totalNumberOfNewOD;
    }

    public BigDecimal getTotalNumberContingenPropose() {
        return totalNumberContingenPropose;
    }

    public void setTotalNumberContingenPropose(BigDecimal totalNumberContingenPropose) {
        this.totalNumberContingenPropose = totalNumberContingenPropose;
    }

    public BigDecimal getTotalNumberProposeCreditFac() {
        return totalNumberProposeCreditFac;
    }

    public void setTotalNumberProposeCreditFac(BigDecimal totalNumberProposeCreditFac) {
        this.totalNumberProposeCreditFac = totalNumberProposeCreditFac;
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

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
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

    public String getReasonForReduction() {
        return reasonForReduction;
    }

    public void setReasonForReduction(String reasonForReduction) {
        this.reasonForReduction = reasonForReduction;
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

    public int getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(int creditCustomerType) {
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

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public List<NewFeeDetail> getNewFeeDetailList() {
        return newFeeDetailList;
    }

    public void setNewFeeDetailList(List<NewFeeDetail> newFeeDetailList) {
        this.newFeeDetailList = newFeeDetailList;
    }

    public List<NewCreditDetail> getNewCreditDetailList() {
        return newCreditDetailList;
    }

    public void setNewCreditDetailList(List<NewCreditDetail> newCreditDetailList) {
        this.newCreditDetailList = newCreditDetailList;
    }

    public List<NewCollateralDetail> getNewCollateralDetailList() {
        return newCollateralDetailList;
    }

    public void setNewCollateralDetailList(List<NewCollateralDetail> newCollateralDetailList) {
        this.newCollateralDetailList = newCollateralDetailList;
    }

    public List<NewGuarantorDetail> getNewGuarantorDetailList() {
        return newGuarantorDetailList;
    }

    public void setNewGuarantorDetailList(List<NewGuarantorDetail> newGuarantorDetailList) {
        this.newGuarantorDetailList = newGuarantorDetailList;
    }

    public List<NewConditionDetail> getNewConditionDetailList() {
        return newConditionDetailList;
    }

    public void setNewConditionDetailList(List<NewConditionDetail> newConditionDetailList) {
        this.newConditionDetailList = newConditionDetailList;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("wcNeed", wcNeed)
                .append("totalWcTmb", totalWcTmb)
                .append("WCNeedDiffer", WCNeedDiffer)
                .append("totalWcDebit", totalWcDebit)
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
                .append("maximumExistingSMELimit", maximumExistingSMELimit)
                .append("totalPropose", totalPropose)
                .append("totalProposeLoanDBR", totalProposeLoanDBR)
                .append("totalProposeNonLoanDBR", totalProposeNonLoanDBR)
                .append("totalCommercial", totalCommercial)
                .append("totalCommercialAndOBOD", totalCommercialAndOBOD)
                .append("totalExposure", totalExposure)
                .append("totalGuaranteeAmount", totalGuaranteeAmount)
                .append("relatedTMBLending", relatedTMBLending)
                .append("twentyFivePercentShareRelatedTMBLending", twentyFivePercentShareRelatedTMBLending)
                .append("singleLendingLimit", singleLendingLimit)
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
                .append("reasonForReduction", reasonForReduction)
                .append("intFeeDOA", intFeeDOA)
                .append("frontendFeeDOA", frontendFeeDOA)
                .append("guarantorBA", guarantorBA)
                .append("creditCustomerType", creditCustomerType)
                .append("creditRequestType", creditRequestType)
                .append("country", country)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("newFeeDetailList", newFeeDetailList)
                .append("newCreditDetailList", newCreditDetailList)
                .append("newCollateralDetailList", newCollateralDetailList)
                .append("newGuarantorDetailList", newGuarantorDetailList)
                .append("newConditionDetailList", newConditionDetailList)
                .toString();
    }
}