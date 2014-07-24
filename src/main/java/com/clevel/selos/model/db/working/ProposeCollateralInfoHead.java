package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_coll_head")
public class ProposeCollateralInfoHead implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_HEAD_ID", sequenceName = "SEQ_WRK_NEW_COLL_HEAD_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_HEAD_ID")
    private long id;

    @Column(name = "propose_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    @OneToOne
    @JoinColumn(name = "potential_collateral_id")
    private PotentialCollateral potentialCollateral;

    @OneToOne
    @JoinColumn(name = "tcg_collateral_type_id")
    private TCGCollateralType collateralType;

    @Column(name = "existing_credit")
    private BigDecimal existingCredit;

    @Column(name = "title_deed")
    private String titleDeed;

    @Column(name = "collateral_location")
    private String collateralLocation;

    @Column(name = "appraisal_value")
    private BigDecimal appraisalValue;

    @OneToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType  headCollType;

    @Column(name = "insurance_company", columnDefinition = "int default 0")
    private int insuranceCompany;

    @ManyToOne
    @JoinColumn(name = "new_collateral_id")
    private ProposeCollateralInfo proposeCollateral;

    @OneToMany(mappedBy = "proposeCollateralHead", cascade = CascadeType.ALL)
    private List<ProposeCollateralInfoSub> proposeCollateralInfoSubList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    /*** For Appraisal Process ***/
    @Column (name = "collateral_char", columnDefinition = "int default 0")
    private int collateralChar;

    @Column (name = "number_of_documents", columnDefinition = "int default 0")
    private int numberOfDocuments;

    @Column (name = "purpose_review_appraisal", columnDefinition = "int default 0")
    private int purposeReviewAppraisal;

    @Column (name = "purpose_new_appraisal", columnDefinition = "int default 0")
    private int purposeNewAppraisal;

    @Column (name = "purpose_review_building", columnDefinition = "int default 0")
    private int purposeReviewBuilding;

    @Column(name = "appraisal_request", columnDefinition = "int default 0")
    private int appraisalRequest;

    /*** For Post - Insurance Premium Quote Process ***/
    @Column(name = "insurance_company_type", columnDefinition = "int default 0")
    private int insuranceComType;

    @Column(name = "existing_insurance_type", columnDefinition = "int default 0")
    private int existingInsuranceType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public TCGCollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(TCGCollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public BigDecimal getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(BigDecimal existingCredit) {
        this.existingCredit = existingCredit;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public CollateralType getHeadCollType() {
        return headCollType;
    }

    public void setHeadCollType(CollateralType headCollType) {
        this.headCollType = headCollType;
    }

    public int getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(int insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public ProposeCollateralInfo getProposeCollateral() {
        return proposeCollateral;
    }

    public void setProposeCollateral(ProposeCollateralInfo proposeCollateral) {
        this.proposeCollateral = proposeCollateral;
    }

    public List<ProposeCollateralInfoSub> getProposeCollateralInfoSubList() {
        return proposeCollateralInfoSubList;
    }

    public void setProposeCollateralInfoSubList(List<ProposeCollateralInfoSub> proposeCollateralInfoSubList) {
        this.proposeCollateralInfoSubList = proposeCollateralInfoSubList;
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

    public int getCollateralChar() {
        return collateralChar;
    }

    public void setCollateralChar(int collateralChar) {
        this.collateralChar = collateralChar;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(int numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public int getPurposeReviewAppraisal() {
        return purposeReviewAppraisal;
    }

    public void setPurposeReviewAppraisal(int purposeReviewAppraisal) {
        this.purposeReviewAppraisal = purposeReviewAppraisal;
    }

    public int getPurposeNewAppraisal() {
        return purposeNewAppraisal;
    }

    public void setPurposeNewAppraisal(int purposeNewAppraisal) {
        this.purposeNewAppraisal = purposeNewAppraisal;
    }

    public int getPurposeReviewBuilding() {
        return purposeReviewBuilding;
    }

    public void setPurposeReviewBuilding(int purposeReviewBuilding) {
        this.purposeReviewBuilding = purposeReviewBuilding;
    }

    public int getAppraisalRequest() {
        return appraisalRequest;
    }

    public void setAppraisalRequest(int appraisalRequest) {
        this.appraisalRequest = appraisalRequest;
    }

    public int getInsuranceComType() {
        return insuranceComType;
    }

    public void setInsuranceComType(int insuranceComType) {
        this.insuranceComType = insuranceComType;
    }

    public int getExistingInsuranceType() {
        return existingInsuranceType;
    }

    public void setExistingInsuranceType(int existingInsuranceType) {
        this.existingInsuranceType = existingInsuranceType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("proposeType", proposeType).
                append("potentialCollateral", potentialCollateral).
                append("collateralType", collateralType).
                append("existingCredit", existingCredit).
                append("titleDeed", titleDeed).
                append("collateralLocation", collateralLocation).
                append("appraisalValue", appraisalValue).
                append("headCollType", headCollType).
                append("insuranceCompany", insuranceCompany).
                append("proposeCollateral", proposeCollateral).
                append("proposeCollateralInfoSubList", proposeCollateralInfoSubList).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("collateralChar", collateralChar).
                append("numberOfDocuments", numberOfDocuments).
                append("purposeReviewAppraisal", purposeReviewAppraisal).
                append("purposeNewAppraisal", purposeNewAppraisal).
                append("purposeReviewBuilding", purposeReviewBuilding).
                append("appraisalRequest", appraisalRequest).
                append("insuranceComType", insuranceComType).
                append("existingInsuranceType", existingInsuranceType).
                toString();
    }
}