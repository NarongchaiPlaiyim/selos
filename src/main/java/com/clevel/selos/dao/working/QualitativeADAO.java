package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.QualitativeA;
import org.slf4j.Logger;

import javax.inject.Inject;

public class QualitativeADAO extends GenericDAO<QualitativeA, Integer> {
    @Inject
    private Logger log;

    @Inject
    public QualitativeADAO(){

    }
}
