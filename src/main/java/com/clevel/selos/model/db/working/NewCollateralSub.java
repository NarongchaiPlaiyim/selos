package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CollateralType;
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
@Table(name = "wrk_new_coll_sub")
public class NewCollateralSub implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_ID")
    private long id;

    @Column(name = "coll_id")
    private String collID;

    @Column(name = "head_coll_id")
    private String headCollID;

    @Column(name = "line_no")
    private int lineNo;

    @OneToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType collateralTypeType;

    @OneToOne
    @JoinColumn(name = "sub_collateral_type_id")
    private SubCollateralType  subCollateralType;

    @Column(name = "usage")
    private String usage;

    @Column(name = "type_of_usage")
    private String typeOfUsage;

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
    @JoinColumn(name = "new_collateral_head_id")
    private NewCollateralHead newCollateralHead;

    @OneToMany(mappedBy = "newCollateralSub",cascade=CascadeType.ALL)
    private List<NewCollateralSubMortgage> newCollateralSubMortgageList;

    @OneToMany(mappedBy = "newCollateralSub",cascade=CascadeType.ALL)
    private List<NewCollateralSubOwner> newCollateralSubOwnerList;

    @OneToMany(mappedBy = "newCollateralSub")
    private List<NewCollateralSubRelated> newCollateralSubRelatedList;

    @Column(name = "sub_id")
    private String subId;

    public long getId() {
        return id;
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

    public String getHeadCollID() {
        return headCollID;
    }

    public void setHeadCollID(String headCollID) {
        this.headCollID = headCollID;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
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

    public NewCollateralHead getNewCollateralHead() {
        return newCollateralHead;
    }

    public void setNewCollateralHead(NewCollateralHead newCollateralHead) {
        this.newCollateralHead = newCollateralHead;
    }

    public List<NewCollateralSubMortgage> getNewCollateralSubMortgageList() {
        return newCollateralSubMortgageList;
    }

    public void setNewCollateralSubMortgageList(List<NewCollateralSubMortgage> newCollateralSubMortgageList) {
        this.newCollateralSubMortgageList = newCollateralSubMortgageList;
    }

    public List<NewCollateralSubOwner> getNewCollateralSubOwnerList() {
        return newCollateralSubOwnerList;
    }

    public void setNewCollateralSubOwnerList(List<NewCollateralSubOwner> newCollateralSubOwnerList) {
        this.newCollateralSubOwnerList = newCollateralSubOwnerList;
    }

    public List<NewCollateralSubRelated> getNewCollateralSubRelatedList() {
        return newCollateralSubRelatedList;
    }

    public void setNewCollateralSubRelatedList(List<NewCollateralSubRelated> newCollateralSubRelatedList) {
        this.newCollateralSubRelatedList = newCollateralSubRelatedList;
    }

    public CollateralType getCollateralTypeType() {
        return collateralTypeType;
    }

    public void setCollateralTypeType(CollateralType collateralTypeType) {
        this.collateralTypeType = collateralTypeType;
    }

    public SubCollateralType getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(SubCollateralType subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    public void setTypeOfUsage(String typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("collID", collID)
                .append("headCollID", headCollID)
                .append("lineNo", lineNo)
                .append("collateralTypeType", collateralTypeType)
                .append("subCollateralType", subCollateralType)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
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
                .append("newCollateralHead", newCollateralHead)
                .append("newCollateralSubMortgageList", newCollateralSubMortgageList)
                .append("newCollateralSubOwnerList", newCollateralSubOwnerList)
                .append("newCollateralSubRelatedList", newCollateralSubRelatedList)
                .append("subId", subId)
                .toString();
    }
}