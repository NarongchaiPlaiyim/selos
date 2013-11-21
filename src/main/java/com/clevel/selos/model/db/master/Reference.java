package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_reference")
public class Reference implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne
    @JoinColumn(name = "relation_id")
    private Relation relation;
    @OneToOne
    @JoinColumn(name = "customer_entity_id")
    private CustomerEntity customerEntity;
    @Column(name = "description")
    private String description;
    @OneToOne
    @JoinColumn(name = "borrower_type_id")
    private CustomerEntity borrowerType;
    @Column(name = "percent_share")
    private String percentShare;
    @Column(name = "collateral_owner")
    private int collateralOwner;
    @Column(name = "csi")
    private int csi;
    @Column(name = "SLL")
    private int sll;
    @Column(name = "group_income")
    private int groupIncome;
    @Column(name = "kyc_full_screening")
    private int kycFullScreening;
    @Column(name = "kyc_name_screening")
    private int kycNameScreening;
    @Column(name = "link_rm")
    private int linkRm;
    @Column(name = "active")
    private int active;
    @Column(name = "main_customer", nullable = false, columnDefinition = "int default -1")
    private int mainCustomer;
    @Column(name = "spouse", nullable = false, columnDefinition = "int default -1")
    private int spouse;
    @Column(name = "relation_type", nullable = false, columnDefinition = "int default -1")
    private int relationType;

    public Reference() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomerEntity getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(CustomerEntity borrowerType) {
        this.borrowerType = borrowerType;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("relation", relation).
                append("customerEntity", customerEntity).
                append("description", description).
                append("borrowerType", borrowerType).
                append("percentShare", percentShare).
                append("collateralOwner", collateralOwner).
                append("csi", csi).
                append("sll", sll).
                append("groupIncome", groupIncome).
                append("kycFullScreening", kycFullScreening).
                append("kycNameScreening", kycNameScreening).
                append("linkRm", linkRm).
                append("active", active).
                append("mainCustomer", mainCustomer).
                append("spouse", spouse).
                append("relationType", relationType).
                toString();
    }
}
