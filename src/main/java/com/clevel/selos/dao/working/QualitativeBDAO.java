package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.QualitativeB;
import org.slf4j.Logger;

import javax.inject.Inject;

public class QualitativeBDAO extends GenericDAO<QualitativeB, Integer> {
    @Inject
    private Logger log;

    @Inject
    public QualitativeBDAO(){

    }
}
