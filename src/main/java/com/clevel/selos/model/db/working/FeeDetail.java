package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.FeePaymentMethod;
import com.clevel.selos.model.db.master.FeeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_fee_detail")
public class FeeDetail {

    @Id
    @SequenceGenerator(name="SEQ_WRK_FEE_DETAIL_ID", sequenceName="SEQ_WRK_FEE_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_FEE_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private FeePaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "fee_type_id")
    private FeeType feeType;

    @Column(name = "fee_percent")
    private BigDecimal percentFee;

    @Column(name = "fee_percent_after_discount")
    private BigDecimal percent_fee_after;

    @Column(name = "fee_year")
    private BigDecimal feeYear;

    @Column(name = "fee_amount", length = 10, scale = 2)
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeePaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(FeePaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getPercentFee() {
        return percentFee;
    }

    public void setPercentFee(BigDecimal percentFee) {
        this.percentFee = percentFee;
    }

    public BigDecimal getPercent_fee_after() {
        return percent_fee_after;
    }

    public void setPercent_fee_after(BigDecimal percent_fee_after) {
        this.percent_fee_after = percent_fee_after;
    }

    public BigDecimal getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(BigDecimal feeYear) {
        this.feeYear = feeYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("paymentMethod", paymentMethod)
                .append("feeType", feeType)
                .append("percentFee", percentFee)
                .append("percent_fee_after", percent_fee_after)
                .append("feeYear", feeYear)
                .append("amount", amount)
                .toString();
    }
}
