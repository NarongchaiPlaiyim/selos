package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.CrossType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_disburse_mc")
public class DisbursementMC {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_DISBURSE_MC_REQ", sequenceName = "SEQ_WRK_DISBURSE_MC_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DISBURSE_MC_REQ")
    private long id;

    @ManyToOne
    @JoinColumn(name = "disbursement_id")
    private Disbursement disbursement;

    @OneToOne
    @JoinColumn(name = "issued_by_branch_id")
    private BankBranch issuedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issued_date")
    private Date issuedDate;

    @OneToOne
    @JoinColumn(name = "payee_name_bank_id")
    private Bank payeeName;

    @Column(name = "payee_remark", length = 200)
    private String payeeRemark;

    @OneToOne
    @JoinColumn(name = "cross_type_id")
    private CrossType crossType;

    @OneToMany(mappedBy = "disbursementMC")
    private List<DisbursementMCCredit> disbursementMCCreditList;

	public Disbursement getDisbursement() {
		return disbursement;
	}

	public void setDisbursement(Disbursement disbursement) {
		this.disbursement = disbursement;
	}

	public BankBranch getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(BankBranch issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Bank getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(Bank payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeRemark() {
		return payeeRemark;
	}

	public void setPayeeRemark(String payeeRemark) {
		this.payeeRemark = payeeRemark;
	}

	public CrossType getCrossType() {
		return crossType;
	}

	public void setCrossType(CrossType crossType) {
		this.crossType = crossType;
	}

	public List<DisbursementMCCredit> getDisbursementMCCreditList() {
		return disbursementMCCreditList;
	}

	public void setDisbursementMCCreditList(List<DisbursementMCCredit> disbursementMCCreditList) {
		this.disbursementMCCreditList = disbursementMCCreditList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
