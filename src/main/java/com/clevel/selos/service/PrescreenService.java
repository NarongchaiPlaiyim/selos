package com.clevel.selos.service;

import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.model.db.working.Prescreen;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PrescreenService {
    @Inject
    Logger log;

    @Inject
    PrescreenDAO prescreenDAO;

    @Inject
    public PrescreenService(){

    }


    public void save(Prescreen prescreen){
        log.info("Prescreen : {}", prescreen);
        prescreenDAO.persist(prescreen);
    }
}
