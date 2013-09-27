package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.TCG;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TCGDAO extends GenericDAO<TCG, Integer>{
    @Inject
    private Logger log;

    @Inject
    public TCGDAO(){

    }
}
