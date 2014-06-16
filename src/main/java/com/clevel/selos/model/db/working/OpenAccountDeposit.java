package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_open_account_dep")
public class OpenAccountDeposit implements Serializable {
    private static final long serialVersionUID = 7049766944754190125L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_DEP_ID", sequenceName = "SEQ_WRK_OPEN_ACC_DEP_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_DEP_ID")
    private long id;

    @Column(name = "deposit_number", length = 2)
    private String depositNumber;

    @Column(name = "hold_amount")
    private BigDecimal holdAmount;

    @ManyToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDepositNumber() {
		return depositNumber;
	}

	public void setDepositNumber(String depositNumber) {
		this.depositNumber = depositNumber;
	}

	public BigDecimal getHoldAmount() {
		return holdAmount;
	}

	public void setHoldAmount(BigDecimal holdAmount) {
		this.holdAmount = holdAmount;
	}

	public OpenAccount getOpenAccount() {
		return openAccount;
	}

	public void setOpenAccount(OpenAccount openAccount) {
		this.openAccount = openAccount;
	}
    
    

}
