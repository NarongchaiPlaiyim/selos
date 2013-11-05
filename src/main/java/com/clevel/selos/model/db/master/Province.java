package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_province")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Province implements Serializable {
    @Id
    @Column(name = "code")
    private int code;
    @Column(name = "name", length = 100)
    private String name;
    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @Column(name = "active")
    private int active;

    public Province() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int id) {
        this.code = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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
                append("code", code).
                append("name", name).
                append("region", region).
                append("active", active).
                toString();
    }
}
