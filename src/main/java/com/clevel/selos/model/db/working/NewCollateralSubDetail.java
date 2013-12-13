package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.MortgageType;
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
@Table(name = "wrk_new_collateral_sub_detail")
public class NewCollateralSubDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_SUB_COL_DET_ID", sequenceName = "SEQ_WRK_NEW_SUB_COL_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_SUB_COL_DET_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "sub_collateral_type_id")
    private SubCollateralType  subCollTypeCaption;

    @Column(name = "address")
    private String address;

    @Column(name = "land_office")
    private String landOffice;

    @Column(name = "title_deed")
    private String titleDeed;

    @Column(name = "collateral_owner")
    private String collateralOwner;

    @Column(name = "collateral_owner_aad")
    private String collateralOwnerAAD;

    @Column(name = "appraisal_value")
    private BigDecimal appraisalValue;

    @Column(name = "mortgage_value")
    private BigDecimal mortgageValue;

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
    @JoinColumn(name = "new_collateral_head_detail_id")
    private NewCollateralHeadDetail newCollateralHeadDetail;

    @OneToMany
    @JoinColumn(name = "mortgage_type_id")
    private List<MortgageType> mortgageList;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_detail_id")
    private NewCollateralSubDetail newCollateralSubDetail;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SubCollateralType getSubCollTypeCaption() {
        return subCollTypeCaption;
    }

    public void setSubCollTypeCaption(SubCollateralType subCollTypeCaption) {
        this.subCollTypeCaption = subCollTypeCaption;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandOffice() {
        return landOffice;
    }

    public void setLandOffice(String landOffice) {
        this.landOffice = landOffice;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(String collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public String getCollateralOwnerAAD() {
        return collateralOwnerAAD;
    }

    public void setCollateralOwnerAAD(String collateralOwnerAAD) {
        this.collateralOwnerAAD = collateralOwnerAAD;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
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

    public NewCollateralHeadDetail getNewCollateralHeadDetail() {
        return newCollateralHeadDetail;
    }

    public void setNewCollateralHeadDetail(NewCollateralHeadDetail newCollateralHeadDetail) {
        this.newCollateralHeadDetail = newCollateralHeadDetail;
    }

    public NewCollateralSubDetail getNewCollateralSubDetail() {
        return newCollateralSubDetail;
    }

    public void setNewCollateralSubDetail(NewCollateralSubDetail newCollateralSubDetail) {
        this.newCollateralSubDetail = newCollateralSubDetail;
    }

    public List<MortgageType> getMortgageList() {
        return mortgageList;
    }

    public void setMortgageList(List<MortgageType> mortgageList) {
        this.mortgageList = mortgageList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("subCollTypeCaption", subCollTypeCaption)
                .append("address", address)
                .append("landOffice", landOffice)
                .append("titleDeed", titleDeed)
                .append("collateralOwner", collateralOwner)
                .append("collateralOwnerAAD", collateralOwnerAAD)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}