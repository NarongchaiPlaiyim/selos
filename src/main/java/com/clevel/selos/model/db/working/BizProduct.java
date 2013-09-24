package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 23/9/2556
 * Time: 9:57 น.
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "wrk_biz_product")
public class BizProduct {

    @Id
    @SequenceGenerator(name="SEQ_WRK_BIZ_PRODUCT_ID", sequenceName="SEQ_WRK_BIZ_PRODUCT_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BIZ_PRODUCT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="biz_info_detail_id")
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

    public BizProduct() {
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
}
