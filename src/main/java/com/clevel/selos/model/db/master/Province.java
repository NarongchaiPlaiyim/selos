package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_province")
public class Province {
    @Id
    @Column(name = "code")
    private int code;
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name="region_id")
    private Region region;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("code", code).
                append("name", name).
                append("region", region).
                toString();
    }
}
