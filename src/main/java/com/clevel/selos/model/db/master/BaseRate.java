package com.clevel.selos.model.db.master;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "mst_base_rate")
public class BaseRate implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 10)
    private String name;
    @Column(name = "value", length = 7, scale = 3)
    private BigDecimal value;
    @Column(name = "active")
    private int active;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "add_of_date")
    private Date addOfDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getAddOfDate() {
        return addOfDate;
    }

    public void setAddOfDate(Date addOfDate) {
        this.addOfDate = addOfDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("value", value)
                .append("active", active)
                .append("addOfDate", addOfDate)
                .toString();
    }
}
