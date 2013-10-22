package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_biz_product_detail")
public class BizProductDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BIZ_PRODUCT_DETAIL_ID", sequenceName = "SEQ_WRK_BIZ_PRODUCT_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BIZ_PRODUCT_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "biz_info_detail_id")
    private BizInfoDetail bizInfoDetail;

    @Column(name = "no")
    private long no;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "percent_sales_volume")
    private BigDecimal percentSalesVolume;

    @Column(name = "percent_ebit")
    private BigDecimal percentEBIT;

    @Column(name = "product_detail")
    private String productDetail;

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

    public BizProductDetail() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getPercentSalesVolume() {
        return percentSalesVolume;
    }

    public void setPercentSalesVolume(BigDecimal percentSalesVolume) {
        this.percentSalesVolume = percentSalesVolume;
    }

    public BigDecimal getPercentEBIT() {
        return percentEBIT;
    }

    public void setPercentEBIT(BigDecimal percentEBIT) {
        this.percentEBIT = percentEBIT;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public BizInfoDetail getBizInfoDetail() {
        return bizInfoDetail;
    }

    public void setBizInfoDetail(BizInfoDetail bizInfoDetail) {
        this.bizInfoDetail = bizInfoDetail;
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
}
