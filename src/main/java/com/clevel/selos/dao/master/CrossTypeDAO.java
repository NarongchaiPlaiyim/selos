package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.CrossType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.List;

public class CrossTypeDAO extends GenericDAO<CrossType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CrossTypeDAO() {
    }

}
