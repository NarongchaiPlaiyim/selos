package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.QualitativeADAO;
import com.clevel.selos.dao.working.QualitativeBDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.QualitativeA;
import com.clevel.selos.model.db.working.QualitativeB;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.QualitativeView;
import com.clevel.selos.transform.QualitativeTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class QualitativeControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    QualitativeADAO qualitativeADAO;
    @Inject
    QualitativeBDAO qualitativeBDAO;

    @Inject
    QualitativeTransform qualitativeTransform;

    @Inject
    public QualitativeControl() {}


    public void saveQualitativeA(QualitativeView qualitativeAView, long workCaseId ,User user) {
        log.info("start saveQualitativeA ::: ");

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        QualitativeA qualitativeA = qualitativeTransform.transformQualitativeAToModel(qualitativeAView, workCase ,user);
        qualitativeADAO.persist(qualitativeA);

    }

    public QualitativeView getQualitativeA(long workCaseId){
        log.info("getQualitativeA ::: workCaseId : {}", workCaseId);
        QualitativeView  qualitativeView = null;

        try{
            WorkCase workCase = workCaseDAO.findById(workCaseId);

            if(workCase != null)
            {
                QualitativeA  qualitativeA = qualitativeADAO.findByWorkCase(workCase);

                if(qualitativeA != null)
                {
                    log.info("get QualitativeA ::: QualitativeA : {}", qualitativeA.getId());
                    qualitativeView = qualitativeTransform.transformQualitativeAToView(qualitativeA);
                }
            }
        }catch (Exception e){
            log.error( "find workCase error ::: {}" , e.getMessage());
        }finally {
            log.info("getQualitativeA end");
        }

        log.info("getQualitativeA ::: qualitativeView : {}", qualitativeView);
        return qualitativeView;
    }


    public void saveQualitativeB(QualitativeView qualitativeBView, long workCaseId,User user) {
        log.info("start saveQualitativeB ::: ");
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        QualitativeB qualitativeB = qualitativeTransform.transformQualitativeBToModel(qualitativeBView, workCase,user);
        qualitativeBDAO.persist(qualitativeB);

    }

    public QualitativeView getQualitativeB(long workCaseId){
        log.info("getQualitativeB ::: workCaseId : {}", workCaseId);
        QualitativeView  qualitativeView = null;
        try{
            WorkCase workCase = workCaseDAO.findById(workCaseId);

            if(workCase != null)
            {
                QualitativeB  qualitativeB = qualitativeBDAO.findByWorkCase(workCase);

                if(qualitativeB != null)
                {
                    log.info("get QualitativeB ::: QualitativeB : {}", qualitativeB);
                    qualitativeView = qualitativeTransform.transformQualitativeBToView(qualitativeB);
                }
            }
        } catch (Exception e) {
            log.error("saveQualitativeB error" + e.getMessage());
        }

        log.info("getQualitativeB ::: qualitativeView : {}", qualitativeView);
        return qualitativeView;
    }


    /*public void save(WorkCase workCase) {
        workCaseDAO.persist(workCase);
    }*/

    /*public void delete(WorkCase workCase) {
        workCaseDAO.delete(workCase);
    }*/
}
