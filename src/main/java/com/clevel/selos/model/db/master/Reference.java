package com.clevel.selos.model.db.master;

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
}
