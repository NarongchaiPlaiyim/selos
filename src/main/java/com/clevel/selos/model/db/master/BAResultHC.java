package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_ba_result_hc")
public class BAResultHC implements Serializable {

    private static final long serialVersionUID = -1216889744989539179L;

	@Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "active")
    private int active;
    
    @Column(name="required_checkdate",columnDefinition="int default 0")
    private boolean requiredCheckDate;

    public BAResultHC(){}

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

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	public boolean isRequiredCheckDate() {
		return requiredCheckDate;
	}
	public void setRequiredCheckDate(boolean requiredCheckDate) {
		this.requiredCheckDate = requiredCheckDate;
	}
    
    
}
