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
public class BizProduct {

    private String no;
    private String productType;
    private BigDecimal percentSalesVolumn;
    private BigDecimal percentEBIT;
    private String productDetail;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getPercentSalesVolumn() {
        return percentSalesVolumn;
    }

    public void setPercentSalesVolumn(BigDecimal percentSalesVolumn) {
        this.percentSalesVolumn = percentSalesVolumn;
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

    public void reset(){
        no = "";
        productType ="";
        percentSalesVolumn = new BigDecimal(0.0);;
        percentEBIT = new BigDecimal(0.0);
        productDetail = "";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("productType", productType)
                .append("percentSalesVolumn", percentSalesVolumn)
                .append("percentEBIT", percentEBIT)
                .append("productDetail", productDetail)
                .toString();
    }
}
