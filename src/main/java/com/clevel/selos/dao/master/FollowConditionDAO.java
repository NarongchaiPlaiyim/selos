package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.FollowCondition;
import org.slf4j.Logger;

import javax.inject.Inject;

public class FollowConditionDAO extends GenericDAO<FollowCondition, Long> {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    public FollowConditionDAO(){

    }
}
