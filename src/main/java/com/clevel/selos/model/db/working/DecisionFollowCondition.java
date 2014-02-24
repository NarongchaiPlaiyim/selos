package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.FollowCondition;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_decision_follow_condition")
public class DecisionFollowCondition implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_FOLLOW_CONDITION_ID", sequenceName = "SEQ_WRK_FOLLOW_CONDITION_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_FOLLOW_CONDITION_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "follow_condition_id")
    private FollowCondition followCondition;

    @Column(name = "detail", length = 500)
    private String detail;

    @Column(name = "follow_date")
    private Date followDate;

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

    public FollowCondition getFollowCondition() {
        return followCondition;
    }

    public void setFollowCondition(FollowCondition followCondition) {
        this.followCondition = followCondition;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("followCondition", followCondition)
                .append("detail", detail)
                .append("followDate", followDate)
                .toString();
    }
}
