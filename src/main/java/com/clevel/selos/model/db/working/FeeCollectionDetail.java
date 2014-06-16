package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.FeePaymentMethod;
import com.clevel.selos.model.db.master.FeeType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_fee_collection_detail")
public class FeeCollectionDetail implements Serializable {
	private static final long serialVersionUID = -2226174615728590051L;

	@Id
    @SequenceGenerator(name="SEQ_WRK_FEE_COLL_DETAIL_ID", sequenceName="SEQ_WRK_FEE_COLL_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_FEE_COLL_DETAIL_ID")
    private long id;
	
	@ManyToOne
    @JoinColumn(name = "payment_method_id")
    private FeePaymentMethod paymentMethod;

	@ManyToOne
    @JoinColumn(name = "fee_type_id")
    private FeeType feeType;
	
	@Column(name="description")
	private String description;
	
	@Column(name = "fee_amount")
	private BigDecimal amount;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
	
	public FeeCollectionDetail() {
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public WorkCase getWorkCase() {
		return workCase;
	}
	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}
	
}
