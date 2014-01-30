package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_disburse_mc_credit")
public class DisbursementMCCredit {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MC_CREDIT_REQ", sequenceName = "SEQ_WRK_MC_CREDIT_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MC_CREDIT_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "disburse_mc_id")
    private DisbursementMC disbursementMC;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail creditDetail;

    @Column(name = "disburse_amount")
    private BigDecimal disburseAmount;


}
