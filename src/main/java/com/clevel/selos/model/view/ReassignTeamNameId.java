package com.clevel.selos.model.view;


import com.clevel.selos.integration.SELOS;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.io.Serializable;

public class ReassignTeamNameId implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    private String teamname;

    private int teamid;

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("teamname", teamname)
                .append("teamid", teamid)
                .toString();
    }


}
