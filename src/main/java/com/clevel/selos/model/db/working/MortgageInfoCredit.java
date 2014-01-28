package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_mortgage_credit")
public class MortgageInfoCredit {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_MORT_CREDIT_ID", sequenceName = "SEQ_WRK_MORT_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_CREDIT_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "mortgage_info_id")
    private MortgageInfo mortgageInfo;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;

}
