package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_propose_fee_detail")
public class ProposeFeeDetail  implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PROPOSE_FEE_DET_ID", sequenceName = "SEQ_WRK_PROPOSE_FEE_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PROPOSE_FEE_DET_ID")
    private long id;

    @Column(name = "product_program")
    private String  productProgram ;

    @Column(name = "standard_front_end_fee")
    private String  standardFrontEndFee;

    @Column(name = "commitment_fee")
    private String  commitmentFee;

    @Column(name = "extension_fee")
    private String  extensionFee;

    @Column(name = "prepayment_fee")
    private String  prepaymentFee;

    @Column(name = "cancellation_fee")
    private String  cancellationFee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "credit_facility_propose_id")
    private CreditFacilityPropose creditFacilityPropose;

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

    public CreditFacilityPropose getCreditFacilityPropose() {
        return creditFacilityPropose;
    }

    public void setCreditFacilityPropose(CreditFacilityPropose creditFacilityPropose) {
        this.creditFacilityPropose = creditFacilityPropose;
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
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("creditFacilityPropose", creditFacilityPropose)
                .toString();
    }
}