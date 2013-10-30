package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class AttributeTypeLevelBorrower implements Serializable {
    private String botClass; //todo : to be enum
    private boolean activeFlag;
    private String accountRef; //todo : to be enum
    private String custToAccountRelationship; //todo : to be enum
    private BigDecimal numberOfMonthPrincipalAndInterestPastDue;
    private BigDecimal numberOfMonthPrincipalAndInterestPastDueOfTDRAccount;
    private BigDecimal numberODayPrincipalPastDue;
    private BigDecimal numberODayInterestPastDue;
    private String cardBlockCode; //todo : to be enum


    public AttributeTypeLevelBorrower() {
    }

    public AttributeTypeLevelBorrower(String botClass, boolean activeFlag, String accountRef, String custToAccountRelationship, BigDecimal numberOfMonthPrincipalAndInterestPastDue, BigDecimal numberOfMonthPrincipalAndInterestPastDueOfTDRAccount, BigDecimal numberODayPrincipalPastDue, BigDecimal numberODayInterestPastDue, String cardBlockCode) {
        this.botClass = botClass;
        this.activeFlag = activeFlag;
        this.accountRef = accountRef;
        this.custToAccountRelationship = custToAccountRelationship;
        this.numberOfMonthPrincipalAndInterestPastDue = numberOfMonthPrincipalAndInterestPastDue;
        this.numberOfMonthPrincipalAndInterestPastDueOfTDRAccount = numberOfMonthPrincipalAndInterestPastDueOfTDRAccount;
        this.numberODayPrincipalPastDue = numberODayPrincipalPastDue;
        this.numberODayInterestPastDue = numberODayInterestPastDue;
        this.cardBlockCode = cardBlockCode;
    }

    public String getBotClass() {
        return botClass;
    }

    public void setBotClass(String botClass) {
        this.botClass = botClass;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public String getCustToAccountRelationship() {
        return custToAccountRelationship;
    }

    public void setCustToAccountRelationship(String custToAccountRelationship) {
        this.custToAccountRelationship = custToAccountRelationship;
    }

    public BigDecimal getNumberOfMonthPrincipalAndInterestPastDue() {
        return numberOfMonthPrincipalAndInterestPastDue;
    }

    public void setNumberOfMonthPrincipalAndInterestPastDue(BigDecimal numberOfMonthPrincipalAndInterestPastDue) {
        this.numberOfMonthPrincipalAndInterestPastDue = numberOfMonthPrincipalAndInterestPastDue;
    }

    public BigDecimal getNumberOfMonthPrincipalAndInterestPastDueOfTDRAccount() {
        return numberOfMonthPrincipalAndInterestPastDueOfTDRAccount;
    }

    public void setNumberOfMonthPrincipalAndInterestPastDueOfTDRAccount(BigDecimal numberOfMonthPrincipalAndInterestPastDueOfTDRAccount) {
        this.numberOfMonthPrincipalAndInterestPastDueOfTDRAccount = numberOfMonthPrincipalAndInterestPastDueOfTDRAccount;
    }

    public BigDecimal getNumberODayPrincipalPastDue() {
        return numberODayPrincipalPastDue;
    }

    public void setNumberODayPrincipalPastDue(BigDecimal numberODayPrincipalPastDue) {
        this.numberODayPrincipalPastDue = numberODayPrincipalPastDue;
    }

    public BigDecimal getNumberODayInterestPastDue() {
        return numberODayInterestPastDue;
    }

    public void setNumberODayInterestPastDue(BigDecimal numberODayInterestPastDue) {
        this.numberODayInterestPastDue = numberODayInterestPastDue;
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
                .append("botClass", botClass)
                .append("activeFlag", activeFlag)
                .append("accountRef", accountRef)
                .append("custToAccountRelationship", custToAccountRelationship)
                .append("numberOfMonthPrincipalAndInterestPastDue", numberOfMonthPrincipalAndInterestPastDue)
                .append("numberOfMonthPrincipalAndInterestPastDueOfTDRAccount", numberOfMonthPrincipalAndInterestPastDueOfTDRAccount)
                .append("numberODayPrincipalPastDue", numberODayPrincipalPastDue)
                .append("numberODayInterestPastDue", numberODayInterestPastDue)
                .append("cardBlockCode", cardBlockCode)
                .toString();
    }
}
