package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_fee_summary")
public class FeeSummary {
    @Id
    @SequenceGenerator(name="SEQ_WRK_FEE_SUM_ID", sequenceName="SEQ_WRK_FEE_SUM_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_FEE_SUM_ID")
    private long id;


}
