package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ProposeFeeDetailView implements Serializable {
    private long id;
    private ProposeCreditInfoDetailView proposeCreditInfoDetailView;
    private String productProgram;
    private FeeDetailView standardFrontEndFee;
    private FeeDetailView commitmentFee;
    private FeeDetailView extensionFee;
    private FeeDetailView prepaymentFee;
    private FeeDetailView cancellationFee;


    public ProposeFeeDetailView() {
        reset();
    }

    public void reset() {
        this.productProgram = "";
        this.standardFrontEndFee = new FeeDetailView();
        this.commitmentFee = new FeeDetailView();
        this.extensionFee = new FeeDetailView();
        this.prepaymentFee = new FeeDetailView();
        this.cancellationFee = new FeeDetailView();
        this.proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public FeeDetailView getStandardFrontEndFee() {
        return standardFrontEndFee;
    }

    public void setStandardFrontEndFee(FeeDetailView standardFrontEndFee) {
        this.standardFrontEndFee = standardFrontEndFee;
    }

    public FeeDetailView getCommitmentFee() {
        return commitmentFee;
    }

    public void setCommitmentFee(FeeDetailView commitmentFee) {
        this.commitmentFee = commitmentFee;
    }

    public FeeDetailView getExtensionFee() {
        return extensionFee;
    }

    public void setExtensionFee(FeeDetailView extensionFee) {
        this.extensionFee = extensionFee;
    }

    public FeeDetailView getPrepaymentFee() {
        return prepaymentFee;
    }

    public void setPrepaymentFee(FeeDetailView prepaymentFee) {
        this.prepaymentFee = prepaymentFee;
    }

    public FeeDetailView getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(FeeDetailView cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public ProposeCreditInfoDetailView getProposeCreditInfoDetailView() {
        return proposeCreditInfoDetailView;
    }

    public void setProposeCreditInfoDetailView(ProposeCreditInfoDetailView proposeCreditInfoDetailView) {
        this.proposeCreditInfoDetailView = proposeCreditInfoDetailView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productProgram", productProgram)
                .append("standardFrontEndFee", standardFrontEndFee)
                .append("commitmentFee", commitmentFee)
                .append("extensionFee", extensionFee)
                .append("prepaymentFee", prepaymentFee)
                .append("cancellationFee", cancellationFee)
                .toString();
    }
}
