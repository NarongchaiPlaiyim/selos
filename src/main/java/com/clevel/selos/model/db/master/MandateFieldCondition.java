package com.clevel.selos.model.db.master;

import com.clevel.selos.model.ConditionType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "mst_mandate_fields_con")
public class MandateFieldCondition implements Serializable{

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "condition_type", length = 100)
    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;

    @Column(name = "description", length = 200)
    private String conditionDesc;

    @Column(name = "class_name", length = 200)
    private String className;

    @OneToMany(mappedBy = "mandateFieldCondition")
    private List<MandateFieldConditionDetail> mandateFieldConditionDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MandateFieldConditionDetail> getMandateFieldConditionDetailList() {
        return mandateFieldConditionDetailList;
    }

    public void setMandateFieldConditionDetailList(List<MandateFieldConditionDetail> mandateFieldConditionDetailList) {
        this.mandateFieldConditionDetailList = mandateFieldConditionDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("conditionType", conditionType)
                .append("conditionDesc", conditionDesc)
                .append("className", className)
                .append("mandateFieldConditionDetailList", mandateFieldConditionDetailList)
                .toString();
    }
}
