package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BAResultHC;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wrk_bapa_customer")
public class BAPAInfoCustomer {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_BAPA_CUSTOMER_REQ", sequenceName = "SEQ_WRK_BAPA_CUSTOMER_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAPA_CUSTOMER_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "bapa_info_id")
    private BAPAInfo bapaInfo;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "ba_result_hc_id")
    private BAResultHC baResultHC;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "health_check_date")
    private Date healthCheckDate;

}
