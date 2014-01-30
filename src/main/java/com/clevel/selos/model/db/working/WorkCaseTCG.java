package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wrk_case_tcg")
public class WorkCaseTCG extends AbstractWorkCase{
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_TCG_ID", sequenceName = "SEQ_WRK_CASE_TCG_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_TCG_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

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
}
