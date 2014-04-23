package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_coll_head")
public class NewCollateralHead implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_HEAD_ID", sequenceName = "SEQ_WRK_NEW_COLL_HEAD_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_HEAD_ID")
    private long id;

    @Column(name = "coll_id")
    private String collID;

    @OneToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType  headCollType;

    @OneToOne
    @JoinColumn(name = "tcg_collateral_type_id")
    private TCGCollateralType headTcgCollType;

    @OneToOne
    @JoinColumn(name = "sub_coll_type_id")
    private SubCollateralType subCollateralType;

    @OneToOne
    @JoinColumn(name = "potential_collateral_id")
    private PotentialCollateral  potential;

    @Column(name = "title_deed")
    private String titleDeed;

    @Column(name = "collateral_location")
    private String collateralLocation;

    @OneToOne
    @JoinColumn(name = "coll_type_ltv_id")
    private CollateralType collTypePercentLTV;

    @Column(name = "existing_credit")
    private BigDecimal existingCredit;

    @Column(name = "insurance_company")
    private int insuranceCompany;

    @Column(name = "appraisal_value")
    private BigDecimal appraisalValue;

    @ManyToOne
    @JoinColumn(name = "new_collateral_id")
    private NewCollateral newCollateral;

    @OneToMany(mappedBy = "newCollateralHead", cascade = CascadeType.ALL)
    private List<NewCollateralSub> newCollateralSubList;

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
    @Column (name = "collateral_char")
    private int collateralChar;

    @Column (name = "number_of_documents", nullable=false, columnDefinition="int default 0")
    private int numberOfDocuments;

    @Column (name = "purpose_review_appraisal", nullable=false, columnDefinition="int default 0")
    private int purposeReviewAppraisal;

    @Column (name = "purpose_new_appraisal", nullable=false, columnDefinition="int default 0")
    private int purposeNewAppraisal;

    @Column (name = "purpose_review_building", nullable=false, columnDefinition="int default 0")
    private int purposeReviewBuilding;

    @Column(name = "appraisal_request", nullable=false, columnDefinition="int default 0")
    private int appraisalRequest;

    @Column(name = "propose_type")
    private String proposeType;


    /*** For Post - Insurance Premium Quote Process ***/
    @Column(name = "insurance_company_type")
    private int insuranceComType;

    @Column(name = "existing_insurance_type", columnDefinition = "")
    private int existingInsuranceType;

    public long getId() {
        return id;
    }

    public int getAppraisalRequest() {
        return appraisalRequest;
    }

    public void setAppraisalRequest(int appraisalRequest) {
        this.appraisalRequest = appraisalRequest;
    }

    public String getProposeType() {
        return proposeType;
    }

    public void setProposeType(String proposeType) {
        this.proposeType = proposeType;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCollID() {
        return collID;
    }

    public void setCollID(String collID) {
        this.collID = collID;
    }

    public CollateralType getHeadCollType() {
        return headCollType;
    }

    public void setHeadCollType(CollateralType headCollType) {
        this.headCollType = headCollType;
    }

    public PotentialCollateral getPotential() {
        return potential;
    }

    public void setPotential(PotentialCollateral potential) {
        this.potential = potential;
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

    public CollateralType getCollTypePercentLTV() {
        return collTypePercentLTV;
    }

    public void setCollTypePercentLTV(CollateralType collTypePercentLTV) {
        this.collTypePercentLTV = collTypePercentLTV;
    }

    public BigDecimal getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(BigDecimal existingCredit) {
        this.existingCredit = existingCredit;
    }

    public int getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(int insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
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

    public NewCollateral getNewCollateral() {
        return newCollateral;
    }

    public void setNewCollateral(NewCollateral newCollateral) {
        this.newCollateral = newCollateral;
    }

    public SubCollateralType getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(SubCollateralType subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    public List<NewCollateralSub> getNewCollateralSubList() {
        return newCollateralSubList;
    }

    public void setNewCollateralSubList(List<NewCollateralSub> newCollateralSubList) {
        this.newCollateralSubList = newCollateralSubList;
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

    public TCGCollateralType getHeadTcgCollType() {
        return headTcgCollType;
    }

    public void setHeadTcgCollType(TCGCollateralType headTcgCollType) {
        this.headTcgCollType = headTcgCollType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("collID", collID)
                .append("headCollType", headCollType)
                .append("subCollateralType", subCollateralType)
                .append("potential", potential)
                .append("titleDeed", titleDeed)
                .append("collateralLocation", collateralLocation)
                .append("collTypePercentLTV", collTypePercentLTV)
                .append("existingCredit", existingCredit)
                .append("insuranceCompany", insuranceCompany)
                .append("appraisalValue", appraisalValue)
                .append("newCollateral", newCollateral)
                .append("newCollateralSubList", newCollateralSubList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("collateralChar", collateralChar)
                .append("numberOfDocuments", numberOfDocuments)
                .append("purposeReviewAppraisal", purposeReviewAppraisal)
                .append("purposeNewAppraisal", purposeNewAppraisal)
                .append("purposeReviewBuilding", purposeReviewBuilding)
                .append("appraisalRequest", appraisalRequest)
                .append("proposeType", proposeType)
                .append("insuranceComType", insuranceComType)
                .append("existingInsuranceType", existingInsuranceType)
                .toString();
    }
}