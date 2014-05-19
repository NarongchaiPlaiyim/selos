package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_open_account_credit")
public class OpenAccountCredit implements Serializable {
    private static final long serialVersionUID = -48823277081216169L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_CREDIT_ID", sequenceName = "SEQ_WRK_OPEN_ACC_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @ManyToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;
    
    @ManyToOne
    @JoinColumn(name = "existing_credit_detail_id")
    private ExistingCreditDetail existingCreditDetail;
    
    @Column(name="from_pledge",columnDefinition="int default 0")
    private boolean fromPledge;

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

	public NewCreditDetail getNewCreditDetail() {
		return newCreditDetail;
	}

	public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
		this.newCreditDetail = newCreditDetail;
	}

	public ExistingCreditDetail getExistingCreditDetail() {
		return existingCreditDetail;
	}

	public void setExistingCreditDetail(ExistingCreditDetail existingCreditDetail) {
		this.existingCreditDetail = existingCreditDetail;
	}
    
    public boolean isFromPledge() {
		return fromPledge;
	}
    public void setFromPledge(boolean fromPledge) {
		this.fromPledge = fromPledge;
	}
}
