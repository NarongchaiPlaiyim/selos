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
    private String customerPay;
    private BigDecimal limit;
    private BigDecimal premium;
    private BigDecimal morePay;
    private boolean deleteFlag;



    public void reset() {
        this.morePay = BigDecimal.ZERO;
        this.type = BaPaType.BA;
        if(creditType!=null){
            for(int i=0;i<creditType.size();i++){
                this.creditType.get(i).setNoFlag(false);

            }
        }
        this.limit = BigDecimal.ZERO;
        this.premium = BigDecimal.ZERO;
        this.deleteFlag =true;
    }

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

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
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

    public String getCustomerPay() {
        return customerPay;
    }

    public void setCustomerPay(String customerPay) {
        this.customerPay = customerPay;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("type", type)
                .append("creditType", creditType)
                .append("limit",limit)
                .append("premium", premium)
                .append("morePay", morePay)
                .append("customerPay",customerPay)
                .toString();
    }
}
