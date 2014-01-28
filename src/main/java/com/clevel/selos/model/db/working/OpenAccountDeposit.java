package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_open_account_dep")
public class OpenAccountDeposit {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_DEP_ID", sequenceName = "SEQ_WRK_OPEN_ACC_DEP_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_DEP_ID")
    private long id;

    @Column(name = "deposit_number", length = 2)
    private String depositNumber;

    @Column(name = "hold_amount")
    private BigDecimal holdAmount;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

}
