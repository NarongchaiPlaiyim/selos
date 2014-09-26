package com.clevel.selos.model.view.master;


import com.clevel.selos.model.MandateFieldType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UserSysParameterKey;
import com.clevel.selos.model.view.ActionView;
import com.clevel.selos.model.view.StepView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MandateFieldView implements Serializable{
    private long id;
    private MandateFieldClassView mandateFieldClassView;
    private String fieldName;
    private String fieldDesc;
    private String page;
    private MandateFieldType mandateFieldType;
    private boolean checkFieldDetail;
    private String minValue;
    private String maxValue;
    private String matchedValue;
    private int matchedEmpty;
    private String notMatchedValue;
    private int notMatchedEmpty;
    private boolean needUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateFieldClassView getMandateFieldClassView() {
        return mandateFieldClassView;
    }

    public void setMandateFieldClassView(MandateFieldClassView mandateFieldClassView) {
        this.mandateFieldClassView = mandateFieldClassView;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public MandateFieldType getMandateFieldType() {
        return mandateFieldType;
    }

    public void setMandateFieldType(MandateFieldType mandateFieldType) {
        this.mandateFieldType = mandateFieldType;
    }

    public boolean isCheckFieldDetail() {
        return checkFieldDetail;
    }

    public void setCheckFieldDetail(boolean checkFieldDetail) {
        this.checkFieldDetail = checkFieldDetail;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMatchedValue() {
        return matchedValue;
    }

    public void setMatchedValue(String matchedValue) {
        this.matchedValue = matchedValue;
    }

    public int getMatchedEmpty() {
        return matchedEmpty;
    }

    public void setMatchedEmpty(int matchedEmpty) {
        this.matchedEmpty = matchedEmpty;
    }

    public String getNotMatchedValue() {
        return notMatchedValue;
    }

    public void setNotMatchedValue(String notMatchedValue) {
        this.notMatchedValue = notMatchedValue;
    }

    public int getNotMatchedEmpty() {
        return notMatchedEmpty;
    }

    public void setNotMatchedEmpty(int notMatchedEmpty) {
        this.notMatchedEmpty = notMatchedEmpty;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public void updateValues(MandateFieldView view){
        id = view.id;
        fieldName = view.fieldName;
        fieldDesc = view.fieldDesc;
        page = view.page;
        minValue = view.minValue;
        maxValue = view.maxValue;
        checkFieldDetail = view.checkFieldDetail;

        matchedEmpty = view.matchedEmpty;
        if(view.matchedEmpty == RadioValue.YES.value()){
            matchedValue = UserSysParameterKey.STATIC_EMPTY.key();
        } else {
            matchedValue = view.matchedValue;
        }

        notMatchedEmpty = view.notMatchedEmpty;
        if(view.notMatchedEmpty == RadioValue.YES.value()){
            notMatchedValue = UserSysParameterKey.STATIC_EMPTY.key();
        } else {
            notMatchedValue = view.notMatchedValue;
        }
        needUpdate = view.needUpdate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateFieldClassView", mandateFieldClassView)
                .append("fieldName", fieldName)
                .append("fieldDesc", fieldDesc)
                .append("page", page)
                .append("mandateFieldType", mandateFieldType)
                .append("minValue", minValue)
                .append("maxValue", maxValue)
                .append("matchedValue", matchedValue)
                .append("matchedEmpty", matchedEmpty)
                .append("notMatchedValue", notMatchedValue)
                .append("notMatchedEmpty", notMatchedEmpty)
                .append("needUpdate", needUpdate)
                .toString();
    }


}
