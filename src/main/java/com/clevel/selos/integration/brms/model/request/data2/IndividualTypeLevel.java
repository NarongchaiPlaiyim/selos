package com.clevel.selos.integration.brms.model.request.data2;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class IndividualTypeLevel implements Serializable {
    private String personalId;
    private int age;
    private String marriageStatus; //todo : to be enum

    public IndividualTypeLevel() {
    }

    public IndividualTypeLevel(String personalId, int age, String marriageStatus) {
        this.personalId = personalId;
        this.age = age;
        this.marriageStatus = marriageStatus;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("personalId", personalId)
                .append("age", age)
                .append("marriageStatus", marriageStatus)
                .toString();
    }
}
