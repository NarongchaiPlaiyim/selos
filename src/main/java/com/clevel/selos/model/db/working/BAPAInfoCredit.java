package com.clevel.selos.model.db.working;


import javax.persistence.*;

import com.clevel.selos.model.BAPAType;
import com.clevel.selos.model.RadioValue;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_bapa_credit")
public class BAPAInfoCredit implements Serializable {
	
    private static final long serialVersionUID = -5184894374459158420L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_BAPA_CREDIT_REQ", sequenceName = "SEQ_WRK_BAPA_CREDIT_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAPA_CREDIT_REQ")
    private long id;

    @Column(name = "bapa_type",columnDefinition="int default 0")
    private BAPAType bapaType;
    

    @Column(name = "pay_by_customer")
    private int payByCustomer;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "premium_amount")
    private BigDecimal premiumAmount;

    @Column(name = "expense_amount")
    private BigDecimal expenseAmount;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail creditDetail;

    @ManyToOne
    @JoinColumn(name = "bapa_info_id")
    private BAPAInfo bapaInfo;
    

    @Column(name="from_approve",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue fromApprove;

	public long getId() {
		
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BAPAType getBapaType() {
		return bapaType;
	}
	public void setBapaType(BAPAType bapaType) {
		this.bapaType = bapaType;
	}

	public int getPayByCustomer() {
		return payByCustomer;
	}

	public void setPayByCustomer(int payByCustomer) {
		this.payByCustomer = payByCustomer;
	}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public NewCreditDetail getCreditDetail() {
		return creditDetail;
	}

	public void setCreditDetail(NewCreditDetail creditDetail) {
		this.creditDetail = creditDetail;
	}

	public BAPAInfo getBapaInfo() {
		return bapaInfo;
	}

	public void setBapaInfo(BAPAInfo bapaInfo) {
		this.bapaInfo = bapaInfo;
	}


    
}
