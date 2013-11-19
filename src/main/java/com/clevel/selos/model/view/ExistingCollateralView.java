package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExistingCollateralView implements Serializable {
    private long id;
    private List<ExistingCollateralDetailView> collateralDetailViewList;
    private BigDecimal totalAppraisalValue;
    private BigDecimal totalMortgageValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ExistingCollateralDetailView> getCollateralDetailViewList() {
        return collateralDetailViewList;
    }

    public void setCollateralDetailViewList(List<ExistingCollateralDetailView> collateralDetailViewList) {
        this.collateralDetailViewList = collateralDetailViewList;
    }

    public BigDecimal getTotalAppraisalValue() {
        return totalAppraisalValue;
    }

    public void setTotalAppraisalValue(BigDecimal totalAppraisalValue) {
        this.totalAppraisalValue = totalAppraisalValue;
    }

    public BigDecimal getTotalMortgageValue() {
        return totalMortgageValue;
    }

    public void setTotalMortgageValue(BigDecimal totalMortgageValue) {
        this.totalMortgageValue = totalMortgageValue;
    }
}
