package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_rule_name")
public class UWRuleName {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "brms_code", length = 100)
    private String brmsCode;

    @ManyToOne
    @JoinColumn(name = "rule_group")
    private UWRuleGroup ruleGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("ruleGroup", ruleGroup)
                .toString();
    }
}
