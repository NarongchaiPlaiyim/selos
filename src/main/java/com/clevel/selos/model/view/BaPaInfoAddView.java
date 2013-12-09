package com.clevel.selos.model.view;


import com.clevel.selos.model.BaPaType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BaPaInfoAddView implements Serializable{


    private BaPaType type;
    private List<CreditTypeDetailView>creditType;
    private BigDecimal premium;
    private BigDecimal morePay;

    public BaPaType getType() {
        return type;
    }

    public void setType(BaPaType type) {
        this.type = type;
    }

    public List<CreditTypeDetailView> getCreditType() {
        return creditType;
    }

    public void setCreditType(List<CreditTypeDetailView> creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public BigDecimal getMorePay() {
        return morePay;
    }

    public void setMorePay(BigDecimal morePay) {
        this.morePay = morePay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("type", type)
                .append("creditType", creditType)
                .append("premium", premium)
                .append("morePay", morePay)
                .toString();
    }
}
