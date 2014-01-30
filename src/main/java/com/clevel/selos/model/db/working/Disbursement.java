package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "wrk_disbursement")
public class Disbursement {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_DISBURSE_REQ", sequenceName = "SEQ_WRK_DISBURSE_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DISBURSE_REQ")
    private long id;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementCredit> disbursementSummaryList;

    @Column(name = "number_of_mc", columnDefinition = "int default 0")
    private int numberOfCheque;

    @Column(name = "total_mc_disburse")
    private BigDecimal totalMCDisbursement;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementMC> disbursementMCList;

    @Column(name = "number_of_tr", columnDefinition = "int default 0")
    private int numberOfTR;

    @Column(name = "total_tr_disburse")
    private BigDecimal totalTRDisburse;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementTR> disbursementTRList;

    @Column(name = "number_of_bahtnet")
    private int numberOfBahtnet;

    @Column(name = "total_bahtnet_disburse")
    private BigDecimal totalBahtnetDisbursement;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementBahtnet> disbursementBahtnetList;



}
