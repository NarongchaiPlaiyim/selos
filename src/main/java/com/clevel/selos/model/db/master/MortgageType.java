package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_mortgage_type")
public class MortgageType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "mortgage_type", length = 255)
    private String mortgage;

    @Column(name = "redeem")
    private String redeem;

    @Column(name = "active")
    private int active;

    public MortgageType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public String getRedeem() {
        return redeem;
    }

    public void setRedeem(String redeem) {
        this.redeem = redeem;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mortgage", mortgage)
                .append("redeem", redeem)
                .append("active", active)
                .toString();
    }

}
