package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_exist_credit_facility")
public class ExistingCreditFacility implements Serializable{
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXISTING_CREDIT_SUM_ID", sequenceName = "SEQ_WRK_EXISTING_CREDIT_SUM_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXISTING_CREDIT_SUM_ID")
    private long id;

    @Column(name = "total_borrower_com_limit")
    private BigDecimal totalBorrowerComLimit;

    @Column(name = "total_borrower_retail_limit")
    private BigDecimal totalBorrowerRetailLimit;

    @Column(name = "total_borrower_rlos_limit")
    private BigDecimal totalBorrowerAppInRLOSLimit;

    @Column(name = "total_borrower_com")
    private BigDecimal totalBorrowerCom;
    
    @Column(name = "total_borrower_com_obod")
    private BigDecimal totalBorrowerComOBOD;
    
    @Column(name = "total_borrower_exposure")
    private BigDecimal totalBorrowerExposure;

    @Column(name = "total_borrower_number_od")
    private BigDecimal totalBorrowerNumberOfExistingOD;

    @Column(name = "total_borrower_od_limit")
    private BigDecimal totalBorrowerExistingODLimit;

    @Column(name = "total_related_com_limit")
    private BigDecimal totalRelatedComLimit;

    @Column(name = "total_related_retail_limit")
    private BigDecimal totalRelatedRetailLimit;

    @Column(name = "total_related_rlos_limit")
    private BigDecimal totalRelatedAppInRLOSLimit;

    @Column(name = "total_related_com")
    private BigDecimal totalRelatedCom;

    @Column(name = "total_related_com_obod")
    private BigDecimal totalRelatedComOBOD;

    @Column(name = "total_related_exposure")
    private BigDecimal totalRelatedExposure;

    @Column(name = "total_group_com_limit")
    private BigDecimal totalGroupCom;

    @Column(name = "total_group_com_obod_limit")
    private BigDecimal totalGroupComOBOD;

    @Column(name = "total_group_ret_limit")
    private BigDecimal totalGroupExposure;

    @Column(name = "total_borrower_appraisal")
    private BigDecimal totalBorrowerAppraisalValue;

    @Column(name = "total_borrower_mortgage")
    private BigDecimal totalBorrowerMortgageValue;

    @Column(name = "total_related_appraisal")
    private BigDecimal totalRelatedAppraisalValue;

    @Column(name = "total_related_mortgage")
    private BigDecimal totalRelatedMortgageValue;

    @Column(name = "total_guarantee_amount")
    private BigDecimal totalGuaranteeAmount;

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

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToMany(mappedBy = "existingCreditFacility", cascade = CascadeType.ALL)
    private List<ExistingCreditDetail> existingCreditDetailList;

    @OneToMany(mappedBy = "existingCreditFacility", cascade = CascadeType.ALL)
    private List<ExistingCollateralDetail> existingCollateralDetailList;

    @OneToMany(mappedBy = "existingCreditFacility", cascade = CascadeType.ALL)
    private List<ExistingGuarantorDetail> existingGuarantorDetailList;

    @OneToMany(mappedBy = "existingCreditFacility", cascade = CascadeType.ALL)
    private List<ExistingConditionDetail> existingConditionDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalBorrowerComLimit() {
        return totalBorrowerComLimit;
    }

    public void setTotalBorrowerComLimit(BigDecimal totalBorrowerComLimit) {
        this.totalBorrowerComLimit = totalBorrowerComLimit;
    }

    public BigDecimal getTotalBorrowerRetailLimit() {
        return totalBorrowerRetailLimit;
    }

    public void setTotalBorrowerRetailLimit(BigDecimal totalBorrowerRetailLimit) {
        this.totalBorrowerRetailLimit = totalBorrowerRetailLimit;
    }

    public BigDecimal getTotalBorrowerAppInRLOSLimit() {
        return totalBorrowerAppInRLOSLimit;
    }

    public void setTotalBorrowerAppInRLOSLimit(BigDecimal totalBorrowerAppInRLOSLimit) {
        this.totalBorrowerAppInRLOSLimit = totalBorrowerAppInRLOSLimit;
    }

    public BigDecimal getTotalBorrowerCom() {
        return totalBorrowerCom;
    }

    public void setTotalBorrowerCom(BigDecimal totalBorrowerCom) {
        this.totalBorrowerCom = totalBorrowerCom;
    }

    public BigDecimal getTotalBorrowerComOBOD() {
        return totalBorrowerComOBOD;
    }

    public void setTotalBorrowerComOBOD(BigDecimal totalBorrowerComOBOD) {
        this.totalBorrowerComOBOD = totalBorrowerComOBOD;
    }

    public BigDecimal getTotalBorrowerExposure() {
        return totalBorrowerExposure;
    }

    public void setTotalBorrowerExposure(BigDecimal totalBorrowerExposure) {
        this.totalBorrowerExposure = totalBorrowerExposure;
    }

    public BigDecimal getTotalBorrowerNumberOfExistingOD() {
        return totalBorrowerNumberOfExistingOD;
    }

    public void setTotalBorrowerNumberOfExistingOD(BigDecimal totalBorrowerNumberOfExistingOD) {
        this.totalBorrowerNumberOfExistingOD = totalBorrowerNumberOfExistingOD;
    }

    public BigDecimal getTotalBorrowerExistingODLimit() {
        return totalBorrowerExistingODLimit;
    }

    public void setTotalBorrowerExistingODLimit(BigDecimal totalBorrowerExistingODLimit) {
        this.totalBorrowerExistingODLimit = totalBorrowerExistingODLimit;
    }

    public BigDecimal getTotalRelatedComLimit() {
        return totalRelatedComLimit;
    }

    public void setTotalRelatedComLimit(BigDecimal totalRelatedComLimit) {
        this.totalRelatedComLimit = totalRelatedComLimit;
    }

    public BigDecimal getTotalRelatedRetailLimit() {
        return totalRelatedRetailLimit;
    }

    public void setTotalRelatedRetailLimit(BigDecimal totalRelatedRetailLimit) {
        this.totalRelatedRetailLimit = totalRelatedRetailLimit;
    }

    public BigDecimal getTotalRelatedAppInRLOSLimit() {
        return totalRelatedAppInRLOSLimit;
    }

    public void setTotalRelatedAppInRLOSLimit(BigDecimal totalRelatedAppInRLOSLimit) {
        this.totalRelatedAppInRLOSLimit = totalRelatedAppInRLOSLimit;
    }

    public BigDecimal getTotalRelatedCom() {
        return totalRelatedCom;
    }

    public void setTotalRelatedCom(BigDecimal totalRelatedCom) {
        this.totalRelatedCom = totalRelatedCom;
    }

    public BigDecimal getTotalRelatedComOBOD() {
        return totalRelatedComOBOD;
    }

    public void setTotalRelatedComOBOD(BigDecimal totalRelatedComOBOD) {
        this.totalRelatedComOBOD = totalRelatedComOBOD;
    }

    public BigDecimal getTotalRelatedExposure() {
        return totalRelatedExposure;
    }

    public void setTotalRelatedExposure(BigDecimal totalRelatedExposure) {
        this.totalRelatedExposure = totalRelatedExposure;
    }

    public BigDecimal getTotalGroupCom() {
        return totalGroupCom;
    }

    public void setTotalGroupCom(BigDecimal totalGroupCom) {
        this.totalGroupCom = totalGroupCom;
    }

    public BigDecimal getTotalGroupComOBOD() {
        return totalGroupComOBOD;
    }

    public void setTotalGroupComOBOD(BigDecimal totalGroupComOBOD) {
        this.totalGroupComOBOD = totalGroupComOBOD;
    }

    public BigDecimal getTotalGroupExposure() {
        return totalGroupExposure;
    }

    public void setTotalGroupExposure(BigDecimal totalGroupExposure) {
        this.totalGroupExposure = totalGroupExposure;
    }

    public BigDecimal getTotalBorrowerAppraisalValue() {
        return totalBorrowerAppraisalValue;
    }

    public void setTotalBorrowerAppraisalValue(BigDecimal totalBorrowerAppraisalValue) {
        this.totalBorrowerAppraisalValue = totalBorrowerAppraisalValue;
    }

    public BigDecimal getTotalBorrowerMortgageValue() {
        return totalBorrowerMortgageValue;
    }

    public void setTotalBorrowerMortgageValue(BigDecimal totalBorrowerMortgageValue) {
        this.totalBorrowerMortgageValue = totalBorrowerMortgageValue;
    }

    public BigDecimal getTotalRelatedAppraisalValue() {
        return totalRelatedAppraisalValue;
    }

    public void setTotalRelatedAppraisalValue(BigDecimal totalRelatedAppraisalValue) {
        this.totalRelatedAppraisalValue = totalRelatedAppraisalValue;
    }

    public BigDecimal getTotalRelatedMortgageValue() {
        return totalRelatedMortgageValue;
    }

    public void setTotalRelatedMortgageValue(BigDecimal totalRelatedMortgageValue) {
        this.totalRelatedMortgageValue = totalRelatedMortgageValue;
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

    public List<ExistingCreditDetail> getExistingCreditDetailList() {
        return existingCreditDetailList;
    }

    public void setExistingCreditDetailList(List<ExistingCreditDetail> existingCreditDetailList) {
        this.existingCreditDetailList = existingCreditDetailList;
    }

    public List<ExistingCollateralDetail> getExistingCollateralDetailList() {
        return existingCollateralDetailList;
    }

    public void setExistingCollateralDetailList(List<ExistingCollateralDetail> existingCollateralDetailList) {
        this.existingCollateralDetailList = existingCollateralDetailList;
    }

    public List<ExistingGuarantorDetail> getExistingGuarantorDetailList() {
        return existingGuarantorDetailList;
    }

    public void setExistingGuarantorDetailList(List<ExistingGuarantorDetail> existingGuarantorDetailList) {
        this.existingGuarantorDetailList = existingGuarantorDetailList;
    }

    public List<ExistingConditionDetail> getExistingConditionDetailList() {
        return existingConditionDetailList;
    }

    public void setExistingConditionDetailList(List<ExistingConditionDetail> existingConditionDetailList) {
        this.existingConditionDetailList = existingConditionDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("totalBorrowerComLimit", totalBorrowerComLimit)
                .append("totalBorrowerRetailLimit", totalBorrowerRetailLimit)
                .append("totalBorrowerAppInRLOSLimit", totalBorrowerAppInRLOSLimit)
                .append("totalBorrowerNumberOfExistingOD", totalBorrowerNumberOfExistingOD)
                .append("totalBorrowerExistingODLimit", totalBorrowerExistingODLimit)
                .append("totalRelatedComLimit", totalRelatedComLimit)
                .append("totalRelatedRetailLimit", totalRelatedRetailLimit)
                .append("totalRelatedAppInRLOSLimit", totalRelatedAppInRLOSLimit)
                .append("totalGroupCom", totalGroupCom)
                .append("totalGroupComOBOD", totalGroupComOBOD)
                .append("totalGroupExposure", totalGroupExposure)
                .append("totalBorrowerAppraisalValue", totalBorrowerAppraisalValue)
                .append("totalBorrowerMortgageValue", totalBorrowerMortgageValue)
                .append("totalRelatedAppraisalValue", totalRelatedAppraisalValue)
                .append("totalRelatedMortgageValue", totalRelatedMortgageValue)
                .append("totalGuaranteeAmount", totalGuaranteeAmount)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("workCase", workCase)
                .append("workCasePrescreen", workCasePrescreen)
                .append("existingCreditDetailList", existingCreditDetailList)
                .append("existingCollateralDetailList", existingCollateralDetailList)
                .append("existingGuarantorDetailList", existingGuarantorDetailList)
                .append("existingConditionDetailList", existingConditionDetailList)
                .toString();
    }
}
