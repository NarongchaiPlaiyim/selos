package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.SubCollateralType;
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
public class NewCollateralHead implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_HEAD_ID", sequenceName = "SEQ_WRK_NEW_COLL_HEAD_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_HEAD_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType  headCollType;

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
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
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
                .toString();
    }
}