package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 6/9/2556
 * Time: 11:05 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizProductView {

    private long no;
    private String productType;
    private BigDecimal percentSalesVolume;
    private BigDecimal percentEBIT;
    private String productDetail;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("productType", productType)
                .append("percentSalesVolume", percentSalesVolume)
                .append("percentEBIT", percentEBIT)
                .append("productDetail", productDetail)
                .toString();
    }
}
