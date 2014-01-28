package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_open_account_credit")
public class OpenAccountCredit {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_CREDIT_ID", sequenceName = "SEQ_WRK_OPEN_ACC_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_CREDIT_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail creditDetail;
}
