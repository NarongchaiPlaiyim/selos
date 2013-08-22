package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_subdistrict")
public class SubDistrict implements Serializable {
    @Id
    @Column(name = "code")
    private int code;
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name="district_id")
    private District district;
    @OneToOne
    @JoinColumn(name="province_id")
    private Province province;
    @Column(name = "active")
    private int active;

    public SubDistrict() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
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
                append("code", code).
                append("name", name).
                append("district", district).
                append("province", province).
                append("active", active).
                toString();
    }
}
