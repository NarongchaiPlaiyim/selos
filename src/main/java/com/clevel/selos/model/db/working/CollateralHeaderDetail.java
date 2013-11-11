package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name="wrk_collateral_header")
public class CollateralHeaderDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_COLL_HEADER_ID", sequenceName="SEQ_WRK_COLL_HEADER_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_COLL_HEADER_ID")
    private long id;

    @Column(name="no")
    private int no;

    @Column(name="title_deed")
    private String titleDeed;

    @Column(name="collateral_location")
    private String collateralLocation;

    @Column(name="appraisal_value")
    private BigDecimal appraisalValue;

    @OneToOne
    @JoinColumn(name="collateral_type_id")
    private CollateralType headCollType;

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
    @JoinColumn(name = "collateral_id")
    private CollateralDetail collateralDetail;

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

    public CollateralDetail getCollateralDetail() {
        return collateralDetail;
    }

    public void setCollateralDetail(CollateralDetail collateralDetail) {
        this.collateralDetail = collateralDetail;
    }
}
