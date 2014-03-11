package com.clevel.selos.model.db.working;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "wrk_fee_collection_account")
public class FeeCollectionAccount implements Serializable {
	private static final long serialVersionUID = 4259007476373224421L;

	@Id
    @SequenceGenerator(name="SEQ_WRK_FEE_COLL_ACC_ID", sequenceName="SEQ_WRK_FEE_COLL_ACC_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_FEE_COLL_ACC_ID")
    private long id;
	
	@ManyToOne
	@JoinColumn(name="open_account_id")
	private OpenAccount openAccount;
	
	@Column(name="display_account_no")
	private String displayAccountNo;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
	
	public FeeCollectionAccount() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OpenAccount getOpenAccount() {
		return openAccount;
	}

	public void setOpenAccount(OpenAccount openAccount) {
		this.openAccount = openAccount;
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
	
	public String getDisplayAccountNo() {
		return displayAccountNo;
	}
	public void setDisplayAccountNo(String displayAccountNo) {
		this.displayAccountNo = displayAccountNo;
	}
}
