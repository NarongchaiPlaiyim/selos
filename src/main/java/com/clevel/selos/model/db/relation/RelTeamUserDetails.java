package com.clevel.selos.model.db.relation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "rel_tlth_to_team")
public class RelTeamUserDetails implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "tl_th_id")
    private int tlThId;

    @Column(name = "team_id")
    private int team_Id;

    @Column(name = "team_flag")
    private String teamFlag;

    @Column(name = "ra_search_case")
    private String raSearchCase;

    @Column(name = "ra_search_user")
    private String raSearchUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTlThId() {
        return tlThId;
    }

    public void setTlThId(int tlThId) {
        this.tlThId = tlThId;
    }

    public int getTeam_Id() {
        return team_Id;
    }

    public void setTeam_Id(int team_Id) {
        this.team_Id = team_Id;
    }

    public String getRaSearchCase() {
        return raSearchCase;
    }

    public void setRaSearchCase(String raSearchCase) {
        this.raSearchCase = raSearchCase;
    }

    public String getTeamFlag() {
        return teamFlag;
    }

    public void setTeamFlag(String teamFlag) {
        this.teamFlag = teamFlag;
    }

    public String getRaSearchUser() {
        return raSearchUser;
    }

    public void setRaSearchUser(String raSearchUser) {
        this.raSearchUser = raSearchUser;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("tlThId", tlThId).
                append("team_Id", team_Id).
                append("teamFlag", teamFlag).
                append("raSearchCase", raSearchCase).
                append("raSearchUser", raSearchUser).
                toString();
    }
}
