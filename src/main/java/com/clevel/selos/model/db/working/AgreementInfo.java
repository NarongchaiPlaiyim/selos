package com.clevel.selos.model.db.working;

import com.clevel.selos.model.MortgageConfirmedType;
import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.UserZone;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_agreement_info")
public class AgreementInfo implements Serializable {

    private static final long serialVersionUID = -8554828162122084421L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_AGREEMENT_INFO_REQ", sequenceName = "SEQ_WRK_AGREEMENT_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_AGREEMENT_INFO_REQ")
    private long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "loan_contract_date")
    private Date loanContractDate;

    @Column(name = "signing_location")
    @Enumerated(EnumType.ORDINAL)
    private MortgageSignLocationType signingLocation;

    @ManyToOne
    @JoinColumn(name="user_zone_id")
    private UserZone userZone;

    @ManyToOne
    @JoinColumn(name = "bank_branch_id")
    private BankBranch bankBranch;

    @Column(name = "coms_number")
    private String comsNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "first_payment_date")
    private Date firstPaymentDate;

    @Column(name = "pay_date")
    private int payDate;
    
    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;
    
    @Column(name="confirmed",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private MortgageConfirmedType confirmed;
    
    public AgreementInfo() {
    	
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getLoanContractDate() {
		return loanContractDate;
	}

	public void setLoanContractDate(Date loanContractDate) {
		this.loanContractDate = loanContractDate;
	}

	public MortgageSignLocationType getSigningLocation() {
		return signingLocation;
	}

	public void setSigningLocation(MortgageSignLocationType signingLocation) {
		this.signingLocation = signingLocation;
	}

	public UserZone getUserZone() {
		return userZone;
	}

	public void setUserZone(UserZone userZone) {
		this.userZone = userZone;
	}

	public BankBranch getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(BankBranch bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getComsNumber() {
		return comsNumber;
	}

	public void setComsNumber(String comsNumber) {
		this.comsNumber = comsNumber;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public int getPayDate() {
		return payDate;
	}

	public void setPayDate(int payDate) {
		this.payDate = payDate;
	}
    
	public WorkCase getWorkCase() {
		return workCase;
	}
	
	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}

	public MortgageConfirmedType getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(MortgageConfirmedType confirmed) {
		this.confirmed = confirmed;
	}
}
