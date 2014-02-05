package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrk_case_fee")
public class WorkCaseFee {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_FEE_ID", sequenceName = "SEQ_WRK_CASE_FEE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_FEE_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "collect_fee_completed")
    private int collectFeeCompleted;

    @Column(name = "grand_total_fee")
    private int grandTotalFee;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public int getCollectFeeCompleted() {
        return collectFeeCompleted;
    }

    public void setCollectFeeCompleted(int collectFeeCompleted) {
        this.collectFeeCompleted = collectFeeCompleted;
    }

    public int getGrandTotalFee() {
        return grandTotalFee;
    }

    public void setGrandTotalFee(int grandTotalFee) {
        this.grandTotalFee = grandTotalFee;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("collectFeeCompleted", collectFeeCompleted)
                .append("grandTotalFee", grandTotalFee)
                .toString();
    }
}
