package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_dbr")
public class DBR implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_DBR_ID", sequenceName="SEQ_WRK_DBR_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_DBR_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
}
