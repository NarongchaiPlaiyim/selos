package com.clevel.selos.model.db.master;

import com.clevel.selos.model.Screen;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "mst_user_access")
public class UserAccess implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;
    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;
    @Column(name = "screen_id")
    private int screenId;
    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @Column(name = "access_flag", columnDefinition = "int default 0")
    private int accessFlag;
    @Column(name = "active", columnDefinition = "int default 0")
    private int active;

    public UserAccess() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
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

    public int getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(int accessFlag) {
        this.accessFlag = accessFlag;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("stage", stage)
                .append("step", step)
                .append("screenId", screenId)
                .append("role", role)
                .append("accessFlag", accessFlag)
                .append("active", active)
                .toString();
    }
}
