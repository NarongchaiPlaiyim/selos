package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BizProductDetailView implements Serializable {

    private long no;
    private String productType;
    private BigDecimal percentSalesVolume;
    private BigDecimal percentEBIT;
    private String productDetail;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public BizProductDetailView() {
        reset();
    }

    public void reset() {
        this.percentSalesVolume = BigDecimal.ZERO;
        this.percentEBIT = BigDecimal.ZERO;
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
