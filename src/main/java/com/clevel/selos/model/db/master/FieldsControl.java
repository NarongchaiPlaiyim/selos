package com.clevel.selos.model.db.master;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_fields_control")
public class FieldsControl implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_MST_FIELDS_CONTROL_ID", sequenceName = "SEQ_MST_FIELDS_CONTROL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_FIELDS_CONTROL_ID")
    private long id;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "screen_id")
    private int screenId;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "mandate")
    private int mandate;

    @Column(name = "readonly")
    private int readonly;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getMandate() {
        return mandate;
    }

    public void setMandate(int mandate) {
        this.mandate = mandate;
    }

    public int getReadonly() {
        return readonly;
    }

    public void setReadonly(int readonly) {
        this.readonly = readonly;
    }
}
