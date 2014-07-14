package com.clevel.selos.model.db.master;

import com.clevel.selos.model.ProposeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_step")
public class Step implements Serializable {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "code", length = 4)
    private String code;
    @Column(name = "description", length = 100)
    private String description;
    @OneToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;
    @Column(name = "doc_check", columnDefinition = "int default 0")
    private int docCheck;
    @Column(name = "check_brms", columnDefinition = "int default 0")
    private int checkBRMS;
    @Column(name = "propose_flag", columnDefinition = "int default 0")
    private ProposeType proposeType;
    @Column(name = "operation_flag", columnDefinition = "int default 0")
    private int operationFlag;
    @Column(name = "active")
    private int active;

    public Step() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public int getDocCheck() {
        return docCheck;
    }

    public void setDocCheck(int docCheck) {
        this.docCheck = docCheck;
    }

    public int getCheckBRMS() {
        return checkBRMS;
    }

    public void setCheckBRMS(int checkBRMS) {
        this.checkBRMS = checkBRMS;
    }

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(int operationFlag) {
        this.operationFlag = operationFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("code", code)
                .append("description", description)
                .append("stage", stage)
                .append("docCheck", docCheck)
                .append("checkBRMS", checkBRMS)
                .append("proposeType", proposeType)
                .append("active", active)
                .toString();
    }
}
