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
@Table(name = "wrk_credit_facility_propose")
public class CreditFacilityPropose  implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CREDIT_FACILITY_PROPOSE_ID", sequenceName = "SEQ_WRK_CREDIT_FACILITY_PROPOSE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CREDIT_FACILITY_PROPOSE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "wc_need")
    private BigDecimal wcNeed;

    @Column(name = "total_credit_turnover")
    private BigDecimal totalCreditTurnover;

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

    @Column(name = "total_num_propose_credit")
    private BigDecimal totalNumberProposeCreditFac;

    @Column(name = "total_num_contingen")
    private BigDecimal totalNumberContingenPropose;

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

    @OneToMany(mappedBy = "creditFacilityPropose", cascade = CascadeType.ALL)
    private List<ProposeFeeDetail> proposeFeeDetailList;

    @OneToMany(mappedBy = "creditFacilityPropose", cascade = CascadeType.ALL)
    private List<NewCreditDetail> newCreditDetailList;

    @OneToMany(mappedBy = "creditFacilityPropose", cascade = CascadeType.ALL)
    private List<ProposeCollateralDetail> proposeCollateralDetailList;

    @OneToMany(mappedBy = "creditFacilityPropose", cascade = CascadeType.ALL)
    private List<ProposeGuarantorDetail> proposeGuarantorDetailList;

    @OneToMany(mappedBy = "creditFacilityPropose", cascade = CascadeType.ALL)
    private List<ProposeConditionDetail> proposeConditionDetailList;

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

    public BigDecimal getWcNeed() {
        return wcNeed;
    }

    public void setWcNeed(BigDecimal wcNeed) {
        this.wcNeed = wcNeed;
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

    public List<ProposeFeeDetail> getProposeFeeDetailList() {
        return proposeFeeDetailList;
    }

    public void setProposeFeeDetailList(List<ProposeFeeDetail> proposeFeeDetailList) {
        this.proposeFeeDetailList = proposeFeeDetailList;
    }

    public List<NewCreditDetail> getNewCreditDetailList() {
        return newCreditDetailList;
    }

    public void setNewCreditDetailList(List<NewCreditDetail> newCreditDetailList) {
        this.newCreditDetailList = newCreditDetailList;
    }

    public List<ProposeCollateralDetail> getProposeCollateralDetailList() {
        return proposeCollateralDetailList;
    }

    public void setProposeCollateralDetailList(List<ProposeCollateralDetail> proposeCollateralDetailList) {
        this.proposeCollateralDetailList = proposeCollateralDetailList;
    }

    public List<ProposeGuarantorDetail> getProposeGuarantorDetailList() {
        return proposeGuarantorDetailList;
    }

    public void setProposeGuarantorDetailList(List<ProposeGuarantorDetail> proposeGuarantorDetailList) {
        this.proposeGuarantorDetailList = proposeGuarantorDetailList;
    }

    public List<ProposeConditionDetail> getProposeConditionDetailList() {
        return proposeConditionDetailList;
    }

    public void setProposeConditionDetailList(List<ProposeConditionDetail> proposeConditionDetailList) {
        this.proposeConditionDetailList = proposeConditionDetailList;
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


}