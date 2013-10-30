package com.clevel.selos.dao.working;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.DBR;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DBRDAO extends GenericDAO<DBR, Long> {
    @Inject
    private Logger log;

    @Inject
    public DBRDAO() {

    }

}
