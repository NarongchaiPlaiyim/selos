package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_subdistrict")
public class SubDistrict {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private int code;
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name="district_id")
    District district;
    @OneToOne
    @JoinColumn(name="province_id")
    Province province;

    public SubDistrict() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("code", code).
                append("name", name).
                append("district", district).
                append("province", province).
                toString();
    }
}
