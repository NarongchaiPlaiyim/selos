package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.MortgageType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_pledge_info")
public class PledgeInfo {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PLEDGE_INFO_REQ", sequenceName = "SEQ_WRK_PLEDGE_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PLEDGE_INFO_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "new_coll_sub_id")
    NewCollateralSub newCollateralSub;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pledge_signing_date")
    private Date pledgeSigningDate;

    @OneToOne
    @JoinColumn(name = "pledge_type_id")
    private MortgageType pledgeType;

    @Column(name = "pledge_amount")
    private BigDecimal pledgeAmount;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @Column(name = "total_hold_amount")
    private BigDecimal totalHoldAmount;


}
