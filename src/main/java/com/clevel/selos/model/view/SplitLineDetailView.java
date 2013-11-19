package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.ProductProgram;

import java.math.BigDecimal;

public class SplitLineDetailView {
    private int no;
    private ProductProgram productProgram;
    private BigDecimal limit;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }
}
