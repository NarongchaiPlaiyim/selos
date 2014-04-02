package com.clevel.selos.model.db.master;

import com.clevel.selos.model.db.ext.dwh.Product;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_existing_product_formula")
public class ExistingProductFormula {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "tmb_product_group", length = 10)
    private String productGroup;

    @Column(name = "tmb_product_type_code", length = 3)
    private String productTypeCode;

    @Column(name = "tmb_product_code", length = 50)
    private String productCode;

    @Column(name = "tmb_project_code", length = 10)
    private String projectCode;

    @ManyToOne
    @JoinColumn(name = "product_program_id")
    private ProductProgram productProgram;

    @ManyToOne
    @JoinColumn(name = "credit_type_id")
    private CreditType creditType;

    @ManyToOne
    @JoinColumn(name = "product_segment_id")
    private ProductSegment productSegment;

    @ManyToOne
    @JoinColumn(name = "product_master_id", nullable = true)
    private Product dwhProduct;

    @Column(name = "exposure_method")
    private int exposureMethod;

    @Column(name = "wc_calculate")
    private int wcCalculate;

    @Column(name = "dbr_calculate")
    private int dbrCalculate;

    @Column(name = "dbr_method")
    private int dbrMethod;

    @Column(name = "default_value", columnDefinition="int default 0")
    private int defaultValue;

    @Column(name = "active")
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public ProductSegment getProductSegment() {
        return productSegment;
    }

    public void setProductSegment(ProductSegment productSegment) {
        this.productSegment = productSegment;
    }

    public Product getDwhProduct() {
        return dwhProduct;
    }

    public void setDwhProduct(Product dwhProduct) {
        this.dwhProduct = dwhProduct;
    }

    public int getExposureMethod() {
        return exposureMethod;
    }

    public void setExposureMethod(int exposureMethod) {
        this.exposureMethod = exposureMethod;
    }

    public int getWcCalculate() {
        return wcCalculate;
    }

    public void setWcCalculate(int wcCalculate) {
        this.wcCalculate = wcCalculate;
    }

    public int getDbrCalculate() {
        return dbrCalculate;
    }

    public void setDbrCalculate(int dbrCalculate) {
        this.dbrCalculate = dbrCalculate;
    }

    public int getDbrMethod() {
        return dbrMethod;
    }

    public void setDbrMethod(int dbrMethod) {
        this.dbrMethod = dbrMethod;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productGroup", productGroup)
                .append("productTypeCode", productTypeCode)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("productProgram", productProgram)
                .append("creditType", creditType)
                .append("productSegment", productSegment)
                .append("dwhProduct", dwhProduct)
                .append("exposureMethod", exposureMethod)
                .append("wcCalculate", wcCalculate)
                .append("dbrCalculate", dbrCalculate)
                .append("dbrMethod", dbrMethod)
                .append("defaultValue", defaultValue)
                .append("active", active)
                .toString();
    }
}
