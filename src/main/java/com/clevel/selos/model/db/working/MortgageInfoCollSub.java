package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_mortgage_coll_sub")
public class MortgageInfoCollSub {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_MORT_COLL_SUB_ID", sequenceName = "SEQ_WRK_MORT_COLL_SUB_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_COLL_SUB_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "mortgage_info_id")
    private MortgageInfo mortgageInfo;

    @OneToOne
    @JoinColumn(name = "new_coll_sub_id")
    private NewCollateralSub newCollateralSub;
}
