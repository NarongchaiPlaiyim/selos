package com.clevel.selos.model.db.working;

import com.clevel.selos.model.UWResultColor;
import com.clevel.selos.model.UWRuleType;
import com.clevel.selos.model.db.master.UWDeviationFlag;
import com.clevel.selos.model.db.master.UWRejectGroup;
import com.clevel.selos.model.db.master.UWRuleName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_uwresult_detail")
public class UWRuleResultDetail implements Serializable {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_UW_RULE_RESULT_DET_ID", sequenceName = "SEQ_WRK_UW_RULE_RESULT_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_UW_RULE_RESULT_DET_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "uw_rule_name_id")
    private UWRuleName uwRuleName;

    @Column(name = "rule_order", columnDefinition = "int default 0")
    private int ruleOrder;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "color")
    @Enumerated(EnumType.ORDINAL)
    private UWResultColor uwResultColor;

    @Column(name = "rule_type")
    @Enumerated(EnumType.ORDINAL)
    private UWRuleType uwRuleType;

    @ManyToOne
    @JoinColumn(name = "deviation_flag_id")
    private UWDeviationFlag uwDeviationFlag;

    @ManyToOne
    @JoinColumn(name = "reject_group_id")
    private UWRejectGroup rejectGroup;

    @ManyToOne
    @JoinColumn(name = "uw_rule_result_sum_id")
    private UWRuleResultSummary uwRuleResultSummary;

    @Column(name = "reason")
    private String reason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UWRuleName getUwRuleName() {
        return uwRuleName;
    }

    public void setUwRuleName(UWRuleName uwRuleName) {
        this.uwRuleName = uwRuleName;
    }

    public int getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(int ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public UWResultColor getUwResultColor() {
        return uwResultColor;
    }

    public void setUwResultColor(UWResultColor uwResultColor) {
        this.uwResultColor = uwResultColor;
    }

    public UWRuleType getUwRuleType() {
        return uwRuleType;
    }

    public void setUwRuleType(UWRuleType uwRuleType) {
        this.uwRuleType = uwRuleType;
    }

    public UWDeviationFlag getUwDeviationFlag() {
        return uwDeviationFlag;
    }

    public void setUwDeviationFlag(UWDeviationFlag uwDeviationFlag) {
        this.uwDeviationFlag = uwDeviationFlag;
    }

    public UWRejectGroup getRejectGroup() {
        return rejectGroup;
    }

    public void setRejectGroup(UWRejectGroup rejectGroup) {
        this.rejectGroup = rejectGroup;
    }

    public UWRuleResultSummary getUwRuleResultSummary() {
        return uwRuleResultSummary;
    }

    public void setUwRuleResultSummary(UWRuleResultSummary uwRuleResultSummary) {
        this.uwRuleResultSummary = uwRuleResultSummary;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("uwRuleName", uwRuleName).
                append("ruleOrder", ruleOrder).
                append("customer", customer).
                append("uwResultColor", uwResultColor).
                append("uwRuleType", uwRuleType).
                append("uwDeviationFlag", uwDeviationFlag).
                append("rejectGroup", rejectGroup).
                append("uwRuleResultSummary", uwRuleResultSummary).
                append("reason", reason).
                toString();
    }
}
