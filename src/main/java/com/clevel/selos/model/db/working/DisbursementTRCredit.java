package com.clevel.selos.model.db.working;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_disburse_tr_credit")
public class DisbursementTRCredit {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_TR_CREDIT_REQ", sequenceName = "SEQ_WRK_TR_CREDIT_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_TR_CREDIT_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "disburse_tr_id")
    private DisbursementTR disbursementTR;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail creditDetail;

    @Column(name = "disburse_amount")
    private BigDecimal disburseAmount;

	public DisbursementTR getDisbursementTR() {
		return disbursementTR;
	}

	public void setDisbursementTR(DisbursementTR disbursementTR) {
		this.disbursementTR = disbursementTR;
	}

	public NewCreditDetail getCreditDetail() {
		return creditDetail;
	}

	public void setCreditDetail(NewCreditDetail creditDetail) {
		this.creditDetail = creditDetail;
	}

	public BigDecimal getDisburseAmount() {
		return disburseAmount;
	}

	public void setDisburseAmount(BigDecimal disburseAmount) {
		this.disburseAmount = disburseAmount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
