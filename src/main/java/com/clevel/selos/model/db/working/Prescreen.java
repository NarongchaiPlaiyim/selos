package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="wrk_prescreen")
public class Prescreen implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_PRESCREEN_ID", sequenceName="SEQ_WRK_PRESCREEN_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_PRESCREEN_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name="product_group_id")
    private ProductGroup productGroup;

    @Temporal(TemporalType.DATE)
    @Column(name="expected_submit_date")
    private Date expectedSubmitDate;

    @Column(name="group_income")
    private BigDecimal groupIncome;

    @Column(name="group_exposure")
    private BigDecimal groupExposure;

    @OneToOne
    @JoinColumn(name="business_location_id")
    private Province businessLocation;

    @Temporal(TemporalType.DATE)
    @Column(name="register_date")
    private Date registerDate;

    @Temporal(TemporalType.DATE)
    @Column(name="referred_date")
    private Date referredDate;

    @OneToOne
    @JoinColumn(name="referred_experience_id")
    private ReferredExperience referredExperience;

    @Column(name="tcg")
    private boolean tcg;

    @Column(name="refinance")
    private boolean refinance;

    @OneToOne
    @JoinColumn(name="refinance_bank_id")
    private Bank refinanceBank;

    @OneToOne
    @JoinColumn(name="borrowing_type_id")
    private BorrowingType borrowingType;

    @OneToOne
    @JoinColumn(name="bdm_checker_id")
    private User bdmChecker;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public  Prescreen(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public BigDecimal getGroupExposure() {
        return groupExposure;
    }

    public void setGroupExposure(BigDecimal groupExposure) {
        this.groupExposure = groupExposure;
    }

    public BigDecimal getGroupIncome() {
        return groupIncome;
    }

    public void setGroupIncome(BigDecimal groupIncome) {
        this.groupIncome = groupIncome;
    }

    public Province getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(Province businessLocation) {
        this.businessLocation = businessLocation;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getReferredDate() {
        return referredDate;
    }

    public void setReferredDate(Date referredDate) {
        this.referredDate = referredDate;
    }

    public ReferredExperience getReferredExperience() {
        return referredExperience;
    }

    public void setReferredExperience(ReferredExperience referredExperience) {
        this.referredExperience = referredExperience;
    }

    public boolean isTcg() {
        return tcg;
    }

    public void setTcg(boolean tcg) {
        this.tcg = tcg;
    }

    public boolean isRefinance() {
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }

    public Bank getRefinanceBank() {
        return refinanceBank;
    }

    public void setRefinanceBank(Bank refinanceBank) {
        this.refinanceBank = refinanceBank;
    }

    public User getBdmChecker() {
        return bdmChecker;
    }

    public void setBdmChecker(User bdmChecker) {
        this.bdmChecker = bdmChecker;
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

    public BorrowingType getBorrowingType() {
        return borrowingType;
    }

    public void setBorrowingType(BorrowingType borrowingType) {
        this.borrowingType = borrowingType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCasePrescreen", workCasePrescreen)
                .append("productGroup", productGroup)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("groupIncome", groupIncome)
                .append("groupExposure", groupExposure)
                .append("businessLocation", businessLocation)
                .append("registerDate", registerDate)
                .append("referredDate", referredDate)
                .append("referredExperience", referredExperience)
                .append("tcg", tcg)
                .append("refinance", refinance)
                .append("refinanceBank", refinanceBank)
                .append("borrowingType", borrowingType)
                .append("bdmChecker", bdmChecker)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
