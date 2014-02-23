package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_pre_disbursement")
public class PreDisbursementData implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    
    @Column(name = "group_name")
    private String groupName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "priority")
    private int priority;

    public PreDisbursementData() {
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

    

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("groupname", groupName).
                append("description", description).
                append("priority", priority).
                toString();
    }

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
