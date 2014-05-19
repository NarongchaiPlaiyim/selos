package com.clevel.selos.model.report;

import com.clevel.selos.model.view.ExistingCreditTypeDetailView;
import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GuarantorBorrowerDecisionReport extends ReportModel{

    private int count;
    private String guarantorName;
    private String tcgLgNo;
    private BigDecimal totalLimitGuaranteeAmount;
    private List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
    private String path;

    public GuarantorBorrowerDecisionReport() {
        count = 0;
        guarantorName = "";
        tcgLgNo = "";
        existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ExistingCreditTypeDetailView> getExistingCreditTypeDetailViewList() {
        return existingCreditTypeDetailViewList;
    }

    public void setExistingCreditTypeDetailViewList(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList) {
        this.existingCreditTypeDetailViewList = existingCreditTypeDetailViewList;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
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
                .append("guarantorName", guarantorName)
                .append("tcgLgNo", tcgLgNo)
                .append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount)
                .append("existingCreditTypeDetailViewList", existingCreditTypeDetailViewList)
                .toString();
    }
}
