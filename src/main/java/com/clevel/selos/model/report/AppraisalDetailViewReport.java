package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AppraisalDetailViewReport extends ReportModel {

    private String path;
    private int count;
    private String titleDeed;
    private String purposeReviewAppraisalLabel;
    private int characteristic;
    private int numberOfDocuments;

    public AppraisalDetailViewReport() {
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getPurposeReviewAppraisalLabel() {
        return purposeReviewAppraisalLabel;
    }

    public void setPurposeReviewAppraisalLabel(String purposeReviewAppraisalLabel) {
        this.purposeReviewAppraisalLabel = purposeReviewAppraisalLabel;
    }

    public int getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(int characteristic) {
        this.characteristic = characteristic;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(int numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
                .append("path", path)
                .append("count", count)
                .append("titleDeed", titleDeed)
                .append("purposeReviewAppraisalLabel", purposeReviewAppraisalLabel)
                .append("characteristic", characteristic)
                .append("numberOfDocuments", numberOfDocuments)
                .toString();
    }
}
