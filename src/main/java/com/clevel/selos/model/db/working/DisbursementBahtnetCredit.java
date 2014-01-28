package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_disburse_bahtnet_credit")
public class DisbursementBahtnetCredit {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_BAHTNET_CREDIT_REQ", sequenceName = "SEQ_WRK_BAHTNET_CREDIT_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAHTNET_CREDIT_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "disburse_bahtnet_id")
    private DisbursementBahtnet disbursementBahtnet;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail creditDetail;

    @Column(name = "disburse_amount")
    private BigDecimal disburseAmount;
}
