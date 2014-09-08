package com.clevel.selos.model.view.master;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.SELOS;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class ReferenceView implements Serializable{

    private int id;
    private int relationId;
    private int customerEntityId;
    private String description;
    private String brmsCode;
    private int borrowerTypeCusEntityId;
    private String percentShare;
    private int collateralOwner;
    private int csi;
    private int sll;
    private int groupIncome;
    private int kycFullScreening;
    private int kycNameScreening;
    private int linkRm;
    private int active;
    private int mainCustomer;
    private int spouse;
    private int relationType;
    private int unpaidInsurance;
    private int pendingClaimLG;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public int getCustomerEntityId() {
        return customerEntityId;
    }

    public void setCustomerEntityId(int customerEntityId) {
        this.customerEntityId = customerEntityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public int getBorrowerTypeCusEntityId() {
        return borrowerTypeCusEntityId;
    }

    public void setBorrowerTypeCusEntityId(int borrowerTypeCusEntityId) {
        this.borrowerTypeCusEntityId = borrowerTypeCusEntityId;
    }

    public String getPercentShare() {
        return percentShare;
    }

    public void setPercentShare(String percentShare) {
        this.percentShare = percentShare;
    }

    public int getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(int collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public int getCsi() {
        return csi;
    }

    public void setCsi(int csi) {
        this.csi = csi;
    }

    public int getSll() {
        return sll;
    }

    public void setSll(int sll) {
        this.sll = sll;
    }

    public int getGroupIncome() {
        return groupIncome;
    }

    public void setGroupIncome(int groupIncome) {
        this.groupIncome = groupIncome;
    }

    public int getKycFullScreening() {
        return kycFullScreening;
    }

    public void setKycFullScreening(int kycFullScreening) {
        this.kycFullScreening = kycFullScreening;
    }

    public int getKycNameScreening() {
        return kycNameScreening;
    }

    public void setKycNameScreening(int kycNameScreening) {
        this.kycNameScreening = kycNameScreening;
    }

    public int getLinkRm() {
        return linkRm;
    }

    public void setLinkRm(int linkRm) {
        this.linkRm = linkRm;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getMainCustomer() {
        return mainCustomer;
    }

    public void setMainCustomer(int mainCustomer) {
        this.mainCustomer = mainCustomer;
    }

    public int getSpouse() {
        return spouse;
    }

    public void setSpouse(int spouse) {
        this.spouse = spouse;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public int getUnpaidInsurance() {
        return unpaidInsurance;
    }

    public void setUnpaidInsurance(int unpaidInsurance) {
        this.unpaidInsurance = unpaidInsurance;
    }

    public int getPendingClaimLG() {
        return pendingClaimLG;
    }

    public void setPendingClaimLG(int pendingClaimLG) {
        this.pendingClaimLG = pendingClaimLG;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("relationId", relationId)
                .append("customerEntityId", customerEntityId)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("borrowerTypeCusEntityId", borrowerTypeCusEntityId)
                .append("percentShare", percentShare)
                .append("collateralOwner", collateralOwner)
                .append("csi", csi)
                .append("sll", sll)
                .append("groupIncome", groupIncome)
                .append("kycFullScreening", kycFullScreening)
                .append("kycNameScreening", kycNameScreening)
                .append("linkRm", linkRm)
                .append("active", active)
                .append("mainCustomer", mainCustomer)
                .append("spouse", spouse)
                .append("relationType", relationType)
                .append("unpaidInsurance", unpaidInsurance)
                .append("pendingClaimLG", pendingClaimLG)
                .toString();
    }
}
