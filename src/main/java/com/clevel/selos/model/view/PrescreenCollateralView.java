package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;

public class PrescreenCollateralView implements Serializable {
    @Inject
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    private long id;
    private CollateralType collateralType;
    private String     collateralTypeName;
    private BigDecimal collateralAmount;

    public PrescreenCollateralView() {
    }

    public void reset(){
        this.id = 0;
        this.collateralType = new CollateralType();
        this.collateralTypeName = "";
        this.collateralAmount = new BigDecimal(0);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
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
                .append("msg", msg)
                .append("id", id)
                .append("collateralType", collateralType)
                .append("collateralTypeName", collateralTypeName)
                .append("collateralAmount", collateralAmount)
                .toString();
    }
}
