package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.UserZone;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wrk_agreement_info")
public class AgreementInfo {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_AGREEMENT_INFO_REQ", sequenceName = "SEQ_WRK_AGREEMENT_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_AGREEMENT_INFO_REQ")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "loan_contract_date")
    private Date loanContractDate;

    @Column(name = "location_type")
    private int signingLocation;

    @Column(name = "user_zone_id")
    private UserZone userZone;

    @Column(name = "bank_branch_id")
    private BankBranch bankBranch;

    @Column(name = "coms_number")
    private String comsNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "first_payment_date")
    private Date firstPaymentDate;

    @Column(name = "pay_date")
    private int payDate;

}
