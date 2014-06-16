package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProposeFeeInformationDecisionReport extends ReportModel{

    private int count;
    private String productProgram;
    private String standardFront;
    private String commitmentFee;
    private String extensionFee;
    private String prepaymentFee;
    private String cancellationFee;

    public ProposeFeeInformationDecisionReport() {
        count = 0;
        productProgram = "";
        standardFront = "";
        commitmentFee = "";
        extensionFee = "";
        prepaymentFee = "";
        cancellationFee = "";
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getStandardFront() {
        return standardFront;
    }

    public void setStandardFront(String standardFront) {
        this.standardFront = standardFront;
    }

    public String getCommitmentFee() {
        return commitmentFee;
    }

    public void setCommitmentFee(String commitmentFee) {
        this.commitmentFee = commitmentFee;
    }

    public String getExtensionFee() {
        return extensionFee;
    }

    public void setExtensionFee(String extensionFee) {
        this.extensionFee = extensionFee;
    }

    public String getPrepaymentFee() {
        return prepaymentFee;
    }

    public void setPrepaymentFee(String prepaymentFee) {
        this.prepaymentFee = prepaymentFee;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("count", count)
                .append("productProgram", productProgram)
                .append("standardFront", standardFront)
                .append("commitmentFee", commitmentFee)
                .append("extensionFee", extensionFee)
                .append("prepaymentFee", prepaymentFee)
                .append("cancellationFee", cancellationFee)
                .toString();
    }
}
