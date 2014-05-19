package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_district")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class District implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 100)
    private String name;
    @OneToOne
    @JoinColumn(name = "province_id")
    private Province province;
    @JoinColumn(name = "active")
    private int active;

    public District() {
    }

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

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("province", province).
                append("active", active).
                toString();
    }
}
