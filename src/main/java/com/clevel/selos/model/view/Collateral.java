package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 8/16/13
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Collateral {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

    private BigDecimal id;
    private String     collateralTypeName;
    private BigDecimal collateralAmount;

    public Collateral() {
    }

    public String getCollateralTypeName() {
        return collateralTypeName;
    }

    public void setCollateralTypeName(String collateralTypeName) {
        this.collateralTypeName = collateralTypeName;
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
