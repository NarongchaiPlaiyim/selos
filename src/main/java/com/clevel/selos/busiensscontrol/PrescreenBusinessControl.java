package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PrescreenBusinessControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    public PrescreenBusinessControl(){

    }

    public void save(WorkCasePrescreen workCasePrescreen){
        workCasePrescreenDAO.persist(workCasePrescreen);
    }

    public void delete(WorkCasePrescreen workCasePrescreen){
        workCasePrescreenDAO.delete(workCasePrescreen);
    }

}
