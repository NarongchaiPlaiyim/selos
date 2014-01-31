package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.SBFScore;
import com.clevel.selos.model.db.master.ServiceSegment;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="wrk_customer_obl_info")
public class CustomerOblInfo implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_CUS_OBL_ID", sequenceName="SEQ_WRK_CUS_OBL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CUS_OBL_ID")
    private long id;

    @OneToOne(mappedBy = "customerOblInfo")
    private Customer customer;

    @Column(name="existing_sme_customer", nullable=false, columnDefinition="int default 0")
    private int existingSMECustomer;

    @Column(name="last_review_date")
    private Date lastReviewDate;

    @Column(name="extended_review_date")
    private Date extendedReviewDate;

    @Column(name="extended_review_date_flag", nullable=false, columnDefinition="int default 0")
    private int extendedReviewDateFlag;

    @Column(name="next_review_date")
    private Date nextReviewDate;

    @Column(name="next_review_date_flag", nullable=false, columnDefinition="int default 0")
    private int nextReviewDateFlag;

    @Column(name="last_contract_date")
    private Date lastContractDate;

    @Column(name="months_last_contract_date")
    private Date numberOfMonthsLastContractDate;

    @Column(name="adjust_class")
    private String adjustClass;

    @ManyToOne
    @JoinColumn(name="rating_final")
    private SBFScore ratingFinal;

    @Column(name="unpaid_fee_insurance", nullable=false, columnDefinition="int default 0")
    private BigDecimal unpaidFeeInsurance;

    @Column(name="pending_claim_LG", nullable=false, columnDefinition="int default 0")
    private BigDecimal pendingClaimLG;

    @OneToOne
    @JoinColumn(name="service_segment_id")
    private ServiceSegment serviceSegment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(int existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
    }

    public int getExtendedReviewDateFlag() {
        return extendedReviewDateFlag;
    }

    public void setExtendedReviewDateFlag(int extendedReviewDateFlag) {
        this.extendedReviewDateFlag = extendedReviewDateFlag;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public int getNextReviewDateFlag() {
        return nextReviewDateFlag;
    }

    public void setNextReviewDateFlag(int nextReviewDateFlag) {
        this.nextReviewDateFlag = nextReviewDateFlag;
    }

    public Date getLastContractDate() {
        return lastContractDate;
    }

    public void setLastContractDate(Date lastContractDate) {
        this.lastContractDate = lastContractDate;
    }

    public Date getNumberOfMonthsLastContractDate() {
        return numberOfMonthsLastContractDate;
    }

    public void setNumberOfMonthsLastContractDate(Date numberOfMonthsLastContractDate) {
        this.numberOfMonthsLastContractDate = numberOfMonthsLastContractDate;
    }

    public String getAdjustClass() {
        return adjustClass;
    }

    public void setAdjustClass(String adjustClass) {
        this.adjustClass = adjustClass;
    }

    public SBFScore getRatingFinal() {
        return ratingFinal;
    }

    public void setRatingFinal(SBFScore ratingFinal) {
        this.ratingFinal = ratingFinal;
    }

    public BigDecimal getUnpaidFeeInsurance() {
        return unpaidFeeInsurance;
    }

    public void setUnpaidFeeInsurance(BigDecimal unpaidFeeInsurance) {
        this.unpaidFeeInsurance = unpaidFeeInsurance;
    }

    public BigDecimal getPendingClaimLG() {
        return pendingClaimLG;
    }

    public void setPendingClaimLG(BigDecimal pendingClaimLG) {
        this.pendingClaimLG = pendingClaimLG;
    }

    public ServiceSegment getServiceSegment() {
        return serviceSegment;
    }

    public void setServiceSegment(ServiceSegment serviceSegment) {
        this.serviceSegment = serviceSegment;
    }
}
