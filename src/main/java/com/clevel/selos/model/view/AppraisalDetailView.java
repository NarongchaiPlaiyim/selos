package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
public class AppraisalDetailView implements Serializable {
    private long id;
    private int no;
    private String titleDeed;
    private int purposeReviewAppraisal;
    private int purposeNewAppraisal;
    private int purposeReviewBuilding;
    private boolean purposeReviewAppraisalB;
    private boolean purposeNewAppraisalB;
    private boolean purposeReviewBuildingB;
    private int characteristic;
    private String purposeReviewAppraisalLabel;
    private String purposeNewAppraisalLabel;
    private String purposeReviewBuildingLabel;
    private String characteristicLabel;
    private int numberOfDocuments;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public int getPurposeReviewAppraisal() {
        return purposeReviewAppraisal;
    }

    public void setPurposeReviewAppraisal(int purposeReviewAppraisal) {
        this.purposeReviewAppraisal = purposeReviewAppraisal;
    }

    public int getPurposeNewAppraisal() {
        return purposeNewAppraisal;
    }

    public void setPurposeNewAppraisal(int purposeNewAppraisal) {
        this.purposeNewAppraisal = purposeNewAppraisal;
    }

    public int getPurposeReviewBuilding() {
        return purposeReviewBuilding;
    }

    public boolean isPurposeReviewAppraisalB() {
        return purposeReviewAppraisalB;
    }

    public void setPurposeReviewAppraisalB(boolean purposeReviewAppraisalB) {
        this.purposeReviewAppraisalB = purposeReviewAppraisalB;
    }

    public boolean isPurposeNewAppraisalB() {
        return purposeNewAppraisalB;
    }

    public void setPurposeNewAppraisalB(boolean purposeNewAppraisalB) {
        this.purposeNewAppraisalB = purposeNewAppraisalB;
    }

    public boolean isPurposeReviewBuildingB() {
        return purposeReviewBuildingB;
    }

    public void setPurposeReviewBuildingB(boolean purposeReviewBuildingB) {
        this.purposeReviewBuildingB = purposeReviewBuildingB;
    }

    public void setPurposeReviewBuilding(int purposeReviewBuilding) {
        this.purposeReviewBuilding = purposeReviewBuilding;

    }

    public String getPurposeReviewAppraisalLabel() {
        return purposeReviewAppraisalLabel;
    }

    public void setPurposeReviewAppraisalLabel(String purposeReviewAppraisalLabel) {
        this.purposeReviewAppraisalLabel = purposeReviewAppraisalLabel;
    }

    public String getPurposeNewAppraisalLabel() {
        return purposeNewAppraisalLabel;
    }

    public void setPurposeNewAppraisalLabel(String purposeNewAppraisalLabel) {
        this.purposeNewAppraisalLabel = purposeNewAppraisalLabel;
    }

    public String getPurposeReviewBuildingLabel() {
        return purposeReviewBuildingLabel;
    }

    public void setPurposeReviewBuildingLabel(String purposeReviewBuildingLabel) {
        this.purposeReviewBuildingLabel = purposeReviewBuildingLabel;
    }

    public String getCharacteristicLabel() {
        return characteristicLabel;
    }

    public void setCharacteristicLabel(String characteristicLabel) {
        this.characteristicLabel = characteristicLabel;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("titleDeed", titleDeed)
                .append("purposeReviewAppraisal", purposeReviewAppraisal)
                .append("purposeNewAppraisal", purposeNewAppraisal)
                .append("purposeReviewBuilding", purposeReviewBuilding)
                .append("purposeReviewAppraisalB", purposeReviewAppraisalB)
                .append("purposeNewAppraisalB", purposeNewAppraisalB)
                .append("purposeReviewBuildingB", purposeReviewBuildingB)
                .append("characteristic", characteristic)
                .append("purposeReviewAppraisalLabel", purposeReviewAppraisalLabel)
                .append("purposeNewAppraisalLabel", purposeNewAppraisalLabel)
                .append("purposeReviewBuildingLabel", purposeReviewBuildingLabel)
                .append("characteristicLabel", characteristicLabel)
                .append("numberOfDocuments", numberOfDocuments)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
