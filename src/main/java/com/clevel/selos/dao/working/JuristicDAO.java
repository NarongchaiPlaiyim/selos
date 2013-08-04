package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Juristic;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class JuristicDAO extends GenericDAO<Juristic,Long> {
    @Inject
    private Logger log;

    @Inject
    public JuristicDAO() {
    }
}
