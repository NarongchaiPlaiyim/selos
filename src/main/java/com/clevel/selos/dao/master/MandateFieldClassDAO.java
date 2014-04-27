package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldClass;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateFieldClassDAO extends GenericDAO<MandateFieldClass, Long>{

    @Inject
    @SELOS
    Logger log;
    @Inject
    public MandateFieldClassDAO() {
    }
}
