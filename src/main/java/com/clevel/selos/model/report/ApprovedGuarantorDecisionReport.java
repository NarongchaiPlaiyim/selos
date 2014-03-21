package com.clevel.selos.model.report;

import com.clevel.selos.model.view.ProposeCreditDetailView;
import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApprovedGuarantorDecisionReport extends ReportModel{

    private int count;
    private String name;
    private String tcgLgNo;
    private String path;

    private List<ProposeCreditDetailView> proposeCreditDetailViewList;

    private BigDecimal totalLimitGuaranteeAmount;
    private String uwDecision;

    public ApprovedGuarantorDecisionReport() {
        count = getDefaultInteger();
        name = getDefaultString();
        tcgLgNo = getDefaultString();
        proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
        totalLimitGuaranteeAmount = getDefaultBigDecimal();
        uwDecision = getDefaultString();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(String uwDecision) {
        this.uwDecision = uwDecision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProposeCreditDetailView> getProposeCreditDetailViewList() {
        return proposeCreditDetailViewList;
    }

    public void setProposeCreditDetailViewList(List<ProposeCreditDetailView> proposeCreditDetailViewList) {
        this.proposeCreditDetailViewList = proposeCreditDetailViewList;
    }

    public String getTcgLgNo() {
        return tcgLgNo;
    }

    public void setTcgLgNo(String tcgLgNo) {
        this.tcgLgNo = tcgLgNo;
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("count", count)
                .append("name", name)
                .append("tcgLgNo", tcgLgNo)
                .append("proposeCreditDetailViewList", proposeCreditDetailViewList)
                .append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount)
                .append("uwDecision", uwDecision)
                .toString();
    }
}
