package com.clevel.selos.model.db.working;

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
public class ProposeCollateralInfoSub implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "sub_collateral_type_id")
    private SubCollateralType subCollateralType;

    @Column(name = "title_deed")
    private String titleDeed;

    @Column(name = "address")
    private String address;

    @Column(name = "land_office")
    private String landOffice;

    @Column(name = "collateral_owner_aad")
    private String collateralOwnerAAD;

    @Column(name = "appraisal_value")
    private BigDecimal appraisalValue;

    @Column(name = "mortgage_value")
    private BigDecimal mortgageValue;

    @Column(name = "sub_id")
    private String subId;

    @ManyToOne
    @JoinColumn(name = "new_collateral_head_id")
    private ProposeCollateralInfoHead proposeCollateralHead;

    @OneToMany(mappedBy = "proposeCollateralSub", cascade = CascadeType.ALL)
    private List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList;

    @OneToMany(mappedBy = "proposeCollateralSub", cascade = CascadeType.ALL)
    private List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList;

    @OneToMany(mappedBy = "proposeCollateralSub", cascade = CascadeType.ALL)
    private List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @Column (name = "coms", columnDefinition = "int default 0")
    private int coms;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SubCollateralType getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(SubCollateralType subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
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

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public ProposeCollateralInfoHead getProposeCollateralHead() {
        return proposeCollateralHead;
    }

    public void setProposeCollateralHead(ProposeCollateralInfoHead proposeCollateralHead) {
        this.proposeCollateralHead = proposeCollateralHead;
    }

    public List<ProposeCollateralSubOwner> getProposeCollateralSubOwnerList() {
        return proposeCollateralSubOwnerList;
    }

    public void setProposeCollateralSubOwnerList(List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList) {
        this.proposeCollateralSubOwnerList = proposeCollateralSubOwnerList;
    }

    public List<ProposeCollateralSubMortgage> getProposeCollateralSubMortgageList() {
        return proposeCollateralSubMortgageList;
    }

    public void setProposeCollateralSubMortgageList(List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList) {
        this.proposeCollateralSubMortgageList = proposeCollateralSubMortgageList;
    }

    public List<ProposeCollateralSubRelated> getProposeCollateralSubRelatedList() {
        return proposeCollateralSubRelatedList;
    }

    public void setProposeCollateralSubRelatedList(List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList) {
        this.proposeCollateralSubRelatedList = proposeCollateralSubRelatedList;
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

    public int getComs() {
        return coms;
    }

    public void setComs(int coms) {
        this.coms = coms;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("subCollateralType", subCollateralType)
                .append("titleDeed", titleDeed)
                .append("address", address)
                .append("landOffice", landOffice)
                .append("collateralOwnerAAD", collateralOwnerAAD)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("subId", subId)
                .append("proposeCollateralHead", proposeCollateralHead)
                .append("proposeCollateralSubOwnerList", proposeCollateralSubOwnerList)
                .append("proposeCollateralSubMortgageList", proposeCollateralSubMortgageList)
                .append("proposeCollateralSubRelatedList", proposeCollateralSubRelatedList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("coms", coms)
                .toString();
    }
}