package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import org.slf4j.Logger;
import com.clevel.selos.model.db.master.PotentialCollateral;

import javax.inject.Inject;

public class PotentialCollateralDAO extends GenericDAO<PotentialCollateral,Integer> {
    @Inject
    private Logger log;

    @Inject
    PotentialCollateralDAO(){

    }
}