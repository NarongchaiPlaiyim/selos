package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.QualitativeADAO;
import com.clevel.selos.dao.working.QualitativeBDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.QualitativeA;
import com.clevel.selos.model.db.working.QualitativeB;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.QualitativeView;
import com.clevel.selos.transform.QualitativeTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA
 * Date: 30/9/2556
 * Time: 11:20 น.
 * To change this template use File | Settings | File Templates.
 */


@Stateless
public class QualitativeControl extends BusinessControl{

    @Inject
    Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    QualitativeADAO qualitativeADAO;

    @Inject
    QualitativeBDAO qualitativeBDAO;

    @Inject
    QualitativeTransform qualitativeTransform;


    @Inject
    public QualitativeControl(){

    }

    public void saveQualitativeA(QualitativeView qualitativeAView , long workCaseId){
        WorkCase workCase  = workCaseDAO.findById(workCaseId);
        QualitativeA qualitativeA = qualitativeTransform.transformQualitativeAToModel(qualitativeAView,workCase);
        qualitativeADAO.persist(qualitativeA);
    }

    public void saveQualitativeB(QualitativeView qualitativeBView , long workCaseId){
        WorkCase workCase  = workCaseDAO.findById(workCaseId);
        QualitativeB qualitativeB = qualitativeTransform.transformQualitativeBToModel(qualitativeBView, workCase);
        qualitativeBDAO.persist(qualitativeB);
    }

    public void save(WorkCase workCase){
        workCaseDAO.persist(workCase);
    }

    public void delete(WorkCase workCase){
        workCaseDAO.delete(workCase);
    }
}
