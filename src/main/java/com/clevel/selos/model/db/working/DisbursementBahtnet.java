package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Bank;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrk_disburse_bahtnet")
public class DisbursementBahtnet {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_DISBURSE_BAHTNET_REQ", sequenceName = "SEQ_WRK_DISBURSE_BAHTNET_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DISBURSE_BAHTNET_REQ")
    private long id;

    @ManyToOne
    @JoinColumn(name = "disbursement_id")
    private Disbursement disbursement;

    @OneToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @OneToMany(mappedBy = "disbursementBahtnet")
    private List<DisbursementBahtnetCredit> disburseBahtnetCreditList;

	public Disbursement getDisbursement() {
		return disbursement;
	}

	public void setDisbursement(Disbursement disbursement) {
		this.disbursement = disbursement;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public List<DisbursementBahtnetCredit> getDisburseBahtnetCreditList() {
		return disburseBahtnetCreditList;
	}

	public void setDisburseBahtnetCreditList(List<DisbursementBahtnetCredit> disburseBahtnetCreditList) {
		this.disburseBahtnetCreditList = disburseBahtnetCreditList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
