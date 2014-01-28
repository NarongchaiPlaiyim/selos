package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_open_account_name")
public class OpenAccountName {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_NAME_ID", sequenceName = "SEQ_WRK_OPEN_ACC_NAME_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_NAME_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
