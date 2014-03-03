package com.clevel.selos.model.db.master;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "mst_relation")
public class Relation implements Serializable {
    private static final long serialVersionUID = 6735999015637205594L;
	@Id
    @Column(name = "id")
    private int id;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "priority", length = 1)
    private int priority;
    @Column(name = "active")
    private int active;
    @Column(name = "brms_code", length = 2)
    private String brmsCode;
    @Column(name="canbe_poa",columnDefinition="int default 0")
    private boolean canBePOA;
   
    public Relation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int piority) {
        this.priority = piority;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
    public boolean isCanBePOA() {
		return canBePOA;
	}
    public void setCanBePOA(boolean canBePOA) {
		this.canBePOA = canBePOA;
	}

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("description", description).
                append("active", active).
                append("priority",priority).
                append("canBePOA",canBePOA).
                toString();
    }
}
