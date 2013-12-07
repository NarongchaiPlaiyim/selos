package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class NewFeeDetailView implements Serializable {
    private long id;
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

    public NewFeeDetailView() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("productProgram", productProgram)
                .append("standardFrontEndFee", standardFrontEndFee)
                .append("commitmentFee", commitmentFee)
                .append("extensionFee", extensionFee)
                .append("prepaymentFee", prepaymentFee)
                .append("cancellationFee", cancellationFee)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
