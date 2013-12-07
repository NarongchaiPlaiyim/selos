package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_decision")
public class Decision implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_DECISION_ID", sequenceName = "SEQ_WRK_DECISION_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DECISION_ID")
    private long id;

    @ManyToOne
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("workCase", workCase).
                toString();
    }
}
