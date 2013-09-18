package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PotentialColToTCGColDAO extends GenericDAO<PotentialColToTCGCol, Integer> {
    @Inject
    private Logger log;

    @Inject
    PotentialColToTCGColDAO(){

    }
}
