package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_tcg")
public class TCG implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_TCG_ID", sequenceName="SEQ_WRK_TCG_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_TCG_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
}
