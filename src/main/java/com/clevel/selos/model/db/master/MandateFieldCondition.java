package com.clevel.selos.model.db.master;

import com.clevel.selos.model.MandateConDetailType;
import com.clevel.selos.model.MandateDependConType;
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
    @SequenceGenerator(name = "SEQ_MST_MAN_FIELD_COND", sequenceName = "SEQ_MST_MAN_FIELD_COND", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_MAN_FIELD_COND")
    private long id;

    @Column(name = "condition_type", length = 100)
    @Enumerated(EnumType.ORDINAL)
    private MandateConDetailType mandateConDetailType;

    @Column(name = "condition_name", length = 30)
    private String name;

    @Column(name = "description", length = 200)
    private String conditionDesc;

    @OneToOne
    @JoinColumn(name = "class_id")
    private MandateFieldClass mandateFieldClass;

    @Column(name = "depend_type", columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private MandateDependType dependType;

    @Column(name = "depend_con_type", columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private MandateDependConType dependConType;

    @Column(name = "depend_con_id")
    private long dependCondition = 0;

    @OneToMany(mappedBy = "mandateFieldCondition")
    private List<MandateFieldConditionDetail> mandateFieldConditionDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MandateConDetailType getMandateConDetailType() {
        return mandateConDetailType;
    }

    public void setMandateConDetailType(MandateConDetailType mandateConDetailType) {
        this.mandateConDetailType = mandateConDetailType;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    public MandateFieldClass getMandateFieldClass() {
        return mandateFieldClass;
    }

    public void setMandateFieldClass(MandateFieldClass mandateFieldClass) {
        this.mandateFieldClass = mandateFieldClass;
    }

    public MandateDependType getDependType() {
        return dependType;
    }

    public void setDependType(MandateDependType dependType) {
        this.dependType = dependType;
    }

    public MandateDependConType getDependConType() {
        return dependConType;
    }

    public void setDependConType(MandateDependConType dependConType) {
        this.dependConType = dependConType;
    }

    public long getDependCondition() {
        return dependCondition;
    }

    public void setDependCondition(long dependCondition) {
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
                .append("mandateConditionType", mandateConDetailType)
                .append("name", name)
                .append("conditionDesc", conditionDesc)
                .append("mandateFieldClass", mandateFieldClass)
                .append("dependType", dependType)
                .append("dependConType", dependConType)
                .append("dependCondition", dependCondition)
                .append("mandateFieldConditionDetailList", mandateFieldConditionDetailList)
                .toString();
    }
}
