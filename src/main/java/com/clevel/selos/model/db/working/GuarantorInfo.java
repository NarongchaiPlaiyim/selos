package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wrk_guarantor_info")
public class GuarantorInfo {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_GUARANTOR_INFO_REQ", sequenceName = "SEQ_WRK_GUARANTOR_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_GUARANTOR_INFO_REQ")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "guarantor_signing_date")
    private Date guarantorSigningDate;

    @OneToOne
    @JoinColumn(name = "guarantor_id")
    private Customer guarantor;

}
