package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.WorkCase;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class FullAppBusinessControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    public FullAppBusinessControl(){

    }

    public void save(WorkCase workCase){
        workCaseDAO.persist(workCase);
    }

    public void delete(WorkCase workCase){
        workCaseDAO.delete(workCase);
    }
}
