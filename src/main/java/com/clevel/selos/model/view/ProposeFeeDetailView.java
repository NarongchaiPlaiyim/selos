package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.util.Date;

public class ProposeFeeDetailView implements Serializable {

    private String productProgram;
    private String standardFrontEndFee;
    private String commitmentFee;
    private String extensionFee;
    private String prepaymentFee;
    private String cancellationFee;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public ProposeFeeDetailView() {
        reset();
    }

    public void reset() {
        this.productProgram = "";
        this.standardFrontEndFee = "";
        this.commitmentFee = "";
        this.extensionFee = "";
        this.prepaymentFee = "";
        this.cancellationFee = "";
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getStandardFrontEndFee() {
        return standardFrontEndFee;
    }

    public void setStandardFrontEndFee(String standardFrontEndFee) {
        this.standardFrontEndFee = standardFrontEndFee;
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
}
