package com.clevel.selos.model.db.working;

import com.clevel.selos.model.UWResultColor;
import com.clevel.selos.model.db.master.UWDeviationFlag;
import com.clevel.selos.model.db.master.UWRejectGroup;
import com.clevel.selos.model.db.master.UWRuleName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrk_uwresult_summary")
public class UWRuleResultSummary {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_UW_RULE_RESULT_SUM_ID", sequenceName = "SEQ_WRK_UW_RULE_RESULT_SUM_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_UW_RULE_RESULT_SUM_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @ManyToOne
    @JoinColumn(name = "uw_rule_name_id")
    private UWRuleName uwRuleName;

    @Column(name = "color")
    @Enumerated(EnumType.ORDINAL)
    private UWResultColor uwResultColor;

    @ManyToOne
    @JoinColumn(name = "deviation_flag_id")
    private UWDeviationFlag uwDeviationFlag;

    @ManyToOne
    @JoinColumn(name = "reject_group_id")
    private UWRejectGroup rejectGroup;

    @OneToMany(mappedBy = "uwRuleResultSummary")
    private List<UWRuleResultDetail> uwRuleResultDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public UWResultColor getUwResultColor() {
        return uwResultColor;
    }

    public void setUwResultColor(UWResultColor uwResultColor) {
        this.uwResultColor = uwResultColor;
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

    public UWRuleName getUwRuleName() {
        return uwRuleName;
    }

    public void setUwRuleName(UWRuleName uwRuleName) {
        this.uwRuleName = uwRuleName;
    }

    public List<UWRuleResultDetail> getUwRuleResultDetailList() {
        return uwRuleResultDetailList;
    }

    public void setUwRuleResultDetailList(List<UWRuleResultDetail> uwRuleResultDetailList) {
        this.uwRuleResultDetailList = uwRuleResultDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("workCasePrescreen", workCasePrescreen)
                .append("uwRuleName", uwRuleName)
                .append("uwResultColor", uwResultColor)
                .append("uwDeviationFlag", uwDeviationFlag)
                .append("rejectGroup", rejectGroup)
                .append("uwRuleResultDetailList", uwRuleResultDetailList)
                .toString();
    }
}
