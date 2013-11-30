package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_ex_sum_deviate")
public class ExSumDeviate implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EX_SUM_DEVIATE_ID", sequenceName = "SEQ_WRK_EX_SUM_DEVIATE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EX_SUM_DEVIATE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "exsum_id")
    private ExSummary exSummary;

    @OneToOne
    @JoinColumn(name = "reason_id")
    private Reason deviateCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExSummary getExSummary() {
        return exSummary;
    }

    public void setExSummary(ExSummary exSummary) {
        this.exSummary = exSummary;
    }

    public Reason getDeviateCode() {
        return deviateCode;
    }

    public void setDeviateCode(Reason deviateCode) {
        this.deviateCode = deviateCode;
    }
}
