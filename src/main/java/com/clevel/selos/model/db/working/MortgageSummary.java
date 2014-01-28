package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "wrk_mortgage_summary")
public class MortgageSummary implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MORTGAGE_SUMMARY_ID", sequenceName = "SEQ_WRK_MORTGAGE_SUMMARY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORTGAGE_SUMMARY_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    //@OneToMany(mappedBy = "basicInfo")
    //private List<OpenAccount> openAccountList;

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
