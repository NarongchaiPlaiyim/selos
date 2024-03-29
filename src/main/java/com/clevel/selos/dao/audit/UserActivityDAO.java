package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.audit.UserActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserActivityDAO extends GenericDAO<UserActivity, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public UserActivityDAO() {
    }
}
