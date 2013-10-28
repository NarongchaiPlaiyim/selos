package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.UserTeam;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserTeamDAO extends GenericDAO<UserTeam, Integer> {
    @Inject
    private Logger log;

    @Inject
    public UserTeamDAO() {
    }
}
