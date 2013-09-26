package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_tcg_detail")
public class TCGDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_TCG_DETAIL_ID", sequenceName="SEQ_WRK_TCG_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_TCG_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="tcg_id")
    private TCG tcg;




}
