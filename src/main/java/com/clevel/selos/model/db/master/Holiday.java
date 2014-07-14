package com.clevel.selos.model.db.master;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mst_holiday")
public class Holiday implements Serializable {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "description", length = 100)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "holiday_date")
    private Date holidayDate;

    public Holiday() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("holidayDate", holidayDate)
                .toString();
    }
}
