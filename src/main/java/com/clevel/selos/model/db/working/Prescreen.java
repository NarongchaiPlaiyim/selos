package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_prescreen")
public class Prescreen implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PRESCREEN_ID", sequenceName = "SEQ_WRK_PRESCREEN_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PRESCREEN_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;

    @Temporal(TemporalType.DATE)
    @Column(name = "expected_submit_date")
    private Date expectedSubmitDate;

    @Column(name = "group_income")
    private BigDecimal groupIncome;

    @Column(name = "group_exposure")
    private BigDecimal groupExposure;

    @OneToOne
    @JoinColumn(name = "business_location_id")
    private Province businessLocation;

    @OneToOne
    @JoinColumn(name = "registration_country")
    private Country country;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_date")
    private Date registerDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "referred_date")
    private Date referredDate;

    @OneToOne
    @JoinColumn(name = "referred_experience_id")
    private ReferredExperience referredExperience;

    @Column(name = "tcg")
    private int tcg;

    @Column(name = "refinance_in", length = 1, nullable = false, columnDefinition = "int default -1")
    private int refinanceIN;

    @OneToOne
    @JoinColumn(name = "refinance_in_id")
    private Bank refinanceInValue;

    @Column(name = "refinance_out", length = 1, nullable = false, columnDefinition = "int default -1")
    private int refinanceOUT;

    @OneToOne
    @JoinColumn(name = "refinance_out_id")
    private Bank refinanceOutValue;

    @Column(name = "existing_sme_customer", length = 1, nullable = false, columnDefinition = "int default -1")
    private int existingSMECustomer;

    @OneToOne
    @JoinColumn(name = "borrowing_type_id")
    private BorrowingType borrowingType;

    @OneToOne
    @JoinColumn(name = "bdm_checker_id")
    private User bdmChecker;

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

    @Column(name = "modify_flag")
    private int modifyFlag;

    public Prescreen() {

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public int getTcg() {
        return tcg;
    }

    public void setTcg(int tcg) {
        this.tcg = tcg;
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

    public int getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(int modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public int getRefinanceIN() {
        return refinanceIN;
    }

    public void setRefinanceIN(int refinanceIN) {
        this.refinanceIN = refinanceIN;
    }

    public Bank getRefinanceInValue() {
        return refinanceInValue;
    }

    public void setRefinanceInValue(Bank refinanceInValue) {
        this.refinanceInValue = refinanceInValue;
    }

    public int getRefinanceOUT() {
        return refinanceOUT;
    }

    public void setRefinanceOUT(int refinanceOUT) {
        this.refinanceOUT = refinanceOUT;
    }

    public Bank getRefinanceOutValue() {
        return refinanceOutValue;
    }

    public void setRefinanceOutValue(Bank refinanceOutValue) {
        this.refinanceOutValue = refinanceOutValue;
    }

    public int getExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(int existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
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
                .append("refinanceIN", refinanceIN)
                .append("refinanceInValue", refinanceInValue)
                .append("refinanceOUT", refinanceOUT)
                .append("refinanceOutValue", refinanceOutValue)
                .append("existingSMECustomer", existingSMECustomer)
                .append("borrowingType", borrowingType)
                .append("bdmChecker", bdmChecker)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("modifyFlag", modifyFlag)
                .toString();
    }
}
