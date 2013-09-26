package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_dbr_detail")
public class DBRDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_DBR_DETAIL_ID", sequenceName="SEQ_WRK_DBR_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_DBR_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="dbr_id")
    private DBR dbr;




}
