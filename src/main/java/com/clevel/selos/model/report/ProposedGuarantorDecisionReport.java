package com.clevel.selos.model.report;

import com.clevel.selos.model.view.ProposeCreditInfoDetailView;
import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProposedGuarantorDecisionReport extends ReportModel{

    private int count;
    private String name;
    private String tcgLgNo;
    private String path;

    private List<ProposeCreditInfoDetailView> proposeCreditDetailViewList;

    private BigDecimal totalLimitGuaranteeAmount;


    public ProposedGuarantorDecisionReport() {
        count = 0;
        name = "";
        tcgLgNo = "";
        proposeCreditDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditDetailViewList() {
        return proposeCreditDetailViewList;
    }

    public void setProposeCreditDetailViewList(List<ProposeCreditInfoDetailView> proposeCreditDetailViewList) {
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
                .toString();
    }


}
