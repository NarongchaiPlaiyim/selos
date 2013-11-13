package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name ="wrk_sub_collateral_detail")
public class SubCollateralDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_SUB_COL_DETAIL_ID", sequenceName="SEQ_WRK_SUB_COL_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_SUB_COL_DETAIL_ID")
    private long id;

    @Column(name="no")
    private int no;

    @OneToOne
    @JoinColumn(name="sub_collateral_type_id")
    private SubCollateralType subCollateralType;

    @OneToOne
    @JoinColumn(name="collateral_type_id")
    private CollateralType collateralType;

    @Column(name="address")
    private String address;

    @Column(name="land_office")
    private String landOffice;

    @Column(name="title_deed")
    private String titleDeed;

    @Column(name="collateral_owner")
    private String collateralOwner;

    @Column(name="appraisal_value")
    private BigDecimal appraisalValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "collateral_header_id")
    private CollateralHeaderDetail collateralHeaderDetail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public SubCollateralType getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(SubCollateralType subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
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

    public CollateralHeaderDetail getCollateralHeaderDetail() {
        return collateralHeaderDetail;
    }

    public void setCollateralHeaderDetail(CollateralHeaderDetail collateralHeaderDetail) {
        this.collateralHeaderDetail = collateralHeaderDetail;
    }
}
