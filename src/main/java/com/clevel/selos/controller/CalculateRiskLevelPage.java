package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.safewatchandrosc.personal.CalculateRiskLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "calculateRiskLevelPage")

public class CalculateRiskLevelPage implements Serializable {
    private String reqId;
    private String productCode = "SLOS";
    private String acronym = "3100300390029";
    private String selectorFlag = "AAII";
    private String firstName = "พสุธร";
    private String lastName = "กุญชร";
    private String cardId;
    private String result;

    @Inject
    @SELOS
    Logger log;

    @Inject
    CalculateRiskLevel calculateRiskLevel;

    @Inject
    public CalculateRiskLevelPage() {
    }

    public void onSubmit() {
        log.info("========================================= onSubmit()");
        result = "asdfasdfasdfasfd";
        calculateRiskLevel.process(this);
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getSelectorFlag() {
        return selectorFlag;
    }

    public void setSelectorFlag(String selectorFlag) {
        this.selectorFlag = selectorFlag;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("reqId", reqId)
                .append("productCode", productCode)
                .append("acronym", acronym)
                .append("selectorFlag", selectorFlag)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("cardId", cardId)
                .append("result", result)
                .append("calculateRiskLevel", calculateRiskLevel)
                .toString();
    }
}
