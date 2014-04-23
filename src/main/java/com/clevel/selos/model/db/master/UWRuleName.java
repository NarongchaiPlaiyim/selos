package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_uw_rule_name")
public class UWRuleName implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "brms_code", length = 100)
    private String brmsCode;

    @Column(name = "final_rate_flag", length = 1, columnDefinition = "int default 0")
    private boolean finalRateFlag;

    @ManyToOne
    @JoinColumn(name = "rule_group_id")
    private UWRuleGroup ruleGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public boolean isFinalRateFlag() {
        return finalRateFlag;
    }

    public void setFinalRateFlag(boolean finalRateFlag) {
        this.finalRateFlag = finalRateFlag;
    }

    public UWRuleGroup getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(UWRuleGroup ruleGroup) {
        this.ruleGroup = ruleGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("finalRateFlag", finalRateFlag)
                .append("ruleGroup", ruleGroup)
                .toString();
    }
}
