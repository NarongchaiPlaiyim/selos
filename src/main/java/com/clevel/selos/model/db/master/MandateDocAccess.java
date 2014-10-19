package com.clevel.selos.model.db.master;

import javax.persistence.*;
import com.clevel.selos.model.AccessType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_doc_access")
public class MandateDocAccess implements Serializable{

    @Id
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "access_type", columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private AccessType accessType;

    @Column(name = "check_brms", columnDefinition = "int default 0")
    private boolean checkBRMS;

    @Column(name = "check_ecm", columnDefinition = "int default 0")
    private boolean checkECM;

    @Column(name = "active", columnDefinition = "int default 0")
    private int active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public boolean isCheckBRMS() {
        return checkBRMS;
    }

    public void setCheckBRMS(boolean checkBRMS) {
        this.checkBRMS = checkBRMS;
    }

    public boolean isCheckECM() {
        return checkECM;
    }

    public void setCheckECM(boolean checkECM) {
        this.checkECM = checkECM;
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
                .append("step", step)
                .append("role", role)
                .append("accessType", accessType)
                .append("checkBRMS", checkBRMS)
                .append("checkECM", checkECM)
                .append("active", active)
                .toString();
    }
}
