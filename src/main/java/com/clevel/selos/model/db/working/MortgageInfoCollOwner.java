package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_mortgage_coll_owner")
public class MortgageInfoCollOwner {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MORT_COLL_OWNER_ID", sequenceName = "SEQ_WRK_MORT_COLL_OWNER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_COLL_OWNER_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "mortgage_info_id")
    private MortgageInfo mortgageInfo;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "poa_required")
    private int poaRequired;


}
