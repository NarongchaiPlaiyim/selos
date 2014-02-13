package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_case")
public class WorkCase extends AbstractWorkCase{
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_ID", sequenceName = "SEQ_WRK_CASE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "step_owner")
    private User stepOwner;

    @OneToOne
    @JoinColumn(name = "workcaseprescreen_id")
    private WorkCasePrescreen workCasePrescreen;


    @Column(name = "")

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWobNumber() {
        return wobNumber;
    }

    public void setWobNumber(String wobNumber) {
        this.wobNumber = wobNumber;
    }

    public User getStepOwner() {
        return stepOwner;
    }

    public void setStepOwner(User stepOwner) {
        this.stepOwner = stepOwner;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("wobNumber", wobNumber)
                .append("stepOwner", stepOwner)
                .append("workCasePrescreen", workCasePrescreen)
                .toString();
    }
}
