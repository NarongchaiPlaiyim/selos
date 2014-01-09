package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.thoughtworks.xstream.core.JVM;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TCGCollateralTypeDAO extends GenericDAO<TCGCollateralType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    TCGCollateralTypeDAO() {

    }

}
