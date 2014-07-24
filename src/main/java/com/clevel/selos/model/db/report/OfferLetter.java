package com.clevel.selos.model.db.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_config_team")
public class OfferLetter implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String teamName;

    @Column(name = "tel_phone")
    private String telPhone;

    @Column(name = "tel_fax")
    private String telFax;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getTelFax() {
        return telFax;
    }

    public void setTelFax(String telFax) {
        this.telFax = telFax;
    }
}
