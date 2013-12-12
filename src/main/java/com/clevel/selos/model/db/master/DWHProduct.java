package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mst_dwh_product")
public class DWHProduct {
    @Id
    @Column(name = "prod_master_key")
    private int prodMasterKey;

    @Column(name = "prod_key", length = 50)
    private String prodKey;

    @Column(name = "prod_group_l1", length = 300)
    private String prodGroupL1;

    @Column(name = "prod_group_l2", length = 300)
    private String prodGroupL2;

    @Column(name = "prod_group_l3", length = 300)
    private String prodGroupL3;

    @Column(name = "prod_group_l4", length = 300)
    private String prodGroupL4;

    @Column(name = "prod_group_l5", length = 300)
    private String prodGroupL5;

    @Column(name = "tmb_product_code", length = 50)
    private String tmbProductCode;

    @Column(name = "tmb_project_code", length = 10)
    private String tmbProjectCode;

    @Column(name = "tmb_type", length = 10)
    private String tmbType;

    @Column(name = "data_source", length = 10)
    private String dataSource;

    @Column(name = "tmb_rm_ref", length = 20)
    private String tmbRMRef;

    @Column(name = "tmb_ext_product_type_cd", length = 5)
    private String tmbExtProductTypeCD;

    @Column(name = "rec_delete_flag", length = 1)
    private String deleteFlag;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rec_start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rec_end_date")
    private Date endDate;

    public int getProdMasterKey() {
        return prodMasterKey;
    }

    public void setProdMasterKey(int prodMasterKey) {
        this.prodMasterKey = prodMasterKey;
    }

    public String getProdKey() {
        return prodKey;
    }

    public void setProdKey(String prodKey) {
        this.prodKey = prodKey;
    }

    public String getProdGroupL1() {
        return prodGroupL1;
    }

    public void setProdGroupL1(String prodGroupL1) {
        this.prodGroupL1 = prodGroupL1;
    }

    public String getProdGroupL2() {
        return prodGroupL2;
    }

    public void setProdGroupL2(String prodGroupL2) {
        this.prodGroupL2 = prodGroupL2;
    }

    public String getProdGroupL3() {
        return prodGroupL3;
    }

    public void setProdGroupL3(String prodGroupL3) {
        this.prodGroupL3 = prodGroupL3;
    }

    public String getProdGroupL4() {
        return prodGroupL4;
    }

    public void setProdGroupL4(String prodGroupL4) {
        this.prodGroupL4 = prodGroupL4;
    }

    public String getProdGroupL5() {
        return prodGroupL5;
    }

    public void setProdGroupL5(String prodGroupL5) {
        this.prodGroupL5 = prodGroupL5;
    }

    public String getTmbProductCode() {
        return tmbProductCode;
    }

    public void setTmbProductCode(String tmbProductCode) {
        this.tmbProductCode = tmbProductCode;
    }

    public String getTmbProjectCode() {
        return tmbProjectCode;
    }

    public void setTmbProjectCode(String tmbProjectCode) {
        this.tmbProjectCode = tmbProjectCode;
    }

    public String getTmbType() {
        return tmbType;
    }

    public void setTmbType(String tmbType) {
        this.tmbType = tmbType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getTmbRMRef() {
        return tmbRMRef;
    }

    public void setTmbRMRef(String tmbRMRef) {
        this.tmbRMRef = tmbRMRef;
    }

    public String getTmbExtProductTypeCD() {
        return tmbExtProductTypeCD;
    }

    public void setTmbExtProductTypeCD(String tmbExtProductTypeCD) {
        this.tmbExtProductTypeCD = tmbExtProductTypeCD;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("prodMasterKey", prodMasterKey)
                .append("prodKey", prodKey)
                .append("prodGroupL1", prodGroupL1)
                .append("prodGroupL2", prodGroupL2)
                .append("prodGroupL3", prodGroupL3)
                .append("prodGroupL4", prodGroupL4)
                .append("prodGroupL5", prodGroupL5)
                .append("tmbProductCode", tmbProductCode)
                .append("tmbProjectCode", tmbProjectCode)
                .append("tmbType", tmbType)
                .append("dataSource", dataSource)
                .append("tmbRMRef", tmbRMRef)
                .append("tmbExtProductTypeCD", tmbExtProductTypeCD)
                .append("deleteFlag", deleteFlag)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .toString();
    }
}
