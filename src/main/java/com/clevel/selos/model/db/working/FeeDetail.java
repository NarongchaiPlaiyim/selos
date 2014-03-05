package com.clevel.selos.model.db.working;

import com.clevel.selos.model.FeeLevel;
import com.clevel.selos.model.db.master.FeePaymentMethod;
import com.clevel.selos.model.db.master.FeeType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_fee_detail")
public class FeeDetail implements Serializable {

    private static final long serialVersionUID = 1819884749144921628L;

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
    private BigDecimal percentFeeAfter;

    @Column(name = "fee_year")
    private BigDecimal feeYear;

    @Column(name = "fee_amount", length = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name="fee_level",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private FeeLevel feeLevel;
    
    @Column(name="description")
    private String description;
    
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

    public BigDecimal getPercentFeeAfter() {
		return percentFeeAfter;
	}
    public void setPercentFeeAfter(BigDecimal percentFeeAfter) {
		this.percentFeeAfter = percentFeeAfter;
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

    public String getDescription() {
		return description;
	}
    public void setDescription(String description) {
		this.description = description;
	}
    public FeeLevel getFeeLevel() {
		return feeLevel;
	}
    public void setFeeLevel(FeeLevel feeLevel) {
		this.feeLevel = feeLevel;
	}
    public NewCreditDetail getNewCreditDetail() {
		return newCreditDetail;
	}
    public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
		this.newCreditDetail = newCreditDetail;
	}
    public WorkCase getWorkCase() {
		return workCase;
	}
    public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("paymentMethod", paymentMethod)
                .append("feeType", feeType)
                .append("percentFee", percentFee)
                .append("percentFeeAfter", percentFeeAfter)
                .append("feeYear", feeYear)
                .append("amount", amount)
                .append("feeLevel", feeLevel)
                .append("description", description)
                .toString();
    }
}
