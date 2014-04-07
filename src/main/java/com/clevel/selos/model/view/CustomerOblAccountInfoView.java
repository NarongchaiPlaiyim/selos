package com.clevel.selos.model.view;

import com.clevel.selos.model.TMBTDRFlag;
import com.clevel.selos.model.db.working.Customer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerOblAccountInfoView implements Serializable{

    private long id;
    private boolean accountActiveFlag;
    private CustomerInfoSimpleView customerInfoSimpleView;
    private String dataSource;
    private String accountRef;
    private String cusRelAccount;
    private TMBTDRFlag tdrFlag;
    private BigDecimal numMonthIntPastDue;
    private BigDecimal numMonthIntPastDueTDRAcc;
    private BigDecimal tmbDelPriDay;
    private BigDecimal tmbDelIntDay;
    private String cardBlockCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAccountActiveFlag() {
        return accountActiveFlag;
    }

    public void setAccountActiveFlag(boolean accountActiveFlag) {
        this.accountActiveFlag = accountActiveFlag;
    }

    public CustomerInfoSimpleView getCustomerInfoSimpleView() {
        return customerInfoSimpleView;
    }

    public void setCustomerInfoSimpleView(CustomerInfoSimpleView customerInfoSimpleView) {
        this.customerInfoSimpleView = customerInfoSimpleView;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public String getCusRelAccount() {
        return cusRelAccount;
    }

    public void setCusRelAccount(String cusRelAccount) {
        this.cusRelAccount = cusRelAccount;
    }

    public TMBTDRFlag getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(TMBTDRFlag tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public BigDecimal getNumMonthIntPastDue() {
        return numMonthIntPastDue;
    }

    public void setNumMonthIntPastDue(BigDecimal numMonthIntPastDue) {
        this.numMonthIntPastDue = numMonthIntPastDue;
    }

    public BigDecimal getNumMonthIntPastDueTDRAcc() {
        return numMonthIntPastDueTDRAcc;
    }

    public void setNumMonthIntPastDueTDRAcc(BigDecimal numMonthIntPastDueTDRAcc) {
        this.numMonthIntPastDueTDRAcc = numMonthIntPastDueTDRAcc;
    }

    public BigDecimal getTmbDelPriDay() {
        return tmbDelPriDay;
    }

    public void setTmbDelPriDay(BigDecimal tmbDelPriDay) {
        this.tmbDelPriDay = tmbDelPriDay;
    }

    public BigDecimal getTmbDelIntDay() {
        return tmbDelIntDay;
    }

    public void setTmbDelIntDay(BigDecimal tmbDelIntDay) {
        this.tmbDelIntDay = tmbDelIntDay;
    }

    public String getCardBlockCode() {
        return cardBlockCode;
    }

    public void setCardBlockCode(String cardBlockCode) {
        this.cardBlockCode = cardBlockCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountActiveFlag", accountActiveFlag)
                .append("customerInfoSimpleView", customerInfoSimpleView)
                .append("dataSource", dataSource)
                .append("accountRef", accountRef)
                .append("cusRelAccount", cusRelAccount)
                .append("tdrFlag", tdrFlag)
                .append("numMonthIntPastDue", numMonthIntPastDue)
                .append("numMonthIntPastDueTDRAcc", numMonthIntPastDueTDRAcc)
                .append("tmbDelPriDay", tmbDelPriDay)
                .append("tmbDelIntDay", tmbDelIntDay)
                .append("cardBlockCode", cardBlockCode)
                .toString();
    }
}
