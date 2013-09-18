package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.TCGCollateralType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TCGCollateralTypeDAO extends GenericDAO<TCGCollateralType, Integer> {
    @Inject
    private Logger log;

    @Inject
    TCGCollateralTypeDAO(){

    }
}
