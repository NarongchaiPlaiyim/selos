package com.clevel.selos.model.db.master;

import com.clevel.selos.model.MandateConditionType;
import com.clevel.selos.model.MandateDependType;
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
    private MandateConditionType mandateConditionType;

    @Column(name = "description", length = 200)
    private String conditionDesc;

    @Column(name = "class_name", length = 200)
    private String className;

    @Column(name = "depend_type", length = 100)
    @Enumerated(EnumType.STRING)
    private MandateDependType dependType;

    @Column(name = "depent_con_id", length = 100)
    private MandateFieldCondition dependCondition;

    @OneToMany(mappedBy = "mandateFieldCondition")
    private List<MandateFieldConditionDetail> mandateFieldConditionDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateConditionType getMandateConditionType() {
        return mandateConditionType;
    }

    public void setMandateConditionType(MandateConditionType mandateConditionType) {
        this.mandateConditionType = mandateConditionType;
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

    public MandateDependType getDependType() {
        return dependType;
    }

    public void setDependType(MandateDependType dependType) {
        this.dependType = dependType;
    }

    public MandateFieldCondition getDependCondition() {
        return dependCondition;
    }

    public void setDependCondition(MandateFieldCondition dependCondition) {
        this.dependCondition = dependCondition;
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
                .append("mandateConditionType", mandateConditionType)
                .append("conditionDesc", conditionDesc)
                .append("className", className)
                .append("mandateFieldConditionDetailList", mandateFieldConditionDetailList)
                .toString();
    }
}
