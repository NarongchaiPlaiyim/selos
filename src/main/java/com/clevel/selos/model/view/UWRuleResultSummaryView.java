package com.clevel.selos.model.view;

import com.clevel.selos.model.UWResultColor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UWRuleResultSummaryView implements Serializable {

    private long id;
    private long workCaseId;
    private long workCasePrescreenId;
    private UWRuleNameView uwRuleNameView;
    private UWResultColor uwResultColor;
    private UWDeviationFlagView uwDeviationFlagView;
    private UWRejectGroupView rejectGroupView;
    private Map<Integer, UWRuleResultDetailView> uwRuleResultDetailViewMap;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public long getWorkCasePrescreenId() {
        return workCasePrescreenId;
    }

    public void setWorkCasePrescreenId(long workCasePrescreenId) {
        this.workCasePrescreenId = workCasePrescreenId;
    }

    public UWRuleNameView getUwRuleNameView() {
        return uwRuleNameView;
    }

    public void setUwRuleNameView(UWRuleNameView uwRuleNameView) {
        this.uwRuleNameView = uwRuleNameView;
    }

    public UWResultColor getUwResultColor() {
        return uwResultColor;
    }

    public void setUwResultColor(UWResultColor uwResultColor) {
        this.uwResultColor = uwResultColor;
    }

    public UWDeviationFlagView getUwDeviationFlagView() {
        return uwDeviationFlagView;
    }

    public void setUwDeviationFlagView(UWDeviationFlagView uwDeviationFlagView) {
        this.uwDeviationFlagView = uwDeviationFlagView;
    }

    public UWRejectGroupView getRejectGroupView() {
        return rejectGroupView;
    }

    public void setRejectGroupView(UWRejectGroupView rejectGroupView) {
        this.rejectGroupView = rejectGroupView;
    }

    public Map<Integer, UWRuleResultDetailView> getUwRuleResultDetailViewMap() {
        return uwRuleResultDetailViewMap;
    }

    public void setUwRuleResultDetailViewMap(Map<Integer, UWRuleResultDetailView> uwRuleResultDetailViewMap) {
        this.uwRuleResultDetailViewMap = uwRuleResultDetailViewMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCaseId", workCaseId)
                .append("workCasePrescreenId", workCasePrescreenId)
                .append("uwRuleNameView", uwRuleNameView)
                .append("uwResultColor", uwResultColor)
                .append("uwDeviationFlagView", uwDeviationFlagView)
                .append("rejectGroupView", rejectGroupView)
                .append("uwRuleResultDetailViewMap", uwRuleResultDetailViewMap)
                .toString();
    }
}
