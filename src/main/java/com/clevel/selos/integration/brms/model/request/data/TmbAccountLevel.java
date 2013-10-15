package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class TmbAccountLevel implements Serializable {
    private String botClass; //todo : to be enum
    private boolean activeFlag;
    private String dataSource; //todo : to be enum
    private String accountRef; //todo : to be enum
    private String custToAccountRelationship; //todo : to be enum
    private int numberOfDayPrincipalPastDue;
    private int numberOfDayInterestPastDue;
    private String cardBlockCode; //todo : to be enum

    public TmbAccountLevel() {
    }

    public TmbAccountLevel(String botClass, boolean activeFlag, String dataSource, String accountRef, String custToAccountRelationship, int numberOfDayPrincipalPastDue, int numberOfDayInterestPastDue, String cardBlockCode) {
        this.botClass = botClass;
        this.activeFlag = activeFlag;
        this.dataSource = dataSource;
        this.accountRef = accountRef;
        this.custToAccountRelationship = custToAccountRelationship;
        this.numberOfDayPrincipalPastDue = numberOfDayPrincipalPastDue;
        this.numberOfDayInterestPastDue = numberOfDayInterestPastDue;
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

    public String getCustToAccountRelationship() {
        return custToAccountRelationship;
    }

    public void setCustToAccountRelationship(String custToAccountRelationship) {
        this.custToAccountRelationship = custToAccountRelationship;
    }

    public int getNumberOfDayPrincipalPastDue() {
        return numberOfDayPrincipalPastDue;
    }

    public void setNumberOfDayPrincipalPastDue(int numberOfDayPrincipalPastDue) {
        this.numberOfDayPrincipalPastDue = numberOfDayPrincipalPastDue;
    }

    public int getNumberOfDayInterestPastDue() {
        return numberOfDayInterestPastDue;
    }

    public void setNumberOfDayInterestPastDue(int numberOfDayInterestPastDue) {
        this.numberOfDayInterestPastDue = numberOfDayInterestPastDue;
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
                .append("dataSource", dataSource)
                .append("accountRef", accountRef)
                .append("custToAccountRelationship", custToAccountRelationship)
                .append("numberOfDayPrincipalPastDue", numberOfDayPrincipalPastDue)
                .append("numberOfDayInterestPastDue", numberOfDayInterestPastDue)
                .append("cardBlockCode", cardBlockCode)
                .toString();
    }
}
