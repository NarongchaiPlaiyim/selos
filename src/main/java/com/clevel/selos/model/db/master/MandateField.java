package com.clevel.selos.model.db.master;


import com.clevel.selos.model.MandateFieldType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_fields")
public class MandateField implements Serializable{
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "class_name_id")
    private MandateFieldClass mandateFieldClass;

    @Column(name = "parameterized_name", length = 150)
    private String parameterizedName;

    @Column(name = "field_name", length = 50)
    private String fieldName;

    @Column(name = "field_description", length = 100)
    private String fieldDescription;

    @Column(name = "field_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private MandateFieldType mandateFieldType;

    @Column(name = "page_name", length = 100)
    private String page;

    @Column(name = "min_value", length = 100)
    private String minValue;

    @Column(name = "max_value", length = 100)
    private String maxValue;

    @Column(name = "matched_value", length = 100)
    private String matchedValue;

    @Column(name = "not_matched_value", length = 100)
    private String notMatchedValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateFieldClass getMandateFieldClass() {
        return mandateFieldClass;
    }

    public void setMandateFieldClass(MandateFieldClass mandateFieldClass) {
        this.mandateFieldClass = mandateFieldClass;
    }

    public String getParameterizedName() {
        return parameterizedName;
    }

    public void setParameterizedName(String parameterizedName) {
        this.parameterizedName = parameterizedName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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

    public String getNotMatchedValue() {
        return notMatchedValue;
    }

    public void setNotMatchedValue(String notMatchedValue) {
        this.notMatchedValue = notMatchedValue;
    }

    public MandateFieldType getMandateFieldType() {
        return mandateFieldType;
    }

    public void setMandateFieldType(MandateFieldType mandateFieldType) {
        this.mandateFieldType = mandateFieldType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateFieldClass", mandateFieldClass)
                .append("fieldName", fieldName)
                .append("fieldDescription", fieldDescription)
                .append("mandateFieldType", mandateFieldType)
                .append("page", page)
                .append("minValue", minValue)
                .append("maxValue", maxValue)
                .append("matchedValue", matchedValue)
                .append("notMatchedValue", notMatchedValue)
                .toString();
    }
}
