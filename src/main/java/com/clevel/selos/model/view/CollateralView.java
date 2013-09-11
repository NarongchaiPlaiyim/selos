package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;

public class CollateralView {
    @Inject
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    private BigDecimal id;
    private CollateralType collateralType;
    private String     collateralTypeName;
    private BigDecimal collateralAmount;

    public CollateralView() {
    }

    public String getCollateralTypeName() {
        return collateralTypeName;
    }

    public void setCollateralTypeName(String collateralTypeName) {
        this.collateralTypeName = collateralTypeName;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public BigDecimal getCollateralAmount() {
        return collateralAmount;
    }

    public void setCollateralAmount(BigDecimal collateralAmount) {
        this.collateralAmount = collateralAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("collateralTypeName", collateralTypeName)
                .append("collateralAmount", collateralAmount)
                .toString();
    }
}
