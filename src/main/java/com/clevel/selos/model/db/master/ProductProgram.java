package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_product_program")
public class ProductProgram implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "brms_code", length = 5)
    private String brmsCode;
    @Column(name = "active")
    private int active;
    @Column(name="IS_BA",columnDefinition="int default 0")
    private boolean ba;
    @Column(name = "is_existing", columnDefinition="int default 0")
    private int isExisting;
    @Column(name = "is_propose", columnDefinition="int default 0")
    private int isPropose;
    @Column(name = "is_basic_cond", columnDefinition="int default 0")
    private int isBasicCond;

    public ProductProgram() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    public boolean isBa() {
		return ba;
	}
    public void setBa(boolean ba) {
		this.ba = ba;
	}

    public int getExisting() {
        return isExisting;
    }

    public void setExisting(int existing) {
        isExisting = existing;
    }

    public int getPropose() {
        return isPropose;
    }

    public void setPropose(int propose) {
        isPropose = propose;
    }

    public int getBasicCond() {
        return isBasicCond;
    }

    public void setBasicCond(int basicCond) {
        isBasicCond = basicCond;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("description", description).
                append("brmsCode", brmsCode).
                append("active", active).
                append("ba", ba).
                append("isExisting", isExisting).
                append("isPropose", isPropose).
                append("isBasicCond", isBasicCond).
                toString();
    }
}
