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

    @OneToMany(mappedBy = "newCollateralSubDetail", cascade = CascadeType.ALL)
    private List<NewCollateralSubMortgage> newCollateralSubMortgageList;

    @OneToMany(mappedBy = "newCollateralSubDetail", cascade = CascadeType.ALL)
    private List<NewCollateralSubCustomer> newCollateralSubCustomerList;

    @OneToMany(mappedBy = "newCollateralSubDetail", cascade = CascadeType.ALL)
    private List<NewCollateralSubRelate> newCollateralSubRelateList;

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

    public List<NewCollateralSubMortgage> getNewCollateralSubMortgageList() {
        return newCollateralSubMortgageList;
    }

    public void setNewCollateralSubMortgageList(List<NewCollateralSubMortgage> newCollateralSubMortgageList) {
        this.newCollateralSubMortgageList = newCollateralSubMortgageList;
    }

    public List<NewCollateralSubCustomer> getNewCollateralSubCustomerList() {
        return newCollateralSubCustomerList;
    }

    public void setNewCollateralSubCustomerList(List<NewCollateralSubCustomer> newCollateralSubCustomerList) {
        this.newCollateralSubCustomerList = newCollateralSubCustomerList;
    }

    public List<NewCollateralSubRelate> getNewCollateralSubRelateList() {
        return newCollateralSubRelateList;
    }

    public void setNewCollateralSubRelateList(List<NewCollateralSubRelate> newCollateralSubRelateList) {
        this.newCollateralSubRelateList = newCollateralSubRelateList;
    }
}