package com.clevel.selos.model.view;

import com.clevel.selos.integration.brms.model.RuleColorResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class UWRuleResultDetailView implements Serializable {
    private long id;
    private UWRuleNameView uwRuleNameView;
    private int ruleOrder;
    private RuleColorResult ruleColorResult;
    private CustomerInfoSimpleView customerInfoSimpleView;
    private UWDeviationFlagView deviationFlag;
    private UWRejectGroupView rejectGroupCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UWRuleNameView getUwRuleNameView() {
        return uwRuleNameView;
    }

    public void setUwRuleNameView(UWRuleNameView uwRuleNameView) {
        this.uwRuleNameView = uwRuleNameView;
    }

    public int getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(int ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public RuleColorResult getRuleColorResult() {
        return ruleColorResult;
    }

    public void setRuleColorResult(RuleColorResult ruleColorResult) {
        this.ruleColorResult = ruleColorResult;
    }

    public CustomerInfoSimpleView getCustomerInfoSimpleView() {
        return customerInfoSimpleView;
    }

    public void setCustomerInfoSimpleView(CustomerInfoSimpleView customerInfoSimpleView) {
        this.customerInfoSimpleView = customerInfoSimpleView;
    }

    public UWDeviationFlagView getDeviationFlag() {
        return deviationFlag;
    }

    public void setDeviationFlag(UWDeviationFlagView deviationFlag) {
        this.deviationFlag = deviationFlag;
    }

    public UWRejectGroupView getRejectGroupCode() {
        return rejectGroupCode;
    }

    public void setRejectGroupCode(UWRejectGroupView rejectGroupCode) {
        this.rejectGroupCode = rejectGroupCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("uwRuleNameView", uwRuleNameView)
                .append("ruleOrder", ruleOrder)
                .append("ruleColorResult", ruleColorResult)
                .append("customerInfoSimpleView", customerInfoSimpleView)
                .append("deviationFlag", deviationFlag)
                .append("rejectGroupCode", rejectGroupCode)
                .toString();
    }
}
